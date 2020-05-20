package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmGroupShopSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 团购子商品表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmGroupShopSkuMapper extends BaseMapper<LmGroupShopSku> {

	List<LmGroupShopSku> queryMixList(Map<String, Object> map);
}
