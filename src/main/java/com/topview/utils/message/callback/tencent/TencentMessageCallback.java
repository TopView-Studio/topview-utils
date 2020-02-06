package com.topview.utils.message.callback.tencent;

import lombok.Data;

import java.util.Date;

/**
 * 阿里云回调数据
 *
 * @author Albumen
 * @date 2020/2/5
 */
@Data
public class TencentMessageCallback {
    /**
     * 通道扩展码，默认没有开通（需要填空）
     */
    private String extend;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 短信签名
     */
    private String sign;

    /**
     * 用户回复的内容
     */
    private String text;

    /**
     * UNIX 时间戳（单位：秒）
     */
    private long time;

    /**
     * 发送时间（Date格式）
     */
    private Date sendTime;
}
