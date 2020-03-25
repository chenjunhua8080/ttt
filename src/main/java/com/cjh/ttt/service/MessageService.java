package com.cjh.ttt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cjh.ttt.entity.Message;

/**
 * 消息表(Message)表服务接口
 *
 * @author cjh
 * @since 2020-03-25 18:16:48
 */
public interface MessageService extends IService<Message> {

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}