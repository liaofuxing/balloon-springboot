package com.balloon.springboot.security.service.impl;

import com.balloon.springboot.security.entity.SecurityUser;
import com.balloon.springboot.security.service.UserDetailsExtService;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 短信登录数据库校验逻辑
 *
 * @author liaofuxing
 * @date 2020/02/18 11:50
 *
 */
public class DefaultUserSmsDetailsServiceImpl implements UserSmsDetailsService {

    @Override
    public SecurityUser loadUserByPhone(String username) throws UsernameNotFoundException {
        UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("请实现UserSmsDetailsServicerDetailsService接口");
        usernameNotFoundException.printStackTrace();
        throw usernameNotFoundException;
    }

}
