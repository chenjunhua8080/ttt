package com.cjh.ttt.base.map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.util.OkHttpUtil;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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


    /**
     * 地球平均半径,单位：m
     */
    private static final double EARTH_RADIUS = 6371393;

    /**
     * 通过AB点经纬度获取距离
     *
     * @param lngA A点(经，纬)
     * @param latA A点(经，纬)
     * @param lngB B点(经，纬)
     * @param latB B点(经，纬)
     * @return 距离(单位 ： 米)
     *     <p>
     *     https://blog.csdn.net/jk940438163/article/details/83147557#commentsedit
     */
    public double getDistance(double lngA, double latA, double lngB, double latB) {
        Point2D pointA = new Double(lngA, latA);
        Point2D pointB = new Double(lngB, latB);
        // 经纬度（角度）转弧度。弧度用作参数，以调用Math.cos和Math.sin
        // A经弧度
        double radiansAx = Math.toRadians(pointA.getX());
        // A纬弧度
        double radiansAy = Math.toRadians(pointA.getY());
        // B经弧度
        double radiansBx = Math.toRadians(pointB.getX());
        // B纬弧度
        double radiansBy = Math.toRadians(pointB.getY());

        // 公式中“cosβ1cosβ2cos（α1-α2）+sinβ1sinβ2”的部分，得到∠AOB的cos值
        double cos = Math.cos(radiansAy) * Math.cos(radiansBy) * Math.cos(radiansAx - radiansBx)
            + Math.sin(radiansAy) * Math.sin(radiansBy);
//        System.out.println("cos = " + cos); // 值域[-1,1]
        // 反余弦值
        double acos = Math.acos(cos);
//        System.out.println("acos = " + acos); // 值域[0,π]
//        System.out.println("∠AOB = " + Math.toDegrees(acos)); // 球心角 值域[0,180]
        // 最终结果
        return EARTH_RADIUS * acos;
    }

    public double getDistance(String lngA, String latA, String lngB, String latB) {
        return getDistance(
            java.lang.Double.parseDouble(lngA),
            java.lang.Double.parseDouble(latA),
            java.lang.Double.parseDouble(lngB),
            java.lang.Double.parseDouble(latB)
        );
    }

}
