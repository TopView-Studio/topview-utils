package com.topview.utils.message.callback.yunpian;

import com.topview.utils.message.callback.CallbackResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YunpianCallbackResult extends CallbackResult {
    /**
     * Controller返回给云服务的内容
     */
    private String callbackResponse;

    /**
     * 云片回调数据
     */
    private YunpianMessageCallback messageCallback;
}
