package com.topview.utils.captcha.geetest;

import com.topview.utils.captcha.CaptchaUtil;
import com.topview.utils.captcha.DefaultTokenStorage;
import com.topview.utils.captcha.TokenStorage;
import com.topview.utils.captcha.config.CaptchaProperties;
import com.topview.utils.captcha.config.GeetestProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeetestCaptchaUtil implements CaptchaUtil {
    @NonNull
    private GeetestProperties geetestProperties;
    @NonNull
    private CaptchaProperties captchaProperties;

    private TokenStorage tokenStorage = new DefaultTokenStorage();

    @Override
    public String generateCaptcha(HttpServletRequest request) {
        // 初始化极验服务
        GeetestLib lib = new GeetestLib(geetestProperties.getGeetestId(), geetestProperties.getGeetestKey(), geetestProperties.isNewFailback());

        // 验证预处理
        int status = lib.preProcess(getParams(request));

        // 将服务状态存放到 Session 中，在第二步验证时会用到
        request.getSession().setAttribute(lib.gtServerStatusSessionKey, status);

        // 返回生成字串
        return lib.getResponseStr();
    }

    @Override
    public String verifyCaptcha(HttpServletRequest request) {
        GeetestLib lib = new GeetestLib(geetestProperties.getGeetestId(), geetestProperties.getGeetestKey(), geetestProperties.isNewFailback());

        // 接收前端参数，由前端 JS 内部封装处理
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        // 取出第一步初始化验证时存储的服务状态
        int status = (Integer) request.getSession().getAttribute(lib.gtServerStatusSessionKey);

        int result;

        try {
            if (status == 1) {
                // 服务器在线
                result = lib.enhencedValidateRequest(challenge, validate, seccode, getParams(request));
            } else {
                // 服务器离线
                result = lib.failbackValidateRequest(challenge, validate, seccode);
            }

            if (result == 1) {
                // 保存验证信息
                String token = UUID.randomUUID().toString();
                tokenStorage.store(token, captchaProperties.getTtl());
                return token;
            } else {
                log.error("行为验证失败");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("行为验证失败", e);
        }
        return "";
    }

    private HashMap<String, String> getParams(HttpServletRequest request) {
        String clientIp = getIpAddr(request);

        HashMap<String, String> params = new HashMap<>(4);
        params.put("user_id", clientIp);
        params.put("client_type", "web");
        params.put("ip_address", clientIp);

        return params;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            String localhost = "127.0.0.1";
            if (ip.equals(localhost)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ip = Objects.requireNonNull(inet).getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        int length = 15;
        if (ip != null && ip.length() > length) {
            String separator = ",";
            if (ip.indexOf(separator) > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    @Override
    public boolean verifyToken(String token) {
        return tokenStorage.verify(token);
    }

    @Override
    public void setTokenStorage(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }
}
