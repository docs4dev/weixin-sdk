package com.docs4dev.starters.weixin.mp.excepton;

import com.docs4dev.starters.weixin.mp.client.response.WeixinError;
import lombok.Getter;

public class WeixinClientException extends RuntimeException {

    @Getter
    private WeixinError weixinError;

    public WeixinClientException(WeixinError weixinError) {
        super(weixinError == null ? "" : weixinError.toString());
        this.weixinError = weixinError;
    }
}
