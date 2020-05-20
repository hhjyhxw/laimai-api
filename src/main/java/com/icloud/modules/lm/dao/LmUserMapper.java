package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 用户表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmUserMapper extends BaseMapper<LmUser> {

	List<LmUser> queryMixList(Map<String, Object> map);
}
