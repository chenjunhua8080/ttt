package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.cjh.ttt.request.PairingRequest;
import com.cjh.ttt.service.PairService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [匹配] 模块
 *
 * @author cjh
 * @since 2020年3月16日20:46:32
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/pair")
public class PairController {

    private PairService pairService;

    /**
     * 获取配对列表
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(pairService.getPairList());
    }

    /**
     * 配对检测
     */
    @GetMapping("/check")
    public R check(Integer id) {
        return R.ok(pairService.check(id));
    }

    /**
     * 发起配对
     */
    @PostMapping("/pairing")
    public R pairing(@Valid @RequestBody PairingRequest pairingRequest) {
        pairService.pairing(pairingRequest);
        return R.ok("发送成功");
    }

}