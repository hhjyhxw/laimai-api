package com.icloud.api.web.geographic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.util.BaiduMapUtil;
import com.icloud.basecommon.web.AppBaseController;
import com.icloud.common.util.StringUtil;
import com.icloud.common.validator.ValidatorUtils;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmAddress;
import com.icloud.modules.lm.entity.LmDistributionPoint;
import com.icloud.modules.lm.service.LmDistributionPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/location")
public class LocationController extends AppBaseController {

    @Autowired
    private LmDistributionPointService lmDistributionPointService;
    @Autowired
    private StringRedisTemplate userRedisTemplate;
    /**
     * 传入地址位置信息，查看是否存在配送点
     * @return
     */
    @RequestMapping("/checkDistributionpoit")
    @ResponseBody
    public ApiResponse checkDistributionpoit(String addressJson) {
        log.info("checkDistributionpoit:addressJson==="+addressJson);
        if(!StringUtil.checkStr(addressJson)){
            return new ApiResponse().error("参数为空",false);
        }
        LmAddress address =  JSON.parseObject(addressJson, LmAddress.class);
        ValidatorUtils.validateDTO(address);
        if(address.getLnt()==null || address.getLat()==null){
            String[] o = BaiduMapUtil.getCoordinate(address.getProvince()+address.getCity()+address.getCounty()+address.getAddress());
            if(o==null || o[0]==null || o[1]==null){
                return new ApiResponse().error("根据地址获取经纬度失败",false);
            }
            address.setLnt(new BigDecimal(o[0]));
            address.setLat(new BigDecimal(o[1]));
        }
        //根据经纬度检查是否存在配送点
        LmDistributionPoint point = lmDistributionPointService.selectLatestDisbutPoit(address.getLnt(),address.getLat());
        if(point==null){
            ApiResponse r = new ApiResponse().error("没用符合的配送点,请更换地址再试",false);
            r.setCode(3000);
            return r;
        }
        //设置默认送货地址 与 最近配送点关系
        String accessToken = request.getHeader("accessToken");
        Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
        UserDTO user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
        user.setDistrpoint(point);
        userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(user));
        return new ApiResponse().ok(address);
    }
}