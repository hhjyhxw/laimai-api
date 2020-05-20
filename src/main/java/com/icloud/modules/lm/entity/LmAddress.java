package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户收货地址 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Data
@TableName("lm_address")
public class LmAddress implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 收货地址ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;

       @NotNull
   	   	   /* 省 */
       @TableField("province")
       private String province;
    @NotNull
   	   	   /* 市 */
       @TableField("city")
       private String city;
    @NotNull
   	   	   /* 县 */
       @TableField("county")
       private String county;
      @NotNull
   	   	   /* 详细地址 */
       @TableField("address")
       private String address;
   	   	   /* 用户ID */
       @TableField("user_id")
       private Long userId;
   	   	   /* 联系电话 */
       @NotNull
       @TableField("phone")
       private String phone;
       @NotNull
   	   	   /* 收件人 */
       @TableField("consignee")
       private String consignee;
   	   	   /* 是否为默认地址 1:默认，0：不是默认 */
       @TableField("is_default")
       private String isDefault;
   	   	   /* 经度 */
        @NotNull
       @TableField("lnt")
       private BigDecimal lnt;
        @NotNull
   	   	   /* 纬度 */
       @TableField("lat")
       private BigDecimal lat;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
