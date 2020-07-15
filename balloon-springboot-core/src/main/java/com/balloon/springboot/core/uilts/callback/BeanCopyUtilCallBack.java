package com.balloon.springboot.core.uilts.callback;

@FunctionalInterface
public interface BeanCopyUtilCallBack <S, T> {

    /**
     * 定义默认回调方法
     * @param s 数据源类
     * @param t 目标
     */
    void callBack(S s, T t);
}