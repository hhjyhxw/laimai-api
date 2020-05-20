package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品：标准化产品单元 SPU = Standard Product Unit （标准化产品单元）,SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_spu")
public class LmSpu implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* SPU主键 */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 商品原价 */
       @TableField("original_price")
       private Integer originalPrice;
   	   	   /* 商品价格 单位 分 */
       @TableField("price")
       private Integer price;
   	   	   /* 会员价格 */
       @TableField("vip_price")
       private Integer vipPrice;
   	   	   /* 商品标题 */
       @TableField("title")
       private String title;
   	   	   /* 商品销量 */
       @TableField("sales")
       private Integer sales;
   	   	   /* 商品主图（冗余信息） */
       @TableField("img")
       private String img;
   	   	   /* 商品详情 */
       @TableField("detail")
       private String detail;
   	   	   /* 商品一句话描述 */
       @TableField("description")
       private String description;
   	   	   /* 商品类目id */
       @TableField("category_id")
       private Long categoryId;
   	   	   /* 运费模板 */
       @TableField("freight_template_id")
       private Long freightTemplateId;
   	   	   /* 单位 */
       @TableField("unit")
       private String unit;
   	   	   /* 商品状态 0:下架，1:正常 */
       @TableField("status")
       private String status;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 创建人 */
       @TableField("created_by")
       private String createdBy;
   	   	   /* 更新人 */
       @TableField("updated_by")
       private String updatedBy;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
