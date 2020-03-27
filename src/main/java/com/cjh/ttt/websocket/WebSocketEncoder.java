package com.cjh.ttt.websocket;

import com.alibaba.fastjson.JSONObject;
import com.cjh.ttt.dto.MessageDto;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * encoder
 *
 * @author cjh
 * @date 2020/3/27
 */
@Slf4j
public class WebSocketEncoder implements Encoder.Text<MessageDto> {


    @Override
    public String encode(MessageDto messageDto) {
        try {
            return JSONObject.toJSONString(messageDto);
        } catch (Exception e) {
            log.error("encode异常", e);
            return null;
        }

    }


    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
