package com.icloud.wx.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.icloud.config.global.MyPropertitys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rize on 2019/6/9.
 */
@Configuration
public class WxConfig {

    @Autowired
    private MyPropertitys myPropertitys;

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(myPropertitys.getWx().getPay().getMchid());
        payConfig.setMchKey(myPropertitys.getWx().getPay().getMchkey());
        payConfig.setNotifyUrl(myPropertitys.getWx().getPay().getNotifyurl());
        payConfig.setKeyPath(myPropertitys.getWx().getPay().getKeypath());
        payConfig.setSignType("MD5");
        return payConfig;
    }


    @Bean
    public WxPayService wxPayService(WxPayConfig payConfig) {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }
}
