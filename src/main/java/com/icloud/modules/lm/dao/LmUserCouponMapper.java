package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.UserCouponDTO;
import com.icloud.modules.lm.entity.LmUserCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户领取的代金券 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmUserCouponMapper extends BaseMapper<LmUserCoupon> {

	List<LmUserCoupon> queryMixList(Map<String, Object> map);

    public List<UserCouponDTO> getUserCoupons(Long userId);

    public UserCouponDTO getUserCouponById(@Param("userCouponId") Long userCouponId, @Param("userId") Long userId);

}
