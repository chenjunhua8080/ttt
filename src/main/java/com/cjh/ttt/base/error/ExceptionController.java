package com.cjh.ttt.base.error;

import com.baomidou.mybatisplus.extension.api.R;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 捕获错误页面
 */
@Controller
public class ExceptionController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = {"/error"})
    @ResponseBody
    public R error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        ErrorEnum errorEnum = null;
        if (statusCode == 404) {
            errorEnum = ErrorEnum.from(statusCode);
        } else {
            Object error = request
                .getAttribute("org.springframework.boot.web.servlet.error.DefaultErrorAttributes.ERROR");
            ServiceException exception;
            if (error instanceof ServiceException) {
                exception = (ServiceException) error;
                errorEnum = ErrorEnum.from(exception.getCode());
            }
        }
        return R.failed(errorEnum == null ? "服务器发生错误！" : errorEnum.getName());
    }

}
