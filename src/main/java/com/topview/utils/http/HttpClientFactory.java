package com.topview.utils.http;

import com.topview.utils.http.config.ConnectionProperties;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author Albumen
 * @date 2020/2/4
 */
@Component
@RequiredArgsConstructor
public class HttpClientFactory {
    @NonNull
    private HttpClientUtil httpClientUtil;
    @NonNull
    private ConnectionProperties connectionProperties;

    public Builder getClient() {
        Builder builder = new Builder(httpClientUtil);
        builder.setCharset(connectionProperties.getDefaultCharset());
        return builder;
    }


    public Builder getClient(String url) {
        Builder builder = new Builder(httpClientUtil);
        builder.setCharset(connectionProperties.getDefaultCharset());
        builder.setUrl(url);
        return builder;
    }

    @Data
    @Accessors(chain = true)
    public static class Builder {
        private String url;
        private List<BasicNameValuePair> header;
        private List<BasicNameValuePair> params;
        private HttpEntity httpEntity;
        private String jsonBody;
        private List<NameValuePair> formBody;
        private String charset;
        private String contentType;

        private HttpClientUtil httpClientUtil;

        protected Builder(HttpClientUtil httpClientUtil) {
            this.httpClientUtil = httpClientUtil;
        }

        public String get() {
            if (Objects.isNull(params)) {
                return httpClientUtil.get(url, header);
            } else {
                return httpClientUtil.get(url, header, params, charset);
            }
        }

        public String postJson() {
            return httpClientUtil.postJson(url, header, params, jsonBody, charset);
        }

        public String postForm() {
            return httpClientUtil.postForm(url, header, params, formBody, charset);
        }

        public String post() {
            return httpClientUtil.post(url, httpEntity, charset, contentType, header);
        }
    }
}
