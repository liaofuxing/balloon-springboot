package com.balloon.springboot.security.config;


import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.filter.UserSmsAuthenticationFilter;
import com.balloon.springboot.security.provider.UserSmsAuthenticationProvider;
import com.balloon.springboot.security.handler.DefaultAuthenticationSuccessHandler;
import com.balloon.springboot.security.handler.SmsLoginAuthenticationFailureHandler;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * 短信验证码登录配置器
 *
 * @author liaofuxing
 * @date 2020/02/28 20:13
 */
public class SmsCodeAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    @Setter
    private UserSmsDetailsService defaultUserDetailsServiceImpl;

    @Setter
    private RedisUtils redisUtils;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler =  new DefaultAuthenticationSuccessHandler();
        defaultAuthenticationSuccessHandler.setRedisUtils(redisUtils);
        SmsLoginAuthenticationFailureHandler smsLoginAuthenticationFailureHandler = new SmsLoginAuthenticationFailureHandler();

        // 自定义过滤器
        UserSmsAuthenticationFilter userSmsAuthenticationFilter = new UserSmsAuthenticationFilter();
        userSmsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        userSmsAuthenticationFilter.setAuthenticationSuccessHandler(defaultAuthenticationSuccessHandler);
        userSmsAuthenticationFilter.setAuthenticationFailureHandler(smsLoginAuthenticationFailureHandler);

        // 自定义userSmsAuthenticationProvider， 并为Provider 设置 userSmsDetailsService
        UserSmsAuthenticationProvider userSmsAuthenticationProvider = new UserSmsAuthenticationProvider();
        userSmsAuthenticationProvider.setUserSmsDetailsService(defaultUserDetailsServiceImpl);
        userSmsAuthenticationProvider.setRedisUtils(redisUtils);
        http.authenticationProvider(userSmsAuthenticationProvider)
                .addFilterAfter(userSmsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
