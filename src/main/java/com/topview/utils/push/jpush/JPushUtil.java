package com.topview.utils.push.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.topview.utils.push.PlatformEnum;
import com.topview.utils.push.PushUtil;
import com.topview.utils.push.config.JPushProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/6
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JPushUtil implements PushUtil {
    @NonNull
    private JPushProperties jPushProperties;

    @Override
    public boolean sendMessageByTag(String content, List<String> tags) {
        return sendMessageByTag(content, PlatformEnum.All, tags);
    }

    @Override
    public boolean sendMessageByTag(String content, PlatformEnum platform, List<String> tags) {
        return sendMessage(content, Audience.tag(tags), changeToJPushPlatform(platform));
    }

    @Override
    public boolean sendMessageByAlias(String content, List<String> alias) {
        return sendMessageByAlias(content, PlatformEnum.All, alias);
    }

    @Override
    public boolean sendMessageByAlias(String content, PlatformEnum platform, List<String> alias) {
        return sendMessage(content, Audience.alias(alias), changeToJPushPlatform(platform));
    }

    @Override
    public boolean sendNoticeByTag(String content, List<String> tags) {
        return sendNoticeByTag(content, PlatformEnum.All, tags);
    }

    @Override
    public boolean sendNoticeByTag(String content, PlatformEnum platform, List<String> tags) {
        return sendNotice(content, Audience.tag(tags), changeToJPushPlatform(platform));
    }

    @Override
    public boolean sendNoticeByAlias(String content, List<String> alias) {
        return sendNoticeByAlias(content, PlatformEnum.All, alias);
    }

    @Override
    public boolean sendNoticeByAlias(String content, PlatformEnum platform, List<String> alias) {
        return sendNotice(content, Audience.alias(alias), changeToJPushPlatform(platform));
    }

    private boolean sendMessage(String content, Audience audience, Platform platform) {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
        return send(pushPayload, 3);
    }

    private boolean sendNotice(String content, Audience audience, Platform platform) {
        PushPayload pushPayload = PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(audience)
                .setNotification(Notification.alert(content))
                .build();
        return send(pushPayload, 3);
    }

    private boolean send(PushPayload pushPayload, int retry) {
        try {
            JPushClient jPushClient = new JPushClient(jPushProperties.getMasterSecret(), jPushProperties.getAppKey());
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            if (pushResult.isResultOK()) {
                return true;
            } else {
                log.error("推送出错！ " + JSONObject.toJSONString(pushResult));
                return false;
            }
        } catch (APIConnectionException e) {
            log.error("连接失败，尝试重试！", e);
            if (retry > 0) {
                return send(pushPayload, retry - 1);
            } else {
                log.error("3次重试失败！");
                return false;
            }
        } catch (APIRequestException e) {
            log.error("连接出错!", e);
            return false;
        }
    }

    private Platform changeToJPushPlatform(PlatformEnum platformEnum) {
        switch (platformEnum) {
            case Android:
                return Platform.android();
            case IOS:
                return Platform.ios();
            case WinPhone:
                return Platform.winphone();
            default:
                return Platform.all();
        }
    }
}
