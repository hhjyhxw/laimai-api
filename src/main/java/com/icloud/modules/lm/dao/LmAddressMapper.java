package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 用户收货地址 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
public interface LmAddressMapper extends BaseMapper<LmAddress> {

	List<LmAddress> queryMixList(Map<String, Object> map);
}
