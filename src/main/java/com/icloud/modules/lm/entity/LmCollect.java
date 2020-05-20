package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户收藏夹 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Data
@TableName("lm_collect")
public class LmCollect implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 收藏ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 收藏类型 0：SPUID */
       @TableField("collect_type")
       private String collectType;
   	   	   /* 收藏对象id */
       @TableField("collect_object_id")
       private Long collectObjectId;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
