package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmGroupShop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmGroupShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 团购主商品表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmGroupShopService extends BaseServiceImpl<LmGroupShopMapper,LmGroupShop> {

    @Autowired
    private LmGroupShopMapper lmGroupShopMapper;
}

