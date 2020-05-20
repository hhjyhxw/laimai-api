package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品：标准化产品单元属性 SPU 属性(不会影响到库存和价格的属性, 又叫关键属性)
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_spu_attribute")
public class LmSpuAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* SPU属性ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* SPUID */
       @TableField("spu_id")
       private Long spuId;
   	   	   /* SPU属性名 */
       @TableField("attribute")
       private String attribute;
   	   	   /* SPU属性值 */
       @TableField("value")
       private String value;
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
