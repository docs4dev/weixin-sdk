package com.docs4dev.starters.weixin.mp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("weixin.mp")
public class WeixinMpProperties {

    private String appId;
    private String appSecret;
    private String token;
    private String encodingAesKey;
}
