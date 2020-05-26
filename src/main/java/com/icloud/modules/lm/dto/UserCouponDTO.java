package com.icloud.modules.lm.dto;

import lombok.Data;

import java.util.Date;

/**
 * Created by rize on 2019/7/5.
 */
@Data
public class UserCouponDTO extends SuperDTO {

    private String title;

    private String categoryTitle;

    private Long categoryId;

    private Integer min;//使用优惠券最顶订单金额

    /**
     * 优惠券价格
     */
    private Integer discount;

    private Long userId;

    private Long couponId;

    private Long orderId;

    private Date gmtUsed;

    private Date start;

    private Date end;

}
