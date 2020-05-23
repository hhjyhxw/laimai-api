package com.icloud.api.web.address;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ServiceException;
import com.icloud.annotation.LoginUser;
import com.icloud.api.web.address.service.AddressService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.web.AppBaseController;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/*
*/
@Controller
@RequestMapping("/api/address")
public class AddressController extends AppBaseController {

    @Autowired
    private AddressService addressService;

    /**
     * 添加用户地址
     * @return
     */
    //option的value的内容是这个method的描述，notes是详细描述，response是最终返回的json model。其他可以忽略
//    @ApiOperation(value = "绑定烟时扫二维码获取信息", notes = "增加收货地址")
    @RequestMapping("/addAddress")
    @ResponseBody
    public ApiResponse addAddress(LmAddress address, @LoginUser UserDTO user) throws ApiException {
        Map map = getMap("address", JSON.toJSONString(address),null);
        getMap("user", JSON.toJSONString(user),map);
        println(map,"addAddress");

        if(address.getId()==null){
            ValidatorUtils.validateDTO(address);
        }
        address.setUserId(user.getId());
        Boolean result = addressService.addOrUpdateAddress(address);
       return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/deleteAddress")
    @ResponseBody
    public ApiResponse deleteAddress(Long addressId,@LoginUser UserDTO user) throws ApiException {
        Boolean result =  addressService.deleteAddress(addressId,user.getId());
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/updateAddress")
    @ResponseBody
    public ApiResponse updateAddress(Long addressId, String province, String city, String county, String address, String isDefault,String phone, String consignee, @LoginUser UserDTO user) throws ApiException {
        LmAddress addressDO = new LmAddress();
        addressDO.setUserId(user.getId());
        addressDO.setId(addressId);
        addressDO.setProvince(province);
        addressDO.setCity(city);
        addressDO.setCounty(county);
        addressDO.setAddress(address);
        addressDO.setPhone(phone);
        addressDO.setConsignee(consignee);
        addressDO.setIsDefault(isDefault);

        Map map = getMap("address", JSON.toJSONString(addressDO),null);
        getMap("user", JSON.toJSONString(user),map);
        println(map,"updateAddress");
        Boolean result = addressService.addOrUpdateAddress(addressDO);
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/getAllAddress")
    @ResponseBody
    public ApiResponse getAllAddress(@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(addressService.getAllAddress(user.getId()));
    }

    @RequestMapping("/getAddressById")
    @ResponseBody
    public ApiResponse getAddressById(Long addressId) throws ServiceException {
        return new ApiResponse().ok(addressService.getAddressById(addressId));
    }

    @RequestMapping("/getDefAddress")
    @ResponseBody
    public ApiResponse getDefAddress(@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(addressService.getDefAddress(user.getId()));
    }

}
