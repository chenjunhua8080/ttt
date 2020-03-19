package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dao.PairDao;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.dto.PairDto.UserBean;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.enums.PairStatusEnum;
import com.cjh.ttt.enums.PairTitleEnum;
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

    @Override
    public List<PairDto> getPairList() {
        List<PairDto> list = new ArrayList<>();
        Integer userId = UserContext.getUserId();
        User user = userDao.selectById(userId);
        int sex = user.getSex();
        if (sex == 0) {
            throw new ServiceException(ErrorEnum.SEX_NOT_SET);
        }
        if (user.getBirthday() == null) {
            throw new ServiceException(ErrorEnum.BIRTHDAY_NOT_SET);
        }
        sex = sex == 1 ? 2 : 1;
        //去重ids
        List<Integer> ids = new ArrayList<>();
        ids.add(userId);

        //匹配: 命中注定
        List<User> userList = userDao.selectBySexAndBirthday(sex, user.getBirthday(), ids);
        //封装数据
        List<UserBean> userBeanList = userList.stream().map(source -> {
            UserBean userBean = new UserBean();
            BeanUtils.copyProperties(source, userBean);
            return userBean;
        }).collect(Collectors.toList());
        PairDto pairDto = new PairDto();
        pairDto.setUserList(userBeanList);
        pairDto.setPairType(PairTitleEnum.TITLE1.getCode());
        pairDto.setPairTypeValue(PairTitleEnum.TITLE1.getName());
        list.add(pairDto);

        //去重ids
        ids.addAll(userList.stream().map(User::getId).collect(Collectors.toList()));

        //匹配: 专属推荐
        userList = userDao.selectBySexAndNearByBirthday(sex, user.getBirthday(), ids);
        //封装数据
        userBeanList = userList.stream().map(source -> {
            UserBean userBean = new UserBean();
            BeanUtils.copyProperties(source, userBean);
            return userBean;
        }).collect(Collectors.toList());
        pairDto = new PairDto();
        pairDto.setUserList(userBeanList);
        pairDto.setPairType(PairTitleEnum.TITLE2.getCode());
        pairDto.setPairTypeValue(PairTitleEnum.TITLE2.getName());
        list.add(pairDto);

        //去重ids
        ids.addAll(userList.stream().map(User::getId).collect(Collectors.toList()));

        //匹配: 萍水相逢
        userList = userDao.selectPageNotInIds(new Page<>(), ids).getRecords();
        //封装数据
        userBeanList = userList.stream().map(source -> {
            UserBean userBean = new UserBean();
            BeanUtils.copyProperties(source, userBean);
            return userBean;
        }).collect(Collectors.toList());
        pairDto = new PairDto();
        pairDto.setUserList(userBeanList);
        pairDto.setPairType(PairTitleEnum.TITLE3.getCode());
        pairDto.setPairTypeValue(PairTitleEnum.TITLE3.getName());
        list.add(pairDto);

        return list;
    }

    @Override
    public void pairing(PairingRequest pairingRequest) {
        Integer userId = UserContext.getUserId();
        Integer pairUserId = pairingRequest.getId();
        User pairUser = userDao.selectById(pairUserId);
        Pair oldPair = baseMapper.selectBySenderAndRecipient(userId, pairUserId);
        if (oldPair != null) {
            if (PairStatusEnum.WAIT.getCode() == oldPair.getStatus()) {
                throw new ServiceException(ErrorEnum.PAIR_SEND_ED);
            }
            if (PairStatusEnum.SUCCESS.getCode() == oldPair.getStatus()) {
                throw new ServiceException(ErrorEnum.PAIR_SUCCESS_ED);
            }
            //修改配对关系
            oldPair.setStatus(PairStatusEnum.WAIT.getCode());
            oldPair.setUpdateTime(new Date());
            baseMapper.updateById(oldPair);
        } else {
            //建立配对关系
            Pair pair = new Pair();
            pair.setSender(userId);
            pair.setRecipient(pairUser.getId());
            pair.setContent(pairingRequest.getContent());
            baseMapper.insert(pair);
        }

    }

    @Override
    public int check(Integer id) {
        return baseMapper.selectBySender(id);
    }

}