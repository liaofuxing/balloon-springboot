package com.balloon.springboot.core.uilts.callback;

import java.util.List;

/**
 * 拷贝工具类回调接口
 * <p>该接口只会在 List 拷贝完成后调用, 并且只会调用一次, 不会映射到 List 中的每个一个对象</p>
 *
 * @author liaofuxing
 * @date 2020/03/10 22:01
 *
 * @param <T> 拷贝完成返回类
 */
@FunctionalInterface
public interface BeanCopyUtilCallBackOnlyOne<T> {

    /**
     * 定义默认回调方法
     *
     * @param t 拷贝完成后返回的类
     */
    void callBack(List<T> t);

}