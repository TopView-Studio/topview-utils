package com.topview.utils.message.tencent;

import com.alibaba.fastjson.JSONObject;
import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.topview.utils.message.MessageUtil;
import com.topview.utils.message.callback.CallbackBase;
import com.topview.utils.message.callback.tencent.TencentCallbackResponse;
import com.topview.utils.message.callback.tencent.TencentCallbackResult;
import com.topview.utils.message.callback.tencent.TencentMessageCallback;
import com.topview.utils.message.config.TencentMessageProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TencentMessageUtil extends CallbackBase implements MessageUtil {
    @NonNull
    private TencentMessageProperties tencentMessageProperties;

    @Override
    public TencentCallbackResult callback(HttpServletRequest request) {
        TencentMessageCallback tencentMessageCallback = JSONObject.parseObject(parseRequest(request)).toJavaObject(TencentMessageCallback.class);
        tencentMessageCallback.setSendTime(new Date(tencentMessageCallback.getTime() * 1000));

        TencentCallbackResult tencentCallbackResult = new TencentCallbackResult();

        TencentCallbackResponse tencentCallbackResponse = new TencentCallbackResponse(0, "OK");

        tencentCallbackResult.setCallbackResponse(tencentCallbackResponse);
        tencentCallbackResult.setMessageCallback(tencentMessageCallback);

        return tencentCallbackResult;
    }

    @Override
    public TencentMessageResult send(String phone, String template) {
        return send(phone, tencentMessageProperties.getSign(), template);
    }

    @Override
    public TencentMessageResult send(List<String> phones, String template) {
        return send(phones, tencentMessageProperties.getSign(), template);
    }

    @Override
    public TencentMessageResult send(String phone, String sign, String template) {
        return send(phone, sign, template, null);
    }

    @Override
    public TencentMessageResult send(List<String> phones, String sign, String template) {
        return send(phones, sign, template, null);
    }

    @Override
    public TencentMessageResult send(String phone, String template, Map<String, String> params) {
        return send(phone, tencentMessageProperties.getSign(), template, params);
    }

    @Override
    public TencentMessageResult send(List<String> phones, String template, Map<String, String> params) {
        return send(phones, tencentMessageProperties.getSign(), template, params);
    }

    @Override
    public TencentMessageResult send(String phone, String sign, String template, Map<String, String> params) {
        ArrayList<String> phones = new ArrayList<>(1);
        phones.add(phone);
        return sendTencent(phones, sign, template, params);
    }

    @Override
    public TencentMessageResult send(List<String> phones, String sign, String template, Map<String, String> params) {
        if (Objects.isNull(phones) || phones.size() == 0) {
            throw new RuntimeException("手机号不能为空！");
        }
        return sendTencent(new ArrayList<>(phones), sign, template, params);
    }

    private TencentMessageResult sendTencent(ArrayList<String> phones, String sign, String template, Map<String, String> params) {
        SmsMultiSender smsMultiSender = new SmsMultiSender(tencentMessageProperties.getAppId(), tencentMessageProperties.getAppKey());
        ArrayList<String> paramList;
        if (Objects.nonNull(params)) {
            paramList = new ArrayList<>(params.size());
            for (int i = 1; i <= params.entrySet().size(); i++) {
                paramList.add(params.get(String.valueOf(i)));
            }
        } else {
            paramList = new ArrayList<>(1);
        }
        TencentMessageResult tencentMessageResult = new TencentMessageResult();
        try {
            SmsMultiSenderResult smsMultiSenderResult = smsMultiSender.sendWithParam("86", new ArrayList<>(phones), Integer.parseInt(template), paramList, sign, "", "");
            tencentMessageResult.setResult(smsMultiSenderResult.result);
            tencentMessageResult.setSuccess(smsMultiSenderResult.result == 0);
            tencentMessageResult.setErrMsg(smsMultiSenderResult.errMsg);
            tencentMessageResult.setExt(smsMultiSenderResult.ext);
            if (tencentMessageResult.isSuccess()) {
                List<TencentMessageResult.Detail> detailList = new LinkedList<>();
                smsMultiSenderResult.details.forEach(
                        e -> detailList.add(
                                TencentMessageResult.Detail
                                        .builder()
                                        .result(e.result)
                                        .errMsg(e.errmsg)
                                        .fee(e.fee)
                                        .mobile(e.mobile)
                                        .sid(e.sid)
                                        .build()));
                tencentMessageResult.setDetailList(detailList);
            }
        } catch (HTTPException | IOException e) {
            log.error("腾讯云短信发送失败！", e);
            tencentMessageResult.setSuccess(false);
        }
        return tencentMessageResult;
    }
}
