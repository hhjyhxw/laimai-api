package com.icloud.modules.lm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单表 
 * 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Data
@TableName("lm_order")
public class LmOrder implements Serializable {
	private static final long serialVersionUID = 1L;

   	   /* 订单ID */
       @TableId(value="id", type= IdType.AUTO)
       private Long id;
   	   	   /* 下单渠道 */
       @TableField("channel")
       private String channel;
   	   	   /* 订单号 */
       @TableField("order_no")
       private String orderNo;
   	   	   /* 用户id */
       @TableField("user_id")
       private Long userId;
   	   	   /* 订单状态 0：未付款 */
       @TableField("status")
       private String status;
   	   	   /* 商品(sku)原始价总额 */
       @TableField("sku_original_total_price")
       private Integer skuOriginalTotalPrice;
   	   	   /* 商品(sku)现价总额 */
       @TableField("sku_total_price")
       private Integer skuTotalPrice;
   	   	   /* 运费 */
       @TableField("freight_price")
       private Integer freightPrice;
   	   	   /* 代金券优惠价 */
       @TableField("coupon_price")
       private Integer couponPrice;
   	   	   /* 代金券id */
       @TableField("coupon_id")
       private Long couponId;
   	   	   /* 团购商品id */
       @TableField("group_shop_id")
       private Long groupShopId;
   	   	   /* 实付订单金额 */
       @TableField("actual_price")
       private Integer actualPrice;
   	   	   /* 支付金额 */
       @TableField("pay_price")
       private Integer payPrice;
   	   	   /* 交易流水id */
       @TableField("pay_id")
       private String payId;
   	   	   /* 支付渠道名称 */
       @TableField("pay_channel")
       private String payChannel;
   	   	   /* 支付时间 */
       @TableField("pay_time")
       private Date payTime;
   	   	   /* 物流单号 */
       @TableField("ship_no")
       private String shipNo;
   	   	   /*  */
       @TableField("ship_code")
       private String shipCode;
   	   	   /* 发货时间 */
       @TableField("ship_time")
       private Date shipTime;
   	   	   /* 确认收货时间 */
       @TableField("confirm_time")
       private Date confirmTime;
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
   	   	   /* 收货人手机 */
       @TableField("phone")
       private String phone;
   	   	   /* 收货人 */
       @TableField("consignee")
       private String consignee;
   	   	   /* 备注 */
       @TableField("mono")
       private String mono;
   	   	   /* 配送点id */
       @TableField("distribution_point_id")
       private String distributionPointId;
   	   	   /*  */
       @TableField("admin_mono_level")
       private String adminMonoLevel;
   	   	   /* 管理员备注 */
       @TableField("admin_mono")
       private String adminMono;
   	   	   /* 退款原因 */
       @TableField("refund_reason")
       private String refundReason;
   	   	   /* 创建时间 */
       @TableField("created_time")
       private Date createdTime;
   	   	   /* 更新时间 */
       @TableField("updated_time")
       private Date updatedTime;
   	
}
