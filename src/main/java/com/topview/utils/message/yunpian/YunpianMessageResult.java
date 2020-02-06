package com.topview.utils.message.yunpian;

import com.topview.utils.message.MessageResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class YunpianMessageResult extends MessageResult {
    /**
     * 发送是否成功，可通过此值判断发送结果
     */
    private boolean success;

    private int totalCount;

    /**
     * 总扣费金额
     */
    private String totalFee;

    /**
     * 计费单位
     */
    private String unit;

    private List<Data> data;

    @lombok.Data
    public static class Data {
        /**
         * 0表示成功
         */
        private int code;

        private String msg;

        private int count;

        /**
         * 扣费金额
         */
        private String fee;

        /**
         * 计费单位
         */
        private String unit;

        private String mobile;

        /**
         * 短信 id
         */
        private long sid;
    }
}
