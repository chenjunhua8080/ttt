package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.entity.Dict;
import com.cjh.ttt.service.DictService;
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
 * (Dict)表控制层
 *
 * @author cjh
 * @since 2020-02-28 15:23:42
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/dict")
public class DictController {

    private DictService dictService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dict 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    public R selectAll(Page<Dict> page, Dict dict) {
        return R.ok(dictService.page(page, new QueryWrapper<>(dict)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/info/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(dictService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dict 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public R insert(@RequestBody Dict dict) {
        return R.ok(dictService.save(dict));
    }

    /**
     * 修改数据
     *
     * @param dict 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@RequestBody Dict dict) {
        return R.ok(dictService.updateById(dict));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("delete")
    public R delete(@RequestParam("id") Serializable id) {
        return R.ok(dictService.removeById(id));
    }
}