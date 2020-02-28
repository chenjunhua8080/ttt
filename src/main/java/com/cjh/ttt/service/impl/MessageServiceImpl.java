package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.dao.MessageDao;
import com.cjh.ttt.entity.Message;
import com.cjh.ttt.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (Message)表服务实现类
 *
 * @author cjh
 * @since 2020-02-28 15:23:39
 */
@Slf4j
@AllArgsConstructor
@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {
    
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