package com.topview.utils.message.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Configuration
@EnableConfigurationProperties({AliyunMessageProperties.class, TencentMessageProperties.class, YunpianMessageProperties.class})
public class MessageConfiguration {
}
