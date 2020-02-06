package com.topview.utils.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/30
 */
@Data
@ConfigurationProperties(prefix = "util.oss.tencent")
public class TencentProperties {
    private String secretId;
    private String secretKey;
    /**
     * 存储 bucket
     */
    private String bucket;
    /**
     * 存储区
     */
    private String region;
}
