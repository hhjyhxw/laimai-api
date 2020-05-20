package com.icloud.modules.lm.dto;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:

 */
@Data
public class RecommendDTO extends SuperDTO {

    private Integer recommendType;

    private Long spuId;

    private Integer spuOriginalPrice;

    private Integer spuPrice;

    private Integer spuVipPrice;

    private Integer spuSales;

    private String spuImg;

    private String spuTitle;

    private Long spuCategoryId;
}
