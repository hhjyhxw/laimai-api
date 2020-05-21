package com.icloud.api.web.goods;

import com.icloud.annotation.AuthIgnore;
import com.icloud.api.sevice.goods.GoodsBizService;
import com.icloud.api.sevice.groupshop.GroupShopBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.goods.SpuDTO;
import com.icloud.modules.lm.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/goods")
public class GoodsController {

   @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private GroupShopBizService groupShopBizService;

    @RequestMapping("/getGoodsPage")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) throws ApiException {
        Page<SpuDTO> page = goodsBizService.getGoodsPage(pageNo, pageSize, categoryId, orderBy, isAsc, title);
        return new ApiResponse().ok(page);
    }

    @RequestMapping("/getGoods")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getGoods(Long spuId, Long groupShopId, Long userId) throws ApiException {
        //若团购Id不为空，则额外添加团购信息
        SpuDTO goods = goodsBizService.getGoods(spuId, userId);
        if (groupShopId != null) {
            goods.setGroupShop(groupShopBizService.getGroupShopById(groupShopId));
        }
        return new ApiResponse().ok(goods);
    }


}
