package com.topview.utils.oss.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.topview.utils.aliyun.AliyunSecurity;
import com.topview.utils.aliyun.AliyunStsBean;
import com.topview.utils.aliyun.config.AliyunSecurityProperties;
import com.topview.utils.oss.OssUtil;
import com.topview.utils.oss.config.AliyunOssProperties;
import com.topview.utils.oss.config.OssProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunOssUtil implements OssUtil {
    @NonNull
    private AliyunOssProperties aliyunOssProperties;
    @NonNull
    private AliyunSecurityProperties aliyunSecurityProperties;
    @NonNull
    private OssProperties ossProperties;
    @NonNull
    private AliyunSecurity aliyunSecurity;

    private String checkSessionName(String sessionName) {
        String pattern = "^[a-zA-Z0-9.@\\-_]+$";
        if (sessionName.length() > 32 || sessionName.length() < 2) {
            return "UNKNOWN";
        } else if (!Pattern.matches(pattern, sessionName)) {
            return "UNKNOWN";
        } else {
            return sessionName;
        }
    }

    /**
     * 签发浏览器直传授权（无限制文件名）
     *
     * @param sessionName 此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @return 授权 token
     */
    public AliyunUploadToken signUploadToken(String sessionName) {
        AliyunStsBean aliyunStsBean = aliyunSecurity.signUploadOssSts(checkSessionName(sessionName), aliyunOssProperties.getBucket());
        if (Objects.nonNull(aliyunStsBean)) {
            return new AliyunUploadToken(200,
                    aliyunStsBean.getAccessKeyId(),
                    aliyunStsBean.getAccessKeySecret(),
                    aliyunStsBean.getSecurityToken(),
                    aliyunStsBean.getExpiration());
        } else {
            return new AliyunUploadToken(500, "", "", "", "");
        }
    }

    /**
     * 签发浏览器直传授权（限制上传文件名）
     *
     * @param sessionName 此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @param filename    文件名
     * @return 授权信息
     */
    public AliyunUploadToken signUploadToken(String sessionName, String filename) {
        return signUploadToken(sessionName, null, aliyunOssProperties.getBucket(), filename);
    }

    /**
     * 签发浏览器直传授权（限制上传文件名）
     *
     * @param sessionName 此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @param bucket      存储 bucket
     * @param filename    文件名
     * @return 授权信息
     */
    public AliyunUploadToken signUploadToken(String sessionName, String region, String bucket, String filename) {
        AliyunStsBean aliyunStsBean = aliyunSecurity.signUploadOssSts(checkSessionName(sessionName), aliyunOssProperties.getBucket(), filename);
        if (Objects.nonNull(aliyunStsBean)) {
            return new AliyunUploadToken(200,
                    aliyunStsBean.getAccessKeyId(),
                    aliyunStsBean.getAccessKeySecret(),
                    aliyunStsBean.getSecurityToken(),
                    aliyunStsBean.getExpiration());
        } else {
            return new AliyunUploadToken(500, "", "", "", "");
        }
    }

    @Override
    public boolean upload(InputStream inputStream, String path) {
        return upload(inputStream, aliyunOssProperties.getEndpoint(), aliyunOssProperties.getBucket(), path);
    }

    @Override
    public boolean upload(InputStream inputStream, String endpoint, String bucket, String path) {
        OSS ossClient = getOssClient(endpoint);
        if (log.isTraceEnabled()) {
            log.trace("阿里云上传开始，bucket:" + bucket + ",path:" + path);
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, inputStream);
        try {
            ossClient.putObject(putObjectRequest);
        } catch (RuntimeException e) {
            log.error("阿里云上传失败", e);
            return false;
        } finally {
            ossClient.shutdown();
        }
        return true;
    }

    @Override
    public InputStream download(String path) {
        return download(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getBucket(), path);
    }

    @Override
    public InputStream download(String endpoint, String bucket, String path) {
        OSS ossClient = getOssClient(endpoint);
        OSSObject ossObject = ossClient.getObject(bucket, path);
        InputStream objectContent = ossObject.getObjectContent();
        try {
            byte[] result = IOUtils.toByteArray(objectContent);
            objectContent.close();
            ossClient.shutdown();
            return new ByteArrayInputStream(result);
        } catch (IOException e) {
            log.error("OSS文件下载失败！", e);
            return null;
        }
    }

    @Override
    public String generateDownloadUrl(String path) {
        return generateDownloadUrl(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getBucket(), path);
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path) {
        return generateDownloadUrl(endpoint, bucket, path, ossProperties.getUrlExpireTime());
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path, long expireTime) {
        OSS ossClient = getOssClient(endpoint);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, path);
        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + expireTime));
        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
        ossClient.shutdown();
        return url.toString();
    }

    @Override
    public boolean deleteFile(String path) {
        return deleteFile(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getBucket(), path);
    }

    @Override
    public boolean deleteFile(String endpoint, String bucket, String path) {
        OSS ossClient = getOssClient(endpoint);
        if (!ossClient.doesObjectExist(bucket, path)) {
            return false;
        }
        ossClient.deleteObject(bucket, path);
        ossClient.shutdown();
        return true;
    }

    private OSS getOssClient(String endpoint) {
        if (aliyunSecurityProperties.isEnableRam()) {
            AliyunStsBean aliyunStsBean = aliyunSecurity.signSystemOssSts();
            return new OSSClientBuilder()
                    .build(endpoint,
                            aliyunStsBean.getAccessKeyId(),
                            aliyunStsBean.getAccessKeySecret(),
                            aliyunStsBean.getSecurityToken());
        } else {
            return new OSSClientBuilder()
                    .build(endpoint,
                            aliyunSecurityProperties.getAccessKeyId(),
                            aliyunSecurityProperties.getAccessSecret());
        }
    }
}
