package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_user")
public class LmUser implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 用户ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 电话 */
       @TableField("phone")
       private String phone;
   	   	   /* 密码 */
       @TableField("password")
       private String password;
   	   	   /* 登陆类型 */
       @TableField("login_type")
       private String loginType;
   	   	   /* 登陆名称 */
       @TableField("login_name")
       private String loginName;
   	   	   /* 邮箱 */
       @TableField("email")
       private String email;
   	   	   /* openid */
       @TableField("open_id")
       private String openId;
   	   	   /* 昵称 */
       @TableField("nick_name")
       private String nickName;
   	   	   /* 头像url */
       @TableField("avatar_url")
       private String avatarUrl;
   	   	   /* 会员等级 */
       @TableField("level")
       private String level;
   	   	   /* 生日 */
       @TableField("birthday")
       private Date birthday;
   	   	   /* 性别 */
       @TableField("gender")
       private String gender;
   	   	   /* 最近登陆时间 */
       @TableField("last_login_time")
       private Date lastLoginTime;
   	   	   /* 最近登陆ip */
       @TableField("last_login_ip")
       private String lastLoginIp;
   	   	   /* 状态 1 正常 2禁用 3注销 */
       @TableField("status")
       private String status;
   	   	   /* 创建人 */
       @TableField("created_by")
       private String createdBy;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新人 */
       @TableField("updated_by")
       private String updatedBy;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
