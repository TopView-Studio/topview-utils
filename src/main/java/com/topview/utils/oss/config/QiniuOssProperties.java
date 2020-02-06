package com.topview.utils.oss.config;

import com.topview.utils.oss.qiniu.QiniuRegionEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/29
 */
@Data
@ConfigurationProperties(prefix = "util.oss.qiniu")
public class QiniuOssProperties {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domainOfBucket;
    private QiniuRegionEnum region = QiniuRegionEnum.AUTO;
}
