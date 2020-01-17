package com.docs4dev.starters.weixin.mp;

import com.docs4dev.starters.weixin.mp.config.WeixinMpProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(WeixinMpProperties.class)
public class WeixinMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinMpApplication.class, args);
    }

}
