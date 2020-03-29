package com.cjh.ttt.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.PairUpdateRequest;
import com.cjh.ttt.request.PairingRequest;
import com.cjh.ttt.request.UserIdRequest;
import com.cjh.ttt.service.PairService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * 获取匹配列表
     */
    @GetMapping("/list")
    public R list(Page<User> page, Integer type) {
        return R.ok(pairService.getPairList(page, type));
    }

    /**
     * 获取配对信息
     */
    @GetMapping("/info/{userId}")
    public R info(@PathVariable Integer userId) {
        return R.ok(pairService.getPairInfo(userId));
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

    /**
     * [同意/拒绝]配对
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody PairUpdateRequest pairUpdateRequest) {
        pairService.updateStatus(pairUpdateRequest.getSender(),pairUpdateRequest.getStatus());
        return R.ok("操作成功");
    }


    /**
     * 解除配对
     */
    @PostMapping("/relieve")
    public R relieve(@Valid @RequestBody UserIdRequest UserIdRequest) {
        pairService.relieve(UserIdRequest.getUserId());
        return R.ok("解除成功");
    }

    /**
     * 查询待我同意的配对列表
     */
    @GetMapping("/getNewPairList")
    public R getNewPairList() {
        Integer userId = UserContext.getUserId();
        return R.ok(pairService.getNewPairList(userId));
    }

    /**
     * 配对成功列表公告
     */
    @GetMapping("/getOkPairList")
    public R getPairSuccessList(Page<Pair> page) {
        return R.ok(pairService.getPairSuccessList(page));
    }
}