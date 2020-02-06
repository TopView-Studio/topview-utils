package com.topview.utils.file.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author yongPhone
 * @date on 2018/5/7
 */
public interface FileUpload {
    /**
     * 保存文件，默认保存于默认文件夹
     *
     * @param multipartFile 文件
     * @return 文件路径
     */
    String upload(MultipartFile multipartFile);

    /**
     * 保存文件
     *
     * @param multipartFile 文件
     * @param path          文件路径，通过后缀分隔符判断是否包含文件名，未包含将自动添加文件名 <p/>
     *                      如果为路径结果需要包含分隔符
     * @return 文件路径
     */
    String upload(MultipartFile multipartFile, String path);

    /**
     * 保存文件，默认保存于默认文件夹
     *
     * @param multipartFiles 文件
     * @return 文件路径数组
     */
    String[] upload(MultipartFile[] multipartFiles);

    /**
     * 保存文件
     *
     * @param multipartFiles 文件
     * @param folder         文件夹路径，路径结果需要包含分隔符
     * @return 文件路径数组
     */
    String[] upload(MultipartFile[] multipartFiles, String folder);

    /**
     * 保存文件
     *
     * @param inputStream 文件流
     * @param path        文件路径
     * @return 文件路径
     */
    String upload(InputStream inputStream, String path);
}
