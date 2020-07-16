package com.balloon.springboot.security.provider;

import com.balloon.springboot.security.common.AuthenticationChecks;
import com.balloon.springboot.security.service.UserDetailsExtService;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户名密码登录校验 Provider
 *
 * @author liaofuxing
 * @date 2020/04/24 01:50
 */
public class SystemUserAuthenticationProvider implements AuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    @Setter
    private UserDetailsExtService systemUserDetailsService;

    private PasswordEncoder passwordEncoder;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authenticationToken.getPrincipal();
        //调用自定义的userDetailsService认证
        UserDetails userDetails = systemUserDetailsService.loadUserByUsername(username);

        AuthenticationChecks.additionalAuthenticationChecks(userDetails, authenticationToken, passwordEncoder);

        //如果user不为空重新构建UsernamePasswordAuthenticationToken（已认证）
        UsernamePasswordAuthenticationToken authenticationResult = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;

    }


    /**
     * 只有 Authentication 为 UsernamePasswordAuthenticationToken 使用此 Provider 认证
     * @param authentication Provider
     *
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
