package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmRecommend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmRecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 首页商品推荐表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmRecommendService extends BaseServiceImpl<LmRecommendMapper,LmRecommend> {

    @Autowired
    private LmRecommendMapper lmRecommendMapper;
}

