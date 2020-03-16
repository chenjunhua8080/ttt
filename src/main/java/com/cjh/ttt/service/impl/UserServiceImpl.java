package com.cjh.ttt.service.impl;

import com.alibaba.nacos.common.util.UuidUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.interceptor.UserContext;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.LoginReq;
import com.cjh.ttt.service.UserService;
import com.cjh.ttt.toutiao.TouTiaoApiService;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    public TokenDto login(LoginReq loginReq) {
        String openId = touTiaoApiService.code2Session(loginReq.getCode());
        User user = baseMapper.selectByOpenId(openId);

        //用户不存在，尝试注册/存在则更新
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setUsername(UuidUtils.generateUuid().replaceAll("-", ""));
            user.setNickname(loginReq.getNickname());
            user.setAvatar(loginReq.getAvatar());
            user.setSex(loginReq.getSex());
            baseMapper.insert(user);
        }

        //创建token默认3天
        String token = UuidUtils.generateUuid().replaceAll("-", "");
        redisService.set(RedisKeys.getTokenKey(token), user.getId(), RedisService.DAY_3);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(token);
        tokenDto.setExpires(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2));

        //userId
        tokenDto.setUserId(user.getId());
        return tokenDto;
    }

    @Override
    public void logout() {
        redisService.delete(UserContext.getToken());
    }

    @Override
    public void update(User user) {
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
}