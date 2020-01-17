package com.docs4dev.starters.weixin.mp.service;

import com.docs4dev.starters.weixin.mp.client.WeixinClient;
import com.docs4dev.starters.weixin.mp.model.AccessToken;
import com.docs4dev.starters.weixin.mp.store.TokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenStore tokenStore;
    private final WeixinClient weixinClient;

    @Scheduled(fixedRate = 1000 * 60 * 59 * 2)
    public void refreshToken() {
        AccessToken accessToken = weixinClient.getAccessToken();
        tokenStore.store(accessToken);
    }
}
