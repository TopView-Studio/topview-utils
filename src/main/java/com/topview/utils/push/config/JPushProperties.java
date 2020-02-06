package com.topview.utils.push.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Data
@ConfigurationProperties(prefix = "util.push.jpush")
public class JPushProperties {
    private String appKey;
    private String masterSecret;
}
