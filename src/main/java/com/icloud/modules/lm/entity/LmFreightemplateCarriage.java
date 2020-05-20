package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运费模板属性 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_freight_template_carriage")
public class LmFreightemplateCarriage implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Integer id;
   	   	   /* 模板id */
       @TableField("template_id")
       private Integer templateId;
   	   	   /* 应用地区 */
       @TableField("designated_area")
       private String designatedArea;
   	   	   /* 默认包邮额度 */
       @TableField("free_price")
       private Integer freePrice;
   	   	   /* 计费首次数量 */
       @TableField("first_num")
       private Integer firstNum;
   	   	   /* 计费首次价格 */
       @TableField("first_money")
       private Integer firstMoney;
   	   	   /* 计费续件数量 */
       @TableField("continue_num")
       private Integer continueNum;
   	   	   /* 计费续件价格 */
       @TableField("continue_money")
       private Integer continueMoney;
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
