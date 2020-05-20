package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmUserAppraiseMapper;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.entity.LmUserAppraise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评价表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmUserAppraiseService extends BaseServiceImpl<LmUserAppraiseMapper,LmUserAppraise> {

    @Autowired
    private LmUserAppraiseMapper lmUserAppraiseMapper;


    //根据商品spu_id，分页获取所有评价
//    public List<AppraiseResponseDTO> selectSpuAllAppraise(Long spuId){
//        return lmUserAppraiseMapper.selectSpuAllAppraise(spuId);
//    }

    //根据用户id，分页获取所有评价
    public List<AppraiseResponseDTO> selectUserAllAppraise(Long userId, Integer offset, Integer size){
        return lmUserAppraiseMapper.selectUserAllAppraise(userId,offset,size);
    }

//    //根据商品spu_id，分页获取所有评价
    public List<AppraiseResponseDTO> selectSpuAllAppraise(Long spuId, Integer offset, Integer size){
        return lmUserAppraiseMapper.selectSpuAllAppraise(spuId,offset,size);
    }

    //根据评价ID，查询评价
    public AppraiseResponseDTO selectOneById(Long appraiseId){
        return lmUserAppraiseMapper.selectOneById(appraiseId);
    }

    //根据传入条件，查询
    public List<AppraiseResponseDTO> selectAppraiseCondition(Long id,String userName,String spuName,Long orderId,Integer score,String content,Integer offset,Integer limit){
        return lmUserAppraiseMapper.selectAppraiseCondition(id,userName,spuName,orderId,score,content,offset,limit);
    }

    //根据传入条件，查询符合条件总数
    public Integer countAppraiseCondition(Long id,String userName,String spuName,Long orderId,Integer score,String content){
        return lmUserAppraiseMapper.countAppraiseCondition(id,userName,spuName,orderId,score,content);
    }

}

