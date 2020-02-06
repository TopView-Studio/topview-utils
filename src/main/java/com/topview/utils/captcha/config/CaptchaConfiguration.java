package com.topview.utils.captcha.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Configuration
@EnableConfigurationProperties({CaptchaProperties.class, GeetestProperties.class})
public class CaptchaConfiguration {
}
