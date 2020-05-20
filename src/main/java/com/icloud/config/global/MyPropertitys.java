package com.icloud.config.global;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix="mypros")//加载自定义属性
@Configuration
public class MyPropertitys {


    //微信参数
    private Wx wx;
    //云存储
    private OSS oss;
    //快递查询
    private KDN kdn;
    //短信配置
    private SMS sms;
    //管理员通知配置
    private AdminNotify adminNotify;

    /**
     * 微信相关参数
     */
    @Data
    public static class Wx{
        private Mini mini;
        private App app;
        private H5 h5;
        private Pay pay;


        @Data
        public static class Mini{
            private String appid;
            private String appsecret;
        }
        @Data
        public static class App{
            private String appid;
            private String appsecret;
        }
        @Data
        public static class H5{
            private String appid;
            private String appsecret;
        }
        @Data
        public static class Pay{
            private String mchid;
            private String mchkey;
            private String notifyurl;
            private String keypath;
        }
    }



    /**
     * 云存储
     */
    @Data
    public static class OSS{
        private String accessId;
        private String accessKey;
        private String endpoint;
        private String bucket;
        private String callbackUrl;
        private String dir;
        private String basekUrl;

    }

    /**
     * 快递查询 相关参数
     */
    @Data
    public static class KDN{
        private String appkey;//
        private String businessid;//
    }

    /**
     * 快递查询 相关参数
     */
    @Data
    public static class SMS{
        private String enable;//
        private Qcloud qcloud;
        private Aliyun aliyun;// #

        @Data
        public static class Qcloud{
            private String appid;
            private String appkey;
            private String registerTemplateId;
            private String bindPhoneTemplateId;
            private String resetPasswordTemplateId;
            private String adminLoginTemplateId;
        }

        @Data
        public static class Aliyun{
            private String accessId;
            private String accessKey;
            private String signature;
            private String registerTemplateId;
            private String bindPhoneTemplateId;
            private String resetPasswordTemplateId;
            private String adminLoginTemplateId;
        }
    }

    @Data
    public static class AdminNotify{
        private String enable;
        private String appId;
        private String appsecret;
        private String url;
    }
}
