package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmFreightemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 运费模板 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmFreightemplateMapper extends BaseMapper<LmFreightemplate> {

	List<LmFreightemplate> queryMixList(Map<String, Object> map);

    public LmFreightemplate selectFreightBySkuId(Long skuId);
}
