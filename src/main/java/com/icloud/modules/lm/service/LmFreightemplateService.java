package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmFreightemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmFreightemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 运费模板 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmFreightemplateService extends BaseServiceImpl<LmFreightemplateMapper,LmFreightemplate> {

    @Autowired
    private LmFreightemplateMapper lmFreightemplateMapper;
}

