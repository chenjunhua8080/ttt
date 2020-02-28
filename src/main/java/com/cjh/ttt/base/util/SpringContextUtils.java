
package com.cjh.ttt.base.util;

import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Context 工具类
 *
 * @author cjh
 * @date 2020/2/27
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    public static volatile String profile;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
        SpringContextUtils.profile = applicationContext.getEnvironment().getRequiredProperty("spring.profiles.active");
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 根据注解返回bean
     */
    public static Map<String, Object> getBeansWithAnnotation(Class clazz) {
        return applicationContext.getBeansWithAnnotation(clazz);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    public static boolean isOnline() {
        return "online".equals(profile);
    }

}
