package com.balloon.springboot.core.exception;


import com.balloon.core.exception.IBaseExceptionEnums;

/**
 * 基础的系统异常
 *
 * @author liaofuxing
 */
public enum BaseExceptionEnums implements IBaseExceptionEnums {

    SYSTEM_ERROR(1000, "系统异常"),

    INTERFACE_NOT_IMPL(2000, "接口未实现");

    private final Integer code;

    private final String msg;


    BaseExceptionEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
