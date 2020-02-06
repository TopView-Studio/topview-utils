package com.topview.utils.captcha;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Albumen
 * @date 2020/2/6
 */
public interface CaptchaUtil {
    /**
     * 生成验证信息
     *
     * @param request 请求信息
     * @return 生成信息
     */
    String generateCaptcha(HttpServletRequest request);

    /**
     * 验证信息
     *
     * @param request 请求信息
     * @return 验证结果
     */
    String verifyCaptcha(HttpServletRequest request);

    /**
     * 验证token
     *
     * @param token token
     * @return 结果
     */
    boolean verifyToken(String token);

    /**
     * 设置Token存储工具（可自定义使用 Redis)
     *
     * @param tokenStorage 存储工具
     */
    void setTokenStorage(TokenStorage tokenStorage);
}
