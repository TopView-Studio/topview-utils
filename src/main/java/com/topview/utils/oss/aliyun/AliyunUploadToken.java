package com.topview.utils.oss.aliyun;

import com.topview.utils.oss.UploadToken;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Albumen
 * @date 2020/1/29
 */
@Data
@AllArgsConstructor
public class AliyunUploadToken implements UploadToken {
    private int status;
    private String AccessKeyId;
    private String AccessKeySecret;
    private String SecurityToken;
    private String Expiration;
}
