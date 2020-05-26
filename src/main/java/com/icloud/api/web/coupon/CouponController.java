package com.icloud.api.web.coupon;

import com.icloud.annotation.LoginUser;
import com.icloud.api.web.coupon.service.CouponService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.service.LmUserCouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private LmUserCouponService lmUserCouponService;



    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);


    /**
     * 领取优惠券
     * @param couponId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/obtainCoupon")
    @ResponseBody
    public ApiResponse obtainCoupon(Long couponId, @LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(couponService.obtainCoupon(couponId,user.getId()));
    }

    @RequestMapping("/getObtainableCoupon")
    @ResponseBody
    public ApiResponse getObtainableCoupon(@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(couponService.getObtainableCoupon(user.getId()));
    }

    @RequestMapping("/getUserCoupons")
    @ResponseBody
    public ApiResponse getUserCoupons(@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(couponService.getUserCoupons(user.getId()));
    }

}
