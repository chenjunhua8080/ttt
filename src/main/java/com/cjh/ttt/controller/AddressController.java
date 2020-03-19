package com.cjh.ttt.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.service.AddressService;
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
 * 地址表(Address)表控制层
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param address 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    public R selectAll(Page<Address> page, Address address) {
        return R.ok(addressService.page(page, new QueryWrapper<>(address)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public R selectOne(@PathVariable Serializable id) {
        return R.ok(addressService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param address 实体对象
     * @return 新增结果
     */
    @PostMapping("/insert")
    public R insert(@RequestBody Address address) {
        return R.ok(addressService.save(address));
    }

    /**
     * 修改数据
     *
     * @param address 实体对象
     * @return 修改结果
     */
    @PostMapping("/update")
    public R update(@RequestBody Address address) {
        return R.ok(addressService.updateById(address));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("delete")
    public R delete(@RequestParam("id") Serializable id) {
        return R.ok(addressService.removeById(id));
    }
}