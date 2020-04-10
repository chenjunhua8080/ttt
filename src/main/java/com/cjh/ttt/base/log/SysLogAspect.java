package com.cjh.ttt.base.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qingzu.common.base.annotation.SysLog;
import com.qingzu.common.base.jwt.UserContext;
import com.qingzu.common.base.util.IPUtils;
import com.qingzu.renter.modules.sys.po.SysLogPO;
import com.qingzu.renter.modules.sys.service.SysLogService;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 系统日志，切面处理类<br/> 切面通知类型：@Before 前置通知
 *
 * @AfterReturning 后置通知
 * @After 最终通知
 * @AfterThrowing 异常通知
 * @Around 环绕通知 这5种类型的通知，在内部调用时这样组织<br/> try {<br/> 调用前置通知 环绕前置处理 调用目标对象方法 环绕后置处理 调用后置通知 }catch(Exception e) {<br/>
 * 调用异常通知 }finally{<br/> 调用最终通知 }<br/>
 * @date 2019年2月21日 上午10:59:27
 */
@Slf4j
// 该注解标示该类为切面类
@Aspect
// 注入依赖
@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    // 定义一个切入点 @SysLogPO
    @Pointcut("execution(* com.qingzu.community..controller..*.*(..)) && (@annotation(com.qingzu.common.base.annotation.SysLog) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.RequestMapping))")
    public void logPointCut() {
    }

    // 环绕通知 方法访问前后处理
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Throwable ex = null;
        Object pointResult = null;
        String result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            pointResult = joinPoint.proceed();
            result = JSON.toJSONString(pointResult, SerializerFeature.WriteDateUseDateFormat);
        } catch (JSONException e) {

        } catch (Throwable e) {
            result = ObjectUtils.toString(e.getMessage(), "");
            // log.error("异常", e);
            ex = e;
        }
        // 执行时长(毫秒)
        int time = (int) (System.currentTimeMillis() - beginTime);

        // 获取request,这种方式获取不到PHP请求过来的request参数，所以换一种方式，直接从方法参数中获取
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getRequest();
        Object[] methodArgs = joinPoint.getArgs();
        for (int i = 0; i < methodArgs.length; i++) {
            if (methodArgs[i] instanceof HttpServletRequest) {
                request = (HttpServletRequest) methodArgs[i];
                break;
            }
        }
        if (ex == null) {
            // GET请求时长小于6s不做日志
            if (RequestMethod.GET.name().equals(request.getMethod().toUpperCase()) && time <= 6000) {
                return pointResult;
            }
        }
        //保存日志
        saveSysLogPO(joinPoint, request, time, result);

        if (ex != null) {
            // 继续抛出等待过滤器捕捉
            throw ex;
        }
        return pointResult;
    }

    private void saveSysLogPO(ProceedingJoinPoint joinPoint, HttpServletRequest request, int time, String result) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            SysLogPO sysLogPO = new SysLogPO();
            SysLog SysLogPO = method.getAnnotation(SysLog.class);
            if (SysLogPO != null) {
                //注解上的描述
                sysLogPO.setOperation(SysLogPO.value());
            }

            //请求的方法名
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();
            sysLogPO.setMethod(request.getServletPath() + "【" + className + "." + methodName + "()】");

            //请求的参数
            Map<String, String[]> requestParams = request.getParameterMap();
            Map<String, String> args = new TreeMap<String, String>();
            if (requestParams != null) {
                for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                    String name = entry.getKey();
                    String[] values = entry.getValue();
                    String valueStr = "";
                    for (int i = 0; i < values.length; i++) {
                        valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                    }
                    // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                    // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                    args.put(name, valueStr);
                }
            }
            String params = JSON.toJSONString(args);
            if (request.getContentType() != null && request.getContentType().contains("application/json")) {
                Object[] methodArgs = joinPoint.getArgs();
                if (methodArgs.length > 0) {
                    if (methodArgs[0] instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) methodArgs[0];
                        params = jsonArray.toJSONString();
                    } else if (methodArgs[0] instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) methodArgs[0];
                        params = jsonObject.toJSONString();
                    } else {
                        params = JSON.toJSONString(methodArgs[0]);
                    }
                }
            }
            sysLogPO.setParams(params);

            //设置IP地址
            sysLogPO.setIp(IPUtils.getIpAddr(request));

            //用户名

            sysLogPO.setUsername(UserContext.getJwtUser() != null ? UserContext.getUserName() : null);

            sysLogPO.setTime(time);
            sysLogPO.setResult(result);
            // 平台类型：1-租客、2-房东，3-硬件安装工，4-后台管理员，5-物业管理员，6-crm用户，7-轻租智慧社区
            sysLogPO.setPlatfromType((byte) 7);
            //保存系统日志
            sysLogService.save(sysLogPO);
        } catch (Exception e) {
            log.error("add SysLogPO error: {}", e.getMessage());
        }
    }

}
