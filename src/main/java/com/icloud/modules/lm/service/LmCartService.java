package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmCartMapper;
import com.icloud.modules.lm.dto.CartDTO;
import com.icloud.modules.lm.entity.LmCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:48
 */
@Service
@Transactional
public class LmCartService extends BaseServiceImpl<LmCartMapper,LmCart> {

    @Autowired
    private LmCartMapper lmCartMapper;

    public List<CartDTO> getCartList(Long userId,Long addressId){
        return lmCartMapper.getCartList(userId,addressId);
    }
}

