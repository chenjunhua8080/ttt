package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.LoginRequest;

/**
 * 用户表(User)表服务接口
 *
 * @author cjh
 * @since 2020-02-27 15:16:43
 */
public interface UserService extends IService<User> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 登录获取token
     */
    TokenDto login(LoginRequest loginRequest);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 修改资料
     */
    void update(User user);

    /**
     * 查询用户信息
     */
    UserDto info(Integer userId);
}