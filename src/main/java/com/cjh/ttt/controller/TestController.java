package com.cjh.ttt.controller;


import cn.hutool.core.io.resource.ResourceUtil;
import com.baomidou.mybatisplus.extension.api.R;
import java.io.FileNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * test
 *
 * @author cjh
 * @since 2020-02-27 17:51:12
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public R hello() {
        return R.ok("hello");
    }

    @GetMapping("/location")
    public R getLocation() throws FileNotFoundException {
        return R.ok(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath());
    }
}