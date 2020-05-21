package com.icloud.config.notify;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.icloud.config.global.MyPropertitys;
import com.icloud.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rize on 2019/7/1.
 */
public class QCloudSMSClient implements SMSClient, InitializingBean {

    private SmsSingleSender sender;

//    @Value("${sms.qcloud.app-id}")
//    private Integer appid;
//    @Value("${sms.qcloud.app-key}")
//    private String appkey;
//    @Value("${sms.qcloud.register-template-id}")
//    private Integer registerTemplateId;
//    @Value("${sms.qcloud.bind-phone-template-id}")
//    private Integer bindPhoneTemplateId;
//    @Value("${sms.qcloud.reset-password-template-id}")
//    private Integer resetPasswordTemplateId;
//    @Value("${sms.qcloud.admin-login-template-id}")
//    private Integer adminLoginTemplateId;
//    @Value("${sms.qcloud.sign}")
//    private String sign;

    private static final Logger logger = LoggerFactory.getLogger(QCloudSMSClient.class);

    @Autowired
    private MyPropertitys myPropertitys;

    @Override
    public void afterPropertiesSet() throws Exception {
        sender = new SmsSingleSender(myPropertitys.getSms().getQcloud().getAppid(), myPropertitys.getSms().getQcloud().getAppkey());
    }

    public SMSResult sendMsg(String phone, int templateId, String ...params) throws ApiException {
        try {
            SmsSingleSenderResult smsSingleSenderResult = sender.sendWithParam("86", phone, templateId, params, myPropertitys.getSms().getQcloud().getSign(), "", "");
            if (smsSingleSenderResult.result == 0) {
                SMSResult smsResult = new SMSResult();
                smsResult.setSucc(true);
                smsResult.setMsg("成功");
                return smsResult;
            } else {
                logger.info("[腾讯短信发送] 回复与预期不一致 result=" + smsSingleSenderResult.result + ";errMsg=" + smsSingleSenderResult.errMsg);
                throw new ApiException("[腾讯短信发送] 回复与预期不一致 result=");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[腾讯短信发送] 异常", e);
            throw new ApiException("腾讯云短信发送未知异常");
        }
    }


    @Override
    public SMSResult sendRegisterVerify(String phone, String verifyCode) throws ApiException {
        return sendMsg(phone, myPropertitys.getSms().getQcloud().getRegisterTemplateId(), verifyCode);
    }

    @Override
    public SMSResult sendBindPhoneVerify(String phone, String verifyCode) throws ApiException {
        return sendMsg(phone, myPropertitys.getSms().getQcloud().getRegisterTemplateId(), verifyCode);
    }

    @Override
    public SMSResult sendResetPasswordVerify(String phone, String verifyCode) throws ApiException {
        return sendMsg(phone, myPropertitys.getSms().getQcloud().getRegisterTemplateId(), verifyCode);
    }

    @Override
    public SMSResult sendAdminLoginVerify(String phone, String verifyCode) throws ApiException {
        return sendMsg(phone, myPropertitys.getSms().getQcloud().getRegisterTemplateId(),verifyCode);
    }
}
