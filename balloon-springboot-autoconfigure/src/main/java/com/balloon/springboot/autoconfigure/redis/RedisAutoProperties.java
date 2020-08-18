package com.balloon.springboot.autoconfigure.redis;


import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.REDIS)
public class RedisAutoProperties {

    // 是否 redis
    private boolean enabled;

    // 是否开启缓存
    private boolean cache;
}
