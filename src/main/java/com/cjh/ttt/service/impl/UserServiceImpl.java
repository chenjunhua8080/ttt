package com.cjh.ttt.service.impl;

import com.alibaba.nacos.common.util.UuidUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.map.MapDto;
import com.cjh.ttt.base.map.MapDto.ResultBean;
import com.cjh.ttt.base.map.MapDto.ResultBean.AdInfoBean;
import com.cjh.ttt.base.map.MapService;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dao.AddressDao;
import com.cjh.ttt.dao.PairDao;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.AddressDto;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.LoginRequest;
import com.cjh.ttt.service.UserService;
import com.cjh.ttt.toutiao.TouTiaoApiService;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author cjh
 * @since 2020-02-27 15:16:43
 */
@Slf4j
@AllArgsConstructor
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    private RedisService redisService;
    private TouTiaoApiService touTiaoApiService;
    private AddressDao addressDao;
    private MapService mapService;
    private PairDao pairDao;

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public TokenDto login(LoginRequest loginRequest) {
        //code转换openId
        String openId = touTiaoApiService.code2Session(loginRequest.getCode());

        //用户不存在，尝试注册/存在则更新
        User user = baseMapper.selectByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setUsername(UuidUtils.generateUuid().replaceAll("-", ""));
            user.setNickname(loginRequest.getNickname());
            user.setAvatar(loginRequest.getAvatar());
            user.setSex(loginRequest.getSex());
            baseMapper.insert(user);
        }
        Integer userId = user.getId();

        //设置/更新用户地址
        setAddress(loginRequest.getLng(), loginRequest.getLat(), userId);

        //返回token
        return createTokenDto(userId);
    }

    /**
     * 设置/更新用户地址
     */
    private void setAddress(String lng, String lat, Integer userId) {
        if (lng == null || lat == null) {
            return;
        }
        MapDto mapDto = mapService.geocoder(lng, lat, 0);
        ResultBean result = mapDto.getResult();
        Address address = addressDao.selectByUserId(userId);
        AdInfoBean adInfo = result.getAdInfo();
        if (address == null) {
            address = new Address();
            address.setUserId(userId);
            address.setLng(lng);
            address.setLat(lat);
            address.setProvince(adInfo.getProvince());
            address.setCity(adInfo.getProvince());
            address.setDetail(result.getAddress());
            addressDao.insert(address);
        } else {
            address.setLng(lng);
            address.setLat(lat);
            address.setProvince(adInfo.getProvince());
            address.setCity(adInfo.getProvince());
            address.setDetail(result.getAddress());
            address.setUpdateTime(new Date());
            addressDao.updateById(address);
        }
    }

    /**
     * 创建token
     */
    private TokenDto createTokenDto(Integer userId) {
        //创建token默认3天
        String token = UuidUtils.generateUuid().replaceAll("-", "");
        redisService.set(RedisKeys.getTokenKey(token), userId, RedisService.DAY_3);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setExpires(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2));

        //userId
        tokenDto.setUserId(userId);
        return tokenDto;
    }

    @Override
    public void logout() {
        redisService.delete(UserContext.getToken());
    }

    @Override
    public void update(User user) {
        //校验昵称
        String nickname = user.getNickname();
        if (StringUtils.isNotBlank(nickname)) {
            if (nickname.length() > 10) {
                throw new ServiceException(ErrorEnum.NICKNAME_LENGTH_10);
            }
        }
        //校验性别
        Integer sex = user.getSex();
        if (sex != null) {
            if (sex != 1 && sex != 2) {
                throw new ServiceException(ErrorEnum.ERROR_413);
            }
        }
        //校验手机
        String phone = user.getPhone();
        if (StringUtils.isNotBlank(phone)) {
            User phoneUser = baseMapper.selectByPhone(phone);
            if (phoneUser != null && !user.getId().equals(phoneUser.getId())) {
                throw new ServiceException(ErrorEnum.PHONE_BIND_ED);
            }
        }
        baseMapper.updateById(user);
    }

    @Override
    public UserDto info(Integer userId) {
        //权限限制
        canUseInfo(userId);

        //查询用户
        UserDto userDto = new UserDto();
        User user = baseMapper.selectById(userId);
        BeanUtils.copyProperties(user, userDto);

        //查询地址
        Address address = addressDao.selectByUserId(userId);
        if (address != null) {
            AddressDto addressDto = new AddressDto();
            BeanUtils.copyProperties(address, addressDto);
            userDto.setAddress(addressDto);
        }

        return userDto;
    }

    /**
     * 能否查询用户信息
     */
    private void canUseInfo(Integer userId) {
        Integer id = UserContext.getUserId();
        //是否自己
        if (id.equals(userId)) {
            return;
        }
        //是否有配对关系，不管成功与否
        int count = pairDao.countPairHistory(id, userId);
        if (count == 0) {
            throw new ServiceException(ErrorEnum.ERROR_412);
        }
    }

}