package com.topview.utils.file.upload.impl;

import com.topview.utils.file.upload.config.FileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author yongPhone
 * @date on 2018/5/7
 */
@Slf4j
@Component
public class LocalUpload extends AbstractFileUpload {
    public LocalUpload(FileProperties fileProperties) {
        super(fileProperties);
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("文件流写入错误", e);
            throw new RuntimeException("文件流写入错误！");
        }
        return path;
    }
}
