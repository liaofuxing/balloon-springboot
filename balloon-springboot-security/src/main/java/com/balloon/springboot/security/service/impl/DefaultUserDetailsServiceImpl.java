package com.balloon.springboot.security.service.impl;

import com.balloon.springboot.security.entity.SecurityUser;
import com.balloon.springboot.security.service.UserDetailsExtService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



public class DefaultUserDetailsServiceImpl implements UserDetailsExtService {

    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException("请实现DefaultUserDetailsService接口");
        usernameNotFoundException.printStackTrace();
        throw usernameNotFoundException;

    }
}
