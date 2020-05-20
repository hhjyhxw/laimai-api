package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.FootprintDTO;
import com.icloud.modules.lm.entity.LmFooprint;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户足迹 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmFooprintMapper extends BaseMapper<LmFooprint> {

	List<LmFooprint> queryMixList(Map<String, Object> map);

    public List<FootprintDTO> getAllFootprint(@Param("userId")Long userId, @Param("offset")Integer offset, @Param("size")Integer size);

}
