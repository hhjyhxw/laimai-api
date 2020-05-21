package com.icloud.config.notify;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.icloud.config.global.MyPropertitys;
import com.icloud.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by rize on 2019/8/1.
 */
public class AliyunSMSClient implements SMSClient, InitializingBean {

//    @Value("${sms.aliyun.accessId}")
//    private String accessId;
//    @Value("${sms.aliyun.accessKey}")
//    private String accessKey;
//    @Value("${sms.aliyun.register-template-id}")
//    private String registerTemplateId;
//    @Value("${sms.aliyun.bind-phone-template-id}")
//    private String bindPhoneTemplateId;
//    @Value("${sms.aliyun.reset-password-template-id}")
//    private String resetPasswordTemplateId;
//    @Value("${sms.aliyun.admin-login-template-id}")
//    private String adminLoginTemplateId;
//    @Value("${sms.aliyun.signature}")
//    private String signature;

    @Autowired
    private MyPropertitys myPropertitys;

    private IAcsClient client;

    private static final Logger logger = LoggerFactory.getLogger(AliyunSMSClient.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.client = new DefaultAcsClient(DefaultProfile.getProfile("default", myPropertitys.getSms().getAliyun().getAccessId(),  myPropertitys.getSms().getAliyun().getAccessKey()));
    }

    @Override
    public SMSResult sendRegisterVerify(String phone, String verifyCode) throws ApiException {
        return sendCommon(phone, verifyCode, myPropertitys.getSms().getAliyun().getRegisterTemplateId(), myPropertitys.getSms().getAliyun().getSignature());
    }

    @Override
    public SMSResult sendBindPhoneVerify(String phone, String verifyCode) throws ApiException {
        return sendCommon(phone, verifyCode, myPropertitys.getSms().getAliyun().getBindPhoneTemplateId(), myPropertitys.getSms().getAliyun().getSignature());
    }

    @Override
    public SMSResult sendResetPasswordVerify(String phone, String verifyCode) throws ApiException {
        return sendCommon(phone, verifyCode, myPropertitys.getSms().getAliyun().getResetPasswordTemplateId(), myPropertitys.getSms().getAliyun().getSignature());
    }

    @Override
    public SMSResult sendAdminLoginVerify(String phone, String verifyCode) throws ApiException {
        return sendCommon(phone, verifyCode, myPropertitys.getSms().getAliyun().getAdminLoginTemplateId(), myPropertitys.getSms().getAliyun().getSignature());
    }

    private SMSResult sendCommon(String phone, String verifyCode, String templateId, String signature) throws ApiException {
        try {
            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "default");
            request.putQueryParameter("PhoneNumbers", phone);
            request.putQueryParameter("SignName", signature);
            request.putQueryParameter("TemplateCode", templateId);
            request.putQueryParameter("TemplateParam", "{\"code\":\"" + verifyCode + "\"}");
            CommonResponse commonResponse = client.getCommonResponse(request);
            String data = commonResponse.getData();
            JSONObject jsonObject = JSONObject.parseObject(data);
            String message = jsonObject.getString("Message");
            SMSResult smsResult = new SMSResult();
            smsResult.setSucc("OK".equalsIgnoreCase(message));
            smsResult.setMsg(message);
            return smsResult;
        } catch (ClientException e) {
            throw new ApiException(e.getMessage());
        } catch (Exception e) {
            logger.error("[阿里云短信发送] 异常", e);
            throw new ApiException("[阿里云短信发送] 异常");
        }
    }



}
