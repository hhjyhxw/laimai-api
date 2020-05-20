package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 团购子商品表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_group_shop_sku")
public class LmGroupShopSku implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableField("id")
       private Long id;
   	   	   /* skuid */
       @TableField("sku_id")
       private Long skuId;
   	   	   /* 团购id */
       @TableField("group_shop_id")
       private Long groupShopId;
   	   	   /* 团购价格 */
       @TableField("sku_group_shop_price")
       private Integer skuGroupShopPrice;
   	   	   /* 创建人 */
       @TableId(value="id", type= IdType.AUTO)
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
