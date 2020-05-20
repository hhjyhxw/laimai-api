package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmSpuSupplier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 商品与供货商关联表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmSpuSupplierMapper extends BaseMapper<LmSpuSupplier> {

	List<LmSpuSupplier> queryMixList(Map<String, Object> map);
}
