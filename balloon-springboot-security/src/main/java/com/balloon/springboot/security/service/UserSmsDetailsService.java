package com.balloon.springboot.security.service;

import com.balloon.springboot.security.entity.SecurityUser;

public interface UserSmsDetailsService {
    SecurityUser loadUserByPhone(String phone) throws Exception;
}
