package com.icloud.modules.lm.dto;

import com.icloud.modules.lm.entity.LmSpu;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 首页聚合接口DTO
 */
@Data
public class IntegralIndexDataDTO {

    private Map<String, List<AdvertisementDTO>> advertisement;//banner广告、首页分类（分类包含对应商品）

    private List<RecommendDTO> windowRecommend;//后台推荐商品

    private List<LmSpu> salesTop;//销量倒序

    private List<LmSpu> newTop;//新品

}
