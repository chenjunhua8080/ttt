package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.entity.Message;
import com.cjh.ttt.service.MessageService;
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
 * (Message)表控制层
 *
 * @author cjh
 * @since 2020-02-28 15:23:40
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private MessageService messageService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param message 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    public R selectAll(Page<Message> page, Message message) {
        return R.ok(messageService.page(page, new QueryWrapper<>(message)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/info/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(messageService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param message 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public R insert(@RequestBody Message message) {
        return R.ok(messageService.save(message));
    }

    /**
     * 修改数据
     *
     * @param message 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@RequestBody Message message) {
        return R.ok(messageService.updateById(message));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("delete")
    public R delete(@RequestParam("id") Serializable id) {
        return R.ok(messageService.removeById(id));
    }
}