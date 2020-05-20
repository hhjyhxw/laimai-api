package com.icloud.modules.lm.dto.goods;

import com.icloud.modules.lm.dto.CategoryDTO;
import com.icloud.modules.lm.dto.SuperDTO;
import com.icloud.modules.lm.dto.appraise.AppraiseResponseDTO;
import com.icloud.modules.lm.dto.freight.FreightTemplateDTO;
import com.icloud.modules.lm.entity.LmSku;
import com.icloud.modules.lm.entity.LmSpuAttribute;
import com.icloud.modules.lm.model.Page;
import lombok.Data;

import java.util.List;

/**
 * Created by rize on 2019/7/2.
 * SPU = Standard Product Unit (标准化产品单元)
 * SPU是商品信息聚合的最小单位，是一组可复用、易检索的标准化信息的集合，
 * 该集合描述了一个产品的特性。通俗点讲，属性值、特性相同的商品就可以称为一个SPU。
 */
@Data
public class SpuDTO extends SuperDTO {

    private List<LmSku> skuList;

    private Integer originalPrice;

    private Integer price;

    private Integer vipPrice;

    private Integer stock;

    private Integer sales;

    private String title;

    /**
     * 主图
     */
    private String img;

    /**
     * 后面的图，仅在详情接口才出现
     */
    private List<String> imgList;

    private String detail;

    private String description;

    private Long categoryId;

    private List<Long> categoryIds;

    private List<CategoryDTO> categoryList;

    private List<LmSpuAttribute> attributeList;

    /**
     * 商品的第一页(前10条)评价
     */
    private Page<AppraiseResponseDTO> appraisePage;

    /**
     * 商品现在携带的团购信息
     */
    private GroupShopDTO groupShop;

    private String unit;

    private Long freightTemplateId;

    private FreightTemplateDTO freightTemplate;

    private Boolean collect;

    private Integer status;

}
