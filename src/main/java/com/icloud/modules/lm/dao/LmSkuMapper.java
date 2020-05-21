package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.goods.SkuDTO;
import com.icloud.modules.lm.entity.LmSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品：库存量单位 SKU=stock keeping unit(库存量单位) SKU即库存进出计量的单位（买家购买、商家进货、供应商备货、工厂生产都是依据SKU进行的）。
SKU是物理上不可分割的最小存货单元。也就是说一款商品，可以根据SKU来确定具体的货物存量。
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmSkuMapper extends BaseMapper<LmSku> {

	List<LmSku> queryMixList(Map<String, Object> map);

    public SkuDTO getSkuDTOById(Long skuId);

    public Integer decSkuStock(@Param("skuId") Long skuId, @Param("stock") Integer stock);

    public Integer returnSkuStock(@Param("skuId") Long skuId, @Param("stock") Integer stock);

    public Integer decSkuFreezeStock(@Param("skuId") Long skuId, @Param("stock") Integer stock);

    /**
     * 删除SPUID
     * @param spuId
     * @return
     */
    public List<Long> getSkuIds(@Param("spuId") Long spuId);

    List<Long> selectSkuIdsBySpuIds(@Param("ids") List<Long> ids);
}
