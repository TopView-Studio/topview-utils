package com.topview.utils.oss.tencent;

import com.tencent.cloud.CosStsClient;
import com.topview.utils.oss.config.TencentProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

/**
 * @author Albumen
 * @date 2020/1/30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentSecurity {
    @NonNull
    private TencentProperties tencentProperties;

    /**
     * 调用频率限制600QPS，由于腾讯云接口设计并不好，暂不做缓存处理
     */
    public TencentStsBean signUploadToken(String region, String bucket, String allowPrefix) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            config.put("secretId", tencentProperties.getSecretId());
            config.put("secretKey", tencentProperties.getSecretKey());
            config.put("durationSeconds", 3600);
            config.put("bucket", bucket);
            config.put("region", region);
            config.put("allowPrefix", allowPrefix);
            config.put("secretType", 0);

            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传
                    "name/cos:PostObject",
                    // 分片上传： 初始化分片
                    "name/cos:InitiateMultipartUpload",
                    // 分片上传： 查询 bucket 中未完成分片上传的UploadId
                    "name/cos:ListMultipartUploads",
                    // 分片上传： 查询已上传的分片
                    "name/cos:ListParts",
                    // 分片上传： 上传分片块
                    "name/cos:UploadPart",
                    // 分片上传： 完成分片上传
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            // 请求临时密钥信息
            JSONObject credential = CosStsClient.getCredential(config);

            TencentStsBean tencentStsBean = new TencentStsBean();
            tencentStsBean.setTmpSecretId(credential.getJSONObject("credentials").getString("tmpSecretId"));
            tencentStsBean.setTmpSecretKey(credential.getJSONObject("credentials").getString("tmpSecretKey"));
            tencentStsBean.setSessionToken(credential.getJSONObject("credentials").getString("sessionToken"));
            tencentStsBean.setStartTime(credential.getLong("startTime"));
            tencentStsBean.setExpiredTime(credential.getLong("expiredTime"));
            tencentStsBean.setExpiration(credential.getString("expiration"));
            return tencentStsBean;
        } catch (Exception e) {
            // 请求失败，抛出异常
            log.error("腾讯云COS STS申请失败！", e);
            throw new IllegalArgumentException("no valid secret !");
        }
    }
}
