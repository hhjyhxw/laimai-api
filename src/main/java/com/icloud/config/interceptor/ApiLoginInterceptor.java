package com.icloud.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.icloud.annotation.AuthIgnore;
import com.icloud.basecommon.service.redis.RedisService;
import com.icloud.common.IpUtil;
import com.icloud.config.global.Constants;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.service.LmUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class ApiLoginInterceptor extends HandlerInterceptorAdapter {
    private Logger log = LoggerFactory.getLogger(getClass());
    // @Autowired
    //private TbTokenService tokenService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private LmUserService lmUserService;
    @Autowired
    private StringRedisTemplate userRedisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        printlnVisitInfo(request,handler);

        //从header中获取token
        //1、
        String accessToken = request.getHeader("accessToken");
        log.info("======accessToken:{}",accessToken);

        AuthIgnore annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthIgnore.class);
        }else{
            return true;
        }
        //如果有@IgnoreAuth注解，则不验证token
        if(annotation != null){
            //不是必须登陆页面的 加载登陆用户数据
            if (!StringUtils.isBlank(accessToken)) {
                Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
                if (sessuser!=null) {
                    UserDTO user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
                    if(user!=null){
                        request.setAttribute(Constants.USER_KEY, user);
                    }
                }
            }
            return true;
        }


        //token为空
        if (StringUtils.isBlank(accessToken)) {
            log.info("======accessToken为空，访问失败");
            // throw new RRException(token不能为空);
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":1000,\"message\":\"accessToken为空\"}");
            out.flush();
            out.close();
            return false;
        }

        //2、
//        log.info("redisService===="+redisService);
//        Object user = redisService.get(accessToken);
        Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
        log.info("缓存中获取用户信息====="+sessuser);
        if(sessuser==null){
            log.info("======读取用户登陆缓存为空");
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":1000,\"message\":\"用户没有登录,请先登录！\"}");
            out.flush();
            out.close();
            return false;
        }

        UserDTO user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
        if (user==null) {
            log.info("======user不存在或者已经失效");
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":1000,\"message\":\"用户没有登录,请先登录！\"}");
            out.flush();
            out.close();
            return false;
        }else{
            //用于其他方法获取用户信息
            request.setAttribute(Constants.USER_KEY, user);
//            redisService.set(unionid.toString(), t, LoginUtils.LOGIN_EXPIRY_TIME);  //重新激活登录时间
        }
        log.info("======验证token成功");
        return true;
    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }

    private void printlnVisitInfo(HttpServletRequest request,Object handler) throws IOException {
        // 所有请求第一个进入的方法
        String reqURL = request.getRequestURL().toString();
        String ip = IpUtil.getIpAddr(request);
//        InputStream is = request.getInputStream ();
//        StringBuilder responseStrBuilder = new StringBuilder ();
//        BufferedReader streamReader = new BufferedReader (new InputStreamReader(is,"UTF-8"));
//        String inputStr;
//        while ((inputStr = streamReader.readLine ()) != null)
//            responseStrBuilder.append (inputStr);
//        String parmeter = responseStrBuilder.toString();

        String parmeter = null;

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        if (handler instanceof HandlerMethod) {
            StringBuilder sb = new StringBuilder(1000);
            HandlerMethod h = (HandlerMethod) handler;
            //Controller 的包名
            sb.append("\nController: ").append(h.getBean().getClass().getName()).append("\n");
            //方法名称
            sb.append("Method    : ").append(h.getMethod().getName()).append("\n");
            //请求方式  post\put\get 等等
            sb.append("RequestMethod    : ").append(request.getMethod()).append("\n");
            //所有的请求参数
            sb.append("Params    : ").append(parmeter).append("\n");
            //部分请求链接
            sb.append("URI       : ").append(request.getRequestURI()).append("\n");
            //完整的请求链接
            sb.append("AllURI    : ").append(reqURL).append("\n");
            //请求方的 ip地址
            sb.append("request IP: ").append(ip).append("\n");

            log.info(sb.toString());
        }
    }
}
