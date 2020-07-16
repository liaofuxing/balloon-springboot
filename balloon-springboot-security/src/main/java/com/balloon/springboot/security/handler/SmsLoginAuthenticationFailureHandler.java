package com.balloon.springboot.security.handler;

import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import com.balloon.springboot.core.enums.StatusCodeEnums;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 手机号短信登录失败
 */
public class SmsLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        if(exception instanceof UsernameNotFoundException){
            response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.sms_not_find_phone(StatusCodeEnums.SMS_NOT_FIND_PHONE.getName())));
        }else {
            response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.sms_code_error(StatusCodeEnums.SMS_CODE_ERROR.getName())));
        }
        response.flushBuffer();
    }
}
