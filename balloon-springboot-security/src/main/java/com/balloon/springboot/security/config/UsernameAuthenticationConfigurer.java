package com.balloon.springboot.security.config;

import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.filter.UserAuthenticationFilter;
import com.balloon.springboot.security.provider.UserAuthenticationProvider;
import com.balloon.springboot.security.handler.DefaultAuthenticationFailureHandler;
import com.balloon.springboot.security.handler.DefaultAuthenticationSuccessHandler;
import com.balloon.springboot.security.service.UserDetailsExtService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * 用户名密码登录配置文件(配置器)
 *
 * @author liaofuxing
 * @date 2020/02/18 11:50
 */
public class UsernameAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Setter
    private UserDetailsExtService userDetailsExtServiceImpl;

    @Setter
    private RedisUtils redisUtils;

    @Override
    public void configure(HttpSecurity http) throws Exception {


        DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler =  new DefaultAuthenticationSuccessHandler();
        defaultAuthenticationSuccessHandler.setRedisUtils(redisUtils);
        DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler = new DefaultAuthenticationFailureHandler();

        // 自定义过滤器
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        userAuthenticationFilter.setAuthenticationSuccessHandler(defaultAuthenticationSuccessHandler);
        userAuthenticationFilter.setAuthenticationFailureHandler(defaultAuthenticationFailureHandler);

        // 自定义systemUserAuthenticationProvider， 并为Provider 设置 systemUserDetailsService
        UserAuthenticationProvider userAuthenticationProvider = new UserAuthenticationProvider();
        userAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        userAuthenticationProvider.setSystemUserDetailsService(userDetailsExtServiceImpl);
        http.authenticationProvider(userAuthenticationProvider)
            .addFilterAfter(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
