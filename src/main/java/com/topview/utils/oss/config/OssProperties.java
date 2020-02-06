package com.topview.utils.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Data
@ConfigurationProperties(prefix = "util.oss")
public class OssProperties {
    /**
     * 获取下载地址有效时间（默认1小时）
     */
    private long urlExpireTime = 3600000L;
}
