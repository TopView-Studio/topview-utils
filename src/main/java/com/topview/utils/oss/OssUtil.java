package com.topview.utils.oss;

import java.io.InputStream;

/**
 * @author Albumen
 * @date 2020/01/27
 */
public interface OssUtil {
    /**
     * 签发浏览器直传授权（无限制文件名）
     *
     * @param sessionName 仅阿里云使用，其他云存储传空或 null即可 </p>
     *                    此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @return 授权 token
     */
    UploadToken signUploadToken(String sessionName);

    /**
     * 签发浏览器直传授权（限制上传文件名）
     *
     * @param sessionName 仅阿里云使用，其他云存储传空或 null即可 </p>
     *                    此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @param filename    文件名，对于阿里云、腾讯云已做后缀通配（*）处理，对于七牛云限定前端使用的文件名需一致（不可使用通配）
     * @return 授权信息
     */
    UploadToken signUploadToken(String sessionName, String filename);

    /**
     * 签发浏览器直传授权（限制上传文件名）
     *
     * @param sessionName 仅阿里云使用，其他云存储传空或 null即可 </p>
     *                    此参数用来区分不同的令牌，可用于用户级别的访问审计。<p/>
     *                    支持输入2~32个字符，请输入至少2个字符，如果只有1个字符，会出现错误。<p/>
     *                    格式：^[a-zA-Z0-9\.@\-_]+$
     * @param region      存储地区，仅腾讯云使用，其他云存储传空或 null即可
     * @param bucket      存储 bucket
     * @param filename    文件名，对于阿里云、腾讯云已做后缀通配（*）处理，对于七牛云限定前端使用的文件名需一致（不可使用通配）
     * @return 授权信息
     */
    UploadToken signUploadToken(String sessionName, String region, String bucket, String filename);

    /**
     * 简单上传
     *
     * @param inputStream 数据流
     * @param path        保存路径（需包括文件名）
     * @return 上传结果
     */
    boolean upload(InputStream inputStream, String path);

    /**
     * 简单上传
     *
     * @param inputStream 数据流
     * @param endpoint    阿里云：存储库绑定的地址，需带上协议，如 https://oss-cn-shenzhen.aliyuncs.com <p/>
     *                    七牛云：存储库所在区，{@link com.topview.utils.oss.qiniu.QiniuRegionEnum#getRegionEnum}
     *                    腾讯云：存储所在区
     * @param bucket      存储 bucket
     * @param path        保存路径
     * @return 上传结果
     */
    boolean upload(InputStream inputStream, String endpoint, String bucket, String path);

    /**
     * 获取文件下载流
     *
     * @param path 文件路径
     * @return 文件流
     */
    InputStream download(String path);

    /**
     * 获取文件下载流
     *
     * @param endpoint 阿里云：存储库绑定的地址，需带上协议，如 https://oss-cn-shenzhen.aliyuncs.com <p/>
     *                 七牛云：存储库绑定的地址，bucket 域名，需带上协议
     *                 腾讯云：存储所在区
     * @param bucket   存储 bucket
     * @param path     文件路径
     * @return 文件流
     */
    InputStream download(String endpoint, String bucket, String path);

    /**
     * 获取文件访问地址
     *
     * @param path 文件路径
     * @return 可访问地址
     */
    String generateDownloadUrl(String path);

    /**
     * 获取文件访问地址
     *
     * @param endpoint 阿里云：存储库绑定的地址，需带上协议，如 https://oss-cn-shenzhen.aliyuncs.com <p/>
     *                 七牛云：存储库绑定的地址，bucket 域名，需带上协议
     *                 腾讯云：存储所在区
     * @param bucket   存储 bucket
     * @param path     文件路径
     * @return 可访问地址
     */
    String generateDownloadUrl(String endpoint, String bucket, String path);

    /**
     * 获取文件访问地址
     *
     * @param endpoint   阿里云：存储库绑定的地址，需带上协议，如 https://oss-cn-shenzhen.aliyuncs.com <p/>
     *                   七牛云：存储库绑定的地址，bucket 域名，需带上协议
     *                   腾讯云：存储所在区
     * @param bucket     存储 bucket
     * @param path       文件路径
     * @param expireTime 地址过期时间（单位：毫秒）
     * @return 可访问地址
     */
    String generateDownloadUrl(String endpoint, String bucket, String path, long expireTime);

    /**
     * 删除文件
     *
     * @param path 文件路径
     * @return 操作结果
     */
    boolean deleteFile(String path);

    /**
     * 删除文件
     *
     * @param endpoint 阿里云：存储库绑定的地址，需带上协议，如 https://oss-cn-shenzhen.aliyuncs.com <p/>
     *                 七牛云：存储库所在区，{@link com.topview.utils.oss.qiniu.QiniuRegionEnum#getRegionEnum}
     *                 腾讯云：存储所在区
     * @param bucket   存储 bucket
     * @param path     文件路径
     * @return 操作结果
     */
    boolean deleteFile(String endpoint, String bucket, String path);
}
