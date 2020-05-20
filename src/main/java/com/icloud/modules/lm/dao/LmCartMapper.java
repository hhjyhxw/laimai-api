package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.CartDTO;
import com.icloud.modules.lm.entity.LmCart;

import java.util.List;
import java.util.Map;

/**
 * 购物车 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
public interface LmCartMapper extends BaseMapper<LmCart> {

	List<LmCart> queryMixList(Map<String, Object> map);

    public List<CartDTO> getCartList(Long userId);
}
