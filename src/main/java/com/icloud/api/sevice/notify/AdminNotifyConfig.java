package com.icloud.api.sevice.notify;

import com.icloud.config.global.MyPropertitys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: rize
 * Date: 2019/12/27
 * Time: 20:15
 */
@Configuration
public class AdminNotifyConfig {

    @Autowired
    private MyPropertitys myPropertitys;

    private String enable = myPropertitys.getAdminNotify().getEnable();

    @Bean
    public AdminNotifyBizService adminNotifyBizService() {
        if ("mock".equalsIgnoreCase(enable)) {
            return new MockAdminNotifyBizServiceImpl();
        } else if ("uninotify".equalsIgnoreCase(enable)) {
            return new UniNotifyAdminNotifyBizServiceImpl();
        } else {
            return null;
        }
    }

}
