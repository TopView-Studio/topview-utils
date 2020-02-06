package com.topview.utils.message.aliyun;

import com.aliyuncs.http.ProtocolType;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
@Component
public class AliyunProperties {
    private String messageRegion = "cn-hangzhou";
    private String messageDomain = "dysmsapi.aliyuncs.com";
    private ProtocolType messageProtocol = ProtocolType.HTTPS;
    private String messageVersion = "2017-05-25";
}
