package com.docs4dev.starters.weixin.mp.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeixinRequest {

    private String signature;
    private String nonce;
    private String echostr;
    private Long timestamp;
}
