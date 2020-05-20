package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 产品：类目 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Service
@Transactional
public class LmCategoryService extends BaseServiceImpl<LmCategoryMapper,LmCategory> {

    @Autowired
    private LmCategoryMapper lmCategoryMapper;
}

