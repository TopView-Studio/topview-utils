package com.topview.utils.captcha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Data
@ConfigurationProperties(prefix = "util.captcha")
public class CaptchaProperties {
    /**
     * 验证成功 token 存活时间
     */
    private long ttl = 300000;
}
