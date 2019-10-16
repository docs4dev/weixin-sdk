package com.docs4dev.starters.weixin.mp.client;

import org.junit.Test;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class WeixinClientTest {

    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new SimpleClientHttpRequestFactory();
    }

    @Test
    public void getAccessToken() {

        /*WeixinMpProperties weixinMpProperties = new WeixinMpProperties();
        weixinMpProperties.setAppId("");
        weixinMpProperties.setAppSecret("");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(clientHttpRequestFactory()));
        restTemplate.setInterceptors(Lists.newArrayList(new ClientHttpRequestInterceptor() {

            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
                ClientHttpResponse clientHttpResponse = execution.execute(request, body);
                String responseText = StreamUtils.copyToString(clientHttpResponse.getBody(), DEFAULT_CHARSET);
                if (StringUtils.isBlank(responseText)) {
                    return clientHttpResponse;
                }
                boolean hasError = JsonUtils.hasNode(responseText, "errcode");
                if (hasError) {
                    throw new WeixinClientException(JsonUtils.parse(responseText, WeixinError.class));
                }
                return clientHttpResponse;
            }
        }));
        WeixinClient weixinClient = new WeixinClient(restTemplate, weixinMpProperties);
        AccessToken accessToken = weixinClient.getAccessToken();*/
    }
}
