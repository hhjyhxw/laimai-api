package com.icloud.api.web.cart;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.category.CategoryBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.exceptions.ApiException;
import com.icloud.exceptions.ServiceException;
import com.icloud.modules.lm.dto.CartDTO;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmAddress;
import com.icloud.modules.lm.entity.LmCart;
import com.icloud.modules.lm.service.LmAddressService;
import com.icloud.modules.lm.service.LmCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private LmCartService lmCartService;
    @Autowired
    private LmAddressService lmAddressService;

    @Autowired
    private CategoryBizService categoryBizService;

    @RequestMapping("/addCartItem")
    @ResponseBody
    public ApiResponse addCartItem(Long skuId, Integer num, @LoginUser UserDTO user) throws ApiException {
        LmAddress defautAddress = getDefaultAddress(user);
        List<LmCart> cartDOS = lmCartService.list(
                new QueryWrapper<LmCart>().eq("sku_id", skuId).eq("user_id", user.getId())
                        .eq("address_id", defautAddress.getId()));
        LmCart cartDO = new LmCart();
        Date now = new Date();
        if (!CollectionUtils.isEmpty(cartDOS)) {
            //若非空
            cartDO.setId(cartDOS.get(0).getId());
            cartDO.setNum(cartDOS.get(0).getNum() + num);
            cartDO.setUpdatedTime(now);
            return new ApiResponse().okOrError(lmCartService.updateById(cartDO));
        } else {
            //不存在，则添加购物车
            cartDO.setSkuId(skuId);
            cartDO.setNum(num);
            cartDO.setUserId(user.getId());
            cartDO.setAddressId(defautAddress.getId());
            cartDO.setCreatedTime(now);
            cartDO.setUpdatedTime(now);
            return new ApiResponse().okOrError(lmCartService.save(cartDO));
        }
    }

    @RequestMapping("/subCartItem")
    @ResponseBody
    public ApiResponse subCartItem(Long skuId, Integer num, @LoginUser UserDTO user) throws ApiException {
        List<LmCart> cartDOS = lmCartService.list(
                new QueryWrapper<LmCart>().eq("sku_id", skuId).eq("user_id", user.getId())
                        .eq("address_id", getDefaultAddress(user).getId()));

        LmCart cartDO = new LmCart();
        if (!CollectionUtils.isEmpty(cartDOS)) {
            cartDO.setId(cartDOS.get(0).getId());
            cartDO.setNum(cartDOS.get(0).getNum() - num);
            if (cartDO.getNum() <= 0) {
                //直接删除此商品
                return new ApiResponse().okOrError(lmCartService.removeById(cartDO));
            } else {
                return new ApiResponse().okOrError(lmCartService.updateById(cartDO));
            }
        }
        return new ApiResponse().ok();

    }

    @RequestMapping("/removeCartItem")
    @ResponseBody
    public ApiResponse removeCartItem(Long cartId, @LoginUser UserDTO user) throws ApiException {
        boolean result = lmCartService.remove(
                new QueryWrapper<LmCart>() .eq("id", cartId).eq("user_id", user.getId())
                        .eq("address_id", getDefaultAddress(user).getId()));
        return new ApiResponse().okOrError(result);

    }

    @RequestMapping("/removeCartItemBatch")
    @ResponseBody
    public ApiResponse removeCartItemBatch(String cartIdList, @LoginUser UserDTO user) throws ApiException {
        if (StringUtils.isEmpty(cartIdList)) {
            throw new ApiException("购物车ids参数为空");
        }
        String[] split = cartIdList.split(",");
        if (split.length == 0) {
            throw new ApiException("删除购物车参数不正确");
        }
        List<Long> array = new ArrayList<>(split.length);
        for (String idRaw : split) {
            array.add(new Long(idRaw));
        }
        Boolean result = lmCartService.remove(new QueryWrapper<LmCart>()
                .in("id", array)
                .eq("user_id", user.getId()));
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/removeCartAll")
    @ResponseBody
    public ApiResponse removeCartAll(@LoginUser UserDTO user) throws ServiceException {
        Boolean result = lmCartService.remove(
                new QueryWrapper<LmCart>().eq("user_id", user.getId())
                        .eq("address_id", getDefaultAddress(user).getId()));
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/updateCartItemNum")
    @ResponseBody
    public ApiResponse updateCartItemNum(Long cartId, Integer num, @LoginUser UserDTO user) throws ServiceException {
        LmCart cartDO = new LmCart();
        cartDO.setNum(num);
        cartDO.setUpdatedTime(new Date());
        Boolean result = lmCartService.update(cartDO,new UpdateWrapper<LmCart>()
                .eq("id", cartId)
                .eq("user_id", user.getId()));

        if (result) {
            return new ApiResponse().ok(num);
        }
        return new ApiResponse().error();
    }

    @RequestMapping("/countCart")
    @ResponseBody
    public ApiResponse countCart(@LoginUser UserDTO user) throws ServiceException {
        Integer num = lmCartService.count(new QueryWrapper<LmCart>().eq("user_id", user.getId())
                .eq("address_id", getDefaultAddress(user).getId()));
        return new ApiResponse().ok(num);
    }

    @RequestMapping("/getCartList")
    @ResponseBody
    public ApiResponse getCartList(@LoginUser UserDTO user) throws ServiceException {
        List<CartDTO> cartList = lmCartService.getCartList(user.getId(),getDefaultAddress(user).getId() );
        for (CartDTO cartDTO : cartList) {
            List<Long> categoryFamily = categoryBizService.getCategoryFamily(cartDTO.getCategoryId());
            cartDTO.setCategoryIdList(categoryFamily);
        }
        return new ApiResponse().ok(cartList);
    }

    private LmAddress getDefaultAddress(UserDTO user){
        List<LmAddress> defautAdress = lmAddressService.list(
                new QueryWrapper<LmAddress>().eq("user_id", user.getId())
                        .eq("is_default", "1"));
        if(defautAdress==null || defautAdress.size()==0){
            throw new ApiException("没有默认地址");
        }
        return defautAdress.get(0);
    }
}
