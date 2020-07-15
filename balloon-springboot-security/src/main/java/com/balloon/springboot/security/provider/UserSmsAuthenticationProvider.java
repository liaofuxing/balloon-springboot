package com.balloon.springboot.security.provider;

import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.comm.AuthenticationChecks;
import com.balloon.springboot.security.service.UserSmsDetailsService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 短信验证码登录验证器
 *
 * @author liaofuxing
 * @date 2020/04/24 01:50
 */
public class UserSmsAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private UserSmsDetailsService userSmsDetailsService;

    @Setter
    private RedisUtils redisUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        //调用自定义的userDetailsService认证
        UserDetails user = userSmsDetailsService.loadUserByPhone((String) authenticationToken.getPrincipal());

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        // 校验验证码
        AuthenticationChecks.smsCodeAuthenticationChecks(authenticationToken, redisUtils);

        //如果user不为空重新构建SmsCodeAuthenticationToken（已认证）
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;

    }

    /**
     * 只有Authentication为SmsCodeAuthenticationToken使用此Provider认证
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
