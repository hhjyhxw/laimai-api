package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmSkuMapper;
import com.icloud.modules.lm.dto.goods.SkuDTO;
import com.icloud.modules.lm.entity.LmSku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 产品：库存量单位 SKU=stock keeping unit(库存量单位) SKU即库存进出计量的单位（买家购买、商家进货、供应商备货、工厂生产都是依据SKU进行的）。
SKU是物理上不可分割的最小存货单元。也就是说一款商品，可以根据SKU来确定具体的货物存量。
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmSkuService extends BaseServiceImpl<LmSkuMapper,LmSku> {

    @Autowired
    private LmSkuMapper lmSkuMapper;

    public SkuDTO getSkuDTOById(Long skuId){
        return lmSkuMapper.getSkuDTOById(skuId);
    }

    public Integer decSkuStock(Long skuId,Integer stock){
        return lmSkuMapper.decSkuStock(skuId,stock);
    }

    public Integer returnSkuStock(Long skuId,Integer stock){
        return lmSkuMapper.returnSkuStock(skuId,stock);
    }

    public Integer decSkuFreezeStock(Long skuId,Integer stock){
        return lmSkuMapper.decSkuFreezeStock(skuId,stock);
    }

    /**
     * 删除SPUID
     * @param spuId
     * @return
     */
    public List<Long> getSkuIds(Long spuId){
        return lmSkuMapper.getSkuIds(spuId);
    }

    List<Long> selectSkuIdsBySpuIds(List<Long> ids){
        return lmSkuMapper.selectSkuIdsBySpuIds(ids);
    }
}

