package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.dao.DictDao;
import com.cjh.ttt.dto.DictDto;
import com.cjh.ttt.entity.Dict;
import com.cjh.ttt.service.DictService;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (Dict)表服务实现类
 *
 * @author cjh
 * @since 2020-02-28 15:23:42
 */
@Slf4j
@AllArgsConstructor
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictDao, Dict> implements DictService {

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

    @Override
    public DictDto getValueByTypeAndKey(String type, Integer key) {
        return baseMapper.getValueByTypeAndKey(type, key);
    }
}