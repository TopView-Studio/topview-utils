package com.topview.utils.captcha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Data
@ConfigurationProperties(prefix = "util.captcha.geetest")
public class GeetestProperties {
    private String geetestId;
    private String geetestKey;
    private boolean newFailback = true;
}
