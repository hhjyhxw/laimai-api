package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 用户收货地址 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Service
@Transactional
public class LmAddressService extends BaseServiceImpl<LmAddressMapper,LmAddress> {

    @Autowired
    private LmAddressMapper lmAddressMapper;
}

