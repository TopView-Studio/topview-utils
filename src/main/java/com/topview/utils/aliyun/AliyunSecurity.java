package com.topview.utils.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.topview.utils.aliyun.config.AliyunSecurityProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunSecurity {
    @NonNull
    private AliyunSecurityProperties aliyunSecurityProperties;
    @NonNull
    private AliyunTokenCache aliyunTokenCache;

    private AliyunStsBean assumeRole(String policy, String sessionRole) {
        try {
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", aliyunSecurityProperties.getAccessKeyId(), aliyunSecurityProperties.getAccessSecret());
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysEndpoint(aliyunSecurityProperties.getStsEndpoint());
            request.setSysProtocol(aliyunSecurityProperties.getProtocol());
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(aliyunSecurityProperties.getRoleArn());
            request.setRoleSessionName(sessionRole);
            request.setPolicy(policy);
            // 请求获取结果
            AssumeRoleResponse response = client.getAcsResponse(request);

            // 缓存结果
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return new AliyunStsBean(response.getCredentials().getAccessKeyId(),
                    response.getCredentials().getAccessKeySecret(),
                    response.getCredentials().getSecurityToken(),
                    response.getCredentials().getExpiration(),
                    sdf.parse(response.getCredentials().getExpiration()));
        } catch (ClientException | ParseException e) {
            log.error("获取STS令牌失败！", e);
            return null;
        }
    }

    public AliyunStsBean signSystemOssSts() {
        // 读取缓存
        AliyunStsBean ossTokenCache = aliyunTokenCache.readOssAllCache();
        if (Objects.nonNull(ossTokenCache)) {
            return ossTokenCache;
        }
        ossTokenCache = assumeRole(ossAllPolicy(), aliyunSecurityProperties.getRoleSessionName());
        aliyunTokenCache.writeOssAllCache(ossTokenCache);
        return ossTokenCache;
    }

    public AliyunStsBean signUploadOssSts(String sessionRole, String bucket) {
        // 读取缓存
        AliyunStsBean ossTokenCache = aliyunTokenCache.readOssUploadCache(bucket);
        if (Objects.nonNull(ossTokenCache)) {
            return ossTokenCache;
        }
        ossTokenCache = assumeRole(ossUploadPolicy(bucket), sessionRole);
        aliyunTokenCache.writeOssUploadCache(ossTokenCache, bucket);
        return ossTokenCache;
    }

    public AliyunStsBean signUploadOssSts(String sessionRole, String bucket, String filename) {
        // 读取缓存
        AliyunStsBean ossTokenCache = aliyunTokenCache.readOssUploadCache(bucket, filename);
        if (Objects.nonNull(ossTokenCache)) {
            return ossTokenCache;
        }
        ossTokenCache = assumeRole(ossUploadPolicy(bucket, filename), sessionRole);
        aliyunTokenCache.writeOssUploadCache(ossTokenCache, bucket, filename);
        return ossTokenCache;
    }

    public AliyunStsBean signSmsMessageSts() {
        // 读取缓存
        AliyunStsBean ossTokenCache = aliyunTokenCache.readOssAllCache();
        if (Objects.nonNull(ossTokenCache)) {
            return ossTokenCache;
        }
        ossTokenCache = assumeRole(ossSmsPolicy(), aliyunSecurityProperties.getRoleSessionName());
        aliyunTokenCache.writeOssAllCache(ossTokenCache);
        return ossTokenCache;
    }

    private static String ossAllPolicy() {
        return "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private static String ossUploadPolicy(String bucket) {
        return "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:" + bucket + "/*\", \"acs:oss:*:*:" + bucket + "\"\n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private static String ossUploadPolicy(String bucket, String filename) {
        return "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:PutObject\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:" + bucket + "/" + filename + "*\", \"acs:oss:*:*:" + bucket + "\"\n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    private static String ossSmsPolicy() {
        return "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"dysms:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
