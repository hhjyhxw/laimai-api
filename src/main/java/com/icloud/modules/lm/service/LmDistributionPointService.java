package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmDistributionPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmDistributionPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 供货商配送点 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmDistributionPointService extends BaseServiceImpl<LmDistributionPointMapper,LmDistributionPoint> {

    @Autowired
    private LmDistributionPointMapper lmDistributionPointMapper;
}

