package com.topview.utils.oss.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.topview.utils.oss.OssUtil;
import com.topview.utils.oss.config.OssProperties;
import com.topview.utils.oss.config.QiniuOssProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Albumen
 * @date 2020/1/29
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QiniuOssUtil implements OssUtil {
    @NonNull
    private OssProperties ossProperties;
    @NonNull
    private QiniuOssProperties qiniuOssProperties;

    @Override
    public QiniuUploadToken signUploadToken(String sessionName) {
        Auth auth = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
        return new QiniuUploadToken(auth.uploadToken(qiniuOssProperties.getBucket()), "");
    }

    @Override
    public QiniuUploadToken signUploadToken(String sessionName, String filename) {
        return signUploadToken(sessionName, null, qiniuOssProperties.getBucket(), filename);
    }

    @Override
    public QiniuUploadToken signUploadToken(String sessionName, String region, String bucket, String filename) {
        Auth auth = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
        return new QiniuUploadToken(auth.uploadToken(bucket, filename), filename);
    }

    @Override
    public boolean upload(InputStream inputStream, String path) {
        return upload(inputStream, qiniuOssProperties.getRegion().getName(), qiniuOssProperties.getBucket(), path);
    }

    @Override
    public boolean upload(InputStream inputStream, String endpoint, String bucket, String path) {
        Auth auth = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
        String upToken = auth.uploadToken(bucket);
        Configuration cfg = new Configuration(QiniuRegionEnum.getRegionEnum(endpoint).getRegion());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(inputStream, path, upToken, null, null);
            int successCode = 200;
            return response.statusCode == successCode;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("七牛OSS上传失败，" + r.toString());
            try {
                log.error("七牛OSS上传失败，" + r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return false;
        }
    }

    @Override
    public InputStream download(String path) {
        try {
            return new URL(generateDownloadUrl(path)).openStream();
        } catch (IOException e) {
            log.error("打开七牛云下载流失败！", e);
            return null;
        }
    }

    @Override
    public InputStream download(String endpoint, String bucket, String path) {
        try {
            return new URL(generateDownloadUrl(endpoint, bucket, path)).openStream();
        } catch (IOException e) {
            log.error("打开七牛云下载流失败！", e);
            return null;
        }
    }

    @Override
    public String generateDownloadUrl(String path) {
        return generateDownloadUrl(qiniuOssProperties.getDomainOfBucket(), qiniuOssProperties.getDomainOfBucket(), path);
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path) {
        return generateDownloadUrl(endpoint, bucket, path, ossProperties.getUrlExpireTime());
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path, long expireTime) {
        try {
            String encodedFileName = URLEncoder.encode(path, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.error("生成七牛云下载路径失败，找不到字符库！", e);
            return "";
        }
        String publicUrl = String.format("%s/%s", endpoint, path);
        Auth auth = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
        return auth.privateDownloadUrl(publicUrl, expireTime / 1000);
    }

    @Override
    public boolean deleteFile(String path) {
        return deleteFile(qiniuOssProperties.getRegion().getName(), qiniuOssProperties.getBucket(), path);
    }

    @Override
    public boolean deleteFile(String endpoint, String bucket, String path) {
        Auth auth = Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
        Configuration cfg = new Configuration(QiniuRegionEnum.getRegionEnum(endpoint).getRegion());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(bucket, path);
            return true;
        } catch (QiniuException e) {
            log.error("七牛云文件删除失败！code:" + e.code() + "response:" + e.response.toString());
            return false;
        }
    }
}
