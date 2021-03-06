package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.entity.LmSpu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 产品：标准化产品单元 SPU = Standard Product Unit （标准化产品单元）,SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，该集合描述了一个产品的特性。
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmSpuMapper extends BaseMapper<LmSpu> {

	List<LmSpu> queryMixList(Map<String, Object> map);

    public List<LmSpu> getAllPage(Map<String, Object> map,@Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    List<LmSpu> getAllPageByMap(Map<String, Object> params);

    Integer counts(Map<String, Object> params);

    public Integer incSales(@Param("spuId") Long spuId,@Param("delta") Integer delta);
}
