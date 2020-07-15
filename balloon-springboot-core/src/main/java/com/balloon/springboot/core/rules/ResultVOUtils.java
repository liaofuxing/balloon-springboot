package com.balloon.springboot.core.rules;


import com.balloon.springboot.core.enums.ResultStatusCodeEnums;
import com.balloon.springboot.core.enums.StatusCodeEnums;


/**
 * 返回前端的统一工具类
 *
 * @author liaofuxing
 * @date 2019/03/10 4:10
 */
public class ResultVOUtils {

    // 普通请求

    /**
     * 请求成功 20001
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> success(T t) {
        return new ResultVO<>(ResultStatusCodeEnums.OK.getCode(), ResultStatusCodeEnums.OK.getMessage(), t);
    }

    /**
     * 请求失败 50000
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> error(T t) {
        return new ResultVO<>(ResultStatusCodeEnums.ERROR.getCode(), ResultStatusCodeEnums.ERROR.getMessage(), t);
    }


    // 登录请求

    /**
     * 登录成功 20000
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> login_success(T t) {
        return new ResultVO<>(StatusCodeEnums.LOGIN_SUCCESS.getCode(), StatusCodeEnums.LOGIN_SUCCESS.getName(), t);
    }

    /**
     * 用户名或密码错误 50001
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> login_failure(T t) {
        return new ResultVO<>(StatusCodeEnums.LOGIN_FAILURE.getCode(), StatusCodeEnums.LOGIN_FAILURE.getName(), t);
    }

    /**
     * 注销成功 50002
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> logout_success(T t) {
        return new ResultVO<>(StatusCodeEnums.LOGOUT_SUCCESS.getCode(), StatusCodeEnums.LOGOUT_SUCCESS.getName(), t);
    }

    /**
     * 用户未登录 50003
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> credentials_expired(T t) {
        return new ResultVO<>(StatusCodeEnums.CREDENTIALS_EXPIRED.getCode(), StatusCodeEnums.CREDENTIALS_EXPIRED.getName(), t);
    }

    /**
     * 未授权  50004
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> unauthorized(T t) {
        return new ResultVO<>(StatusCodeEnums.UNAUTHORIZED.getCode(), StatusCodeEnums.UNAUTHORIZED.getName(), t);
    }

    /**
     * 短信登录 手机号不正确  50006
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> sms_not_find_phone(T t) {
        return new ResultVO<>(StatusCodeEnums.SMS_NOT_FIND_PHONE.getCode(), StatusCodeEnums.SMS_NOT_FIND_PHONE.getName(), t);
    }

    /**
     * 短信登录 验证码不正确  50007
     *
     * @param t t
     * @return <T> ResultVO<T>
     */
    public static <T> ResultVO<T> sms_code_error(T t) {
        return new ResultVO<>(StatusCodeEnums.SMS_CODE_ERROR.getCode(), StatusCodeEnums.SMS_CODE_ERROR.getName(), t);
    }

    //接口请求

}
