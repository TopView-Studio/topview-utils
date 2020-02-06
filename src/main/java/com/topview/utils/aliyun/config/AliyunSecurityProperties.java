package com.topview.utils.aliyun.config;

import com.aliyuncs.http.ProtocolType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Data
@ConfigurationProperties(prefix = "util.aliyun")
public class AliyunSecurityProperties {
    private String accessKeyId;
    private String accessSecret;

    /**
     * 启用访问控制（仅用于系统操作时辨别）
     */
    private boolean enableRam = true;
    private String stsEndpoint = "sts.aliyuncs.com";
    private ProtocolType protocol = ProtocolType.HTTPS;
    private String roleArn;
    /**
     * 默认系统操作访问审计<p/>
     * 此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     * 支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     * 格式：^[a-zA-Z0-9\.@\-_]+$
     */
    private String roleSessionName;
}
