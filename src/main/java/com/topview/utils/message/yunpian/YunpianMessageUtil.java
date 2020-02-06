package com.topview.utils.message.yunpian;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.topview.utils.http.HttpClientUtil;
import com.topview.utils.message.MessageUtil;
import com.topview.utils.message.callback.yunpian.YunpianCallbackResult;
import com.topview.utils.message.callback.yunpian.YunpianMessageCallback;
import com.topview.utils.message.config.YunpianMessageProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class YunpianMessageUtil implements MessageUtil {
    @NonNull
    private YunpianMessageProperties yunpianMessageProperties;
    @NonNull
    private HttpClientUtil httpClientUtil;

    @Override
    public YunpianCallbackResult callback(HttpServletRequest request) {
        String smsReply = request.getParameter("sms_reply");
        try {
            YunpianMessageCallback yunpianMessageCallback = JSONObject.parseObject(URLDecoder.decode(smsReply, "utf-8"), YunpianMessageCallback.class);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            yunpianMessageCallback.setReplyDate(simpleDateFormat.parse(yunpianMessageCallback.getReplyTime()));
            YunpianCallbackResult yunpianCallbackResult = new YunpianCallbackResult();
            yunpianCallbackResult.setCallbackResponse("SUCCESS");
            yunpianCallbackResult.setMessageCallback(yunpianMessageCallback);
            return yunpianCallbackResult;
        } catch (Exception ex) {
            log.error("云片返回数据解析失败！", ex);
            return null;
        }
    }

    @Override
    public YunpianMessageResult send(String phone, String template) {
        return send(phone, "", template);
    }

    @Override
    public YunpianMessageResult send(List<String> phones, String template) {
        return send(phones, "", template);
    }

    @Override
    public YunpianMessageResult send(String phone, String sign, String template) {
        return send(phone, sign, template, null);
    }

    @Override
    public YunpianMessageResult send(List<String> phones, String sign, String template) {
        return send(phones, sign, template, null);
    }

    @Override
    public YunpianMessageResult send(String phone, String template, Map<String, String> params) {
        return send(phone, "", template, params);
    }

    @Override
    public YunpianMessageResult send(List<String> phones, String template, Map<String, String> params) {
        return send(phones, "", template, params);
    }

    @Override
    public YunpianMessageResult send(String phone, String sign, String template, Map<String, String> params) {
        return sendYunpian(phone, sign, template, params);
    }

    @Override
    public YunpianMessageResult send(List<String> phones, String sign, String template, Map<String, String> params) {
        if (Objects.isNull(phones) || phones.size() == 0) {
            throw new RuntimeException("手机号不能为空！");
        }
        return sendYunpian(String.join(",", phones), sign, template, params);
    }

    private YunpianMessageResult sendYunpian(String phones, String sign, String template, Map<String, String> params) {
        List<NameValuePair> requestParams = new LinkedList<>();
        requestParams.add(new BasicNameValuePair("apikey", yunpianMessageProperties.getApiKey()));
        requestParams.add(new BasicNameValuePair("mobile", phones));
        requestParams.add(new BasicNameValuePair("tpl_id", template));
        StringJoiner stringJoiner;
        if (Objects.nonNull(params)) {
            try {
                stringJoiner = new StringJoiner("&");
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    stringJoiner.add(URLEncoder.encode("#" + k + "#", "utf-8") + "=" + URLEncoder.encode(v, "utf-8"));
                }
            } catch (UnsupportedEncodingException ex) {
                log.error("系统不支持UTF-8字符集！", ex);
                throw new RuntimeException("系统不支持UTF-8字符集！");
            }
            requestParams.add(new BasicNameValuePair("tpl_value", stringJoiner.toString()));
        } else {
            requestParams.add(new BasicNameValuePair("tpl_value", ""));
        }

        String yunpianResult = httpClientUtil.postForm("https://sms.yunpian.com/v2/sms/tpl_batch_send.json", requestParams, "utf-8");

        JSONObject jsonObject = JSONObject.parseObject(yunpianResult);
        YunpianMessageResult yunpianMessageResult = new YunpianMessageResult();
        yunpianMessageResult.setTotalCount(jsonObject.getInteger("total_count"));
        yunpianMessageResult.setTotalFee(jsonObject.getString("total_fee"));
        yunpianMessageResult.setUnit(jsonObject.getString("unit"));
        JSONArray data = jsonObject.getJSONArray("data");
        yunpianMessageResult.setData(data.toJavaList(YunpianMessageResult.Data.class));

        boolean success = true;
        for (YunpianMessageResult.Data e : yunpianMessageResult.getData()) {
            if (e.getCode() != 0) {
                success = false;
                break;
            }
        }
        yunpianMessageResult.setSuccess(success);

        return yunpianMessageResult;
    }
}
