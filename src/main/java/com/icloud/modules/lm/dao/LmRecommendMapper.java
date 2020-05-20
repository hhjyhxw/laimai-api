package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.RecommendDTO;
import com.icloud.modules.lm.entity.LmRecommend;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 首页商品推荐表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmRecommendMapper extends BaseMapper<LmRecommend> {

	List<LmRecommend> queryMixList(Map<String, Object> map);

    //根据推荐类型，查找商品信息
    public List<RecommendDTO> getRecommendByType(@Param("recommendType") Integer recommendType, @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    public List<RecommendDTO> getAllRecommend(@Param("offset") Integer offset,@Param("pageSize") Integer pageSize);
}
