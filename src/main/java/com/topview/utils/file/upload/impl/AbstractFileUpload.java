package com.topview.utils.file.upload.impl;

import com.topview.utils.file.upload.FileUpload;
import com.topview.utils.file.upload.config.FileProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @author yongPhone
 * @date on 2018/5/7
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractFileUpload implements FileUpload {
    @NonNull
    private FileProperties fileProperties;
    private static final String FOLDER_SEPARATOR_WIN = "\\";
    private static final String FOLDER_SEPARATOR_LINUX = "/";

    @Override
    public String upload(MultipartFile multipartFile) {
        return upload(multipartFile, fileProperties.getDefaultFolder());
    }

    @Override
    public String upload(MultipartFile multipartFile, String path) {
        String uploadPath;
        String filePathWithName = getPathWithName(multipartFile, path);
        try {
            uploadPath = upload(multipartFile.getInputStream(), filePathWithName);
        } catch (IOException e) {
            log.error("文件保存失败", e);
            throw new RuntimeException("文件保存流出现错误！");
        }
        return uploadPath;
    }

    @Override
    public String[] upload(MultipartFile[] multipartFiles) {
        return upload(multipartFiles, fileProperties.getDefaultFolder());
    }

    @Override
    public String[] upload(MultipartFile[] multipartFiles, String folder) {
        String[] strings = new String[multipartFiles.length];
        for (int i = 0; i < multipartFiles.length; i++) {
            MultipartFile multipartFile = multipartFiles[i];
            strings[i] = upload(multipartFile, getPathWithName(multipartFile, folder));
        }
        return strings;
    }


    private String getPathWithName(MultipartFile file, String path) {
        if (isPathWithName(path)) {
            return path;
        } else {
            if (StringUtils.isNotEmpty(path) && !path.endsWith(FOLDER_SEPARATOR_WIN) && !path.endsWith(FOLDER_SEPARATOR_LINUX)) {
                throw new RuntimeException("文件夹路径格式错误！" + path);
            }
            // 使用随机字符串作为文件名防止文件冲突
            return path + getRandomFileName() + "." + file.getContentType();
        }
    }

    private boolean isPathWithName(String path) {
        // Windows系统路径格式切割
        String[] folderWin = path.split(Pattern.quote(FOLDER_SEPARATOR_WIN));
        if (folderWin.length == 0) {
            return true;
        }
        // 去除目录结构后结果
        String lastPath = folderWin[folderWin.length - 1];

        // Linux系统路径格式切割
        String[] folderLinux = lastPath.split(FOLDER_SEPARATOR_LINUX);
        if (folderLinux.length == 0) {
            return true;
        }
        // 去除目录结构后结果
        lastPath = folderLinux[folderLinux.length - 1];

        // 判断是否含有文件后缀分隔符
        return lastPath.contains(fileProperties.getFileSuffixSeparator());
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
