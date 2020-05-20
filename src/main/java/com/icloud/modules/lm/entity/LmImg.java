package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 图片存储表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:00
 */
@Data
@TableName("lm_img")
public class LmImg implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* id */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* biz类型 */
       @TableField("biz_type")
       private String bizType;
   	   	   /* bizid */
       @TableField("biz_id")
       private Long bizId;
   	   	   /* 图片地址 */
       @TableField("url")
       private String url;
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
