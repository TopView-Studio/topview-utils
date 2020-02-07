package aliyun;

import com.aliyuncs.http.ProtocolType;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.topview.utils.aliyun.AliyunSecurity;
import com.topview.utils.aliyun.AliyunTokenCache;
import com.topview.utils.aliyun.config.AliyunSecurityProperties;
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

/**
 * @author Albumen
 * @date 2020/2/5
 */
@SpringBootTest
@SpringBootApplication
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "com.topview.utils")
public class AliyunSecurityTest {
    @Rule
    public static WireMockRule wireMockRule = new WireMockRule(8501);
    @Autowired
    private AliyunSecurityProperties aliyunSecurityProperties;
    @Autowired
    private AliyunTokenCache aliyunTokenCache;

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
        AliyunStsMock.setStub(wireMockRule);
    }

    @Test
    public void test() {
        aliyunSecurityProperties.setStsEndpoint("localhost:8501");
        aliyunSecurityProperties.setProtocol(ProtocolType.HTTP);
        AliyunSecurity aliyunSecurity = new AliyunSecurity(aliyunSecurityProperties, aliyunTokenCache);
        System.out.println(aliyunSecurity.signSystemOssSts());
    }
}
