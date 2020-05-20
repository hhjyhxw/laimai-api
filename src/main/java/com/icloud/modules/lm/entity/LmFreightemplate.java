package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运费模板 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_freight_template")
public class LmFreightemplate implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 运费模板名称 */
       @TableField("template_name")
       private String templateName;
   	   	   /* 应用地区 */
       @TableField("spu_location")
       private String spuLocation;
   	   	   /* 发货期限 */
       @TableField("delivery_deadline")
       private Integer deliveryDeadline;
   	   	   /* 默认包邮额度 */
       @TableField("default_free_price")
       private Integer defaultFreePrice;
   	   	   /* 计费首次数量 */
       @TableField("default_first_num")
       private Integer defaultFirstNum;
   	   	   /* 计费首次价格 */
       @TableField("default_first_money")
       private Integer defaultFirstMoney;
   	   	   /* 计费续件数量 */
       @TableField("default_continue_num")
       private Integer defaultContinueNum;
   	   	   /* 计费续件价格 */
       @TableField("default_continue_money")
       private Integer defaultContinueMoney;
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
