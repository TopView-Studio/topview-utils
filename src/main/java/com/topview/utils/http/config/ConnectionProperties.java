package com.topview.utils.http.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/4
 */
@Data
@ConfigurationProperties(prefix = "util.http")
public class ConnectionProperties {
    private int maxTotal = 200;
    private int defaultMaxPerRoute = 20;
    private int connectTimeout = 10000;
    private int socketTimeout = 10000;
    private String defaultCharset = "utf-8";
}
