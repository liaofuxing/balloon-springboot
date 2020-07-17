package com.balloon.springboot.security.handler;

import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import com.balloon.springboot.core.uilts.BeanCopyUtil;
import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.entity.SecurityUser;
import com.balloon.springboot.security.entity.SecurityUserVo;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 登录成功处理
 */
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Setter
    private RedisUtils redisUtils;

    private final static JacksonObjectMapper mapper = new JacksonObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        String token = UUID.randomUUID().toString();

        //将用户信息存入存入redis
        String userStr = mapper.writeValueAsString(user);

        redisUtils.setEx("SYSTEM_USER_INFO:" + token, userStr, 30, TimeUnit.MINUTES);
        redisUtils.setEx("SYSTEM_SECURITY_TOKEN:" + user.getUsername(), token, 30, TimeUnit.MINUTES);
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        SecurityUserVo securityUserVo = BeanCopyUtil.copyProperties(user, SecurityUserVo :: new);
        securityUserVo.setToken(token);
        response.getWriter().print(mapper.writeValueAsString(ResultVOUtils.login_success(securityUserVo)));
        response.flushBuffer();
    }
}
