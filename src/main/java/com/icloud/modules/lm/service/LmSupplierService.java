package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmSupplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmSupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 供货商 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmSupplierService extends BaseServiceImpl<LmSupplierMapper,LmSupplier> {

    @Autowired
    private LmSupplierMapper lmSupplierMapper;
}

