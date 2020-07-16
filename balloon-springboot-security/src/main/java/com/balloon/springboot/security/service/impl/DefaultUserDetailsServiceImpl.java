package com.balloon.springboot.security.service.impl;

import com.balloon.core.exception.BusinessRuntimeException;
import com.balloon.springboot.security.entity.SecurityUser;
import com.balloon.springboot.security.service.UserDetailsExtService;

/**
 * 用户名密码登录校验逻辑
 *
 * <p>因为 balloon-springboot 项目不提供 ORM 框架, 具体数据校验逻辑需要第三方实现, 这个默认的实现仅提示作用</p>
 *
 * @author liaofuxing
 * @date 2020/07/16 15:53
 */
public class DefaultUserDetailsServiceImpl implements UserDetailsExtService {

    @Override
    public SecurityUser loadUserByUsername(String username) throws BusinessRuntimeException {
        BusinessRuntimeException businessRuntimeException = new BusinessRuntimeException(1010, "接口未实现,请实现 UserDetailsExtService 接口");
        businessRuntimeException.printStackTrace();
        throw businessRuntimeException;
    }
}
