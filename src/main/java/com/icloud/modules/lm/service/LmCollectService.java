package com.icloud.modules.lm.service;

import com.icloud.modules.lm.entity.LmCollect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 用户收藏夹 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Service
@Transactional
public class LmCollectService extends BaseServiceImpl<LmCollectMapper,LmCollect> {

    @Autowired
    private LmCollectMapper lmCollectMapper;
}

