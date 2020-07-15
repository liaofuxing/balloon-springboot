package com.balloon.springboot.security.securityhandler;

import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import com.balloon.springboot.core.enums.StatusCodeEnums;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException , ServletException {
        int status = StatusCodeEnums.UNAUTHORIZED.getCode();
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.sms_not_find_phone(null)));
        response.flushBuffer();
        throw new AuthenticationServiceException(StatusCodeEnums.getName(status));
    }
}
