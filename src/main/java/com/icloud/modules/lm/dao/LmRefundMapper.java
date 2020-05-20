package com.icloud.modules.lm.dao;

import com.icloud.modules.lm.entity.LmRefund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * 退款表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmRefundMapper extends BaseMapper<LmRefund> {

	List<LmRefund> queryMixList(Map<String, Object> map);
}
