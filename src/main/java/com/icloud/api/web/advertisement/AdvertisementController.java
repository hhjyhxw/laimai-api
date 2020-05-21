package com.icloud.api.web.advertisement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.AuthIgnore;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.entity.LmAdvert;
import com.icloud.modules.lm.enums.StatusType;
import com.icloud.modules.lm.service.LmAdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/advertisement")
public class AdvertisementController {

    @Autowired
    private LmAdvertService lmAdvertService;
    @Autowired
    private CacheComponent cacheComponent;

    private final static String ADVERTISEMENT_NAME = "ADVERTISEMENT_TYPE_";

    @RequestMapping("/getActiveAd")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getActiveAd(Integer adType) throws ApiException {
        List<LmAdvert> advertisementDOList  = cacheComponent.getObjList(ADVERTISEMENT_NAME + adType, LmAdvert.class);
        if (CollectionUtils.isEmpty(advertisementDOList)) {
            QueryWrapper<LmAdvert> wrapper = new QueryWrapper<LmAdvert>().eq("status", String.valueOf(StatusType.ACTIVE.getCode()));
            if (adType != null) {
                wrapper.eq("ad_type", String.valueOf(adType));
            }
            advertisementDOList = lmAdvertService.list(wrapper);
            cacheComponent.putObj(ADVERTISEMENT_NAME + adType, advertisementDOList, 100);
        }
        return new ApiResponse().ok(advertisementDOList);
    }


}
