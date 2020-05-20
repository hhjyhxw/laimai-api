package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.entity.LmUserAppraise;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评价表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmUserAppraiseMapper extends BaseMapper<LmUserAppraise> {

	List<LmUserAppraise> queryMixList(Map<String, Object> map);

    //根据商品spu_id，分页获取所有评价
    public List<AppraiseResponseDTO> selectSpuAllAppraise(@Param("spuId")Long spuId,@Param("offset")Integer offset, @Param("size")Integer size);

    //根据用户id，分页获取所有评价
    public List<AppraiseResponseDTO> selectUserAllAppraise(@Param("userId")Long userId, @Param("offset")Integer offset, @Param("size")Integer size);

//    //根据商品spu_id，分页获取所有评价
//    public List<AppraiseResponseDTO> selectSpuAllAppraise(@Param("spuId")Long spuId, @Param("offset")Integer offset, @Param("size")Integer size);

    //根据评价ID，查询评价
    public AppraiseResponseDTO selectOneById(@Param("appraiseId") Long appraiseId);

    //根据传入条件，查询
    public List<AppraiseResponseDTO> selectAppraiseCondition(@Param("id") Long id,@Param("userName") String userName,@Param("spuName") String spuName,@Param("orderId") Long orderId,@Param("score") Integer score,@Param("content") String content,@Param("offset") Integer offset,@Param("limit")Integer limit);

    //根据传入条件，查询符合条件总数
    public Integer countAppraiseCondition(@Param("id") Long id,@Param("userName") String userName,@Param("spuName") String spuName,@Param("orderId") Long orderId,@Param("score") Integer score,@Param("content") String content);


}
