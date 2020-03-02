package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.dto.TokenDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.service.UserService;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户表(User)表控制层
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
     * 登录
     *
     * @param user 实体
     * @return token
     */
    @PostMapping("/login")
    public R login(@RequestBody User user) {
        TokenDto tokenDto = userService.login(user.getUsername());
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
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param user 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    public R selectAll(Page<User> page, User user) {
        return R.ok(this.userService.page(page, new QueryWrapper<>(user)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/info/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(this.userService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param user 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public R insert(@RequestBody User user) {
        return R.ok(this.userService.save(user));
    }

    /**
     * 修改数据
     *
     * @param user 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@RequestBody User user) {
        return R.ok(this.userService.updateById(user));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("delete")
    public R delete(@RequestParam("id") Serializable id) {
        return R.ok(this.userService.removeById(id));
    }
}