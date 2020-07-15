package com.balloon.springboot.security.config;


import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.filter.SmsCodeAuthenticationFilter;
import com.balloon.springboot.security.provider.UserSmsAuthenticationProvider;
import com.balloon.springboot.security.securityhandler.DefaultAuthenticationSuccessHandler;
import com.balloon.springboot.security.securityhandler.SmsLoginAuthenticationFailureHandler;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import com.balloon.springboot.security.service.impl.DefaultUserSmsDetailsServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * 短信验证码登录配置器
 *
 * @author liaofuxing
 * @date 2020/02/28 20:13
 */
public class SmsCodeAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

//    @Autowired
//    private AuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
//
//    @Autowired
//    private SmsLoginAuthenticationFailureHandler smsLoginAuthenticationFailureHandler;

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
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(defaultAuthenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(smsLoginAuthenticationFailureHandler);

        // 自定义userSmsAuthenticationProvider， 并为Provider 设置 userSmsDetailsService
        UserSmsAuthenticationProvider userSmsAuthenticationProvider = new UserSmsAuthenticationProvider();
        userSmsAuthenticationProvider.setUserSmsDetailsService(defaultUserDetailsServiceImpl);
        userSmsAuthenticationProvider.setRedisUtils(redisUtils);
        http.authenticationProvider(userSmsAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
