package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.dao.FriendDao;
import com.cjh.ttt.entity.Friend;
import com.cjh.ttt.service.FriendService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (Friend)表服务实现类
 *
 * @author cjh
 * @since 2020-02-28 15:23:40
 */
@Slf4j
@AllArgsConstructor
@Service("friendService")
public class FriendServiceImpl extends ServiceImpl<FriendDao, Friend> implements FriendService {
    
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