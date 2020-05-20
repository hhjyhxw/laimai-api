package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 购物车 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Data
@TableName("lm_cart")
public class LmCart implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 购物车ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 收货地址ID */
       @TableField("address_id")
       private Long addressId;
   	   	   /* 商品id */
       @TableField("sku_id")
       private Long skuId;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 数量 */
       @TableField("num")
       private Integer num;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
