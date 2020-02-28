package com.cjh.ttt.base.error;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author cjh
 * @date 2020/2/28
 */
@Data
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;

    public ServiceException(String msg) {
        super(msg);
        this.code = ErrorEnum.ERROR_500.getCode();
        this.msg = msg;
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(ErrorEnum statusCode) {
        super(statusCode.getName());
        this.code = statusCode.getCode();
        this.msg = statusCode.getName();
    }

}
