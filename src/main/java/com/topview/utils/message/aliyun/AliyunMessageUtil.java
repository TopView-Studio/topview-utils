package com.topview.utils.message.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.topview.utils.aliyun.AliyunSecurity;
import com.topview.utils.aliyun.AliyunStsBean;
import com.topview.utils.message.MessageUtil;
import com.topview.utils.message.callback.CallbackBase;
import com.topview.utils.message.callback.aliyun.AliyunCallbackResponse;
import com.topview.utils.message.callback.aliyun.AliyunCallbackResult;
import com.topview.utils.message.callback.aliyun.AliyunMessageCallback;
import com.topview.utils.message.config.AliyunMessageProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AliyunMessageUtil extends CallbackBase implements MessageUtil {
    @NonNull
    private AliyunSecurity aliyunSecurity;
    @NonNull
    private AliyunMessageProperties aliyunMessageProperties;
    @NonNull
    private AliyunProperties aliyunProperties;

    @Override
    public AliyunCallbackResult callback(HttpServletRequest request) {
        List<AliyunMessageCallback> aliyunMessageCallbacks = JSONArray.parseArray(parseRequest(request)).toJavaList(AliyunMessageCallback.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        aliyunMessageCallbacks.forEach(e -> {
            try {
                e.setSendDate(simpleDateFormat.parse(e.getSendTime()));
            } catch (ParseException ex) {
                log.error("阿里云回调短信发送时间解析错误！", ex);
                throw new RuntimeException("阿里云回调短信发送时间解析错误！");
            }
        });
        AliyunCallbackResult callbackResult = new AliyunCallbackResult();

        AliyunCallbackResponse aliyunCallbackResponse = new AliyunCallbackResponse();
        aliyunCallbackResponse.setCode(0);
        aliyunCallbackResponse.setMsg("接收成功");
        callbackResult.setCallbackResponse(aliyunCallbackResponse);

        callbackResult.setMessageCallbackList(aliyunMessageCallbacks);

        return callbackResult;
    }

    @Override
    public AliyunMessageResult send(String phone, String template) {
        return send(phone, aliyunMessageProperties.getSign(), template);
    }

    @Override
    public AliyunMessageResult send(List<String> phones, String template) {
        return send(phones, aliyunMessageProperties.getSign(), template);
    }

    @Override
    public AliyunMessageResult send(String phone, String sign, String template) {
        return send(phone, sign, template, null);
    }

    @Override
    public AliyunMessageResult send(List<String> phones, String sign, String template) {
        return send(phones, sign, template, null);
    }

    @Override
    public AliyunMessageResult send(String phone, String template, Map<String, String> params) {
        return send(phone, aliyunMessageProperties.getSign(), template, params);
    }

    @Override
    public AliyunMessageResult send(List<String> phones, String template, Map<String, String> params) {
        return send(phones, aliyunMessageProperties.getSign(), template, params);
    }

    @Override
    public AliyunMessageResult send(String phone, String sign, String template, Map<String, String> params) {
        return sendAliyun(phone, sign, template, params);
    }

    @Override
    public AliyunMessageResult send(List<String> phones, String sign, String template, Map<String, String> params) {
        if (Objects.isNull(phones) || phones.size() == 0) {
            throw new RuntimeException("手机号不能为空！");
        }
        return sendAliyun(String.join(",", phones), sign, template, params);
    }

    private AliyunMessageResult sendAliyun(String phones, String sign, String template, Map<String, String> params) {
        // 使用STS访问短信服务
        AliyunStsBean aliyunStsBean = aliyunSecurity.signSmsMessageSts();
        DefaultProfile profile = DefaultProfile
                .getProfile(aliyunProperties.getMessageRegion(),
                        aliyunStsBean.getAccessKeyId(),
                        aliyunStsBean.getAccessKeySecret(),
                        aliyunStsBean.getSecurityToken());
        IAcsClient client = new DefaultAcsClient(profile);

        // 初始化请求结构
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliyunProperties.getMessageDomain());
        request.setSysVersion(aliyunProperties.getMessageVersion());
        request.setSysProtocol(aliyunProperties.getMessageProtocol());
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", aliyunProperties.getMessageRegion());
        request.putQueryParameter("PhoneNumbers", phones);
        if (Objects.nonNull(sign)) {
            request.putQueryParameter("SignName", sign);
        } else {
            request.putQueryParameter("SignName", aliyunMessageProperties.getSign());
        }
        request.putQueryParameter("TemplateCode", template);
        if (Objects.nonNull(params)) {
            JSONObject jsonObject = new JSONObject();
            params.forEach(jsonObject::put);
            request.putQueryParameter("TemplateParam", jsonObject.toJSONString());
        }

        // 提交请求
        try {
            CommonResponse response = client.getCommonResponse(request);

            JSONObject jsonObject = JSON.parseObject(response.getData());
            AliyunMessageResult aliyunMessageResult = new AliyunMessageResult();
            aliyunMessageResult.setCode(jsonObject.getString("Code"));
            aliyunMessageResult.setMessage(jsonObject.getString("Message"));
            aliyunMessageResult.setSuccess("OK".equalsIgnoreCase(jsonObject.getString("Code")));

            return aliyunMessageResult;
        } catch (ClientException e) {
            log.error("阿里云发送短信接口调用失败！", e);

            AliyunMessageResult aliyunMessageResult = new AliyunMessageResult();
            aliyunMessageResult.setSuccess(false);

            return aliyunMessageResult;
        }
    }
}
