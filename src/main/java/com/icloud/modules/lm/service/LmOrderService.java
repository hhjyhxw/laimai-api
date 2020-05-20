package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 订单表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmOrderService extends BaseServiceImpl<LmOrderMapper,LmOrder> {

    @Autowired
    private LmOrderMapper lmOrderMapper;
}

