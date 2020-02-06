package com.topview.utils.oss.qiniu;

import com.topview.utils.oss.UploadToken;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Albumen
 * @date 2020/1/29
 */
@Data
@AllArgsConstructor
public class QiniuUploadToken implements UploadToken {
    private String token;
    private String key;
}
