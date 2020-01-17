package com.docs4dev.starters.weixin.mp.config;

import static com.docs4dev.starters.weixin.mp.Constants.DEFAULT_CHARSET;

import com.docs4dev.starters.weixin.mp.client.response.WeixinError;
import com.docs4dev.starters.weixin.mp.excepton.WeixinClientException;
import com.docs4dev.starters.weixin.mp.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeixinConfig {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new SimpleClientHttpRequestFactory();
    }

    @Bean
    public RestTemplate weixinRestTemplate(RestTemplateBuilder builder) {
        return builder
            .requestFactory(() -> new BufferingClientHttpRequestFactory(clientHttpRequestFactory()))
            .interceptors((request, body, execution) -> {
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
            })
            .build();
    }
}
