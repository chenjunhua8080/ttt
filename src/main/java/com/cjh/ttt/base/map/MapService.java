package com.cjh.ttt.base.map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.util.OkHttpUtil;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 腾讯地图api
 *
 * @author cjh
 * @date 2020/2/24
 */
@Slf4j
@RefreshScope
@Component
public class MapService {

    @Value("${map.key}")
    private String key;

    /**
     * 逆地址解析
     * https://lbs.qq.com/webservice_v1/guide-gcoder.html
     */
    private final static String url_geocoder = "https://apis.map.qq.com/ws/geocoder/v1/?location=LAT,LNG&key=KEY&get_poi=POI";

    /**
     * 逆地址解析
     */
    public MapDto geocoder(String lng, String lat, int poi) {
        String url = url_geocoder.replace("KEY", key)
            .replace("LNG", lng)
            .replace("LAT", lat)
            .replace("POI", String.valueOf(poi));
        JSONObject jsonObject = getCommon(url, null);
        if (0 != jsonObject.getIntValue("status")) {
            throw new ServiceException(ErrorEnum.MAP_ERROR.getCode(),
                ErrorEnum.MAP_ERROR.getName() + ": " + jsonObject.getString("message"));
        }
        return jsonObject.toJavaObject(MapDto.class);
    }

    /**
     * get共有的方法
     *
     * @param url    :参数
     * @param params : 接口名字
     */
    private JSONObject getCommon(String url, Map<String, String> params) {
        log.debug("请求腾讯地图api，url --> {}", url);
        log.debug("请求腾讯地图api，params --> {}", params);
        String resp = OkHttpUtil.get(url, params);
        // 返回结果解析
        JSONObject jsonObject = JSON.parseObject(resp);
        log.debug("请求腾讯地图api，resp --> {}", resp);
        return jsonObject;
    }

}
