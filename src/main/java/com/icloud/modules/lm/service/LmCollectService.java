package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCollectMapper;
import com.icloud.modules.lm.dto.CollectDTO;
import com.icloud.modules.lm.entity.LmCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    /**
     * 获得用户所有收藏
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    public List<CollectDTO> getCollectAll(Long userId,Integer offset,Integer size){
        return lmCollectMapper.getCollectAll(userId,offset,size);
    }

    /**
     * 获得某一收藏
     * @param userId
     * @param collectId
     * @param spuId
     * @return
     */
    public CollectDTO getCollectById(Long userId,Long collectId,Long spuId){
        return lmCollectMapper.getCollectById(userId,collectId,spuId);
    }

    public List<String> getUserCollectSpuIds(Long userId){
        return lmCollectMapper.getUserCollectSpuIds(userId);
    }
}

