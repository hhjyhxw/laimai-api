package com.icloud.api.web.user;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.icloud.annotation.AuthIgnore;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.user.UserBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.util.GeneratorUtil;
import com.icloud.basecommon.util.SHAUtil;
import com.icloud.common.IpUtil;
import com.icloud.config.global.MyPropertitys;
import com.icloud.config.notify.SMSClient;
import com.icloud.config.notify.SMSResult;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.modules.lm.conts.Const;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.entity.LmUser;
import com.icloud.modules.lm.enums.UserLoginType;
import com.icloud.modules.lm.service.LmUserService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/user")
public class UserController {

    private static final String VERIFY_CODE_PREFIX = "VERIFY_CODE_";

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private MyPropertitys myPropertitys;
    @Autowired
    private LmUserService lmUserService;

    @Autowired
    private SMSClient smsClient;

    @Autowired
    private CacheComponent cacheComponent;

    @Autowired
    private UserBizService userBizService;

    @Autowired
    private StringRedisTemplate userRedisTemplate;

    @Autowired
    private HttpServletRequest request;

    private OkHttpClient okHttpClient = new OkHttpClient();

//    @Value("${com.iotechn.unimall.wx.mini.app-id}")
//    private String wxMiNiAppid;
//
//    @Value("${com.iotechn.unimall.wx.mini.app-secret}")
//    private String wxMiNiSecret;
//
//    @Value("${com.iotechn.unimall.wx.app.app-id}")
//    private String wxAppAppid;
//
//    @Value("${com.iotechn.unimall.wx.app.app-secret}")
//    private String wxAppSecret;
//
//    @Value("${com.iotechn.unimall.wx.h5.app-id}")
//    private String wxH5Appid;
//
//    @Value("${com.iotechn.unimall.wx.h5.app-secret}")
//    private String wxH5Secret;

    /**
     * 发送验证码
     * @param phone
     * @return
     * @throws ApiException
     */
    @RequestMapping("/sendVerifyCode")
    @ResponseBody
    @AuthIgnore
    public ApiResponse sendVerifyCode(String phone) throws ApiException {
        String verifyCode = GeneratorUtil.genSixVerifyCode();
        SMSResult res = smsClient.sendRegisterVerify(phone, verifyCode);
        if (res.isSucc()) {
            cacheComponent.putRaw(VERIFY_CODE_PREFIX + phone, verifyCode, 300);
            return new ApiResponse().ok("ok");
        } else {
            throw new ApiException(res.getMsg());
        }

    }

    @RequestMapping("/register")
    @ResponseBody
    @AuthIgnore
    public ApiResponse register(String phone, String password, String verifyCode, String ip) throws ApiException {
        logger.info("register:phone=="+phone+";password=="+password+";verifyCode=="+verifyCode+";ip=="+ip);
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) ||  StringUtils.isEmpty(verifyCode)  ) {
            throw new ApiException("参数不正确");
        }
        //1.校验验证码
        checkVerifyCode(phone, verifyCode);
        //2.校验用户是否存在
        Integer count = lmUserService.count(  new QueryWrapper<LmUser>().eq("phone", phone));
        if (count > 0) {
            throw new ApiException("该手机号码已注册");
        }
        //3.校验成功，注册用户
        Date now = new Date();
        LmUser userDO = new LmUser();
        userDO.setPhone(phone);
        userDO.setPassword(Md5Crypt.md5Crypt(password.getBytes(), "$1$" + phone.substring(0, 7)));
        userDO.setLastLoginIp(ip);
        userDO.setLastLoginTime(now);
        userDO.setCreatedTime(now);
        userDO.setUpdatedTime(now);
        userDO.setLoginType(String.valueOf(UserLoginType.REGISTER.getCode()));
        lmUserService.save(userDO);
        //返回用户DTO
        cacheComponent.del(VERIFY_CODE_PREFIX + phone);
        return new ApiResponse().ok(userDO);
    }

    @RequestMapping("/bindPhone")
    @ResponseBody
    public ApiResponse bindPhone(String phone, String password, String verifyCode, @LoginUser UserDTO user) throws ApiException {
        logger.info("bindPhone:phone=="+phone+";password=="+password+";verifyCode=="+verifyCode);
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) ||  StringUtils.isEmpty(verifyCode)  ) {
            throw new ApiException("参数不正确");
        }
        //1.校验验证码
        checkVerifyCode(phone, verifyCode);
        //2.校验用户是否存在
        Integer count = lmUserService.count(new QueryWrapper<LmUser>().eq("phone", phone));
        if (count > 0) {
            throw new ApiException("该手机号码已绑定");
        }
        //3.校验成功，绑定手机
        LmUser updateUserDO = new LmUser();
        updateUserDO.setId(user.getId());
        updateUserDO.setPhone(phone);
        updateUserDO.setUpdatedTime(new Date());
        if (lmUserService.updateById(updateUserDO)) {
            cacheComponent.del(VERIFY_CODE_PREFIX + phone);
            return new ApiResponse().ok("ok");
        }
        throw new ApiException("绑定手机号异常");
    }

    @RequestMapping("/resetPassword")
    @ResponseBody
    public ApiResponse resetPassword(String phone, String password, String verifyCode,@LoginUser UserDTO user) throws ApiException {
        logger.info("resetPassword:phone=="+phone+";password=="+password+";verifyCode=="+verifyCode);
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) ||  StringUtils.isEmpty(verifyCode)  ) {
            throw new ApiException("参数不正确");
        }
        //1.校验验证码
        checkVerifyCode(phone, verifyCode);
        //2.校验用户是否存在
        List<LmUser> targetUserList = lmUserService.list(
                new QueryWrapper<LmUser>() .eq("phone", phone));
        if (CollectionUtils.isEmpty(targetUserList)) {
            throw new ApiException("该手机用户不存在");
        }
        Long id = targetUserList.get(0).getId();
        //3.校验成功，重置密码
        LmUser updateUserDO = new LmUser();
        updateUserDO.setId(id);
        updateUserDO.setPassword(Md5Crypt.md5Crypt(password.getBytes(), "$1$" + phone.substring(0, 7)));
        updateUserDO.setUpdatedTime(new Date());
        if (lmUserService.updateById(updateUserDO)) {
            cacheComponent.del(VERIFY_CODE_PREFIX + phone);
            return new ApiResponse().ok("ok");
        }
        throw new ApiException("重置密码失败");
    }

    /**
     * 验证码抽取校验
     *
     * @param phone
     * @param verifyCode
     * @throws ApiException
     */
    private void checkVerifyCode(String phone, String verifyCode) throws ApiException {
        String raw = cacheComponent.getRaw(VERIFY_CODE_PREFIX + phone);
        if (StringUtils.isEmpty(raw)) {
            throw new ApiException("验证码不存在");
        }
        if (!raw.equals(verifyCode)) {
            throw new ApiException("验证码不正确");
        }
    }

    @RequestMapping("/login")
    @ResponseBody
    @AuthIgnore
    public ApiResponse login(String phone, String password, String loginType, String raw, String ip) throws ApiException {
        logger.info("login:phone=="+phone+";password=="+password+";loginType=="+loginType);
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || loginType==null) {
            throw new ApiException("参数不正确");
        }
        String cryptPassword = Md5Crypt.md5Crypt(password.getBytes(), "$1$" + phone.substring(0, 7));
        UserDTO userDTO = lmUserService.login(phone, cryptPassword);
        if (userDTO == null) {
            throw new ApiException("无法找到用户");
        }
        //检查帐号是否已经冻结
        if ("0".equals(userDTO.getStatus())) {
            throw new ApiException("账号已冻结");
        }
        if (!StringUtils.isEmpty(raw) && UserLoginType.contains(Integer.parseInt(loginType))) {
            if (Integer.parseInt(loginType) == UserLoginType.MP_WEIXIN.getCode()) {
                try {
                    JSONObject thirdPartJsonObject = JSONObject.parseObject(raw);
                    String code = thirdPartJsonObject.getString("code");
                    String body = okHttpClient.newCall(new Request.Builder()
                            .url("https://api.weixin.qq.com/sns/jscode2session?appid=" + (UserLoginType.MP_WEIXIN.getCode() == Integer.parseInt(loginType) ? myPropertitys.getWx().getMini().getAppid() : myPropertitys.getWx().getApp().getAppid()) +
                                    "&secret=" + (UserLoginType.MP_WEIXIN.getCode() == Integer.parseInt(loginType) ? myPropertitys.getWx().getMini().getAppsecret() : myPropertitys.getWx().getApp().getAppsecret()) +
                                    "&grant_type=authorization_code&js_code=" + code).get().build()).execute().body().string();
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    Integer errcode = jsonObject.getInteger("errcode");
                    if (errcode == null || errcode == 0) {
                        String miniOpenId = jsonObject.getString("openid");
                        //将此次登录的openId，暂且放入user的域里面，支付的时候会用到
                        userDTO.setLoginType(loginType);
                        userDTO.setOpenId(miniOpenId);
                    }
                } catch (Exception e) {
                    logger.error("[微信第三方登录] 异常", e);
                    throw new ApiException("[微信第三方登录] 异常");
                }
            }
        }
        String accessToken = GeneratorUtil.genSessionId();
        //放入SESSION专用Redis数据源中
        userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(userDTO));
        userDTO.setAccessToken(accessToken);
        LmUser userUpdateDO = new LmUser();
        userUpdateDO.setId(userDTO.getId());
        userUpdateDO.setLastLoginTime(new Date());
        userUpdateDO.setLastLoginIp(ip);
        lmUserService.updateById(userUpdateDO);
        return new ApiResponse().ok(userDTO);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ApiResponse logout(String accessToken, @LoginUser UserDTO user) throws ApiException {
        userRedisTemplate.delete(accessToken);
        return new ApiResponse().ok("ok");
    }

    @RequestMapping("/thirdPartLogin")
    @ResponseBody
    @AuthIgnore
    public ApiResponse thirdPartLogin(Integer loginType, String ip, String raw) throws ApiException {
        try {
            logger.info("thirdPartLogin:loginType=="+loginType+";ip=="+ip+";raw=="+raw);
            if (UserLoginType.MP_WEIXIN.getCode() == loginType) {
                return new ApiResponse().ok(wechatLogin(loginType, ip, raw));
            } else if (UserLoginType.H5_WEIXIN.getCode() == loginType) {
                //H5 微信公众号网页登录
                String json = okHttpClient.newCall(
                        new Request.Builder().url("https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + myPropertitys.getWx().getH5().getAppid() + "&secret=" + myPropertitys.getWx().getH5().getAppsecret() + "&code=" + raw + "&grant_type=authorization_code").build()).execute().body().string();
                JSONObject jsonObject = JSONObject.parseObject(json);
                Integer errcode = jsonObject.getInteger("errcode");
                if (errcode == null || errcode == 0) {
                    String openid = jsonObject.getString("openid");
                    List<LmUser> userDOS = lmUserService.list(new QueryWrapper<LmUser>().eq("open_id", openid).eq("login_type", String.valueOf(loginType)));
                    if (!CollectionUtils.isEmpty(userDOS)) {
                        //若用户已经注册，则直接返回用户
                        String accessToken = GeneratorUtil.genSessionId();
                        UserDTO userDTO = new UserDTO();
                        BeanUtils.copyProperties(userDOS.get(0), userDTO);
                        userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(userDTO));
                        userDTO.setAccessToken(accessToken);
                        return new ApiResponse().ok(userDTO);
                    } else {
                        String userAccessToken = jsonObject.getString("access_token");
                        //通过用户AccessToken换取用户信息
                        String userInfoJson = okHttpClient.newCall(
                                new Request.Builder().url("https://api.weixin.qq.com/sns/userinfo?access_token="
                                        + userAccessToken + "&openid=" + openid + "&lang=zh_CN").build()).execute().body().string();
                        JSONObject userInfoJsonObject = JSONObject.parseObject(userInfoJson);
                        Date now = new Date();
                        LmUser newUserDO = new LmUser();
                        newUserDO.setLoginType(String.valueOf(loginType));
                        newUserDO.setNickName(userInfoJsonObject.getString("nickname"));
                        newUserDO.setAvatarUrl(userInfoJsonObject.getString("headimgurl"));
                        newUserDO.setGender(String.valueOf(userInfoJsonObject.getInteger("sex")));
                        newUserDO.setOpenId(openid);
                        newUserDO.setLastLoginIp(ip);
                        newUserDO.setLastLoginTime(now);
                        newUserDO.setCreatedTime(now);
                        newUserDO.setUpdatedTime(now);
                        newUserDO.setLevel("0");
                        lmUserService.save(newUserDO);
                        //这一步是为了封装上数据库上配置的默认值
                        LmUser userDO = (LmUser) lmUserService.getById(newUserDO.getId());
                        String accessToken = GeneratorUtil.genSessionId();
                        UserDTO userDTO = new UserDTO();
                        BeanUtils.copyProperties(userDO, userDTO);
                        userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(userDTO));
                        userDTO.setAccessToken(accessToken);
                        return new ApiResponse().ok(userDTO);
                    }
                } else {
                    throw new ApiException("H5微信登录");
                }
            } else if (UserLoginType.APP_WEIXIN.getCode() == loginType) {
                //return wechatLogin(loginType, ip, raw);
                //UNI-APP 的 微信APP登录 APPSecret是保存在前端的。这点非常不安全。但是用了他的框架，也没有办法
                JSONObject jsonObject = JSONObject.parseObject(raw);
                JSONObject authResult = jsonObject.getJSONObject("authResult");
                String openid = authResult.getString("openid");
//                String openid = "osTQe6L_JOoJkrMO1vAT_peMivjA";
                List<LmUser> userDOS = lmUserService.list(new QueryWrapper<LmUser>().eq("open_id", openid).eq("login_type", String.valueOf(loginType)));
                LmUser userDO;
                if (CollectionUtils.isEmpty(userDOS)) {
                    //创建新用户
                    Date now = new Date();
                    LmUser newUserDO = new LmUser();
                    newUserDO.setLoginType(String.valueOf(loginType));
                    newUserDO.setOpenId(openid);
                    newUserDO.setLastLoginIp(ip);
                    newUserDO.setLastLoginTime(now);
                    newUserDO.setCreatedTime(now);
                    newUserDO.setUpdatedTime(now);
                    newUserDO.setLevel("0");
                    lmUserService.save(newUserDO);
                    //这一步是为了封装上数据库上配置的默认值
                    userDO = (LmUser) lmUserService.getById(newUserDO.getId());
                } else {
                    userDO = userDOS.get(0);
                    LmUser userUpdateDO = new LmUser();
                    userUpdateDO.setId(userDO.getId());
                    userUpdateDO.setLastLoginTime(new Date());
                    userUpdateDO.setLastLoginIp(ip);
                    lmUserService.updateById(userUpdateDO);
                }
                //检查帐号是否已经冻结
                if (Integer.parseInt(userDO.getStatus()) == 0) {
                    throw new ApiException("用户已被冻结");
                }
                String accessToken = GeneratorUtil.genSessionId();
                UserDTO userDTO = new UserDTO();
                BeanUtils.copyProperties(userDO, userDTO);
                userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(userDTO));
                userDTO.setAccessToken(accessToken);
                return new ApiResponse().ok(userDTO);
            } else {
                throw new ApiException("不支持的登陆方式");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[用户第三方登录] 异常", e);
            throw new ApiException("[用户第三方登录] 异常");
        }
    }

    /**
     * 微信第三方登录 抽取接口(目前只有小程序)
     *
     * @param loginType
     * @param ip
     * @param raw
     * @return
     * @throws Exception
     */
    private UserDTO wechatLogin(Integer loginType, String ip, String raw) throws ApiException, IOException {
        //微信第三方登录
        JSONObject thirdPartJsonObject = JSONObject.parseObject(raw);
        String code = thirdPartJsonObject.getString("code");
        String body = okHttpClient.newCall(new Request.Builder()
                .url("https://api.weixin.qq.com/sns/jscode2session?appid=" + (UserLoginType.MP_WEIXIN.getCode() == loginType ? myPropertitys.getWx().getMini().getAppid() : myPropertitys.getWx().getApp().getAppid()) +
                        "&secret=" + (UserLoginType.MP_WEIXIN.getCode() == loginType ? myPropertitys.getWx().getMini().getAppsecret() : myPropertitys.getWx().getApp().getAppsecret()) +
                        "&grant_type=authorization_code&js_code=" + code).get().build()).execute().body().string();
        JSONObject jsonObject = JSONObject.parseObject(body);
        Integer errcode = jsonObject.getInteger("errcode");
        if (errcode == null || errcode == 0) {
            String miniOpenId = jsonObject.getString("openid");
            List<LmUser> userDOS = lmUserService.list(new QueryWrapper<LmUser>().eq("open_id", miniOpenId).eq("login_type", String.valueOf(loginType)));
            LmUser userDO;
            if (CollectionUtils.isEmpty(userDOS)) {
                //若用户为空，则注册此用户
                Date now = new Date();
                LmUser newUserDO = new LmUser();
                newUserDO.setLoginType(String.valueOf(loginType));
                newUserDO.setOpenId(miniOpenId);
                newUserDO.setLastLoginIp(ip);
                newUserDO.setLastLoginTime(now);
                newUserDO.setCreatedTime(now);
                newUserDO.setUpdatedTime(now);
                newUserDO.setStatus("1");
                newUserDO.setLevel("0");
                lmUserService.save(newUserDO);
                //这一步是为了封装上数据库上配置的默认值
                userDO = (LmUser) lmUserService.getById(newUserDO.getId());
            } else {
                userDO = userDOS.get(0);
                LmUser userUpdateDO = new LmUser();
                userUpdateDO.setId(userDO.getId());
                userUpdateDO.setLastLoginTime(new Date());
                userUpdateDO.setLastLoginIp(IpUtil.getIpAddr(request));
                lmUserService.updateById(userUpdateDO);
            }
            //检查帐号是否已经冻结
            if (Integer.parseInt(userDO.getStatus()) == 0) {
                throw new ApiException("用户已被冻结");
            }
            String accessToken = GeneratorUtil.genSessionId();
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDO, userDTO);
            userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(userDTO));
            userDTO.setAccessToken(accessToken);
            return userDTO;
        } else {
            logger.info("[微信登录] 回复失败 回复报文：" + body);
            throw new ApiException("小程序登陆失败");
        }

    }

    /**
     * 更新用户信息
     * @param nickName
     * @param nickname
     * @param avatarUrl
     * @param gender
     * @param birthday
     * @param accessToken
     * @return
     * @throws ApiException
     */
    @RequestMapping("/syncUserInfo")
    @ResponseBody
    public ApiResponse syncUserInfo(String nickName, String nickname, String avatarUrl, String gender, Long birthday, String accessToken, @LoginUser UserDTO user) throws ApiException {
        LmUser updateUserDO = new LmUser();
        updateUserDO.setId(user.getId());
        updateUserDO.setNickName(StringUtils.isEmpty(nickName) ? nickname: nickName);
        updateUserDO.setAvatarUrl(avatarUrl);
        updateUserDO.setGender(String.valueOf(gender));
        updateUserDO.setUpdatedTime(new Date());
        if (birthday != null)
            updateUserDO.setBirthday(new Date(birthday));
        if (lmUserService.updateById(updateUserDO)) {
            //更新SESSION缓存
//            UserDTO user = SessionUtil.getUser();
//            Object sessuser = userRedisTemplate.opsForValue().get(Const.USER_REDIS_PREFIX + accessToken);
//            user = JSONObject.parseObject(sessuser.toString(),UserDTO.class);
            if (!StringUtils.isEmpty(nickName)) {
                user.setNickName(nickName);
            }
            if (!StringUtils.isEmpty(avatarUrl)) {
                user.setAvatarUrl(avatarUrl);
            }
            if (birthday != null) {
                user.setBirthday(new Date(birthday));
            }
            if (gender != null) {
                user.setGender(gender);
            }
            userRedisTemplate.opsForValue().set(Const.USER_REDIS_PREFIX + accessToken, JSONObject.toJSONString(user));
            return new ApiResponse().ok("ok");
        }
        throw new ApiException("更新用户信息失败");
    }

    @RequestMapping("/getH5Sign")
    @ResponseBody
    public ApiResponse getH5Sign(String url) throws ApiException {
        try {
            String wxH5AccessToken = userBizService.getWxH5AccessToken();
            //我也不知道为什么微信这里要换两次
            String wxH5Ticket = userBizService.getWxH5Ticket(wxH5AccessToken);
            String noncestr = GeneratorUtil.genUUId();
            long timestamp = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append("jsapi_ticket=");
            sb.append(wxH5Ticket);
            sb.append("&noncestr=");
            sb.append(noncestr);
            sb.append("&timestamp=");
            sb.append(timestamp);
            sb.append("&url=");
            sb.append(url);
            //明文
            String content = sb.toString();
            String signature = SHAUtil.shaEncode(content);
            Map<String, Object> obj = new HashMap<>();
            obj.put("noncestr", noncestr);
            obj.put("timestamp", timestamp);
            obj.put("sign", signature);
            return new ApiResponse().ok(obj);
        } catch (Exception e) {
            logger.info("[获取H5签名] 异常", e);
            throw new ApiException("获取H5签名");
        }
    }




}
