package http;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.topview.utils.http.HttpClientFactory;
import com.topview.utils.http.HttpClientUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author Albumen
 * @date 2020/2/4
 */
@SpringBootTest
@SpringBootApplication
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.topview.utils")
public class HttpTest {
    @Rule
    public static WireMockRule wireMockRule = new WireMockRule(8500);
    @Autowired
    private HttpClientUtil httpClientUtil;
    @Autowired
    private HttpClientFactory httpClientFactory;
    private final static String SUCCESS = "成功获取";

    @BeforeAll
    public static void setUp() {
        wireMockRule.start();
        setStub();
    }

    @AfterAll
    public static void after() {
        wireMockRule.stop();
    }

    private static void setStub() {
    }

    @Test
    public void testPostJson() throws UnsupportedEncodingException {
        wireMockRule.stubFor(post(urlEqualTo("/testJson"))
                .withHeader("Content-Type", equalToIgnoreCase("application/json;charset=utf-8"))
                .withRequestBody(equalTo("{\"test\"=\"1\"}"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes(StandardCharsets.UTF_8))
                                .withHeader("Content-Type", "text/plain;charset=utf-8")));

        Assert.assertEquals(SUCCESS, httpClientUtil.postJson("http://localhost:8500/testJson", "{\"test\"=\"1\"}"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testJson")
                        .setJsonBody("{\"test\"=\"1\"}")
                        .postJson()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testJson"))
                .withHeader("Content-Type", equalToIgnoreCase("application/json;charset=gbk"))
                .withRequestBody(equalTo("{\"test\"=\"2\"}"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        Assert.assertEquals(SUCCESS, httpClientUtil.postJson("http://localhost:8500/testJson",
                "{\"test\"=\"2\"}", "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testJson")
                        .setJsonBody("{\"test\"=\"2\"}")
                        .setCharset("gbk")
                        .postJson()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testJson?test=param"))
                .withHeader("Content-Type", equalToIgnoreCase("application/json;charset=gbk"))
                .withRequestBody(equalTo("{\"test\"=\"3\"}"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<BasicNameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("test", "param"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postJson("http://localhost:8500/testJson", params,
                "{\"test\"=\"3\"}", "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testJson")
                        .setJsonBody("{\"test\"=\"3\"}")
                        .setParams(params)
                        .setCharset("gbk")
                        .postJson()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testJson?test=param"))
                .withHeader("Content-Type", equalToIgnoreCase("application/json;charset=gbk"))
                .withRequestBody(equalTo("{\"test\"=\"4\"}"))
                .withHeader("Test-Header", equalToIgnoreCase("header"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<BasicNameValuePair> header = new LinkedList<>();
        header.add(new BasicNameValuePair("Test-Header", "header"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postJson("http://localhost:8500/testJson", header, params,
                "{\"test\"=\"4\"}", "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testJson")
                        .setJsonBody("{\"test\"=\"4\"}")
                        .setHeader(header)
                        .setParams(params)
                        .setCharset("gbk")
                        .postJson()
        );
    }

    @Test
    public void testForm() throws UnsupportedEncodingException {
        wireMockRule.stubFor(post(urlEqualTo("/testForm"))
                .withHeader("Content-Type", equalToIgnoreCase("application/x-www-form-urlencoded;charset=utf-8"))
                .withRequestBody(equalTo("test=5&kind=form"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<NameValuePair> test5 = new LinkedList<>();
        test5.add(new BasicNameValuePair("test", "5"));
        test5.add(new BasicNameValuePair("kind", "form"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postForm("http://localhost:8500/testForm", test5));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testForm")
                        .setFormBody(test5)
                        .postForm()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testForm"))
                .withHeader("Content-Type", equalToIgnoreCase("application/x-www-form-urlencoded;charset=gbk"))
                .withRequestBody(equalTo("test=6&kind=form"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes(StandardCharsets.UTF_8))
                                .withHeader("Content-Type", "text/plain;charset=utf-8")));
        List<NameValuePair> test6 = new LinkedList<>();
        test6.add(new BasicNameValuePair("test", "6"));
        test6.add(new BasicNameValuePair("kind", "form"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postForm("http://localhost:8500/testForm", test6, "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testForm")
                        .setFormBody(test6)
                        .setCharset("gbk")
                        .postForm()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testForm?test=param"))
                .withHeader("Content-Type", equalToIgnoreCase("application/x-www-form-urlencoded;charset=gbk"))
                .withRequestBody(equalTo("test=7&kind=form"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<NameValuePair> test7 = new LinkedList<>();
        test7.add(new BasicNameValuePair("test", "7"));
        test7.add(new BasicNameValuePair("kind", "form"));
        List<BasicNameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("test", "param"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postForm("http://localhost:8500/testForm", params, test7, "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testForm")
                        .setFormBody(test7)
                        .setParams(params)
                        .setCharset("gbk")
                        .postForm()
        );

        wireMockRule.stubFor(post(urlEqualTo("/testForm?test=param"))
                .withHeader("Content-Type", equalToIgnoreCase("application/x-www-form-urlencoded;charset=gbk"))
                .withRequestBody(equalTo("test=8&kind=form"))
                .withHeader("Test-Header", equalToIgnoreCase("header"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<NameValuePair> test8 = new LinkedList<>();
        test8.add(new BasicNameValuePair("test", "7"));
        test8.add(new BasicNameValuePair("kind", "form"));
        List<BasicNameValuePair> headers = new LinkedList<>();
        headers.add(new BasicNameValuePair("Test-Header", "header"));
        Assert.assertEquals(SUCCESS, httpClientUtil.postForm("http://localhost:8500/testForm", headers, params, test8, "gbk"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient()
                        .setUrl("http://localhost:8500/testForm")
                        .setFormBody(test8)
                        .setParams(params)
                        .setHeader(headers)
                        .setCharset("gbk")
                        .postForm()
        );
    }

    @Test
    public void testGet() throws UnsupportedEncodingException {
        wireMockRule.stubFor(get(urlEqualTo("/testGet"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        Assert.assertEquals(SUCCESS, httpClientUtil.get("http://localhost:8500/testGet"));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient("http://localhost:8500/testGet")
                        .get()
        );

        wireMockRule.stubFor(get(urlEqualTo("/testGet"))
                .withHeader("Test-Header", equalToIgnoreCase("header"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<BasicNameValuePair> headers = new LinkedList<>();
        headers.add(new BasicNameValuePair("Test-Header", "header"));
        Assert.assertEquals(SUCCESS, httpClientUtil.get("http://localhost:8500/testGet", headers));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient("http://localhost:8500/testGet")
                        .setHeader(headers)
                        .get()
        );

        wireMockRule.stubFor(get(urlEqualTo("/testGet?test=Get"))
                .withHeader("Test-Header", equalToIgnoreCase("header"))
                .willReturn(
                        aResponse()
                                .withBody(SUCCESS.getBytes("gbk"))
                                .withHeader("Content-Type", "text/plain;charset=gbk")));
        List<BasicNameValuePair> params = new LinkedList<>();
        params.add(new BasicNameValuePair("test", "Get"));
        Assert.assertEquals(SUCCESS, httpClientUtil.get("http://localhost:8500/testGet", headers, params));
        Assert.assertEquals(SUCCESS,
                httpClientFactory
                        .getClient("http://localhost:8500/testGet")
                        .setHeader(headers)
                        .setParams(params)
                        .get()
        );
    }
}
