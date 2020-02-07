package aliyun;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author Albumen
 * @date 2020/2/5
 */
public class AliyunStsMock {
    public static void setStub(WireMockRule wireMockRule) {
        wireMockRule.stubFor(
                post(anyUrl())
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", "application/json;charset=utf-8")
                                        .withBody("{\n" +
                                                "    \"Credentials\": {\n" +
                                                "        \"AccessKeyId\": \"STS.L4aBSCSJVMuKg5U1****\",\n" +
                                                "        \"AccessKeySecret\": \"wyLTSmsyPGP1ohvvw8xYgB29dlGI8KMiH2pK****\",\n" +
                                                "        \"Expiration\": \"2021-04-09T11:52:19Z\",\n" +
                                                "        \"SecurityToken\": \"********\"\n" +
                                                "    },\n" +
                                                "    \"AssumedRoleUser\": {\n" +
                                                "        \"arn\": \"acs:ram::123456789012****:assumed-role/AdminRole/alice\",\n" +
                                                "        \"AssumedRoleUserId\":\"34458433936495****:alice\"\n" +
                                                "        },\n" +
                                                "    \"RequestId\": \"6894B13B-6D71-4EF5-88FA-F32781734A7F\"\n" +
                                                "}")));
    }
}
