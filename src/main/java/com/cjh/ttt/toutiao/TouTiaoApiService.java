package com.cjh.ttt.toutiao;

import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.base.util.ImgUtil;
import com.cjh.ttt.base.util.JsonUtil;
import com.cjh.ttt.base.util.OkHttpUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 头条api
 * https://developer.toutiao.com/dev/cn/mini-app/develop/server/log-in/code2session
 *
 * @author cjh
 * @date 2020/3/10
 */
@Component
@AllArgsConstructor
@Slf4j
public class TouTiaoApiService {

    private TouTiaoConfig touTiaoConfig;
    private RedisService redisService;

    /**
     * appid	小程序 ID
     * secret	小程序的 APP Secret，可以在开发者后台获取
     * code	login接口返回的登录凭证
     * anonymous_code	login接口返回的匿名登录凭证
     */
    private static final String GET_CODE_2_SESSION = "https://developer.toutiao.com/api/apps/jscode2session";
    /**
     * appid	小程序 ID
     * secret	小程序的 APP Secret，可以在开发者后台获取
     * grant_type	获取 access_token 时值为 client_credential
     */
    private static final String GET_ACCESS_TOKEN = "https://developer.toutiao.com/api/apps/token";
    /**
     * access_token	String	是	服务端 API 调用标识，获取方法
     * touser	String	是	要发送给用户的 open_id, open_id 的获取请参考登录
     * template_id	String	是	在开发者平台配置消息模版后获得的模版 id
     * page	String	否	点击消息卡片之后打开的小程序页面地址，空则无跳转
     * form_id	String	是	可以通过<form />组件获得 form_id, 获取方法
     * data	dict<String, SubData>	是	模板中填充着的数据，key 必须是 keyword 为前缀
     */
    private static final String POST_SEND_TEMPLATE = "https://developer.toutiao.com/api/apps/game/template/send";
    /**
     * access_token	是		服务端 API 调用标识，获取方法
     * appname	否	toutiao	是打开二维码的字节系 app 名称，默认为今日头条，取值如下表所示
     * path	否		小程序/小游戏启动参数，小程序则格式为 encode({path}?{query})，小游戏则格式为 JSON 字符串，默认为空
     * width	否	430	二维码宽度，单位 px，最小 280px，最大 1280px，默认为 430px
     * line_color	否	{"r":0,"g":0,"b":0}	二维码线条颜色，默认为黑色
     * background	否		二维码背景颜色，默认为透明
     * set_icon	否	FALSE	是否展示小程序/小游戏 icon，默认不展示
     */
    private static final String POST_CREATE_QR_CODE = "https://developer.toutiao.com/api/apps/qrcode";

    //#################################################################

    /**
     * code换取openId
     */
    public String code2Session(String code) {
        HashMap<String, String> params = new HashMap<>(3);
        params.put("appid", touTiaoConfig.getAppid());
        params.put("secret", touTiaoConfig.getSecret());
        params.put("code", code);
        RespBody respBody = sendGetRequest(GET_CODE_2_SESSION, params);
        return respBody.getOpenid();
    }

    /**
     * 获取基础支持
     */
    public String getAccessToken() {
        //查询缓存，获取取不到在请求新的
        String accessToken = redisService.get(RedisKeys.getToutiaoTokenKey());
        if (accessToken != null) {
            return accessToken;
        }
        HashMap<String, String> params = new HashMap<>(3);
        params.put("appid", touTiaoConfig.getAppid());
        params.put("secret", touTiaoConfig.getSecret());
        params.put("grant_type", "client_credential");
        RespBody respBody = sendGetRequest(GET_ACCESS_TOKEN, params);
        accessToken = respBody.getAccess_token();
        redisService.set(RedisKeys.getToutiaoTokenKey(), accessToken, RedisService.HOUSE_1 * 2);
        return accessToken;
    }

    /**
     * 发送模板消息
     */
    public void sendTemplate(String openId, String templateId, String formId, String data) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("access_token", getAccessToken());
        params.put("touser", openId);
        params.put("template_id", templateId);
        params.put("page", null);
        params.put("form_id", formId);
        params.put("data", data);
        sendPostRequest(POST_SEND_TEMPLATE, params);
    }

    /**
     * 创建二维码
     */
    public String createQrCode() {
        Map<String, Object> params = new HashMap<>(7);
        params.put("access_token", getAccessToken());
        params.put("appname", null);
        params.put("path", null);
        params.put("width", null);
        params.put("line_color", null);
        params.put("background", null);
        params.put("set_icon", true);
        InputStream inputStream = sendPostRequestGetIO(POST_CREATE_QR_CODE, params);
        String file = ImgUtil.inputStream2file(inputStream);
        if (file == null) {
            throw new ServiceException("二维码生成失败");
        }
        return file;
    }


    /**
     * 统一执行get请求
     */
    private RespBody sendGetRequest(String url, HashMap<String, String> params) {
        return getRespBody(url, params, OkHttpUtil.get(url, params));
    }

    /**
     * 统一执行post请求
     */
    private RespBody sendPostRequest(String url, Map<String, Object> params) {
        String json = JsonUtil.java2Json(params);
        return getRespBody(url, json, OkHttpUtil.postJson(url, json));
    }

    /**
     * 统一执行post请求
     */
    private InputStream sendPostRequestGetIO(String url, Map<String, Object> params) {
        String json = JsonUtil.java2Json(params);
        log.info("########## start #######");
        log.info("url -> {}", url);
        log.info("params -> {}", params);
        InputStream inputStream = OkHttpUtil.postJsonReturnIO(url, json);
        try {
            log.info("resp -> size: {}", inputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("########## end #######");
        return inputStream;
    }

    /**
     * 统一记录日志
     */
    private RespBody getRespBody(String url, Object params, String resp) {
        log.info("########## start #######");
        log.info("url -> {}", url);
        log.info("params -> {}", params);
        log.info("resp -> {}", resp);
        log.info("########## end #######");
        RespBody respBody = JsonUtil.json2java(resp, RespBody.class);
        if (respBody.getErrcode() != 0) {
            throw new ServiceException(respBody.getErrcode(), respBody.getErrmsg());
        }
        return respBody;
    }

}
