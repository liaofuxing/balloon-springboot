package com.balloon.springboot.security.service;

import com.balloon.springboot.security.entity.SecurityUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserSmsDetailsService {
    SecurityUser loadUserByPhone(String phone) throws UsernameNotFoundException;
}
