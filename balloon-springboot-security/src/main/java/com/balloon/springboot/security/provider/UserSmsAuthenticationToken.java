package com.balloon.springboot.security.provider;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 短信登录用户验证适配器
 *
 * @author liaofuxing
 */
public class UserSmsAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 2383092775910246006L;

    /**
     * 手机号
     */
    private final Object principal;

    /**
     * 验证码
     */
    private String smsCode;

    /**
     * SmsCodeAuthenticationFilter中构建的未认证的Authentication
     *
     * @param phone   手机号
     * @param smsCode 验证码
     */
    public UserSmsAuthenticationToken(String phone, String smsCode) {
        super(null);
        this.principal = phone;
        this.smsCode = smsCode;
        setAuthenticated(false);
    }

    /**
     * SmsCodeAuthenticationProvider中构建已认证的Authentication
     *
     * @param principal
     * @param authorities
     */
    public UserSmsAuthenticationToken(Object principal,
                                      Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true); // must use super, as we override
    }



    /**
     * @param isAuthenticated
     * @throws IllegalArgumentException
     */
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
