package com.topview.utils.message.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
@ConfigurationProperties(prefix = "util.message.tencent")
public class TencentMessageProperties {
    private int appId;
    private String appKey;

    /**
     * 默认短信签名
     */
    private String sign;
}
