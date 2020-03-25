package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.base.util.ImgUtil;
import com.cjh.ttt.entity.Dict;
import com.cjh.ttt.service.DictService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统控制层
 *
 * @author cjh
 * @since 2020-02-28 15:23:42
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/system")
public class SystemController {

    private DictService dictService;

    /**
     * 列表
     *
     * @param dict 查询实体
     * @return 所有数据
     */
    @GetMapping("/dict/list")
    public R selectAll(Dict dict) {
        return R.ok(dictService.list(new QueryWrapper<>(dict)));
    }

    /**
     * 查询字典值，根据类型和键
     *
     * @param type 字典类型
     * @param key  字典key
     * @return 单条数据
     */
    @GetMapping("/dict/value")
    public R getValueByTypeAndKey(@RequestParam String type, @RequestParam Integer key) {
        return R.ok(dictService.getValueByTypeAndKey(type, key));
    }

    /**
     * 新增字典
     *
     * @param dict 实体对象
     * @return 新增结果
     */
    @PostMapping("/dict/insert")
    public R dictInsert(@RequestBody Dict dict) {
        return R.ok(dictService.save(dict));
    }

    /**
     * 修改字典
     *
     * @param dict 实体对象
     * @return 修改结果
     */
    @PostMapping("/dict/update")
    public R dictUpdate(@RequestBody Dict dict) {
        return R.ok(dictService.updateById(dict));
    }

    /**
     * 删除字典
     *
     * @param id 主键
     * @return 删除结果
     */
    @PostMapping("/dict/delete")
    public R dictDelete(@RequestParam("id") Serializable id) {
        return R.ok(dictService.removeById(id));
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public R upload(MultipartFile multipartFile) {
        String fileName = System.currentTimeMillis() + ".png";
        if (multipartFile == null) {
            return R.failed("获取上传文件失败");
        }
        if (multipartFile.isEmpty()) {
            return R.failed("没有选择文件");
        }
        //保存文件
        try {
            String realPath = ImgUtil.getRealPath(fileName);
            File file = new File(realPath);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return R.failed("文件上传发生异常");
        }

        return R.ok(ImgUtil.getResultPath(fileName));
    }
}