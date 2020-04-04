package com.cjh.ttt.base.token;

import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.service.UserService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * token拦截器
 *
 * @author cjh
 * @date 2020/2/28
 */
@Component
@AllArgsConstructor
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private RedisService redisService;
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //如果是对应controller的url，handler是HandlerMethod
        //否则是ResourceHttpRequestHandler
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        //跳过不拦截的url
        if (continueUrl(request)) {
            return true;
        }

        //校验token
        String token = request.getHeader("token");
        token = token != null ? token : request.getParameter("token");
        if (token == null) {
            throw new ServiceException(ErrorEnum.TOKEN_NULL);
        }
        String userId = redisService.get(RedisKeys.getTokenKey(token.trim()));
        if (userId == null) {
            throw new ServiceException(ErrorEnum.TOKEN_EXPIRE);
        }

        //设置user到上下文
        User user = userService.getById(userId);
        user.setToken(token);
        UserContext.setUser(user);

        //刷新token
        redisService.expire(token, RedisService.DAY_3);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {
        //清除
        UserContext.clearUser();
    }

    /**
     * 是否跳过拦截
     */
    private boolean continueUrl(HttpServletRequest request) {
        String url = request.getServletPath();
        String[] strings = {
            "/user/login.*",
            "/user/logout.*",
            "/test.*",
            "/error"
        };
        boolean match = false;
        for (String item : strings) {
            if (Pattern.matches(item, url)) {
                match = true;
            }
        }
        log.info("〓〓〓〓 {} 〓〓 {} : {}",
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
            match,
            url);
        return match;
    }

}
