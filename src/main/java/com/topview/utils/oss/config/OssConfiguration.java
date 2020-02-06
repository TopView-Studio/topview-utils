package com.topview.utils.oss.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albumen
 * @date 2020/1/25
 */
@Configuration
@EnableConfigurationProperties({OssProperties.class, AliyunOssProperties.class, QiniuOssProperties.class, TencentProperties.class})
public class OssConfiguration {
}
