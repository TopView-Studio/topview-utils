package com.topview.utils.oss.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.topview.utils.oss.config.TencentProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Albumen
 * @date 2020/2/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CosClientCache {
    @NonNull
    private TencentProperties tencentProperties;

    private final static ConcurrentHashMap<String, COSClient> COS_CACHE = new ConcurrentHashMap<>(8);

    public COSClient getCosClient(String regionName) {
        COSClient cosClient = COS_CACHE.get(regionName);
        synchronized (this) {
            if (Objects.nonNull(cosClient)) {
                return cosClient;
            }
            COSCredentials cred = new BasicCOSCredentials(tencentProperties.getSecretId(), tencentProperties.getSecretKey());
            Region region = new Region(regionName);
            ClientConfig clientConfig = new ClientConfig(region);
            clientConfig.setHttpProtocol(HttpProtocol.https);
            cosClient = new COSClient(cred, clientConfig);
            COS_CACHE.put(regionName, cosClient);
            return cosClient;
        }
    }
}
