package com.topview.utils.aliyun;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Albumen
 * @date 2020/1/27
 */
@Component
@Scope("singleton")
public class AliyunTokenCache {
    private final static ConcurrentHashMap<String, AliyunStsBean> STS_CACHE = new ConcurrentHashMap<>(512);

    private boolean checkExpire(AliyunStsBean aliyunStsBean) {
        return aliyunStsBean.getExpire().after(new Date());
    }

    public AliyunStsBean readOssAllCache() {
        AliyunStsBean aliyunStsBean = STS_CACHE.get("0-OssAll");
        if (Objects.nonNull(aliyunStsBean) && checkExpire(aliyunStsBean)) {
            return aliyunStsBean;
        } else {
            return null;
        }
    }

    public void writeOssAllCache(AliyunStsBean aliyunStsBean) {
        STS_CACHE.put("0-OssAll", aliyunStsBean);
    }

    public AliyunStsBean readOssUploadCache(String bucket) {
        AliyunStsBean aliyunStsBean = STS_CACHE.get("1-OssUpload-Bucket-" + bucket);
        if (Objects.nonNull(aliyunStsBean) && checkExpire(aliyunStsBean)) {
            return aliyunStsBean;
        } else {
            return null;
        }
    }

    public void writeOssUploadCache(AliyunStsBean aliyunStsBean, String bucket) {
        STS_CACHE.put("1-OssUpload-Bucket-" + bucket, aliyunStsBean);
    }

    public AliyunStsBean readOssUploadCache(String bucket, String filename) {
        AliyunStsBean aliyunStsBean = STS_CACHE.get("2-OssUpload-Bucket-" + bucket + "-filename-" + filename);
        if (Objects.nonNull(aliyunStsBean) && checkExpire(aliyunStsBean)) {
            return aliyunStsBean;
        } else {
            return null;
        }
    }

    public void writeOssUploadCache(AliyunStsBean aliyunStsBean, String bucket, String filename) {
        STS_CACHE.put("2-OssUpload-Bucket-" + bucket + "-filename-" + filename, aliyunStsBean);
    }

    @Scheduled(fixedRate = 30000)
    private void cycleCheckExpire() {
        STS_CACHE.forEach((k, v) -> {
            if (!checkExpire(v)) {
                STS_CACHE.remove(k);
            }
        });
    }
}
