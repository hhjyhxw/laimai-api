package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmSupplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 供货商 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmSupplierMapper extends BaseMapper<LmSupplier> {

	List<LmSupplier> queryMixList(Map<String, Object> map);
}
