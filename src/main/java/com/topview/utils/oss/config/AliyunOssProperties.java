package com.topview.utils.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/25
 */
@Data
@ConfigurationProperties(prefix = "util.oss.aliyun")
public class AliyunOssProperties {
    /**
     * 存储 bucket，仅用于阿里云
     */
    private String bucket;
    /**
     * 存储地点
     */
    private String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";
}
