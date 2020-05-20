package com.icloud.api.lm.freight;

import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.freight.FreightBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.order.OrderRequestDTO;
import com.icloud.modules.lm.entity.LmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/freight")
public class FreightTemplateController {

    @Autowired
    private FreightBizService freightBizService;

    /**
     * 得到订单的运费"
     * @param orderRequestDTO
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getFreightMoney")
    @ResponseBody
    public ApiResponse getFreightMoney(@LoginUser LmUser user, OrderRequestDTO orderRequestDTO) throws ApiException {
        Integer totalFee = freightBizService.getFreightMoney(orderRequestDTO);
        return new ApiResponse().ok(totalFee);
    }


}
