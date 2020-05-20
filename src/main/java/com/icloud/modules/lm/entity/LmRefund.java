package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 退款表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_refund")
public class LmRefund implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 订单id */
       @TableField("order_id")
       private Long orderId;
   	   	   /* 退款单号 */
       @TableField("refund_sn")
       private String refundSn;
   	   	   /* 退款金额 */
       @TableField("refund_amount")
       private Integer refundAmount;
   	   	   /* 退款时间 */
       @TableField("gmt_refund")
       private Date gmtRefund;
   	   	   /* 状态 */
       @TableField("status")
       private String status;
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
