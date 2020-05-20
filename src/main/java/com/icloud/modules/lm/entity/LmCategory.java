package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品：类目 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Data
@TableName("lm_category")
public class LmCategory implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 分类ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 分类标题 */
       @TableField("title")
       private String title;
   	   	   /* 上级分类ID */
       @TableField("parent_id")
       private Long parentId;
   	   	   /* 图标 */
       @TableField("icon_url")
       private String iconUrl;
   	   	   /* 分类图片 */
       @TableField("pic_url")
       private String picUrl;
   	   	   /* 分类级别 0 根级别 1 一级分类 2 二级分类  3 三级分类*/
       @TableField("level")
       private Integer level;
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
