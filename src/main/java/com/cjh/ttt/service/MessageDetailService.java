package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.entity.MessageDetail;

/**
 * 消息内容表(MessageDetail)表服务接口
 *
 * @author cjh
 * @since 2020-03-25 18:16:50
 */
public interface MessageDetailService extends IService<MessageDetail> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}