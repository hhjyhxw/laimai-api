package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmUserCoupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmUserCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 用户领取的代金券 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmUserCouponService extends BaseServiceImpl<LmUserCouponMapper,LmUserCoupon> {

    @Autowired
    private LmUserCouponMapper lmUserCouponMapper;
}

