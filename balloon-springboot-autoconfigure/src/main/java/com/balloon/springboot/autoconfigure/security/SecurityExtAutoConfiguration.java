package com.balloon.springboot.autoconfigure.security;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import com.balloon.springboot.autoconfigure.redis.RedisUtilsAutoConfiguration;
import com.balloon.springboot.mail.service.impl.MailServiceImpl;
import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.config.UsernameAuthenticationConfigurer;
import com.balloon.springboot.security.config.SmsCodeAuthenticationConfigurer;
import com.balloon.springboot.security.filter.TokenAuthorizationFilter;
import com.balloon.springboot.security.securityhandler.TokenAccessDeniedHandler;
import com.balloon.springboot.security.securityhandler.TokenAuthenticationEntryPointHandler;
import com.balloon.springboot.security.securityhandler.TokenLogoutSuccessHandler;
import com.balloon.springboot.security.service.UserDetailsExtService;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import com.balloon.springboot.security.service.impl.DefaultUserDetailsServiceImpl;
import com.balloon.springboot.security.service.impl.DefaultUserSmsDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.annotation.PostConstruct;

/**
 * spring security 自动装载
 * 可以在配置文件中设置是否装载生效, 将 balloon.security.enable 属性修改为false 不生效, true 生效
 *
 * @author liaofuxing
 *
 */
@SuppressWarnings("all")
@Configuration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
@EnableConfigurationProperties(value = SecurityExtAutoProperties.class)
@ConditionalOnProperty(prefix = AutoConfigConstant.SECURITY, name = AutoConfigConstant.ENABLED, havingValue = AutoConfigConstant.TRUE)
public class SecurityExtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtilsAutoConfiguration.class);


    @Autowired
    private UserSmsDetailsService  userSmsDetailsService;

    @Autowired
    private UserDetailsExtService userDetailsExtService;

    @Autowired
    private RedisUtils redisUtils;

    @Configuration
    @ConditionalOnClass(WebSecurityConfigurerAdapter.class)
    @EnableWebSecurity
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        /**
         * 用户名密码登录配置器
         */
        @Autowired
        private UsernameAuthenticationConfigurer usernameAuthenticationConfigurer;

        /**
         * 短信验证码登录配置器
         */
        @Autowired
        private SmsCodeAuthenticationConfigurer smsCodeAuthenticationConfigurer;

        /**
         * 注销登录 handler
         */
        @Autowired
        private TokenLogoutSuccessHandler tokenLogoutSuccessHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //处理跨域请求
            http
                    .cors().and().csrf().disable()
                    .apply(usernameAuthenticationConfigurer)
                    .and()
                    .apply(smsCodeAuthenticationConfigurer)
                    .and()
                    //权限不足结果处理
                    .exceptionHandling().authenticationEntryPoint(new TokenAuthenticationEntryPointHandler())
                    .accessDeniedHandler(new TokenAccessDeniedHandler())
                    .and()
                    //设置登出url
                    .logout().logoutUrl("/user/logout")
                    //设置登出成功处理器（下面介绍）
                    .logoutSuccessHandler(tokenLogoutSuccessHandler).and()
                    .authorizeRequests()
                    .antMatchers("/user/**").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated();

            /*  authorizationFilter是用来拦截登录请求判断请求中是否带有token,并且token是否有对应的已经登录的用户,如果有应该直接授权通过
             *  所以这个过滤器应该在UsernamePasswordAuthenticationFilter过滤器之前执行,所以放在LogoutFilter之后
             */
            http.addFilterAfter(new TokenAuthorizationFilter(), LogoutFilter.class);
        }

    }

    /**
     * 提供一个默认的 UserDetailsExtService
     *
     * @return 返回 UserDetailsExtService
     */
    @Bean
    @ConditionalOnMissingBean(UserDetailsExtService.class)
    public UserDetailsExtService userDetailsExtService() {
        return new DefaultUserDetailsServiceImpl();
    }

    /**
     * 提供一个默认的 UserSmsDetailsService
     *
     * @return 返回 UserSmsDetailsService
     */
    @Bean
    @ConditionalOnMissingBean(UserSmsDetailsService.class)
    public UserSmsDetailsService UserSmsDetailsService() {
        return new DefaultUserSmsDetailsServiceImpl();
    }

    @Bean
    public TokenLogoutSuccessHandler tokenLogoutSuccessHandler() {
        TokenLogoutSuccessHandler tokenLogoutSuccessHandler = new TokenLogoutSuccessHandler();
        tokenLogoutSuccessHandler.setRedisUtils(redisUtils);
        return tokenLogoutSuccessHandler;
    }

    @Bean
    @ConditionalOnBean(UserDetailsExtService.class)
    public UsernameAuthenticationConfigurer UsernameAuthenticationConfigurer() {
        UsernameAuthenticationConfigurer usernameAuthenticationConfigurer = new UsernameAuthenticationConfigurer();
        usernameAuthenticationConfigurer.setRedisUtils(redisUtils);
        usernameAuthenticationConfigurer.setUserDetailsExtServiceImpl(userDetailsExtService);
        return usernameAuthenticationConfigurer;
    }

    @Bean
    @ConditionalOnBean(UserSmsDetailsService.class)
    public SmsCodeAuthenticationConfigurer SmsCodeAuthenticationConfigurer() {
        SmsCodeAuthenticationConfigurer smsCodeAuthenticationConfigurer = new SmsCodeAuthenticationConfigurer();
        smsCodeAuthenticationConfigurer.setDefaultUserDetailsServiceImpl(userSmsDetailsService);
        smsCodeAuthenticationConfigurer.setRedisUtils(redisUtils);
        return smsCodeAuthenticationConfigurer;
    }

    //密码加密器，在授权时，框架为我们解析用户名密码时，密码会通过加密器加密在进行比较
    //将密码加密器交给spring管理，在注册时，密码也是需要加密的，再存入数据库中
    //用户输入登录的密码用加密器加密，再与数据库中查询到的用户密码比较
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 自动装载添加拦截器
     */
    @PostConstruct
    public void init() {
        logger.info("SecurityExtAutoConfiguration 已被自动装载");
    }

}
