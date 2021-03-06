package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.map.MapDto;
import com.cjh.ttt.base.map.MapDto.ResultBean;
import com.cjh.ttt.base.map.MapDto.ResultBean.AdInfoBean;
import com.cjh.ttt.base.map.MapService;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dao.AddressDao;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.request.AddressRequest;
import com.cjh.ttt.service.AddressService;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 地址表(Address)表服务实现类
 *
 * @author cjh
 * @since 2020-03-19 15:41:08
 */
@Slf4j
@AllArgsConstructor
@Service("addressService")
public class AddressServiceImpl extends ServiceImpl<AddressDao, Address> implements AddressService {

    private MapService mapService;

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
    public void update(AddressRequest addressRequest) {
        Integer userId = UserContext.getUserId();
        String lng = addressRequest.getLng();
        String lat = addressRequest.getLat();
        MapDto mapDto = mapService.geocoder(lng, lat, 0);
        ResultBean result = mapDto.getResult();
        Address address = baseMapper.selectByUserId(userId);
        AdInfoBean adInfo = result.getAdInfo();
        if (address == null) {
            address = new Address();
            address.setUserId(userId);
            address.setLng(lng);
            address.setLat(lat);
            address.setProvince(adInfo.getProvince());
            address.setCity(adInfo.getCity());
            address.setDetail(result.getAddress());
            baseMapper.insert(address);
        } else {
            address.setLng(lng);
            address.setLat(lat);
            address.setProvince(adInfo.getProvince());
            address.setCity(adInfo.getCity());
            address.setDetail(result.getAddress());
            address.setUpdateTime(new Date());
            baseMapper.updateById(address);
        }
    }
}