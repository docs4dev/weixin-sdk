package com.docs4dev.starters.weixin.mp.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * refer: https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Global_Return_Code.html
 */
@Getter
@Setter
@ToString
public class WeixinError {

    @JsonProperty("errcode")
    private String code;
    @JsonProperty("errmsg")
    private String message;
}
