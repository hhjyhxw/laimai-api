package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmFreightemplateCarriage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmFreightemplateCarriageMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 运费模板属性 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmFreightemplateCarriageService extends BaseServiceImpl<LmFreightemplateCarriageMapper,LmFreightemplateCarriage> {

    @Autowired
    private LmFreightemplateCarriageMapper lmFreightemplateCarriageMapper;
}

