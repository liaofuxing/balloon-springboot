package com.balloon.springboot.core.enums;

import lombok.Getter;

/**
 * @author liaofuxing
 * @date 2020/07/13 9:39
 * @E-mail liaofuxing@outlook.com
 */
@Getter
public enum ResultStatusCodeEnums {

    OK(20001, "请求成功"),
    ERROR(50000, "系统异常"),
    ;

    private final Integer code;

    private final String message;

    ResultStatusCodeEnums(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
