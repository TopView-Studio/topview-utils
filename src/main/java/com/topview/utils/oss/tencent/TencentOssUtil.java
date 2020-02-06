package com.topview.utils.oss.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.*;
import com.topview.utils.oss.OssUtil;
import com.topview.utils.oss.UploadToken;
import com.topview.utils.oss.config.OssProperties;
import com.topview.utils.oss.config.TencentProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Albumen
 * @date 2020/2/1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentOssUtil implements OssUtil {
    @NonNull
    private TencentSecurity tencentSecurity;
    @NonNull
    private TencentProperties tencentProperties;
    @NonNull
    private CosClientCache cosClientCache;
    @NonNull
    private OssProperties ossProperties;

    @Override
    public UploadToken signUploadToken(String sessionName) {
        TencentStsBean tencentStsBean = tencentSecurity.signUploadToken(tencentProperties.getRegion(),
                tencentProperties.getBucket(), "*");
        TencentUploadToken tencentUploadToken = new TencentUploadToken();
        BeanUtils.copyProperties(tencentStsBean, tencentUploadToken);
        return tencentUploadToken;
    }

    @Override
    public UploadToken signUploadToken(String sessionName, String filename) {
        return null;
    }

    @Override
    public UploadToken signUploadToken(String sessionName, String region, String bucket, String filename) {
        TencentStsBean tencentStsBean = tencentSecurity.signUploadToken(region, bucket, filename + "*");
        TencentUploadToken tencentUploadToken = new TencentUploadToken();
        BeanUtils.copyProperties(tencentStsBean, tencentUploadToken);
        return tencentUploadToken;
    }

    @Override
    public boolean upload(InputStream inputStream, String path) {
        return upload(inputStream, tencentProperties.getRegion(), tencentProperties.getBucket(), path);
    }

    @Override
    public boolean upload(InputStream inputStream, String endpoint, String bucket, String path) {
        try {
            COSClient cosClient = cosClientCache.getCosClient(endpoint);

            // 解决COS工具不能识别文件类型本地缓存文件
            File file = new File("TencentCosTemp-" + getRandomFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, fileOutputStream);
            fileOutputStream.close();

            // 附加文件类型
            ObjectMetadata objectMetadata = new ObjectMetadata();
            FileInputStream fileInputStream = new FileInputStream(file);
            objectMetadata.setContentLength(file.length());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            MagicMatch magicMatch = Magic.getMagicMatch(file, false, true);
            objectMetadata.setContentType(magicMatch.getMimeType());

            // 上传文件
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, fileInputStream, objectMetadata);
            cosClient.putObject(putObjectRequest);
            fileInputStream.close();
            file.delete();
            return true;
        } catch (Exception e) {
            log.error("腾讯云上传失败！", e);
            return false;
        }
    }

    @Override
    public InputStream download(String path) {
        return download(tencentProperties.getRegion(), tencentProperties.getBucket(), path);
    }

    @Override
    public InputStream download(String endpoint, String bucket, String path) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, path);
        COSObject cosObject = cosClientCache.getCosClient(endpoint).getObject(getObjectRequest);
        return cosObject.getObjectContent();
    }

    @Override
    public String generateDownloadUrl(String path) {
        return generateDownloadUrl(tencentProperties.getRegion(), tencentProperties.getBucket(), path);
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path) {
        return generateDownloadUrl(endpoint, bucket, path, ossProperties.getUrlExpireTime());
    }

    @Override
    public String generateDownloadUrl(String endpoint, String bucket, String path, long expireTime) {
        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucket, path, HttpMethodName.GET);
        Date expirationDate = new Date(System.currentTimeMillis() + expireTime);
        req.setExpiration(expirationDate);
        URL url = cosClientCache.getCosClient(endpoint).generatePresignedUrl(req);
        return url.toString();
    }

    @Override
    public boolean deleteFile(String path) {
        return deleteFile(tencentProperties.getRegion(), tencentProperties.getBucket(), path);
    }

    @Override
    public boolean deleteFile(String endpoint, String bucket, String path) {
        cosClientCache.getCosClient(endpoint).deleteObject(bucket, path);
        return true;
    }

    /**
     * 获取随机字符串
     *
     * @return 随机字符串，格式：年月日时分秒毫秒 + 八位随机数
     */
    private static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        int rand = (int) (Math.random() * 100000000) + 10000000;
        return str + rand;
    }
}
