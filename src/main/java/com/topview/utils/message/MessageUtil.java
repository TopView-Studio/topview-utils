package com.topview.utils.message;

import com.topview.utils.message.callback.CallbackResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Albumen
 * @date on 2020/02/05
 */
public interface MessageUtil {
    /**
     * 短信回复云平台回调接口 <p/>
     * 结果中的 callbackResponse 为 Controller 可以直接返回对象，JSON化后符合云平台预期结果
     *
     * @param request http请求信息
     * @return 回调结果（包含返回给 Controller 和短信回复的数据）
     */
    CallbackResult callback(HttpServletRequest request);

    /**
     * 发送短信
     *
     * @param phone    手机号
     * @param template 模板号
     * @return 发送结果
     */
    MessageResult send(String phone, String template);

    /**
     * 发送短信
     *
     * @param phones   手机号列表
     * @param template 模板号
     * @return 发送结果
     */
    MessageResult send(List<String> phones, String template);

    /**
     * 发送短信
     *
     * @param phone    手机号
     * @param sign     短信签名（云片的签名整合在模板中，此参数传任意值均可）
     * @param template 模板号
     * @return 发送结果
     */
    MessageResult send(String phone, String sign, String template);

    /**
     * 发送短信
     *
     * @param phones   手机号列表
     * @param sign     短信签名（云片的签名整合在模板中，此参数传任意值均可）
     * @param template 模板号
     * @return 发送结果
     */
    MessageResult send(List<String> phones, String sign, String template);

    /**
     * 发送短信
     *
     * @param phone    手机号
     * @param template 模板号
     * @param params   模板参数 <p/>
     *                 阿里云： K-模板中关键词，V-对应置换的词 <p/>
     *                 腾讯云： K-模板中关键词的编号（从1开始，Map中有多少个元素对应的编号都要有），V-对应置换的词 <p/>
     *                 云片：  K-模板中关键词，V-对应置换的词 <p/>
     * @return 发送结果
     */
    MessageResult send(String phone, String template, Map<String, String> params);

    /**
     * 发送短信
     *
     * @param phones   手机号列表
     * @param template 模板号
     * @param params   模板参数 <p/>
     *                 阿里云： K-模板中关键词，V-对应置换的词 <p/>
     *                 腾讯云： K-模板中关键词的编号（从1开始，Map中有多少个元素对应的编号都要有），V-对应置换的词 <p/>
     *                 云片：  K-模板中关键词，V-对应置换的词 <p/>
     * @return 发送结果
     */
    MessageResult send(List<String> phones, String template, Map<String, String> params);

    /**
     * 发送短信
     *
     * @param phone    手机号
     * @param sign     短信签名（云片的签名整合在模板中，此参数传任意值均可）
     * @param template 模板号
     * @param params   模板参数 <p/>
     *                 阿里云： K-模板中关键词，V-对应置换的词 <p/>
     *                 腾讯云： K-模板中关键词的编号（从1开始，Map中有多少个元素对应的编号都要有），V-对应置换的词 <p/>
     *                 云片：  K-模板中关键词，V-对应置换的词 <p/>
     * @return 发送结果
     */
    MessageResult send(String phone, String sign, String template, Map<String, String> params);

    /**
     * 发送短信
     *
     * @param phones   手机号列表
     * @param sign     短信签名（云片的签名整合在模板中，此参数传任意值均可）
     * @param template 模板号
     * @param params   模板参数 <p/>
     *                 阿里云： K-模板中关键词，V-对应置换的词 <p/>
     *                 腾讯云： K-模板中关键词的编号（从1开始，Map中有多少个元素对应的编号都要有），V-对应置换的词 <p/>
     *                 云片：  K-模板中关键词，V-对应置换的词 <p/>
     * @return 发送结果
     */
    MessageResult send(List<String> phones, String sign, String template, Map<String, String> params);
}
