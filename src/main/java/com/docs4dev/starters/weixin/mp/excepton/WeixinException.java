package com.docs4dev.starters.weixin.mp.excepton;

public class WeixinException extends RuntimeException {

    public WeixinException(String message) {
        super(message);
    }

    public WeixinException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
