package com.topview.utils.oss.tencent;

import com.topview.utils.oss.UploadToken;
import lombok.Data;

/**
 * @author Albumen
 * @date 2020/2/2
 */
@Data
public class TencentUploadToken implements UploadToken {
    private String tmpSecretId;
    private String tmpSecretKey;
    private String sessionToken;
    private long startTime;
    private long expiredTime;
}
