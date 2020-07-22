package com.balloon.springboot.security.provider;

import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.common.AuthenticationChecks;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信验证码登录验证 Provider
 *
 * @author liaofuxing
 * @date 2020/04/24 01:50
 */
public class UserSmsAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserSmsAuthenticationProvider.class);

    @Setter
    private UserSmsDetailsService userSmsDetailsService;

    @Setter
    private RedisUtils redisUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("正在认证登录用户...");
        UserSmsAuthenticationToken authenticationToken = (UserSmsAuthenticationToken) authentication;
        //调用自定义的userDetailsService认证

        UserDetails user = userSmsDetailsService.loadUserByPhone((String) authenticationToken.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        // 校验验证码
        AuthenticationChecks.smsCodeAuthenticationChecks(authenticationToken, redisUtils);

        //如果user不为空重新构建SmsCodeAuthenticationToken（已认证）
        UserSmsAuthenticationToken authenticationResult = new UserSmsAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;

    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     * @param authentication authentication
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UserSmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
