package com.cjh.ttt.base.error;

import com.baomidou.mybatisplus.extension.api.IErrorCode;
import com.baomidou.mybatisplus.extension.api.R;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author cjh
 * @date 2020/2/28
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
    public R handleApiException(ServiceException e) {
        if (ErrorEnum.TOKEN_NULL.getCode() == e.getCode()) {
            log.error(e.getMessage());
        } else {
            log.error(e.getMessage(), e);
        }
        return R.failed(new IErrorCode() {
            @Override
            public long getCode() {
                return e.getCode();
            }

            @Override
            public String getMsg() {
                return e.getMessage();
            }
        });
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return R.failed("数据库中已存在该记录");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public R handleNullException(NullPointerException e) {
        log.error(e.getMessage(), e);
        return R.failed("为空异常");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BindException.class)
    public R handleException(BindException e) {
        // 获得的类型为typeMismatch.java.lang.Long，需要去掉typeMismatch.前缀
        String fieldType = e.getFieldError().getCodes()[2].substring(e.getFieldError().getCodes()[3].length() + 1);
        if (!"typeMismatch".equals(e.getFieldError().getCodes()[3])) {
            // 获取注解错误信息
            return R.failed(e.getFieldError().getDefaultMessage());
        }
        log.error(
            "参数类型不正确,参数名:" + e.getFieldError().getField() + ",参数值:" + e.getFieldError().getRejectedValue() + ",类型应该为:"
                + fieldType);
        return R.failed("参数类型不正确");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        log.error("参数不正确,参数名:" + fieldErrors.get(0).getField() + ",错误结果:" + fieldErrors.get(0).getDefaultMessage());
        return R.failed(fieldErrors.get(0).getDefaultMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public R handleException(HttpMessageNotReadableException e) {
        return R.failed("参数类型不正确:" + e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    public R handleException(MissingServletRequestParameterException e) {
        return R.failed("缺少参数:" + e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R handleException(HttpRequestMethodNotSupportedException e) {
        return R.failed("请求方式不正确");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public R handle(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof ConstraintViolationException) {
            return R.failed("参数异常：" + e.getMessage());
        } else if (e instanceof SQLSyntaxErrorException) {
            return R.failed("sql异常：" + e.getMessage());
        } else {
            return R.failed(e.getMessage());
        }
    }

}
