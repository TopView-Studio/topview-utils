package com.topview.utils.message.callback;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Slf4j
public class CallbackBase {
    public String parseRequest(HttpServletRequest request) {
        StringBuilder requestBody = new StringBuilder();
        try {
            BufferedReader br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                requestBody.append(str);
            }
            br.close();
        } catch (IOException ex) {
            log.error("读取请求失败！", ex);
            throw new RuntimeException("读取请求失败！");
        }
        return requestBody.toString();
    }
}
