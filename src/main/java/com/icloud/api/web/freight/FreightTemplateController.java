package com.icloud.api.web.freight;

import com.alibaba.fastjson.JSON;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.freight.FreightBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.web.AppBaseController;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.dto.order.OrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/freight")
public class FreightTemplateController extends AppBaseController {

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
    public ApiResponse getFreightMoney(@LoginUser UserDTO user, OrderRequestDTO orderRequestDTO) throws ApiException {
               Map map = getMap("orderRequestDTO", JSON.toJSONString(orderRequestDTO),null);
        println(map,"getFreightMoney");
        if(orderRequestDTO==null || orderRequestDTO.getSkuList()==null || orderRequestDTO.getSkuList().size()==0){
            return new ApiResponse().ok(0);
        }
        Integer totalFee = freightBizService.getFreightMoney(orderRequestDTO);
        return new ApiResponse().ok(totalFee);
    }


}
