package com.docs4dev.starters.weixin.mp.store;

import com.docs4dev.starters.weixin.mp.model.AccessToken;
import org.springframework.stereotype.Component;

@Component
public class InMemoryTokenStore implements TokenStore {

    private AccessToken accessToken;

    @Override
    public void store(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public AccessToken read() {
        return accessToken;
    }
}
