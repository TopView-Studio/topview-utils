package com.topview.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/4
 */
public interface HttpClientUtil {
    /**
     * 发送JSON请求
     *
     * @param url  请求地址
     * @param body JSON请求体
     * @return 执行结果（请求JSON格式结果）
     */
    String postJson(String url, String body);

    /**
     * 发送JSON请求
     *
     * @param url     请求地址
     * @param body    JSON请求体
     * @param charset 指定JSON请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postJson(String url, String body, String charset);

    /**
     * 发送JSON请求
     *
     * @param url     请求地址
     * @param params  地址请求参数（如?id=123）（不使用传 null）
     * @param body    JSON请求体
     * @param charset 指定JSON请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postJson(String url, List<BasicNameValuePair> params, String body, String charset);

    /**
     * 发送JSON请求
     *
     * @param url     请求地址
     * @param header  请求头参数（如Authorization:token）（不使用传 null）
     * @param params  地址请求参数（如?id=123）（不使用传 null）
     * @param body    JSON请求体
     * @param charset 指定JSON请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postJson(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, String body, String charset);

    /**
     * 发送表单请求
     *
     * @param url  请求地址
     * @param body 表单请求体
     * @return 执行结果（请求JSON格式结果）
     */
    String postForm(String url, List<NameValuePair> body);

    /**
     * 发送表单请求
     *
     * @param url     请求地址
     * @param body    表单请求体
     * @param charset 指定表单请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postForm(String url, List<NameValuePair> body, String charset);

    /**
     * 发送表单请求
     *
     * @param url     请求地址
     * @param params  地址请求参数（如?id=123)（不使用传 null）
     * @param body    表单请求体
     * @param charset 指定表单请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postForm(String url, List<BasicNameValuePair> params, List<NameValuePair> body, String charset);

    /**
     * 发送表单请求
     *
     * @param url     请求地址
     * @param header  请求头参数（如Authorization:token）（不使用传 null）
     * @param params  地址请求参数（如?id=123)（不使用传 null）
     * @param body    表单请求体
     * @param charset 指定表单请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String postForm(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, List<NameValuePair> body, String charset);

    /**
     * 发送POST请求（兼容旧版，默认JSON请求）
     *
     * @param url    请求地址
     * @param params JSON请求体
     * @return 执行结果（请求JSON格式结果）
     * @deprecated {@link HttpClientUtil#postJson(String, String)}
     */
    @Deprecated
    String post(String url, List<BasicNameValuePair> params);

    /**
     * 发送POST请求（兼容旧版，默认JSON请求）
     *
     * @param url     请求地址
     * @param params  JSON请求体
     * @param charset 指定表单请求体处理字符集
     * @return 执行结果（请求JSON格式结果）
     * @deprecated {@link HttpClientUtil#postJson(String, String, String)}
     */
    @Deprecated
    String post(String url, List<BasicNameValuePair> params, String charset);

    /**
     * 发送POST请求
     *
     * @param url         请求地址
     * @param httpEntity  请求体
     * @param charset     指定请求体处理字符集
     * @param contentType 请求类型
     * @return 执行结果（请求JSON格式结果）
     */
    String post(String url, HttpEntity httpEntity, String charset, String contentType);

    /**
     * 发送POST请求
     *
     * @param url         请求地址
     * @param httpEntity  请求体
     * @param charset     指定请求体处理字符集
     * @param contentType 请求类型
     * @param header      请求头参数（如Authorization:token）（不使用传 null）
     * @return 执行结果（请求JSON格式结果）
     */
    String post(String url, HttpEntity httpEntity, String charset, String contentType, List<BasicNameValuePair> header);

    /**
     * 发送GET请求
     *
     * @param url    请求地址（不附带参数）
     * @param header 请求头参数（如Authorization:token）（不使用传 null）
     * @param params 地址请求参数（如?id=123)
     * @return 执行结果（请求JSON格式结果）
     */
    String get(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params);

    /**
     * 发送GET请求
     *
     * @param url     请求地址（不附带参数）
     * @param header  请求头参数（如Authorization:token）（不使用传 null）
     * @param params  地址请求参数（如?id=123)（不使用传 null）
     * @param charset 指定地址请求参数处理字符集
     * @return 执行结果（请求JSON格式结果）
     */
    String get(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, String charset);

    /**
     * 发送GET请求
     *
     * @param uri    请求地址（附带参数）
     * @param header 请求头参数（如Authorization:token）（不使用传 null）
     * @return 执行结果（请求JSON格式结果）
     */
    String get(String uri, List<BasicNameValuePair> header);

    /**
     * 发送GET请求
     *
     * @param uri 请求地址（附带参数）
     * @return 执行结果（请求JSON格式结果）
     */
    String get(String uri);
}
