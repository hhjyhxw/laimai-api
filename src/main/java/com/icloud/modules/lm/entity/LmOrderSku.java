package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单明细表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_order_sku")
public class LmOrderSku implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 订单明细ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 商品(skuid) */
       @TableField("sku_id")
       private String skuId;
   	   	   /* 商品（spuid） */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* 订单id */
       @TableField("order_id")
       private Long orderId;
   	   	   /* 订单编号 */
       @TableField("order_no")
       private String orderNo;
   	   	   /* 商品spu名称 */
       @TableField("spu_title")
       private String spuTitle;
   	   	   /* 商品sku名称 */
       @TableField("title")
       private String title;
   	   	   /* 条码 */
       @TableField("bar_code")
       private String barCode;
   	   	   /* 数量 */
       @TableField("num")
       private Integer num;
   	   	   /* 计量单位 */
       @TableField("unit")
       private String unit;
   	   	   /* 原价 */
       @TableField("original_price")
       private Integer originalPrice;
   	   	   /* 现价 */
       @TableField("price")
       private Integer price;
   	   	   /* spu图片 */
       @TableField("img")
       private String img;
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
