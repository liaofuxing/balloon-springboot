package com.balloon.springboot.security.securityhandler;


import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import com.balloon.springboot.core.enums.StatusCodeEnums;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 凭证失效
 */
public class TokenAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        int status = StatusCodeEnums.CREDENTIALS_EXPIRED.getCode();
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.credentials_expired(StatusCodeEnums.getName(status))));
        response.flushBuffer();
    }
}
