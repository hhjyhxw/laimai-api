package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 团购主商品表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_group_shop")
public class LmGroupShop implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 关联商品id */
       @TableField("spu_id")
       private Long spuId;
   	   	   /*  */
       @TableField("min_price")
       private Integer minPrice;
   	   	   /*  */
       @TableField("max_price")
       private Integer maxPrice;
   	   	   /* 团购开始时间 */
       @TableField("start_time")
       private Date startTime;
   	   	   /* 团购结束时间 */
       @TableField("end_time")
       private Date endTime;
   	   	   /* 团购基础人数 */
       @TableField("minimum_number")
       private Integer minimumNumber;
   	   	   /* 团购已经购买人数 */
       @TableField("already_buy_number")
       private Integer alreadyBuyNumber;
   	   	   /* 团购结束时购买人数未达到基础人数,是否自动退款(0不退发货、1退) */
       @TableField("automatic_refund")
       private String automaticRefund;
   	   	   /* 0下架、1上架 */
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
