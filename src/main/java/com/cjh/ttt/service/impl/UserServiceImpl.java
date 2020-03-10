package com.cjh.ttt.service.impl;

import com.alibaba.nacos.common.util.UuidUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.interceptor.UserContext;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.service.UserService;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public TokenDto login(String code) {
        User user = baseMapper.selectByUserName(code);
        //用户不存在，尝试注册
        if (user == null) {
            user = new User();
            user.setUsername(code);
            user.setNickname(code);
            baseMapper.insert(user);
        }
        //创建token默认3天
        String uuid = UuidUtils.generateUuid();
        redisService.set(RedisKeys.getTokenKey(uuid), user.getId(), RedisService.DAY_3);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(uuid);
        tokenDto.setExpires(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2));

        return tokenDto;
    }

    @Override
    public void logout() {
        redisService.delete(UserContext.getToken());
    }
}