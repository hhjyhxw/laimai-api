package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品：库存量单位 SKU=stock keeping unit(库存量单位) SKU即库存进出计量的单位（买家购买、商家进货、供应商备货、工厂生产都是依据SKU进行的）。
SKU是物理上不可分割的最小存货单元。也就是说一款商品，可以根据SKU来确定具体的货物存量。
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_sku")
public class LmSku implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* SKUID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* SPUID */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* 条码或二维码 */
       @TableField("bar_code")
       private String barCode;
   	   	   /* SKU显示文字 */
       @TableField("title")
       private String title;
   	   	   /* 商品主图 */
       @TableField("img")
       private String img;
   	   	   /* 商品原价 */
       @TableField("original_price")
       private Integer originalPrice;
   	   	   /* 商品价格 单位 分 */
       @TableField("price")
       private Integer price;
   	   	   /* 会员价格 */
       @TableField("vip_price")
       private Integer vipPrice;
   	   	   /* 库存 */
       @TableField("stock")
       private Integer stock;
   	   	   /* 冻结库存 */
       @TableField("freeze_stock")
       private Integer freezeStock;
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
