package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmGroupShopSku;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmGroupShopSkuMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 团购子商品表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmGroupShopSkuService extends BaseServiceImpl<LmGroupShopSkuMapper,LmGroupShopSku> {

    @Autowired
    private LmGroupShopSkuMapper lmGroupShopSkuMapper;
}

