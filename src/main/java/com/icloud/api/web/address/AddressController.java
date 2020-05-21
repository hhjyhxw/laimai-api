package com.icloud.api.web.address;

import com.aliyun.oss.ServiceException;
import com.icloud.annotation.LoginUser;
import com.icloud.api.web.address.service.AddressService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/*
*/
@Controller
@RequestMapping("/api/address")
public class AddressController {

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
        if(address.getId()==null){
            ValidatorUtils.validateDTO(address);
        }
        address.setUserId(user.getId());
        Boolean result = addressService.addOrUpdateAddress(address);
       return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/deleteAddress")
    @ResponseBody
    public ApiResponse deleteAddress(Long addressId, Long userId,@LoginUser UserDTO user) throws ApiException {
        Boolean result =  addressService.deleteAddress(addressId,user.getId());
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/updateAddress")
    @ResponseBody
    public ApiResponse updateAddress(LmAddress address, @LoginUser UserDTO user) throws ApiException {
        Boolean result = addressService.addOrUpdateAddress(address);
        return new ApiResponse().okOrError(result);
    }

    @RequestMapping("/getAllAddress")
    @ResponseBody
    public ApiResponse getAllAddress(Long userId,@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(addressService.getAllAddress(userId));
    }

    @RequestMapping("/getAddressById")
    @ResponseBody
    public ApiResponse getAddressById(Long addressId) throws ServiceException {
        return new ApiResponse().ok(addressService.getAddressById(addressId));
    }

    @RequestMapping("/getDefAddress")
    @ResponseBody
    public ApiResponse getDefAddress(Long userId,@LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(addressService.getDefAddress(user.getId()));
    }

}
