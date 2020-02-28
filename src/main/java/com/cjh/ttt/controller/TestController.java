package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.extension.api.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}