package com.topview.utils.captcha;

/**
 * @author Albumen
 * @date 2020/2/6
 */
public interface TokenStorage {
    /**
     * 保存 Token
     *
     * @param token token
     * @param ttl   存活时间（单位：毫秒）
     */
    void store(String token, long ttl);

    /**
     * 验证 Token （一次性）
     *
     * @param token token
     * @return 返回是否存在 token，如存在返回 true 并删除 token
     */
    boolean verify(String token);
}
