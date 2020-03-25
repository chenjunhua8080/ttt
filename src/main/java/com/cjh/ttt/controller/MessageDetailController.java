package com.cjh.ttt.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.entity.MessageDetail;
import com.cjh.ttt.service.MessageDetailService;
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
 * 消息内容表(MessageDetail)表控制层
 *
 * @author cjh
 * @since 2020-03-25 18:16:50
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/messageDetail")
public class MessageDetailController {

    private MessageDetailService messageDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param messageDetail 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    public R selectAll(Page<MessageDetail> page, MessageDetail messageDetail) {
        return R.ok(messageDetailService.page(page, new QueryWrapper<>(messageDetail)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(messageDetailService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param messageDetail 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public R insert(@RequestBody MessageDetail messageDetail) {
        return R.ok(messageDetailService.save(messageDetail));
    }

    /**
     * 修改数据
     *
     * @param messageDetail 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@RequestBody MessageDetail messageDetail) {
        return R.ok(messageDetailService.updateById(messageDetail));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("delete")
    public R delete(@RequestParam("id") Serializable id) {
        return R.ok(messageDetailService.removeById(id));
    }
}