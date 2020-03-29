package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.base.util.ThreadUtil;
import com.cjh.ttt.dao.MessageDao;
import com.cjh.ttt.dao.MessageDetailDao;
import com.cjh.ttt.dao.PairDao;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.Message;
import com.cjh.ttt.entity.MessageDetail;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.enums.MessageStatusEnum;
import com.cjh.ttt.enums.MessageTypeEnum;
import com.cjh.ttt.enums.PairStatusEnum;
import com.cjh.ttt.service.MessageService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息表(Message)表服务实现类
 *
 * @author cjh
 * @since 2020-03-25 18:16:48
 */
@Slf4j
@AllArgsConstructor
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

    private UserDao userDao;
    private MessageDetailDao messageDetailDao;
    private PairDao pairDao;

    @Override
    public IPage<MessageDto> getMessageList(Page<MessageDto> page) {
        Integer userId = UserContext.getUserId();

        //分页查询数据
        IPage<MessageDto> pages = baseMapper.getMessageList(page, userId);
        MessageDetail messageDetail;
        User user;
        for (MessageDto item : pages.getRecords()) {
            baseMapper.selectById(item.getId());
            //查询会话信息
            user = userDao.selectById(item.getTargetId());
            item.setTargetAvatar(user.getAvatar());
            item.setTargetNickname(user.getNickname());
            messageDetail = messageDetailDao.selectLastMessage(item.getId());
            if (messageDetail != null) {
                item.setLastText(messageDetail.getContent());
                item.setLastTime(messageDetail.getCreateTime());
                item.setUnreadCount(messageDetailDao.countUnread(item.getId()));
            }
        }
        return pages;
    }

    @Override
    public IPage<MessageDetailDto> getDetailList(Page<MessageDetailDto> page, Integer messageId) {
        //检查权限
        checkMessage(messageId);

        //分页查询数据
        IPage<MessageDetailDto> pages = messageDetailDao.getMessageDetailList(page, messageId);
        List<MessageDetailDto> records = pages.getRecords();
        for (MessageDetailDto item : records) {
            if (item.getStatus() == MessageStatusEnum.WITHDRAW.getCode()) {
                //处理已撤回
                item.setContent("已撤回");
                item.setMessageType(MessageTypeEnum.SYSTEM.getCode());
            }
        }

        //处理已读
        setRead(records);
        return pages;
    }

    @Override
    public void delete(Integer messageId) {
        //检查权限
        checkMessage(messageId);

        baseMapper.deleteById(messageId);
    }

    @Override
    public void deleteMessageDetail(Integer messageDetailId) {
        //检查权限
        checkMessageDetail(messageDetailId);

        messageDetailDao.deleteById(messageDetailId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(Integer messageDetailId) {
        //检查权限
        checkMessageDetail(messageDetailId);

        //撤回我方消息
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setId(messageDetailId);
        messageDetail.setStatus(MessageStatusEnum.WITHDRAW.getCode());
        messageDetailDao.updateById(messageDetail);

        //撤回对方消息
        MessageDetail targetMessageDetail = new MessageDetail();
        messageDetail.setId(messageDetail.getTargetDetailId());
        messageDetail.setStatus(MessageStatusEnum.WITHDRAW.getCode());
        messageDetailDao.updateById(targetMessageDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void send(Integer messageId, String content) {
        if (StringUtils.isBlank(content)) {
            throw new ServiceException(ErrorEnum.ERROR_413);
        }

        //检查权限
        checkMessage(messageId);

        //查询用户
        Message message = baseMapper.selectById(messageId);
        Integer userId = message.getUserId();
        Integer targetId = message.getTargetId();

        //检查配对状态
        boolean relieve = checkPair(userId, targetId);
        //已解除配对，不给对方发送消息
        if (relieve) {
            //我方插入系统消息
            MessageDetail systemMessage = new MessageDetail();
            systemMessage.setMessageType(MessageTypeEnum.SYSTEM.getCode());
            systemMessage.setContent("对方已解除配对，无法收到消息");
            systemMessage.setIsSender(0);
            systemMessage.setMessageId(messageId);
            messageDetailDao.insert(systemMessage);

            //插入我方消息
            MessageDetail messageDetail = new MessageDetail();
            messageDetail.setMessageId(messageId);
            messageDetail.setContent(content);
            messageDetail.setIsSender(1);
            messageDetailDao.insert(messageDetail);
            return;
        }

        //插入我方消息
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setMessageId(messageId);
        messageDetail.setContent(content);
        messageDetail.setIsSender(1);
        messageDetailDao.insert(messageDetail);

        //查询对方会话
        message = baseMapper.selectByUserAndTarget(targetId, userId);
        if (message == null) {
            //创建对方会话列表
            message = new Message();
            message.setUserId(targetId);
            message.setTargetId(userId);
            baseMapper.insert(message);
        }
        //插入对方消息
        MessageDetail targetMessageDetail = new MessageDetail();
        targetMessageDetail.setMessageId(message.getId());
        targetMessageDetail.setTargetDetailId(messageDetail.getId());
        targetMessageDetail.setContent(content);
        targetMessageDetail.setIsSender(0);
        messageDetailDao.insert(targetMessageDetail);

        //修改我方消息关联
        messageDetail.setTargetDetailId(targetMessageDetail.getId());
        messageDetailDao.updateById(messageDetail);
    }

    /**
     * 发送消息检查配对状态，返回是否解除配对
     */
    private boolean checkPair(Integer userId, Integer targetId) {
        Pair pair = pairDao.selectBySenderAndRecipient(userId, targetId);
        if (pair == null) {
            throw new ServiceException(ErrorEnum.ERROR_412);
        } else if (pair.getStatus() == PairStatusEnum.FAIL.getCode()) {
            throw new ServiceException(ErrorEnum.NOT_PAIR);
        } else if (pair.getStatus() == PairStatusEnum.WAIT.getCode()) {
            throw new ServiceException(ErrorEnum.NOT_PAIR);
        }
        return pair.getStatus() == PairStatusEnum.RELIEVE.getCode();
    }

    /**
     * 处理已读
     *
     * TODO 这里要加事物吗
     */
    private void setRead(List<MessageDetailDto> records) {
        ThreadUtil.getThreadPoolExecutor().execute(() -> {
            MessageDetail messageDetail = new MessageDetail();
            for (MessageDetailDto item : records) {
                messageDetail.setId(item.getId());
                messageDetail.setStatus(MessageStatusEnum.READ.getCode());
                messageDetailDao.updateById(messageDetail);
            }
        });
    }

    /**
     * 检查权限
     */
    private void checkMessage(Integer messageId) {
        Message message = baseMapper.selectById(messageId);
        if (message == null || !message.getUserId().equals(UserContext.getUserId())) {
            throw new ServiceException(ErrorEnum.ERROR_412);
        }
    }

    /**
     * 检查权限
     */
    private void checkMessageDetail(Integer messageDetailId) {
        MessageDetail messageDetail = messageDetailDao.selectById(messageDetailId);
        checkMessage(messageDetail.getId());
    }
}