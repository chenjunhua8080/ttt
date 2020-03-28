package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.map.MapService;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.base.util.StringReplaceUtil;
import com.cjh.ttt.dao.AddressDao;
import com.cjh.ttt.dao.MessageDao;
import com.cjh.ttt.dao.MessageDetailDao;
import com.cjh.ttt.dao.PairDao;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.AddressDto;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.dto.PairSuccessDto;
import com.cjh.ttt.dto.PairSuccessDto.PairUserBean;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.entity.Message;
import com.cjh.ttt.entity.MessageDetail;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.enums.MessageTypeEnum;
import com.cjh.ttt.enums.PairStatusEnum;
import com.cjh.ttt.enums.PairTypeEnum;
import com.cjh.ttt.request.PairingRequest;
import com.cjh.ttt.service.PairService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (Pair)表服务实现类
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
@Slf4j
@AllArgsConstructor
@Service("pairService")
public class PairServiceImpl extends ServiceImpl<PairDao, Pair> implements PairService {

    private UserDao userDao;
    private AddressDao addressDao;
    private MapService mapService;
    private MessageDao messageDao;
    private MessageDetailDao messageDetailDao;

    @Override
    public PairDto getPairList(Page<User> page, Integer type) {
        //查询用户
        Integer userId = UserContext.getUserId();
        User user = userDao.selectById(userId);
        int sex = user.getSex();
        if (sex == 0) {
            throw new ServiceException(ErrorEnum.SEX_NOT_SET);
        }
        sex = sex == 1 ? 2 : 1;
        if (user.getBirthday() == null) {
            throw new ServiceException(ErrorEnum.BIRTHDAY_NOT_SET);
        }

        //去重ids
        List<Integer> ids = new ArrayList<>();
        //不包含自己
        ids.add(userId);
        //不包已配对
        List<Integer> pairIds = baseMapper.selectPairIds(userId);
        if (pairIds != null && !pairIds.isEmpty()) {
            ids.addAll(pairIds);
        }

        //根据类型查询
        PairTypeEnum pairType = PairTypeEnum.from(type);
        IPage<User> pages;
        assert pairType != null;
        switch (pairType) {
            case PAIR_TYPE_1:
                pages = userDao.selectBySexAndBirthday(page, sex, user.getBirthday(), ids);
                break;
            case PAIR_TYPE_2:
                pages = userDao.selectBySexAndNearByBirthday(page, sex, user.getBirthday(), ids);
                break;
            case PAIR_TYPE_3:
                pages = selectByAddress(page, userId, ids);
                break;
            default:
                return null;
        }

        //封装数据
        List<UserDto> userBeanList = pages.getRecords().stream().map(source -> {
            UserDto userBean = new UserDto();
            BeanUtils.copyProperties(source, userBean);
            //隐藏手机
            userBean.setPhone(null);
            //查询匹配状态
            userBean.setPairStatus(baseMapper.selectStatus(userId, userBean.getId()));
            return userBean;
        }).collect(Collectors.toList());
        //离我最近，调用地图查询距离
        if (pairType.getCode() == PairTypeEnum.PAIR_TYPE_3.getCode()) {
            Address userAddress = addressDao.selectByUserId(userId);
            for (UserDto userDto : userBeanList) {
                Address address = addressDao.selectByUserId(userDto.getId());
                AddressDto addressDto = new AddressDto();
                BeanUtils.copyProperties(address, addressDto);
                addressDto.setDistance(
                    Math.round(mapService.getDistance(
                        userAddress.getLng(), userAddress.getLat(),
                        address.getLng(), address.getLat()
                    ))
                );
                //隐藏坐标
                addressDto.setLng(null);
                addressDto.setLat(null);
                userDto.setAddress(addressDto);
            }
        }
        PairDto dto = new PairDto();
        Page<UserDto> pageData = new Page<>();
        BeanUtils.copyProperties(pages, pageData);
        pageData.setRecords(userBeanList);
        dto.setPageData(pageData);
        dto.setPairType(pairType.getCode());
        dto.setPairTypeValue(pairType.getName());

        return dto;
    }

    /**
     * 根据坐标查询离得最近的人
     */
    private IPage<User> selectByAddress(Page<User> page, Integer userId, List<Integer> ids) {
        Address address = addressDao.selectByUserId(userId);
        if (address == null) {
            throw new ServiceException(ErrorEnum.SEX_NOT_SET);
        }
        String lng = address.getLng();
        String lat = address.getLat();

        return userDao.selectByDistance(page, lng, lat, ids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pairing(PairingRequest pairingRequest) {
        Integer userId = UserContext.getUserId();
        Integer pairUserId = pairingRequest.getId();
        //检查配对关系
        Pair oldPair = baseMapper.selectBySenderAndRecipient(userId, pairUserId);
        if (oldPair != null) {
            if (PairStatusEnum.SUCCESS.getCode() == oldPair.getStatus()) {
                throw new ServiceException(ErrorEnum.PAIR_SUCCESS_ED);
            }
            if (PairStatusEnum.WAIT.getCode() == oldPair.getStatus()) {
                if (oldPair.getSender().equals(userId)) {
                    throw new ServiceException(ErrorEnum.PAIR_SEND_ED);
                } else {
                    //配对中，如果是对方发起的配对，直接成功
                    oldPair.setStatus(PairStatusEnum.SUCCESS.getCode());
                    oldPair.setUpdateTime(new Date());
                    baseMapper.updateById(oldPair);

                    //配对成功，给双方发送消息
                    sendSystemMessage(userId, pairUserId);
                    sendSystemMessage(pairUserId, userId);
                }
            }
            if (PairStatusEnum.FAIL.getCode() == oldPair.getStatus()) {
                //配对被拒绝，重新发起配对
                oldPair.setStatus(PairStatusEnum.WAIT.getCode());
                oldPair.setUpdateTime(new Date());
                baseMapper.updateById(oldPair);
            }
        } else {
            //建立配对关系
            Pair pair = new Pair();
            pair.setChannel(pairingRequest.getChannel());
            pair.setSender(userId);
            pair.setRecipient(pairUserId);
            pair.setContent(pairingRequest.getContent());
            baseMapper.insert(pair);
        }

    }

    @Override
    public int check(Integer id) {
        return baseMapper.countPairSuccess(id);
    }

    @Override
    public IPage<PairSuccessDto> getPairSuccessList(Page<Pair> page) {
        //分页查询
        IPage<Pair> iPage = baseMapper.getPairSuccessList(page);
        //封装数据
        IPage<PairSuccessDto> pages = new Page<>();
        BeanUtils.copyProperties(iPage, pages);
        List<PairSuccessDto> records = new ArrayList<>();
        PairSuccessDto dto;
        PairUserBean sender;
        PairUserBean recipient;
        User user;
        for (Pair pair : iPage.getRecords()) {
            dto = new PairSuccessDto();

            sender = new PairUserBean();
            user = userDao.selectById(pair.getSender());
            BeanUtils.copyProperties(user, sender);
            sender.setNickname(StringReplaceUtil.userNameReplaceWithStar(sender.getNickname()));
            dto.setSender(sender);

            recipient = new PairUserBean();
            user = userDao.selectById(pair.getRecipient());
            BeanUtils.copyProperties(user, recipient);
            recipient.setNickname(StringReplaceUtil.userNameReplaceWithStar(recipient.getNickname()));
            dto.setRecipient(recipient);

            records.add(dto);
        }
        pages.setRecords(records);
        return pages;
    }

    @Override
    public List<UserDto> getNewPairList(Integer userId) {
        return baseMapper.getNewPairList(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Integer sender, Integer status) {
        //检查配对关系
        Integer userId = UserContext.getUserId();
        Pair pair = baseMapper.selectBySenderAndRecipient(userId, sender);
        if (pair == null) {
            throw new ServiceException(ErrorEnum.ERROR_412);
        }

        //修改配对状态
        Integer recipient = UserContext.getUserId();
        baseMapper.updateStatus(sender, recipient, status);

        //配对成功，给双方发送消息
        if (status == PairStatusEnum.SUCCESS.getCode()) {
            sendSystemMessage(userId, sender);
            sendSystemMessage(sender, userId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void relieve(Integer userId) {
        //检查配对关系
        Integer id = UserContext.getUserId();
        Pair pair = baseMapper.selectBySenderAndRecipient(id, userId);
        if (pair == null) {
            throw new ServiceException(ErrorEnum.ERROR_412);
        }

        //解除配对关系
        pair.setStatus(PairStatusEnum.RELIEVE.getCode());
        pair.setRelive(id);
        pair.setUpdateTime(new Date());
        baseMapper.updateById(pair);

        //移除聊天列表
        Message message = messageDao.selectByUserAndTarget(id, userId);
        messageDao.deleteById(message);
    }

    /**
     * 配对成功，发送系统消息
     */
    private void sendSystemMessage(Integer userId, Integer targetId) {
        //查询是否已存在会话
        Message message = messageDao.selectByUserAndTarget(userId, targetId);
        if (message == null) {
            //插入新的会话
            message = new Message();
            message.setUserId(userId);
            message.setTargetId(targetId);
            messageDao.insert(message);
        }
        //查询消息明细
        MessageDetail messageDetail = new MessageDetail();
        messageDetail.setMessageType(MessageTypeEnum.SYSTEM.getCode());
        messageDetail.setContent("配对成功~ 开始进一步深入了解对方吧^_^");
        messageDetail.setIsSender(0);
        messageDetail.setMessageId(message.getId());
        messageDetailDao.insert(messageDetail);
    }
}