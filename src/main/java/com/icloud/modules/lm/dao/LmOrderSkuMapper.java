package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmOrderSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 订单明细表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmOrderSkuMapper extends BaseMapper<LmOrderSku> {

	List<LmOrderSku> queryMixList(Map<String, Object> map);
}
