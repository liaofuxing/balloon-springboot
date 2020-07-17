package com.balloon.springboot.security.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 返回给前端的用户信息,脱敏处理,不包含密码等敏感信息
 *
 * @author liaofuxing
 */
@Data
@ToString
public class SecurityUserVo {

    private String username;

    private String phone;

    private String token;
}
