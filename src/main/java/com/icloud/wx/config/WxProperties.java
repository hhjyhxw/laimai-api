package com.icloud.wx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.iotechn.unimall.wx")
public class WxProperties {

    private String mchId;

    private String mchKey;

    private String notifyUrl;

    private String keyPath;

}
