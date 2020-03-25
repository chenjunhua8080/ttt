package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.dao.MessageDetailDao;
import com.cjh.ttt.entity.MessageDetail;
import com.cjh.ttt.service.MessageDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 消息内容表(MessageDetail)表服务实现类
 *
 * @author cjh
 * @since 2020-03-25 18:16:50
 */
@Slf4j
@AllArgsConstructor
@Service("messageDetailService")
public class MessageDetailServiceImpl extends ServiceImpl<MessageDetailDao, MessageDetail> implements MessageDetailService {
    
    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return baseMapper.deleteById(id) > 0;
    }
}