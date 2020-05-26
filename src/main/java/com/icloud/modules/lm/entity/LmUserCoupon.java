package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户领取的代金券 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_user_coupon")
public class LmUserCoupon implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 代金券id */
       @TableField("coupon_id")
       private Long couponId;
   	   	   /* 使用订单Id（消费后存在） */
       @TableField("order_id")
       private Long orderId;
   	   	   /* 使用时间，若使用时间为空，表示未使用 */
       @TableField("usered_time")
       private Date useredTime;
   	   	   /* 领取优惠券时写入(有效器开启时间) */
       @TableField("start")
       private Date start;
   	   	   /* 领取优惠券过期时间根据策略计算 */
       @TableField("end")
       private Date end;
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
