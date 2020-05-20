package com.icloud.api.lm.appraise;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.LoginUser;
import com.icloud.api.lm.appraise.service.AppraiseService;
import com.icloud.api.sevice.appriaise.AppraiseBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.exceptions.ServiceException;
import com.icloud.modules.lm.dto.appraise.AppraiseRequestDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseRequestItemDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.entity.*;
import com.icloud.modules.lm.enums.BizType;
import com.icloud.modules.lm.enums.OrderStatusType;
import com.icloud.modules.lm.model.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/appraise")
public class AppraiseController {

    @Autowired
    private AppraiseService appraiseService;

    /**
     * 增加评价
     * @param appraiseRequestDTO
     * @param user
     * @return
     * @throws ApiException
     */
    @RequestMapping("/addAppraise")
    @ResponseBody
    public ApiResponse addAppraise(AppraiseRequestDTO appraiseRequestDTO, @LoginUser LmUser user) throws ApiException {
        if (appraiseRequestDTO.getOrderId() == null) {
            throw new ApiException("评价订单id为空");
        }
        return new ApiResponse().okOrError( appraiseService.addAppraise(appraiseRequestDTO,user));
    }

    /**
     * 根据评论Id删除评论
     * @param appraiseId
     * @param user
     * @return
     * @throws ApiException
     */
    @RequestMapping("/deleteAppraiseById")
    @ResponseBody
    public ApiResponse deleteAppraiseById(Long appraiseId, @LoginUser LmUser user) throws ApiException {
        boolean result = appraiseService.deleteAppraiseById(appraiseId,user);
        return new ApiResponse().okOrError(result);
    }

    /**
     *查询用户所有评论
     * @param user
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getUserAllAppraise")
    @ResponseBody
    public ApiResponse getUserAllAppraise(@LoginUser LmUser user, Integer pageNo, Integer pageSize) throws ApiException {
        Page<AppraiseResponseDTO> result = appraiseService.getUserAllAppraise(user,pageNo,pageSize);
        return new ApiResponse().ok(result);
    }


    /**
     *查询商品的所有评论
     * @param spuId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/getSpuAllAppraise")
    @ResponseBody
    public ApiResponse getSpuAllAppraise(Long spuId, Integer pageNo, Integer pageSize) throws ServiceException {
        Page<AppraiseResponseDTO> result =  appraiseService.getSpuAllAppraise(spuId, pageNo, pageSize);
        return new ApiResponse().ok(result);
    }


    /**
     *查询某一条评论
     * @param appraiseId
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getSpuAllAppraise")
    @ResponseBody
    public ApiResponse getOneById(@LoginUser LmUser user, Long appraiseId) throws ApiException {
        AppraiseResponseDTO result = appraiseService.getOneById(user.getId(),appraiseId);
        return new ApiResponse().ok(result);
    }
}
