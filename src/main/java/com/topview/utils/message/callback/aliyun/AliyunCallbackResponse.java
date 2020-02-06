package com.topview.utils.message.callback.aliyun;

import com.topview.utils.message.callback.CallbackResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Controller返回给云服务的内容
 *
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunCallbackResponse extends CallbackResponse {
    private int code;
    private String msg;
}
