package com.topview.utils.message.callback.yunpian;

import lombok.Data;

import java.util.Date;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
public class YunpianMessageCallback {
    /**
     * 唯一序列号，无特别含义及用途
     */
    private String id;

    /**
     * 回复短信的手机号
     */
    private String mobile;

    /**
     * 回复短信的时间（String格式）
     */
    private String replyTime;

    /**
     * 回复短信的时间（Date格式）
     */
    private Date replyDate;

    /**
     * 回复短信的时间（Date格式）
     */
    private String text;

    /**
     * 发送时传入的扩展子号，未申请扩展功能或者未传入时为空串
     */
    private String extend;

    /**
     * 系统分配的扩展子号
     */
    private String baseExtend;

    /**
     * 签名字段
     */
    private String _sign;
}
