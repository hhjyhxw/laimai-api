package com.icloud.api.web.recommend;

import com.icloud.annotation.AuthIgnore;
import com.icloud.api.sevice.recommend.RecommendBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendBizService recommendBizService;

    @RequestMapping("/getRecommendByType")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getRecommendByType(Integer recommendType, Integer pageNo, Integer pageSize) throws ApiException {
        return new ApiResponse().ok(recommendBizService.getRecommendByType(recommendType,pageNo,pageSize));
    }


}
