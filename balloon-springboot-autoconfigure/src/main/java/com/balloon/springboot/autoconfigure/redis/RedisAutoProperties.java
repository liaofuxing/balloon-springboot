package com.balloon.springboot.autoconfigure.redis;

import com.balloon.springboot.autoconfigure.AutoConfigConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = AutoConfigConstant.REDIS)
public class RedisAutoProperties {

    private boolean enabled;
}
