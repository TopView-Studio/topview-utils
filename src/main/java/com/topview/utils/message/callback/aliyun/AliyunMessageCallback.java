package com.topview.utils.message.callback.aliyun;

import lombok.Data;

import java.util.Date;

/**
 * 阿里云回调数据
 *
 * @author Albumen
 * @date 2020/2/5
 */
@Data
public class AliyunMessageCallback {
    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 发送时间（String格式）
     */
    private String sendTime;

    /**
     * 发送时间（Date格式）
     */
    private Date sendDate;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 签名信息
     */
    private String signName;

    /**
     * 上行短信扩展号码
     */
    private String destCode;

    /**
     * 序列号
     */
    private long sequenceId;
}
