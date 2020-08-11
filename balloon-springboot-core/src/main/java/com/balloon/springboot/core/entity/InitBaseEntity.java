package com.balloon.springboot.core.entity;

import com.balloon.core.date.DateTimeUtils;
import com.balloon.core.utils.StringUtils;
import com.balloon.springboot.core.uilts.BeanCopyUtil;

import java.time.LocalDateTime;

/**
 * 将 BaseEntity 的创建时间和修改时间初始化为当前时间
 * <p>需要将一个基础 BaseEntity 的类的创建时间和修改时间初始化为当前时间时, 可以使用改方法</p>
 *
 * @author liaofuxing
 */
public class InitBaseEntity {

    /**
     * @param source 需要初始化的目标
     * @param <S>    目标类型
     * @return S 已经初始化完成的类
     */
    public static <S> S initDateTime(S source) {
        return initDateTime(source, null);
    }

    /**
     * 将指定对象的 createTime, UpdateTime 按 format 初始化, 当 format 为空时, 将使用默认格式("yyyy-MM-dd HH:mm:ss")初始化
     *
     * @param source 需要初始化的目标对象
     * @param format 需要把时间初始化成字符串格式 eg: format = "yyyy-MM-dd HH:mm:ss"
     * @param <S>    目标类型
     * @return S 已经初始化完成的类
     */
    public static <S> S initDateTime(S source, String format) {
        BaseEntity baseEntity = new BaseEntity();
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        String dateTimeNowStr = DateTimeUtils.formatDateTime(LocalDateTime.now(), format);
        baseEntity.setCreateTime(dateTimeNowStr);
        baseEntity.setUpdateTime(dateTimeNowStr);
        BeanCopyUtil.copyProperties(baseEntity, source);
        return source;
    }

}
