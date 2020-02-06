package com.topview.utils.message.callback.tencent;

import com.topview.utils.message.callback.CallbackResponse;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Controller返回给云服务的内容
 *
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class TencentCallbackResponse extends CallbackResponse {
    private int result;
    private String errmsg;
}
