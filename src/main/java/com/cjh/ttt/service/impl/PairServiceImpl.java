package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.map.MapService;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dao.AddressDao;
import com.cjh.ttt.dao.PairDao;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.AddressDto;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.enums.PairStatusEnum;
import com.cjh.ttt.enums.PairTypeEnum;
import com.cjh.ttt.request.PairingRequest;
import com.cjh.ttt.service.PairService;
import java.util.ArrayList;
import java.util.Collections;
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
    private AddressDao addressDao;
    private MapService mapService;

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
        ids.add(userId);

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
                pages = selectByAddress(page, userId);
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
    private IPage<User> selectByAddress(Page<User> page, Integer userId) {
        Address address = addressDao.selectByUserId(userId);
        if (address == null) {
            throw new ServiceException(ErrorEnum.SEX_NOT_SET);
        }
        String lng = address.getLng();
        String lat = address.getLat();

        return userDao.selectByDistance(page, lng, lat, Collections.singletonList(userId));
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