package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmOrderSku;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmOrderSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 订单明细表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmOrderSkuService extends BaseServiceImpl<LmOrderSkuMapper,LmOrderSku> {

    @Autowired
    private LmOrderSkuMapper lmOrderSkuMapper;
}

