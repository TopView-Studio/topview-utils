package com.topview.utils.message;

import com.topview.utils.message.aliyun.AliyunMessageResult;
import com.topview.utils.message.aliyun.AliyunMessageUtil;
import com.topview.utils.message.config.AliyunMessageProperties;
import com.topview.utils.message.config.TencentMessageProperties;
import com.topview.utils.message.config.YunpianMessageProperties;
import com.topview.utils.message.tencent.TencentMessageResult;
import com.topview.utils.message.tencent.TencentMessageUtil;
import com.topview.utils.message.yunpian.YunpianMessageResult;
import com.topview.utils.message.yunpian.YunpianMessageUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Component
@RequiredArgsConstructor
public class MessageFactory {
    @NonNull
    private AliyunMessageUtil aliyunMessageUtil;
    @NonNull
    private TencentMessageUtil tencentMessageUtil;
    @NonNull
    private YunpianMessageUtil yunpianMessageUtil;
    @NonNull
    private AliyunMessageProperties aliyunMessageProperties;
    @NonNull
    private TencentMessageProperties tencentMessageProperties;
    @NonNull
    private YunpianMessageProperties yunpianMessageProperties;

    public Builder getBuilder() {
        Builder builder = new Builder();
        builder.aliyunMessageProperties = aliyunMessageProperties;
        builder.aliyunMessageUtil = aliyunMessageUtil;
        builder.tencentMessageProperties = tencentMessageProperties;
        builder.tencentMessageUtil = tencentMessageUtil;
        builder.yunpianMessageProperties = yunpianMessageProperties;
        builder.yunpianMessageUtil = yunpianMessageUtil;
        return builder;
    }

    @Data
    @Accessors(chain = true)
    public static class Builder {
        private AliyunMessageUtil aliyunMessageUtil;
        private TencentMessageUtil tencentMessageUtil;
        private YunpianMessageUtil yunpianMessageUtil;
        private AliyunMessageProperties aliyunMessageProperties;
        private TencentMessageProperties tencentMessageProperties;
        private YunpianMessageProperties yunpianMessageProperties;

        private String phone;
        private List<String> phones;
        private String sign;
        private String template;
        private Map<String, String> params;

        public AliyunMessageResult sendAliyun() {
            if (Objects.isNull(sign)) {
                sign = aliyunMessageProperties.getSign();
            }
            if (Objects.nonNull(phone)) {
                return aliyunMessageUtil.send(phone, sign, template, params);
            } else {
                return aliyunMessageUtil.send(phones, sign, template, params);
            }
        }

        public TencentMessageResult sendTencent() {
            if (Objects.isNull(sign)) {
                sign = tencentMessageProperties.getSign();
            }
            if (Objects.nonNull(phone)) {
                return tencentMessageUtil.send(phone, sign, template, params);
            } else {
                return tencentMessageUtil.send(phones, sign, template, params);
            }
        }

        public YunpianMessageResult sendYunpian() {
            if (Objects.nonNull(phone)) {
                return yunpianMessageUtil.send(phone, sign, template, params);
            } else {
                return yunpianMessageUtil.send(phones, sign, template, params);
            }
        }
    }
}
