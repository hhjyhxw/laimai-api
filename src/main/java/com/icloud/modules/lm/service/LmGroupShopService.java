package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmGroupShopMapper;
import com.icloud.modules.lm.dto.goods.GroupShopDTO;
import com.icloud.modules.lm.entity.LmGroupShop;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 团购主商品表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 16:18:01
 */
@Service
@Transactional
public class LmGroupShopService extends BaseServiceImpl<LmGroupShopMapper,LmGroupShop> {

    @Autowired
    private LmGroupShopMapper lmGroupShopMapper;

    public List<GroupShopDTO> getGroupShopPage(Integer offset,  Integer limit){
        return lmGroupShopMapper.getGroupShopPage(offset,limit);
    }

    public GroupShopDTO detail(Long groupShopId){
        return lmGroupShopMapper.detail(groupShopId);
    }

    /**
     * 增加当前人数
     * @param id 团购活动Id
     * @param num 增加人数量
     * @return
     */
    public Integer incCurrentNum(Long id,Integer num){
        return lmGroupShopMapper.incCurrentNum(id,num);
    }
}

