package com.icloud.config.notify;

import com.icloud.exceptions.ApiException;

/**
 * Created by rize on 2019/7/1.
 */
public interface SMSClient {

    public SMSResult sendRegisterVerify(String phone, String verifyCode) throws ApiException;

    public SMSResult sendBindPhoneVerify(String phone, String verifyCode) throws ApiException;

    public SMSResult sendResetPasswordVerify(String phone, String verifyCode) throws ApiException;

    public SMSResult sendAdminLoginVerify(String phone, String verifyCode) throws ApiException;


}
