package com.balloon.springboot.security.filter;

import com.balloon.springboot.core.jackson.JacksonObjectMapper;
import com.balloon.springboot.redis.utils.RedisUtils;
import com.balloon.springboot.security.entity.SecurityUser;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Setter
    public RedisUtils redisUtils;

    private final JacksonObjectMapper mapper = new JacksonObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws IOException, ServletException {
        //从请求头中取出token
        String token = request.getHeader("token");

        if (!StringUtils.isEmpty(token)) {
            //用token从redis中获取用户信息，构造一个SecurityUser

            String userInfoStr = redisUtils.get("SYSTEM_USER_INFO:" + token);
            if (userInfoStr != null) {
                Map<String, String> userMap = mapper.readValue(userInfoStr, Map.class); //json转换成map
                SecurityUser securityUser = new SecurityUser(userMap.get("username"), userMap.get("password"));
                // redis 中存在用户信息,将凭证有效时间延长
                redisUtils.expire("SYSTEM_SECURITY_TOKEN:" + token, 30, TimeUnit.MINUTES);
                redisUtils.expire("SYSTEM_USER_INFO:" + userMap.get("username"), 30, TimeUnit.MINUTES);
                // 设置一个已认证的 authentication
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser,
                        null, securityUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
