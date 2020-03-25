package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.LoginRequest;
import com.cjh.ttt.request.UserUpdateRequest;
import com.cjh.ttt.service.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [我的] 模块
 *
 * @author cjh
 * @since 2020-02-27 17:51:12
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    /**
     * 授权登录
     *
     * @param loginRequest 实体
     * @return token
     */
    @PostMapping("/login")
    public R login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenDto tokenDto = userService.login(loginRequest);
        return R.ok(tokenDto);
    }


    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R logout() {
        userService.logout();
        return R.ok("退出成功");
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/info/{id}")
    public R selectOne(@PathVariable Integer id) {
        UserDto dto = userService.info(id);
        return R.ok(dto);
    }

    /**
     * 修改用户信息
     *
     * @param updateRequest 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody UserUpdateRequest updateRequest) {
        Integer userId = UserContext.getUserId();
        User user = new User();
        BeanUtils.copyProperties(updateRequest, user);
        user.setId(userId);
        userService.update(user);
        return R.ok("修改成功");
    }

}