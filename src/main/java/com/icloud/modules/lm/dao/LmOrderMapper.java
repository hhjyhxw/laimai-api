package com.icloud.modules.lm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icloud.modules.lm.dto.order.OrderDTO;
import com.icloud.modules.lm.entity.LmOrder;
import com.icloud.modules.lm.model.KVModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
public interface LmOrderMapper extends BaseMapper<LmOrder> {

	List<LmOrder> queryMixList(Map<String, Object> map);

    public List<OrderDTO> selectOrderPage(@Param("status") String status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    public Long countOrder(@Param("status") String status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    /**
     * 获取订单地区统计
     * @return
     */
    public List<KVModel<String, Long>> selectAreaStatistics();

    public List<KVModel<String, Long>> selectChannelStatistics();

    public List<KVModel<String, Long>> selectOrderCountStatistics(String gmtStart);

    public List<KVModel<String, Long>> selectOrderSumStatistics(String gmtStart);

    public List<LmOrder> selectExpireOrderNos(@Param("status") String status, @Param("time") Date time);
}
