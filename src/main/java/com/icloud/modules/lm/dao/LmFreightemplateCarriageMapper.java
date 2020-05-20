package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmFreightemplateCarriage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 运费模板属性 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
public interface LmFreightemplateCarriageMapper extends BaseMapper<LmFreightemplateCarriage> {

	List<LmFreightemplateCarriage> queryMixList(Map<String, Object> map);
}
