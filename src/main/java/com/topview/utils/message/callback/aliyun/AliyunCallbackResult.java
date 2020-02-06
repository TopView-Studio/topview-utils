package com.topview.utils.message.callback.aliyun;

import com.topview.utils.message.callback.CallbackResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 接口返回内容
 *
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunCallbackResult extends CallbackResult {
    /**
     * Controller返回给云服务的内容
     */
    private AliyunCallbackResponse callbackResponse;

    /**
     * 阿里云回调数据
     */
    private List<AliyunMessageCallback> messageCallbackList;
}
