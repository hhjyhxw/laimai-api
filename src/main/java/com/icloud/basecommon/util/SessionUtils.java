package com.icloud.basecommon.util;

import com.alibaba.fastjson.JSONObject;
import com.icloud.common.SpringContextHolder;
import com.icloud.common.util.StringUtil;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmDistributionPoint;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;


public class SessionUtils {

    /**
     * 获取缓存用户
     * @param request
     * @return
     */
    public static UserDTO getUserDTO(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("accessToken");
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            StringRedisTemplate userRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
            Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            return JSONObject.parseObject(sessuser.toString(),UserDTO.class);
        }catch (Exception e){
            e.printStackTrace();
        }
       return null;
    }

    /**
     * 设置用户缓存
     * @param request
     * @return
     */
    public static UserDTO setUserDTO(HttpServletRequest request,UserDTO user) {
        try {
            String accessToken = request.getHeader("accessToken");
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            StringRedisTemplate userRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
            Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(user));
            return user;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取缓存用户配送点
     * @param request
     * @return
     */
    public static LmDistributionPoint getLmDistributionPoint(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("accessToken");
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            StringRedisTemplate userRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
            Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
            if(!StringUtil.checkStr(accessToken)){
                return null;
            }
            UserDTO user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
            if(user!=null){
                return user.getDistrpoint();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取可配送商户id，用于查询可配送商户对应的商品
     * 1、从用户缓存中取出可配送点并获取商户Id
     * 2、用户没登陆 或者 没用可用配送点，默认读取商户id为1 商品
     * @param request
     * @return
     */
    public static Long getSupplierId(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("accessToken");
            if(!StringUtil.checkStr(accessToken)){
                return 1L;
            }
            StringRedisTemplate userRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
            Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
            if(!StringUtil.checkStr(accessToken)){
                return 1L;
            }
            UserDTO user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
            if(user!=null){
                return user.getDistrpoint()!=null?user.getDistrpoint().getSupplierId():1L;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 1L;
    }

}