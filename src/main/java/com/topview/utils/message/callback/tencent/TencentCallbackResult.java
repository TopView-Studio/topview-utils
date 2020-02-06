package com.topview.utils.message.callback.tencent;

import com.topview.utils.message.callback.CallbackResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口返回内容
 *
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TencentCallbackResult extends CallbackResult {
    /**
     * Controller返回给云服务的内容
     */
    private TencentCallbackResponse callbackResponse;

    /**
     * 腾讯云回调数据
     */
    private TencentMessageCallback messageCallback;
}
