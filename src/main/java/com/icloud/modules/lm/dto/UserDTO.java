package com.icloud.modules.lm.dto;

import com.icloud.modules.lm.entity.LmDistributionPoint;
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

    //根据默认地址获取最近配送点 并设置用户登陆缓存
    private LmDistributionPoint distrpoint;


}
