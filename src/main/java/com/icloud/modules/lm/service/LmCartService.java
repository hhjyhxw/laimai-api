package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmCart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 购物车 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Service
@Transactional
public class LmCartService extends BaseServiceImpl<LmCartMapper,LmCart> {

    @Autowired
    private LmCartMapper lmCartMapper;
}

