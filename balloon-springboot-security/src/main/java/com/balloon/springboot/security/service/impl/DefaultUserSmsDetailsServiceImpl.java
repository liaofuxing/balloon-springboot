package com.balloon.springboot.security.service.impl;

import com.balloon.core.exception.BusinessRuntimeException;
import com.balloon.springboot.core.exception.BaseExceptionEnums;
import com.balloon.springboot.security.entity.SecurityUser;
import com.balloon.springboot.security.service.UserSmsDetailsService;

/**
 * 短信登录校验逻辑
 *
 * <p>因为 balloon-springboot 项目不提供 ORM 框架, 具体数据校验逻辑需要第三方实现, 默认的实现仅提示作用, 不包含任何实现逻辑</p>
 *
 * @author liaofuxing
 * @date 2020/07/16 15:53
 */
public class DefaultUserSmsDetailsServiceImpl implements UserSmsDetailsService {

    @Override
    public SecurityUser loadUserByPhone(String username) throws BusinessRuntimeException {
        BusinessRuntimeException businessRuntimeException = new BusinessRuntimeException(BaseExceptionEnums.INTERFACE_NOT_IMPL);
        businessRuntimeException.printStackTrace();
        throw businessRuntimeException;
    }
}
