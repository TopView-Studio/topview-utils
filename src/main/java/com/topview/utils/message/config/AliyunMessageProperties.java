package com.topview.utils.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
@ConfigurationProperties(prefix = "util.message.aliyun")
public class AliyunMessageProperties {
    /**
     * 默认短信签名
     */
    private String sign;
}
