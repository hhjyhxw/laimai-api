package com.icloud.config.resolver;

import com.alibaba.fastjson.JSON;
import com.icloud.annotation.LoginUser;
import com.icloud.config.global.Constants;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.enums.UserLoginType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
	protected Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(UserDTO.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        //获取用户ID
        Object object = request.getAttribute(Constants.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if(object == null){
            return null;
        }
        //获取用户信息
        UserDTO user = (UserDTO)object;
        logger.info("user====="+ JSON.toJSONString(user));
        logger.info("======当前登录用户id:{},昵称：{},登陆类型：{}",user.getId(),user.getNickName(),
                "0".equals(user.getLoginType())?UserLoginType.REGISTER.getMsg():("1".equals(user.getLoginType())?UserLoginType.MP_WEIXIN.getMsg():"其他登陆方式"));
        return user;
    }
}
