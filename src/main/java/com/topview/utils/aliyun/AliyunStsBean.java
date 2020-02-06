package com.topview.utils.aliyun;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Data
@AllArgsConstructor
public class AliyunStsBean {
    private String accessKeyId;
    private String accessKeySecret;
    private String securityToken;
    private String expiration;
    private Date expire;
}
