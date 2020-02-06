package com.topview.utils.file.upload.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Albumen
 * @date 2020/1/25
 */
@Data
@ConfigurationProperties(prefix = "util.file")
public class FileProperties {
    /**
     * 文件扩展名分割符
     */
    private String fileSuffixSeparator = ".";

    /**
     * 默认文件夹（非空需带有文件分割符后缀）
     */
    private String defaultFolder = "";
}
