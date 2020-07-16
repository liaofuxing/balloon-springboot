package com.balloon.springboot.security.service;
import com.balloon.springboot.security.entity.SecurityUser;

public interface UserDetailsExtService {
    SecurityUser loadUserByUsername(String username) throws Exception;
}
