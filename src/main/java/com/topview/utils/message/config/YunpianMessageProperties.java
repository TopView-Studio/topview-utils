package com.topview.utils.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
@ConfigurationProperties(prefix = "util.message.yunpian")
public class YunpianMessageProperties {
    private String apiKey;
}
