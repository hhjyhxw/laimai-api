package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 代金券 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Data
@TableName("lm_coupon")
public class LmCoupon implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 代金券名称 */
       @TableField("title")
       private String title;
   	   	   /* 使用类型，如满减 */
       @TableField("type")
       private String type;
   	   	   /* 描述 */
       @TableField("description")
       private String description;
   	   	   /* 商品总额 */
       @TableField("total")
       private Integer total;
   	   	   /* 会员类型0:非会员1:会员2:全部 */
       @TableField("surplus")
       private String surplus;
   	   	   /*  */
       @TableField("limits")
       private Integer limits;
   	   	   /* 扣减金额 */
       @TableField("discount")
       private Integer discount;
   	   	   /* 最低消费金额 */
       @TableField("min")
       private Integer min;
   	   	   /* 是否可用 0不用 1可用 */
       @TableField("status")
       private String status;
   	   	   /* 分类id */
       @TableField("category_id")
       private Long categoryId;
   	   	   /* 过期天数 */
       @TableField("days")
       private Integer days;
   	   	   /* 有效开始时间 */
       @TableField("start_time")
       private Date startTime;
   	   	   /* 有效结束时间 */
       @TableField("end_time")
       private Date endTime;
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
