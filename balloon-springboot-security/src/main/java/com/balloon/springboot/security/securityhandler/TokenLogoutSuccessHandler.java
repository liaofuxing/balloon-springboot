package com.balloon.springboot.security.securityhandler;


import com.balloon.core.utils.StringUtils;
import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.core.rules.ResultVOUtils;
import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.entity.SecurityUser;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class TokenLogoutSuccessHandler implements LogoutSuccessHandler {

    @Setter
    private RedisUtils redisUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String requestToken = request.getHeader("token");
        String userInfoStr = redisUtils.get("SYSTEM_USER_INFO:" + requestToken);
        JacksonObjectMapper mapper = new JacksonObjectMapper();
        SecurityUser securityUser = mapper.readValue(userInfoStr, SecurityUser.class);

        String token = redisUtils.get("SYSTEM_SECURITY_TOKEN:" + securityUser.getUsername());
        if(!StringUtils.isEmpty(token)){
            // 将redis 上的缓存信息设置为即将过期
            redisUtils.expire("SYSTEM_USER_INFO" + token, 0 , TimeUnit.MICROSECONDS);
            redisUtils.expire("SYSTEM_SECURITY_TOKEN:" + securityUser.getUsername(), 0 , TimeUnit.MICROSECONDS);
        }
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(mapper.writeValueAsString((ResultVOUtils.logout_success(null))));
        response.flushBuffer();
    }
}

