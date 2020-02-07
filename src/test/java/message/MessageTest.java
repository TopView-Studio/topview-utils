package message;

import aliyun.AliyunStsMock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aliyuncs.http.ProtocolType;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.topview.utils.aliyun.AliyunSecurity;
import com.topview.utils.aliyun.AliyunTokenCache;
import com.topview.utils.aliyun.config.AliyunSecurityProperties;
import com.topview.utils.message.aliyun.AliyunMessageResult;
import com.topview.utils.message.aliyun.AliyunMessageUtil;
import com.topview.utils.message.callback.aliyun.AliyunCallbackResult;
import com.topview.utils.message.config.AliyunMessageProperties;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Albumen
 * @date 2020/2/5
 */
@SpringBootTest
@SpringBootApplication
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.topview.utils")
public class MessageTest {
    @Rule
    public static WireMockRule aliyunSecurityWireMockRule = new WireMockRule(8502);
    @Rule
    public static WireMockRule aliyunMessageWireMockRule = new WireMockRule(8503);
    @Autowired
    private AliyunSecurityProperties aliyunSecurityProperties;
    @Autowired
    private AliyunTokenCache aliyunTokenCache;
    @Autowired
    private AliyunMessageProperties aliyunMessageProperties;
    @Autowired
    private com.topview.utils.message.aliyun.AliyunProperties aliyunProperties;

    @BeforeAll
    public static void setUp() {
        aliyunSecurityWireMockRule.start();
        aliyunMessageWireMockRule.start();
        setStub();
    }

    private static void setStub() {
        AliyunStsMock.setStub(aliyunSecurityWireMockRule);
        AliyunMessageMock.setStub(aliyunMessageWireMockRule);
    }

    @AfterAll
    public static void after() {
        aliyunSecurityWireMockRule.stop();
        aliyunMessageWireMockRule.stop();
    }

    private AliyunSecurity mockAliyunSecurity() {
        aliyunSecurityProperties.setStsEndpoint("localhost:8502");
        aliyunSecurityProperties.setProtocol(ProtocolType.HTTP);
        return new AliyunSecurity(aliyunSecurityProperties, aliyunTokenCache);
    }

    private AliyunMessageUtil mockAliyunMessageUtil() {
        aliyunProperties.setMessageDomain("localhost:8503");
        aliyunProperties.setMessageProtocol(ProtocolType.HTTP);
        return new AliyunMessageUtil(mockAliyunSecurity(), aliyunMessageProperties, aliyunProperties);
    }

    @Test
    public void testCallback() throws UnsupportedEncodingException {
        JSONArray jsonArray = JSON.parseArray("[\n" +
                "  {\n" +
                "    \"phone_number\" : \"18612345678\",\n" +
                "    \"send_time\" : \"2017-09-01 00:00:00\",\n" +
                "    \"content\" : \"内容\",\n" +
                "    \"sign_name\" : \"签名\",\n" +
                "    \"dest_code\" : \"1234\",\n" +
                "    \"sequence_id\" : 1234567890\n" +
                "  }\n" +
                "]");
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setContent(jsonArray.toString().getBytes("gbk"));
        mockHttpServletRequest.setContentType("application/json;charset=gbk");
        AliyunCallbackResult callback = mockAliyunMessageUtil().callback(mockHttpServletRequest);
        Assert.assertEquals(callback.getCallbackResponse().getCode(), 0);
    }

    @Test
    public void testSend() {
        AliyunMessageResult result1 = mockAliyunMessageUtil().send("13912345678", "test1");
        Assert.assertTrue(result1.isSuccess());

        List<String> phones = new LinkedList<>();
        phones.add("13912345678");
        phones.add("18912345678");
        AliyunMessageResult result2 = mockAliyunMessageUtil().send(phones, "test2");
        Assert.assertTrue(result2.isSuccess());
    }
}
