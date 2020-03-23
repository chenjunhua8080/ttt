package com.cjh.ttt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cjh.ttt.base.map.MapDto;
import com.cjh.ttt.base.map.MapDto.ResultBean.AdInfoBean;
import com.cjh.ttt.base.map.MapService;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.dao.AddressDao;
import com.cjh.ttt.entity.Address;
import com.cjh.ttt.request.AddressRequest;
import com.cjh.ttt.service.AddressService;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        Address address = new Address();
        AdInfoBean adInfo = mapDto.getResult().getAdInfo();
        BeanUtils.copyProperties(adInfo, address);
        address.setUserId(userId);
        address.setLng(lng);
        address.setLat(lat);
        address.setDetail(mapDto.getResult().getAddress());
        baseMapper.updateById(address);
    }
}