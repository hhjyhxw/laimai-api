package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmSpuMapper;
import com.icloud.modules.lm.entity.LmSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 产品：标准化产品单元 SPU = Standard Product Unit （标准化产品单元）,SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmSpuService extends BaseServiceImpl<LmSpuMapper,LmSpu> {

    @Autowired
    private LmSpuMapper lmSpuMapper;

    public Integer incSales(Long spuId,Integer delta){
        return lmSpuMapper.incSales(spuId,delta);
    }
}

