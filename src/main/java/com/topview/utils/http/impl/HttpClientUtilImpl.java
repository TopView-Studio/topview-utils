package com.topview.utils.http.impl;

import com.topview.utils.http.HttpClientUtil;
import com.topview.utils.http.config.ConnectionProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * @author yongPhone
 * @date on 2018/4/20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpClientUtilImpl implements HttpClientUtil {
    @NonNull
    private HttpConnectionManager httpConnectionManager;
    @NonNull
    private ConnectionProperties connectionProperties;

    @Override
    public String postJson(String url, String body) {
        return postJson(url, body, connectionProperties.getDefaultCharset());
    }

    @Override
    public String postJson(String url, String body, String charset) {
        return postJson(url, null, body, charset);
    }

    @Override
    public String postJson(String url, List<BasicNameValuePair> params, String body, String charset) {
        return postJson(url, null, params, body, charset);
    }

    @Override
    public String postJson(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, String body, String charset) {
        StringEntity stringEntity = new StringEntity(body, charset);
        return post(getUri(url, params, charset), stringEntity, charset, "application/json", header);
    }

    @Override
    public String postForm(String url, List<NameValuePair> body) {
        return postForm(url, body, connectionProperties.getDefaultCharset());
    }

    @Override
    public String postForm(String url, List<NameValuePair> body, String charset) {
        return postForm(url, null, body, charset);
    }

    @Override
    public String postForm(String url, List<BasicNameValuePair> params, List<NameValuePair> body, String charset) {
        return postForm(url, null, params, body, charset);
    }

    @Override
    public String postForm(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, List<NameValuePair> body, String charset) {
        try {
            return post(getUri(url, params, charset), new UrlEncodedFormEntity(body, charset), charset, "application/x-www-form-urlencoded", header);
        } catch (UnsupportedEncodingException e) {
            log.error("字符集不支持！", e);
            throw new RuntimeException("字符集不支持！");
        }
    }

    @Override
    @Deprecated
    public String post(String url, List<BasicNameValuePair> params) {
        return post(url, params, connectionProperties.getDefaultCharset());
    }

    @Override
    @Deprecated
    public String post(String url, List<BasicNameValuePair> params, String charset) {
        try {
            return post(url, new UrlEncodedFormEntity(params, charset), charset, "application/json");
        } catch (UnsupportedEncodingException e) {
            log.error("字符集不支持！", e);
            throw new RuntimeException("字符集不支持！");
        }
    }

    @Override
    public String post(String url, HttpEntity httpEntity, String charset, String contentType) {
        return post(url, httpEntity, charset, contentType, null);
    }

    @Override
    public String post(String url, HttpEntity httpEntity, String charset, String contentType, List<BasicNameValuePair> header) {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = getHttpHeader(header, httpPost);

        httpPost.setHeader("Content-Type", contentType + ";charset=" + charset);
        httpPost.setEntity(httpEntity);

        try {
            return responseHandler(httpClient.execute(httpPost));
        } catch (IOException e) {
            log.error("执行请求失败！", e);
            return null;
        }
    }

    @Override
    public String get(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params) {
        return get(url, header, params, connectionProperties.getDefaultCharset());
    }

    @Override
    public String get(String url, List<BasicNameValuePair> header, List<BasicNameValuePair> params, String charset) {
        return get(getUri(url, params, charset), header);
    }

    private String getUri(String url, List<BasicNameValuePair> params, String charset) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (Objects.nonNull(params)) {
            stringBuilder.append("?");
            try {
                for (int i = 0; i < params.size(); i++) {
                    BasicNameValuePair e = params.get(i);
                    if (i > 0) {
                        stringBuilder.append("&");
                    }
                    stringBuilder.append(URLEncoder.encode(e.getName(), charset));
                    stringBuilder.append("=");
                    stringBuilder.append(URLEncoder.encode(e.getValue(), charset));
                }
            } catch (UnsupportedEncodingException ex) {
                log.error("字符集不支持！", ex);
                throw new RuntimeException("字符集不支持！");
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public String get(String uri, List<BasicNameValuePair> header) {
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpClient httpClient = getHttpHeader(header, httpGet);

        try {
            return responseHandler(httpClient.execute(httpGet));
        } catch (IOException e) {
            log.error("执行请求失败！", e);
            return null;
        }
    }

    @Override
    public String get(String uri) {
        return get(uri, null);
    }

    private CloseableHttpClient getHttpHeader(List<BasicNameValuePair> header, HttpRequestBase httpGet) {
        CloseableHttpClient httpClient = httpConnectionManager.getHttpClient();

        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(connectionProperties.getConnectTimeout()).
                setSocketTimeout(connectionProperties.getSocketTimeout())
                .build();

        httpGet.setConfig(requestConfig);

        httpGet.setHeader("Accept", "application/json");
        if (Objects.nonNull(header)) {
            header.forEach(e -> httpGet.setHeader(e.getName(), e.getValue()));
        }
        return httpClient;
    }

    private String responseHandler(CloseableHttpResponse execute) {
        try {
            try (CloseableHttpResponse response = execute) {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    log.error("返回值大于等于300！ 返回值:" + statusLine.getStatusCode() + " 原因：" + statusLine.getReasonPhrase());
                    return null;
                }
                if (entity == null) {
                    log.error("无返回结果！");
                    return null;
                }
                ContentType resultContentType = ContentType.getOrDefault(entity);
                Charset resultCharset = resultContentType.getCharset();
                return EntityUtils.toString(entity, resultCharset);
            }
        } catch (IOException e) {
            log.error("执行请求失败！", e);
            return null;
        }
    }
}
