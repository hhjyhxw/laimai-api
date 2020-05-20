//package com.icloud.api.lm.coupon;
//
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.icloud.exceptions.ApiException;
//import com.icloud.modules.lm.componts.LockComponent;
//import com.icloud.modules.lm.dto.CouponDTO;
//import com.icloud.modules.lm.dto.UserCouponDTO;
//import com.icloud.modules.lm.entity.LmCoupon;
//import com.icloud.modules.lm.enums.StatusType;
//import com.icloud.modules.lm.model.KVModel;
//import com.icloud.modules.lm.service.LmCouponService;
//import com.icloud.modules.lm.service.LmUserCouponService;
//import net.bytebuddy.dynamic.DynamicType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/api/coupon")
//public class CouponController {
//
//    @Autowired
//    private LmCouponService lmCouponService;
//
//    @Autowired
//    private LmUserCouponService lmUserCouponService;
//
//    @Autowired
//    private LockComponent lockComponent;
//
//    private static final String COUPON_LOCK = "COUPON_LOCK_";
//
//    private static final String COUPON_USER_LOCK = "COUPON_USER_LOCK_";
//
//    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);
//
//
//    /**
//     * 领取优惠券
//     * @param couponId
//     * @param userId
//     * @return
//     * @throws ApiException
//     */
//    public String obtainCoupon(Long couponId, Long userId) throws ApiException {
//        //防止用户一瞬间提交两次表单，导致超领
//        if (lockComponent.tryLock(COUPON_USER_LOCK + userId + "_" + couponId, 10)) {
//            try {
//                LmCoupon lmCouponPara = new LmCoupon();
//                lmCouponPara.setId(couponId);
//                LmCoupon couponDO = (LmCoupon)lmCouponService.getById(lmCouponPara);
//                if (String.valueOf(StatusType.LOCK.getCode()).equals(couponDO.getStatus())) {
//                    throw new ApiException("该代金券不可以使用");
//                }
//                Date now = new Date();
//                if (couponDO.getEnd() != null && couponDO.getEnd().getTime() < now.getTime()) {
//                    throw new ApiException("该优惠券已过期");
//                }
//                if (couponDO.getStart() != null && couponDO.getStart().getTime() > now.getTime()) {
//                    throw new ApiException("该优惠券活动还未开始");
//                }
//                if (couponDO.getTotal() != -1 && Integer.parseInt(couponDO.getSurplus()) <= 0) {
//                    throw new ApiException("代金券配置错误");
//                } else {
//                    if (couponDO.getTotal() >= 0) {
//                        if ("1".equals(couponDO.getSurplus())) {
//                            if (!lockComponent.tryLock(COUPON_LOCK + couponId, 10)) {
//                                throw new ApiException("");
//                            }
//                        }
//                        couponMapper.decCoupon(couponId);
//                    }
//                }
//
//
//                if (couponDO.getLimit() != -1) {
//                    //校验用户是否已经领了
//                    Integer count = userCouponMapper.selectCount(
//                            new EntityWrapper<UserCouponDO>()
//                                    .eq("user_id", userId)
//                                    .eq("coupon_id", couponId));
//
//                    if (count >= couponDO.getLimit()) {
//                        throw new AppServiceException(ExceptionDefinition.COUPON_YOU_HAVE_OBTAINED);
//                    }
//                }
//
//                //领取优惠券
//                UserCouponDO userCouponDO = new UserCouponDO();
//                userCouponDO.setUserId(userId);
//                userCouponDO.setCouponId(couponId);
//                if (couponDO.getGmtStart() != null && couponDO.getGmtEnd() != null) {
//                    //如果优惠券是按区间领取的
//                    userCouponDO.setGmtStart(couponDO.getGmtStart());
//                    userCouponDO.setGmtEnd(couponDO.getGmtEnd());
//                } else if (couponDO.getDays() != null) {
//                    //如果是任意领取的，则从当前时间 加上 可用天数
//                    userCouponDO.setGmtStart(now);
//                    userCouponDO.setGmtEnd(new Date(now.getTime() + 1000l * 60 * 60 * 24 * couponDO.getDays()));
//                } else {
//                    throw new AppServiceException(ExceptionDefinition.COUPON_STRATEGY_INCORRECT);
//                }
//
//                userCouponDO.setGmtUpdate(now);
//                userCouponDO.setGmtCreate(now);
//
//                userCouponMapper.insert(userCouponDO);
//                return "ok";
//            } catch (ServiceException e) {
//                throw e;
//            } catch (Exception e) {
//                logger.error("[领取优惠券] 异常", e);
//                throw new AppServiceException(ExceptionDefinition.APP_UNKNOWN_EXCEPTION);
//            } finally {
//                lockComponent.release(COUPON_USER_LOCK + userId + "_" + couponId);
//            }
//        } else {
//            throw new AppServiceException(ExceptionDefinition.SYSTEM_BUSY);
//        }
//
//    }
//
//    @Override
//    public List<CouponDTO> getObtainableCoupon(Long userId) throws ServiceException {
//        List<CouponDTO> couponDOS = couponMapper.getActiveCoupons();
//        //活动中的优惠券Id
//        List<Long> activeCouponIds = couponDOS.stream().map(couponDO -> couponDO.getId()).collect(Collectors.toList());
//
//        if (CollectionUtils.isEmpty(activeCouponIds)) {
//            return new ArrayList<>();
//        }
//
//        List<KVModel<Long, Long>> userCouponsCount = couponMapper.getUserCouponsCount(userId, activeCouponIds);
//
//        List<CouponDTO> couponDTOList = couponDOS.stream().map(item -> {
//            item.setNowCount(0);
//            for (int i = 0; i < userCouponsCount.size(); i++) {
//                KVModel<Long, Long> kv = userCouponsCount.get(i);
//                if (kv != null && kv.getKey().equals(item.getId())) {
//                    item.setNowCount(kv.getValue().intValue());
//                }
//            }
//            return item;
//        }).filter(item -> (item.getCategoryId() == null || (item.getCategoryId() != null && !StringUtils.isEmpty(item.getCategoryTitle())))).collect(Collectors.toList());
//        return couponDTOList;
//    }
//
//    @Override
//    public List<UserCouponDTO> getUserCoupons(Long userId) throws ServiceException {
//        return userCouponMapper.getUserCoupons(userId);
//    }
//
//}
