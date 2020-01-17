package com.docs4dev.starters.weixin.mp.client;

import com.docs4dev.starters.weixin.mp.config.WeixinMpProperties;
import com.docs4dev.starters.weixin.mp.model.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class WeixinClient {

    private final RestTemplate weixinRestTemplate;
    private final WeixinMpProperties weixinMpProperties;

    private static class Uri {

        private static String BASE_URL = "https://api.weixin.qq.com";
        private static String TOKEN_URI =
            BASE_URL + "/cgi-bin/token?grant_type=client_credential&appid={appId}&secret={appSecret}";
    }

    public AccessToken getAccessToken() {
        return weixinRestTemplate
            .getForEntity(Uri.TOKEN_URI, AccessToken.class,
                weixinMpProperties.getAppId(),
                weixinMpProperties.getAppSecret())
            .getBody();
    }
}
