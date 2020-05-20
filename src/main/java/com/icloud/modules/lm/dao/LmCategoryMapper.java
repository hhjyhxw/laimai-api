package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmCategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 产品：类目 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
public interface LmCategoryMapper extends BaseMapper<LmCategory> {

	List<LmCategory> queryMixList(Map<String, Object> map);
}
