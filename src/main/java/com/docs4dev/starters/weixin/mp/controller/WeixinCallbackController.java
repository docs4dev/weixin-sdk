package com.docs4dev.starters.weixin.mp.controller;

import com.docs4dev.starters.weixin.mp.config.WeixinMpProperties;
import com.docs4dev.starters.weixin.mp.controller.request.WeixinRequest;
import com.docs4dev.starters.weixin.mp.util.WeixinSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weixin/mp")
@RequiredArgsConstructor
public class WeixinCallbackController {

    private final WeixinMpProperties weixinMpProperties;

    @GetMapping("callback")
    public String callback(WeixinRequest weixinRequest) {
        String sign = WeixinSecurityUtils
            .sign(weixinRequest.getTimestamp(), weixinRequest.getNonce(), weixinMpProperties.getToken());
        if (sign.equals(weixinRequest.getSignature())) {
            return weixinRequest.getEchostr();
        }
        return "error";
    }
}
