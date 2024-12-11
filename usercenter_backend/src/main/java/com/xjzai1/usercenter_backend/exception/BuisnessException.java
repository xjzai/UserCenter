package com.xjzai1.usercenter_backend.exception;


import com.xjzai1.usercenter_backend.common.ErrorCode;

/**
 * 自定义异常类
 */
public class BuisnessException extends RuntimeException {

    private final int code;
    private final String description;

    public BuisnessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BuisnessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BuisnessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }
}
