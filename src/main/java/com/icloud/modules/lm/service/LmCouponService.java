package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmCoupon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCouponMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 代金券 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmCouponService extends BaseServiceImpl<LmCouponMapper,LmCoupon> {

    @Autowired
    private LmCouponMapper lmCouponMapper;
}

