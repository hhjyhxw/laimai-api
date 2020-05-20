package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmSpuSupplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmSpuSupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 商品与供货商关联表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmSpuSupplierService extends BaseServiceImpl<LmSpuSupplierMapper,LmSpuSupplier> {

    @Autowired
    private LmSpuSupplierMapper lmSpuSupplierMapper;
}

