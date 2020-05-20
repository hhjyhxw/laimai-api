package com.icloud.api.sevice.freight;


import com.aliyun.oss.ServiceException;
import com.icloud.modules.lm.dto.freight.ShipTraceDTO;

/**
 * Created by rize on 2019/7/10.
 */
public interface ShipTraceQuery {

    public ShipTraceDTO query(String shipNo, String shipCode) throws ServiceException;

}
