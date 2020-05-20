package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评价表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_user_appraise")
public class LmUserAppraise implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 商品id */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* 子商品id */
       @TableField("sku_id")
       private Long skuId;
   	   	   /* 订单id */
       @TableField("order_id")
       private Long orderId;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 评价内容 */
       @TableField("content")
       private String content;
   	   	   /* 评价积分 */
       @TableField("score")
       private Integer score;
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
