package com.topview.utils.file.upload.impl;

import com.topview.utils.file.upload.config.FileProperties;
import com.topview.utils.oss.OssUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author yongPhone
 * @date on 2018/5/7
 */
@Slf4j
public class OssUpload extends AbstractFileUpload {
    private OssUtil ossUtil;

    public OssUpload(FileProperties fileProperties, OssUtil ossUtil) {
        super(fileProperties);
        this.ossUtil = ossUtil;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        ossUtil.upload(inputStream, path);
        return path;
    }
}
