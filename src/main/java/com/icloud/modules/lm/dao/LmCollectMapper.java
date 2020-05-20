package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.CollectDTO;
import com.icloud.modules.lm.entity.LmCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户收藏夹 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
public interface LmCollectMapper extends BaseMapper<LmCollect> {

	List<LmCollect> queryMixList(Map<String, Object> map);

    /**
     * 获得用户所有收藏
     * @param userId
     * @param offset
     * @param size
     * @return
     */
    public List<CollectDTO> getCollectAll(@Param("userId") Long userId, @Param("offset")Integer offset, @Param("size")Integer size);

    /**
     * 获得某一收藏
     * @param userId
     * @param collectId
     * @param spuId
     * @return
     */
    public CollectDTO getCollectById(@Param("userId") Long userId, @Param("collectId")Long collectId, @Param("spuId")Long spuId);

    public List<String> getUserCollectSpuIds(Long userId);
}
