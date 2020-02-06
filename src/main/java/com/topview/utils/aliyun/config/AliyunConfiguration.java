package com.topview.utils.aliyun.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Configuration
@EnableConfigurationProperties(AliyunSecurityProperties.class)
@EnableScheduling
public class AliyunConfiguration {
}
