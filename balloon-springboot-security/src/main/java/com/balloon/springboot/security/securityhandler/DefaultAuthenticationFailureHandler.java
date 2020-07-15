package com.balloon.springboot.security.securityhandler;

import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权失败handler
 */

public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.login_failure(null)));
        response.flushBuffer();
    }
}
