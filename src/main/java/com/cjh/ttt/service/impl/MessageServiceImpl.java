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
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.Message;
import com.cjh.ttt.entity.MessageDetail;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.enums.MessageStatusEnum;
import com.cjh.ttt.enums.MessageTypeEnum;
import com.cjh.ttt.service.MessageService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private MessageDetailDao messageDetailDao;
    private UserDao userDao;

    @Override
    public IPage<MessageDto> getMessageList(Page<MessageDto> page) {
        Integer userId = UserContext.getUserId();

        //分页查询数据
        IPage<MessageDto> pages = baseMapper.getMessageList(page, userId);
        MessageDetail messageDetail;
        User user;
        for (MessageDto item : pages.getRecords()) {
            //查询会话信息
            user = userDao.selectById(item.getTargetId());
            item.setTargetAvatar(user.getAvatar());
            item.setTargetNickname(user.getNickname());
            messageDetail = messageDetailDao.selectLastMessage(item.getId());
            item.setLastText(messageDetail.getContent());
            item.setLastTime(messageDetail.getCreateTime());
            item.setUnrealCount(messageDetailDao.countUnread(item.getId()));
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

    @Override
    public void send(Integer messageId, String content) {
        //检查权限
        checkMessage(messageId);

        //TODO 检查配对状态

        //插入我方消息
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setMessageId(messageId);
        messageDetail.setContent(content);
        messageDetail.setIsSender(1);
        messageDetailDao.insert(messageDetail);

        //查询对方id
        Message message = baseMapper.selectById(messageId);
        Integer userId = message.getUserId();
        Integer targetId = message.getTargetId();
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
     * 处理已读
     */
    private void setRead(List<MessageDetailDto> records) {
        ThreadUtil.getThreadPoolExecutor().execute(() -> {
            MessageDetail messageDetail = new MessageDetail();
            for (MessageDetailDto item : records) {
                item.setId(item.getId());
                item.setStatus(MessageStatusEnum.READ.getCode());
                messageDetailDao.updateById(messageDetail);
            }
        });
    }

    /**
     * 检查权限
     */
    private void checkMessage(Integer messageId) {
        Message message = baseMapper.selectById(messageId);
        if (message.getUserId().equals(UserContext.getUserId())) {
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