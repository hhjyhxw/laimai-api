package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmUserCoupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
