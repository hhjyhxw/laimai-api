package com.icloud.modules.lm.dto.goods;


import com.icloud.modules.lm.dto.SuperDTO;
import com.icloud.modules.lm.entity.LmGroupShopSku;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
@ClassName: GroupShopDTO
@Description:
*/

@Data
public class GroupShopDTO extends SuperDTO {

    private Long spuId;

    private Integer minPrice;

    private Integer maxPrice;

    private Date gmtStart;

    private Date gmtEnd;

    private Integer minimumNumber;

    private Integer alreadyBuyNumber;

    private Integer automaticRefund;

    /**
     * GroupShopSkuDTO列表
     */
    private List<GroupShopSkuDTO> groupShopSkuDTOList;

    private List<LmGroupShopSku> groupShopSkuList;

    /**
     * spu属性
     */
    private Integer originalPrice;

    private Integer price;

    private Integer vipPrice;

    private String title;

    private Integer sales;

    private String img;

    private String detail;

    private String description;

    private Long categoryId;

    private Long freightTemplateId;

    private String unit;

    private Integer status;
}
