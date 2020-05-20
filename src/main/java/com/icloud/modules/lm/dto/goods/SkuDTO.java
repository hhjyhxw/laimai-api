package com.icloud.modules.lm.dto.goods;

import com.icloud.modules.lm.dto.SuperDTO;
import lombok.Data;

/**
 * Created by rize on 2019/7/6.
 * SKU=stock keeping unit(库存量单位)
 * SKU即库存进出计量的单位， 可以是以件、盒、托盘等为单位。
 * SKU是物理上不可分割的最小存货单元。在使用时要根据不同业态，
 * 不同管理模式来处理。在服装、鞋类商品中使用最多最普遍。
 *
 * spu 是一款商品
 * sku 是这款商品的 不同规则，每个规格有自己的库存
 */
@Data
public class SkuDTO extends SuperDTO {

    private Long spuId;

    private String barCode;

    private Long categoryId;

    /**
     * SKU显示文字
     */
    private String title;

    private String spuTitle;

    private String img;

    private String spuImg;

    private Integer originalPrice;

    private Integer price;

    private Integer vipPrice;

    private Integer stock;

    private Integer status;

    private Integer freezeStock;

    private String unit;

}
