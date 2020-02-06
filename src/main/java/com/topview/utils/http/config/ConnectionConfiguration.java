package com.topview.utils.http.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Albumen
 * @date 2020/2/4
 */
@Configuration
@EnableConfigurationProperties(ConnectionProperties.class)
public class ConnectionConfiguration {
}
