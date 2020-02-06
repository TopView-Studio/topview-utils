package com.topview.utils.message.aliyun;

import com.topview.utils.message.MessageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunMessageResult extends MessageResult {
    /**
     * 发送是否成功，可通过此值判断发送结果
     */
    private boolean success;

    /**
     * OK 代表成功
     */
    private String code;

    /**
     * 状态码的描述
     */
    private String message;
}
