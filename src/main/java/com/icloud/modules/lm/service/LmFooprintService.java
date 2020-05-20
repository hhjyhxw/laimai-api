package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmFooprintMapper;
import com.icloud.modules.lm.dto.FootprintDTO;
import com.icloud.modules.lm.entity.LmFooprint;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户足迹 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmFooprintService extends BaseServiceImpl<LmFooprintMapper,LmFooprint> {

    @Autowired
    private LmFooprintMapper lmFooprintMapper;


    public List<FootprintDTO> getAllFootprint(Long userId,Integer offset,Integer size){
        return lmFooprintMapper.getAllFootprint(userId,offset,size);
    }

}

