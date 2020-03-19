package com.cjh.ttt.base.token;

import com.cjh.ttt.base.error.ErrorEnum;
import com.cjh.ttt.base.error.ServiceException;
import com.cjh.ttt.base.redis.RedisKeys;
import com.cjh.ttt.base.redis.RedisService;
import com.cjh.ttt.entity.User;
import com.cjh.ttt.service.UserService;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * token拦截器
 *
 * @author cjh
 * @date 2020/2/28
 */
@Component
@AllArgsConstructor
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private RedisService redisService;
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
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
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
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
            "/error.*",
            "/favicon.ico"
        };
        boolean match = false;
        for (String item : strings) {
            if (Pattern.matches(item, url)) {
                match = true;
            }
        }
        System.err.println("******************" + url + "************" + match);
        return match;
    }

}
