package com.balloon.springboot.core.uilts;

import com.balloon.springboot.core.uilts.callback.BeanCopyUtilCallBack;
import com.balloon.springboot.core.uilts.callback.BeanCopyUtilCallBackOnlyOne;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 对象深度拷贝工具类
 * <p>扩展 org.springframework.beans.BeanUtils 方法</p>
 *
 * @author liaofuxing
 * @date 2020/03/10 22:01
 */
public class BeanCopyUtil extends BeanUtils {


    /**
     * 对象拷贝
     *
     * @param source: 数据源类
     * @param target: 目标类::new(eg: UserVO::new)
     * @return T 返回拷贝完成的类
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }


    /**
     * 集合数据的拷贝
     *
     * @param sources: 数据源类
     * @param target:  目标类::new(eg: UserVO::new)
     * @return List<T> 返回拷贝完成的 list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }


    /**
     * 带回调函数的集合数据的拷贝, 回调函数映射到集合中的每一个对象, 每一个对象拷贝完成都会执行该回调函数
     *
     * @param sources:  数据源类
     * @param target:   目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @return List<T> 返回拷贝完成的list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
        }
        return list;
    }

    /**
     * 带回调函数的集合数据的拷贝, 回调函数会在 List 拷贝完成后调用, 并且只会调用一次
     *
     * @param sources:  数据源类
     * @param target:   目标类::new(eg: UserVO::new)
     * @param callBack: 回调函数
     * @return List<T> 返回拷贝完成的list
     */
    public static <S, T> List<T> copyListPropertiesCallBackOnlyOne(List<S> sources, Supplier<T> target, BeanCopyUtilCallBackOnlyOne<T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }
        if (callBack != null) {
            // 回调
            callBack.callBack(list);
        }
        return list;
    }

}
