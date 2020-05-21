package com.icloud.api.web.integral;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.icloud.annotation.AuthIgnore;
import com.icloud.api.sevice.goods.GoodsBizService;
import com.icloud.api.sevice.recommend.RecommendBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.AdvertisementDTO;
import com.icloud.modules.lm.dto.IntegralIndexDataDTO;
import com.icloud.modules.lm.dto.RecommendDTO;
import com.icloud.modules.lm.dto.goods.SpuDTO;
import com.icloud.modules.lm.entity.LmAdvert;
import com.icloud.modules.lm.enums.AdvertisementType;
import com.icloud.modules.lm.enums.RecommendType;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.LmAdvertService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/integral")
public class IntegralController {


    @Autowired
    private LmAdvertService LmAdvertService;

    @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private RecommendBizService recommendBizService;


    /**
     * 获取首页聚合数据
     * @return
     * @throws ApiException
     */
    @RequestMapping("/getIndexData")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getIndexData() throws ApiException {
        //分类
        List<LmAdvert> activeAd = LmAdvertService.list(new QueryWrapper<LmAdvert>().eq("status","1"));
        Map<String, List<AdvertisementDTO>> adDTOMap = activeAd.stream().map(item -> {
            AdvertisementDTO advertisementDTO = new AdvertisementDTO();
            BeanUtils.copyProperties(item, advertisementDTO);
            return advertisementDTO;
        }).collect(Collectors.groupingBy(item -> "t" + item.getAdType()));


        //分类code 4  AdType
        List<AdvertisementDTO> categoryPickAd = adDTOMap.get("t" + AdvertisementType.CATEGORY_PICK.getCode());
        //封装 分类精选 商品
        if (!CollectionUtils.isEmpty(categoryPickAd)) {
            for (AdvertisementDTO item : categoryPickAd) {
                Page<SpuDTO> pickPage = goodsBizService.getGoodsPage(1, 10, new Long(item.getPageUrl().substring(item.getPageUrl().lastIndexOf("=") + 1)), "sales", false,null);
                item.setData(pickPage.getItems());
            }
        }
        IntegralIndexDataDTO integralIndexDataDTO = new IntegralIndexDataDTO();
        integralIndexDataDTO.setAdvertisement(adDTOMap);

        /**
         * 橱窗推荐
         */
        List<RecommendDTO> windowRecommend = recommendBizService.getRecommendByType(RecommendType.WINDOW.getCode(), 1, 10);
        integralIndexDataDTO.setWindowRecommend(windowRecommend);

        /**
         * 销量冠军
         */
        List<SpuDTO> salesTop = goodsBizService.getGoodsPage(1, 8, null, "sales", false, null).getItems();
        integralIndexDataDTO.setSalesTop(salesTop);

        /**
         * 最近上新
         */
        List<SpuDTO> newTop = goodsBizService.getGoodsPage(1, 8, null, "id", false, null).getItems();
        integralIndexDataDTO.setNewTop(newTop);
        return new ApiResponse().ok(integralIndexDataDTO);
    }


}
