package com.balloon.springboot.security.config;

import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.filter.JsonAuthenticationFilter;
import com.balloon.springboot.security.provider.SystemUserAuthenticationProvider;
import com.balloon.springboot.security.securityhandler.DefaultAuthenticationFailureHandler;
import com.balloon.springboot.security.securityhandler.DefaultAuthenticationSuccessHandler;
import com.balloon.springboot.security.service.UserDetailsExtService;
import com.balloon.springboot.security.service.impl.DefaultUserDetailsServiceImpl;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Json 用户名密码登录配置文件(配置器)
 *
 * @author liaofuxing
 * @date 2020/02/18 11:50
 */
@SuppressWarnings("all")
@Configuration
public class UsernameAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

//    @Autowired
//    private AuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
//
//    @Autowired
//    private AuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Autowired
    private UserDetailsExtService userDetailsExtServiceImpl;

    @Setter
    private RedisUtils redisUtils;

    @Override
    public void configure(HttpSecurity http) throws Exception {


        DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler =  new DefaultAuthenticationSuccessHandler();
        defaultAuthenticationSuccessHandler.setRedisUtils(redisUtils);
        DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler = new DefaultAuthenticationFailureHandler();

        // 自定义过滤器
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter();
        jsonAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        jsonAuthenticationFilter.setAuthenticationSuccessHandler(defaultAuthenticationSuccessHandler);
        jsonAuthenticationFilter.setAuthenticationFailureHandler(defaultAuthenticationFailureHandler);

        // 自定义systemUserAuthenticationProvider， 并为Provider 设置 systemUserDetailsService
        SystemUserAuthenticationProvider systemUserAuthenticationProvider = new SystemUserAuthenticationProvider();
        systemUserAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        systemUserAuthenticationProvider.setSystemUserDetailsService(userDetailsExtServiceImpl);
        http.authenticationProvider(systemUserAuthenticationProvider)
            .addFilterAfter(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * 提供一个默认的 UserDetailsExtService
     *
     * @return 返回 UserDetailsExtService
     */
    @Bean
    @ConditionalOnClass(UsernameAuthenticationConfigurer.class)
    @ConditionalOnMissingBean(UserDetailsExtService.class)
    public UserDetailsExtService userDetailsExtService() {
        return new DefaultUserDetailsServiceImpl();
    }
}
