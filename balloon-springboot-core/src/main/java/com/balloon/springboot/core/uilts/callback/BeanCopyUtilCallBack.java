package com.balloon.springboot.core.uilts.callback;

/**
 * 拷贝工具类回调接口
 * <p>对 List 对象拷贝时, 该回调会映射到 List 中每一个对象, 每一个对象拷贝完成都会执行该回调</p>
 *
 * @author liaofuxing
 * @date 2020/03/10 22:01
 *
 * @param <S> 拷贝源
 * @param <T> 拷贝完成返回类
 */
@FunctionalInterface
public interface BeanCopyUtilCallBack<S, T> {

    /**
     * 定义默认回调方法
     *
     * @param s 数据源类
     * @param t 拷贝完成后返回的类
     */
    void callBack(S s, T t);

}