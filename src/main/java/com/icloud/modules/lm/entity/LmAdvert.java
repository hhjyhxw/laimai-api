package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 广告 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Data
@TableName("lm_advert")
public class LmAdvert implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 广告ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 类型 */
       @TableField("ad_type")
       private String adType;
   	   	   /* 标题 */
       @TableField("title")
       private String title;
   	   	   /* 页面地址 */
       @TableField("page_url")
       private String pageUrl;
   	   	   /* 广告图片 */
       @TableField("img_url")
       private String imgUrl;
   	   	   /* 状态 */
       @TableField("status")
       private String status;
   	   	   /* 颜色 */
       @TableField("color")
       private String color;
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
