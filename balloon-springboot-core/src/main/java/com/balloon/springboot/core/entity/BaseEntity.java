package com.balloon.springboot.core.entity;


import lombok.Data;

/**
 * 基础 Entity
 *
 * <p>每一个需要记录创建时间和修改时间的业务实体类都应该继承这个基础类</p>
 *
 * @author liaofuxing
 */

@Data
public class BaseEntity {

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;
}
