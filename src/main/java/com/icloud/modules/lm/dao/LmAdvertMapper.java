package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmAdvert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 广告 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
public interface LmAdvertMapper extends BaseMapper<LmAdvert> {

	List<LmAdvert> queryMixList(Map<String, Object> map);
}
