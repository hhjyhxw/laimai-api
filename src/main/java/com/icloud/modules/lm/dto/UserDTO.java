package com.icloud.modules.lm.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by rize on 2019/7/1.
 */
@Data
public class UserDTO extends SuperDTO {

    private String phone;

    private String loginType;

    private String openId;

    private String nickName;

    private String avatarUrl;

    private String level;

    private Date birthday;

    private String gender;

    private Date gmtLastLogin;

    private String lastLoginIp;

    private String status;

    /**
     * 登录成功，包装此参数
     */
    private String accessToken;
}
