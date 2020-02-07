package message;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author Albumen
 * @date 2020/2/5
 */
public class AliyunMessageMock {
    private final static String SUCCESS =
            "{\n" +
                    "\t\"Message\":\"OK\",\n" +
                    "\t\"RequestId\":\"2184201F-BFB3-446B-B1F2-C746B7BF0657\",\n" +
                    "\t\"BizId\":\"197703245997295588^0\",\n" +
                    "\t\"Code\":\"OK\"\n" +
                    "}";


    public static void setStub(WireMockRule wireMockRule) {
        wireMockRule.stubFor(
                post(anyUrl())
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json;charset=utf-8")
                                        .withBody(SUCCESS))
        );
    }
}
