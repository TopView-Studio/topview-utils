package com.topview.utils.file.upload;

import com.topview.utils.file.upload.config.FileProperties;
import com.topview.utils.file.upload.impl.OssUpload;
import com.topview.utils.oss.aliyun.AliyunOssUtil;
import com.topview.utils.oss.qiniu.QiniuOssUtil;
import com.topview.utils.oss.tencent.TencentOssUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Component
@RequiredArgsConstructor
public class OssUploadFactory {
    @NonNull
    private FileProperties fileProperties;
    @NonNull
    private AliyunOssUtil aliyunOssUtil;
    @NonNull
    private TencentOssUtil tencentOssUtil;
    @NonNull
    private QiniuOssUtil qiniuOssUtil;

    public OssUpload getAliyunUpload() {
        return new OssUpload(fileProperties, aliyunOssUtil);
    }

    public OssUpload getTencentUpload() {
        return new OssUpload(fileProperties, tencentOssUtil);
    }

    public OssUpload getQiniuUpload() {
        return new OssUpload(fileProperties, qiniuOssUtil);
    }
}
