package com.topview.utils.http.impl;

import com.topview.utils.http.config.ConnectionProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * @author yongPhone
 * @date on 2018/4/19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpConnectionManager implements InitializingBean {
    @NonNull
    private ConnectionProperties connectionProperties;

    private static final String HTTPS = "https";
    private static final String HTTP = "http";

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        LayeredConnectionSocketFactory layeredConnectionSocketFactory;
        PlainConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
        try {
            layeredConnectionSocketFactory = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            log.error("HTTPS连接初始化失败，找不到加密算法！", e);
            throw e;
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(HTTPS, layeredConnectionSocketFactory)
                .register(HTTP, plainConnectionSocketFactory).build();
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(connectionProperties.getMaxTotal());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(connectionProperties.getDefaultMaxPerRoute());
    }

    public CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
    }
}
