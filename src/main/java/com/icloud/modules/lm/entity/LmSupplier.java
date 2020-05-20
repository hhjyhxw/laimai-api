package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 供货商 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_supplier")
public class LmSupplier implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 供货商ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 名称 */
       @TableField("title")
       private String title;
   	   	   /* 联系人 */
       @TableField("link_contacts")
       private String linkContacts;
   	   	   /* 联系电话 */
       @TableField("phone")
       private String phone;
   	   	   /* 省 */
       @TableField("province")
       private String province;
   	   	   /* 市 */
       @TableField("city")
       private String city;
   	   	   /* 县 */
       @TableField("county")
       private String county;
   	   	   /* 详细地址 */
       @TableField("address")
       private String address;
   	   	   /* 状态 0：关闭，1：开启 */
       @TableField("status")
       private String status;
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
