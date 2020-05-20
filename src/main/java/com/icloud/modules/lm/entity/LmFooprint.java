package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户足迹 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_foot_print")
public class LmFooprint implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 足迹ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 商品ID */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
