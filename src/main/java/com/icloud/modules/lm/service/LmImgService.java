package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmImgMapper;
import com.icloud.modules.lm.entity.LmImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 图片存储表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:00
 */
@Service
@Transactional
public class LmImgService extends BaseServiceImpl<LmImgMapper,LmImg> {

    @Autowired
    private LmImgMapper lmImgMapper;

    public List<String> getImgs(Integer bizType,Long bizId){
        return lmImgMapper.getImgs(bizType,bizId);
    }
}

