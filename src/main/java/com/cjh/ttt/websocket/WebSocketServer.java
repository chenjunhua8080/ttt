package com.cjh.ttt.websocket;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cjh.ttt.base.token.UserContext;
import com.cjh.ttt.base.util.JsonUtil;
import com.cjh.ttt.base.util.SpringContextUtils;
import com.cjh.ttt.dao.UserDao;
import com.cjh.ttt.dto.MessageDetailDto;
import com.cjh.ttt.dto.MessageDto;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.service.MessageService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * websocket交互
 *
 * @author cjh
 * @date 2020/3/27
 */
@ServerEndpoint(value = "/websocket/msg/{userId}", encoders = {WebSocketEncoder.class})
@Component
@Slf4j
public class WebSocketServer {

    private UserDao userDao;
    private MessageService messageService;

    {
        userDao = SpringContextUtils.getBean("userDao", UserDao.class);
        messageService = SpringContextUtils.getBean("messageService", MessageService.class);
        log.info("注入bean: {},{}", userDao, messageService);
    }

    /**
     * 所有用户Session
     */
    private static Map<String, Session> webSocketServerMap = new ConcurrentHashMap<>();

    /**
     * 1
     * 当前用户Session
     */
    private Session session;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) throws Exception {
        log.debug("用户id -> " + userId);
        User user = userDao.selectById(userId);
        if (user == null) {
            //为空则关闭
            session.close();
        } else {
            UserContext.setUser(user);
            //建立连接
            this.session = session;
            webSocketServerMap.put(userId.toString(), this.session);
            log.info("有新连接加入,当前用户ID为" + userId + "！当前在线人数为:" + webSocketServerMap.size());
            //获取列表
            IPage<MessageDto> initMsg = messageService.getMessageList(new Page<>());
            try {
                sendMessage(session, initMsg);
            } catch (Exception e) {
                System.out.println("websocket IO异常");
            }
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketServerMap.remove(userId);
        log.info(userId + "已下线,当前在线人数为" + webSocketServerMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, @PathParam("userId") Integer userId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Session thisSession = webSocketServerMap.get(userId.toString());
        switch (message) {
            case "getMessageList":
                //消息刷新
                IPage<MessageDto> initMsg = messageService.getMessageList(new Page<>());
                sendMessage(thisSession, initMsg);
                break;
            case "getUserOnloneCount":
                //获取在线用户数
                map.put("socketType", "getAllUserId");
                map.put("data", String.valueOf(webSocketServerMap.size()));
                sendMessage(thisSession, JSONObject.toJSONString(map));
                break;
            case "getAllUserId":
                //获取所有用户ID
                StringBuffer sb = new StringBuffer();
                for (String key : webSocketServerMap.keySet()) {
                    sb.append(key + " ");
                }
                map.put("socketType", "getAllUserId");
                map.put("data", sb.toString());
                sendMessage(thisSession, JSONObject.toJSONString(map));
                break;
            case "heartbeat":
                map.put("socketType", "heartbeat");
                map.put("data", null);
                sendMessage(thisSession, JSONObject.toJSONString(map));
                break;
            default:
                if (message.contains("getMessageDetail")) {
                    String[] strTemp = message.split("=");
                    String[] params = strTemp[1].split(",");
                    IPage<MessageDetailDto> msg = messageService.getDetailList(new Page<>(),Integer.parseInt(params[0]));
                    sendMessage(thisSession, msg);
                    break;
                }

                sendMessage(thisSession, "未知指令");
        }
    }

    /**
     * 错误消息
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("session: {}, error: ", session.getId(), error);
    }


    /**
     * 推送消息
     */

    public void pushMessage(MessageDto messageDto, Integer userId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("socketType", "getMessageItem");
            map.put("data", messageDto);
            Session thisSession = webSocketServerMap.get(userId);
            sendMessage(thisSession, JSONObject.toJSONString(map));
            log.debug("当前推送消息用户号:" + userId);
            log.debug("推送内容 -> " + JSONObject.toJSONString(map));
            log.info("推送成功");
        } catch (Exception e) {
            log.error("异常", e);
        }

    }


    public void pushMessage(String message, Integer userId) {
        try {
            Session thisSession = webSocketServerMap.get(userId);
            sendMessage(thisSession, message);
            log.debug("当前推送消息用户号:" + userId);
            log.debug("推送内容 -> " + message);
            log.info("推送成功");
        } catch (NullPointerException e) {
            log.warn("用户不在线，该消息无法实时推送");
        } catch (Exception e) {
            log.error("发送异常", e);
        }
    }


    /**
     * 服务端主动推送:消息类
     */
    public void sendMessage(Session thisSession, Object message) throws IOException {
        thisSession.getBasicRemote().sendText(JsonUtil.java2Json(message));
    }

    /**
     * 服务端主动推送:对象类
     */
    public void sendMessageWithObject(MessageDto messageDto) throws Exception {
        this.session.getBasicRemote().sendObject(messageDto);
    }


}

