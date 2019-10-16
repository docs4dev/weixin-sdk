package com.docs4dev.starters.weixin.mp.store;

import com.docs4dev.starters.weixin.mp.model.AccessToken;

public interface TokenStore {

    void store(AccessToken accessToken);

    AccessToken read();
}
