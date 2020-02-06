package com.topview.utils.message.tencent;

import com.topview.utils.message.MessageResult;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TencentMessageResult extends MessageResult {
    /**
     * 发送是否成功，可通过此值判断发送结果
     */
    private boolean success;

    /**
     * 0 表示成功
     */
    private int result;

    private String errMsg;

    /**
     * 用户的 session 内容，腾讯 server 回包中会原样返回，暂未使用
     */
    private String ext;

    /**
     * 每条短信发送结果，单发模式下此列表也有一个对象
     */
    private List<Detail> detailList;

    @Data
    @Builder
    public static class Detail {
        /**
         * 0 表示成功
         */
        private int result;

        private String errMsg;

        public String mobile;

        /**
         * 短信计费的条数
         */
        private int fee;

        /**
         * 本次发送标识 ID，标识一次短信下发记录
         */
        private String sid;
    }
}
