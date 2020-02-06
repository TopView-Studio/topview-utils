package com.topview.utils.push;

import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/6
 */
public interface PushUtil {
    /**
     * 发送应用内消息。或者称作：自定义消息，透传消息。
     *
     * @param content 信息内容
     * @param tags    用户标签（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendMessageByTag(String content, List<String> tags);

    /**
     * 发送应用内消息。或者称作：自定义消息，透传消息。
     *
     * @param content  信息内容
     * @param platform 推送平台
     * @param tags     用户标签（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendMessageByTag(String content, PlatformEnum platform, List<String> tags);

    /**
     * 发送应用内消息。或者称作：自定义消息，透传消息。
     *
     * @param content 信息内容
     * @param alias   设备别名（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendMessageByAlias(String content, List<String> alias);

    /**
     * 发送应用内消息。或者称作：自定义消息，透传消息。
     *
     * @param content  信息内容
     * @param platform 推送平台
     * @param alias    设备别名（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendMessageByAlias(String content, PlatformEnum platform, List<String> alias);

    /**
     * 发送通知。
     *
     * @param content 信息内容
     * @param tags    用户标签（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendNoticeByTag(String content, List<String> tags);

    /**
     * 发送通知。
     *
     * @param content  信息内容
     * @param platform 推送平台
     * @param tags     用户标签（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendNoticeByTag(String content, PlatformEnum platform, List<String> tags);

    /**
     * 发送通知。
     *
     * @param content 信息内容
     * @param alias   设备别名（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendNoticeByAlias(String content, List<String> alias);

    /**
     * 发送通知。
     *
     * @param content  信息内容
     * @param platform 推送平台
     * @param alias    设备别名（多标签关系为或，最多20个）
     * @return 执行结果
     */
    boolean sendNoticeByAlias(String content, PlatformEnum platform, List<String> alias);
}
