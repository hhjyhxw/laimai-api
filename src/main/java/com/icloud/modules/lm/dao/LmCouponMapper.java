package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.CouponAdminDTO;
import com.icloud.modules.lm.dto.CouponDTO;
import com.icloud.modules.lm.entity.LmCoupon;
import com.icloud.modules.lm.model.KVModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 代金券 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmCouponMapper extends BaseMapper<LmCoupon> {

	List<LmCoupon> queryMixList(Map<String, Object> map);

    public Integer decCoupon(Long couponId);

    //这样写MyBatis无法直接映射泛型，只能用Long了
    public List<KVModel<Long,Long>> getUserCouponsCount(@Param("userId") Long userId, @Param("couponIds") List<Long> couponIds);

    public List<CouponDTO> getActiveCoupons();

    public List<CouponAdminDTO> getAdminCouponList(@Param("title")String title, @Param("type") Integer type, @Param("status")Integer status, @Param("now") Date now, @Param("offset") Integer offset, @Param("limit") Integer limit);

}
