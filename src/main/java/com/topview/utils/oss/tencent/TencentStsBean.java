package com.topview.utils.oss.tencent;

import lombok.Data;

/**
 * @author Albumen
 * @date 2020/1/31
 */
@Data
public class TencentStsBean {
    private String tmpSecretId;
    private String tmpSecretKey;
    private String sessionToken;
    private String expiration;
    private long startTime;
    private long expiredTime;
}
