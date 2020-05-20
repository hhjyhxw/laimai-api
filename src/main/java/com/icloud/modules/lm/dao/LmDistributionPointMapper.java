package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmDistributionPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 供货商配送点 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmDistributionPointMapper extends BaseMapper<LmDistributionPoint> {

	List<LmDistributionPoint> queryMixList(Map<String, Object> map);
}
