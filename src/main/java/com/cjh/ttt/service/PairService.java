package com.cjh.ttt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.dto.PairDto;
import com.cjh.ttt.dto.PairSuccessDto;
import com.cjh.ttt.dto.UserDto;
import com.cjh.ttt.entity.Pair;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.request.PairingRequest;
import java.util.List;

/**
 * (Pair)表服务接口
 *
 * @author cjh
 * @since 2020-03-16 22:56:18
 */
public interface PairService extends IService<Pair> {


    /**
     * 获取配对人员列表，根据类型
     */
    PairDto getPairList(Page<User> page, Integer type);

    /**
     * 执行配对
     */
    void pairing(PairingRequest pairingRequest);

    /**
     * 配对检测
     */
    int check(Integer id);

    /**
     * 查询配对成功结果
     */
    IPage<PairSuccessDto> getPairSuccessList(Page<Pair> page);

    /**
     * 查询用户待确认配对列表
     */
    List<UserDto> getNewPairList(Integer userId);

    /**
     * [同意/拒绝]配对
     */
    void updateStatus(Integer sender, Integer status);
}