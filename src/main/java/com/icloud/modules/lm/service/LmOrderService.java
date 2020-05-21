package com.icloud.modules.lm.service;

import com.icloud.basecommon.service.BaseServiceImpl;
import com.icloud.modules.lm.dao.LmOrderMapper;
import com.icloud.modules.lm.dto.order.OrderDTO;
import com.icloud.modules.lm.entity.LmOrder;
import com.icloud.modules.lm.model.KVModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单表 
 * @author zdh
 * @email yyyyyy@cm.com
 * @date 2020-05-19 11:08:47
 */
@Service
@Transactional
public class LmOrderService extends BaseServiceImpl<LmOrderMapper,LmOrder> {

    @Autowired
    private LmOrderMapper lmOrderMapper;

    public List<OrderDTO> selectOrderPage( String status,Integer offset,Integer limit,Long userId){
        return lmOrderMapper.selectOrderPage(status,offset,limit,userId);
    }

    public Long countOrder(String status, Integer offset,Integer limit,Long userId){
        return lmOrderMapper.countOrder(status,offset,limit,userId);
    }

    /**
     * 获取订单地区统计
     * @return
     */
    public List<KVModel<String, Long>> selectAreaStatistics(){
        return lmOrderMapper.selectAreaStatistics();
    }

    public List<KVModel<String, Long>> selectChannelStatistics(){
        return lmOrderMapper.selectChannelStatistics();
    }

    public List<KVModel<String, Long>> selectOrderCountStatistics(String gmtStart){
        return lmOrderMapper.selectOrderCountStatistics(gmtStart);
    }

    public List<KVModel<String, Long>> selectOrderSumStatistics(String gmtStart){
        return lmOrderMapper.selectOrderSumStatistics(gmtStart);
    }

    public List<LmOrder> selectExpireOrderNos(@Param("status") String status, @Param("time") Date time){
        return lmOrderMapper.selectExpireOrderNos(status, time);
    }
}

