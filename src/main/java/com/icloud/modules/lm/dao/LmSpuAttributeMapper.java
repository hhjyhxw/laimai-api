package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmSpuAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 产品：标准化产品单元属性 SPU 属性(不会影响到库存和价格的属性, 又叫关键属性)
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmSpuAttributeMapper extends BaseMapper<LmSpuAttribute> {

	List<LmSpuAttribute> queryMixList(Map<String, Object> map);
}
