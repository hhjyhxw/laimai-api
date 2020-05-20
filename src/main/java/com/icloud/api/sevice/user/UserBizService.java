package com.icloud.api.sevice.user;

import com.alibaba.fastjson.JSONObject;
import com.icloud.config.global.MyPropertitys;
import com.icloud.modules.lm.componts.CacheComponent;
import com.icloud.wx.WeChatCommonTemplateMessageModel;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by rize on 2019/9/12.
 */
@Service
public class UserBizService {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private static final String CA_OFFICIAL_WECHAT_ACCESS = "CA_OFFICIAL_WECHAT_ACCESS";

    private static final String CA_OFFICIAL_WECHAT_TICKET = "CA_OFFICIAL_WECHAT_TICKET";

    private static final String CA_MINI_WECHAT_ACCESS = "CA_MINI_WECHAT_ACCESS";

    @Autowired
    private MyPropertitys myPropertitys;

    private String wxH5Appid = myPropertitys.getWx().getH5().getAppid();

    private String wxH5Secret = myPropertitys.getWx().getH5().getAppsecret();

    private String wxMiniAppid = myPropertitys.getWx().getMini().getAppid();

    private String wxMiniSecret = myPropertitys.getWx().getMini().getAppsecret();

    @Autowired
    private CacheComponent cacheComponent;

//    @Autowired
//    private UserFormIdMapper userFormIdMapper;

    private static final Logger logger = LoggerFactory.getLogger(UserBizService.class);

    public String getWxH5AccessToken() throws Exception {
        String wxAccessToken = cacheComponent.getRaw(CA_OFFICIAL_WECHAT_ACCESS);
        if (StringUtils.isEmpty(wxAccessToken)) {
            //尝试获取微信公众号Token
            String accessJson = okHttpClient.newCall(
                    new Request.Builder()
                            .url("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxH5Appid + "&secret=" + wxH5Secret)
                            .get()
                            .build()).execute().body().string();
            JSONObject jsonObject = JSONObject.parseObject(accessJson);
            wxAccessToken = jsonObject.getString("access_token");
            if (!StringUtils.isEmpty(wxAccessToken)) {
                Integer expires_in = jsonObject.getInteger("expires_in");
                //在过期前重置
                Integer cacheExpireSec = expires_in * 4 / 5;
                cacheComponent.putRaw(CA_OFFICIAL_WECHAT_ACCESS, wxAccessToken, cacheExpireSec);
            } else {
                throw new RuntimeException("回复错误:" + accessJson);
            }
        }
        return wxAccessToken;
    }

    public String getWxH5Ticket(String accessToken) throws Exception {
        String wxTicket = cacheComponent.getRaw(CA_OFFICIAL_WECHAT_TICKET);
        if (StringUtils.isEmpty(wxTicket)) {
            //尝试获取微信公众号Ticket
            String ticketJson = okHttpClient.newCall(
                    new Request.Builder()
                            .url("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi")
                            .get()
                            .build()).execute().body().string();
            JSONObject jsonObject = JSONObject.parseObject(ticketJson);
            wxTicket = jsonObject.getString("ticket");
            if (!StringUtils.isEmpty(wxTicket)) {
                Integer expires_in = jsonObject.getInteger("expires_in");
                //在过期前重置
                Integer cacheExpireSec = expires_in * 4 / 5;
                cacheComponent.putRaw(CA_OFFICIAL_WECHAT_TICKET, wxTicket, cacheExpireSec);
            } else {
                throw new RuntimeException("回复错误:" + ticketJson);
            }
        }
        return wxTicket;
    }

    public String getWxMiniAccessToken() throws Exception {
        String access_token = cacheComponent.getRaw(CA_MINI_WECHAT_ACCESS);
        if (StringUtils.isEmpty(access_token)) {
            String accessJson = okHttpClient.newCall(
                    new Request.Builder()
                            .url("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + wxMiniAppid + "&secret=" + wxMiniSecret)
                            .get()
                            .build()).execute().body().string();
            JSONObject jsonObject = JSONObject.parseObject(accessJson);
            access_token = jsonObject.getString("access_token");
            if (!StringUtils.isEmpty(access_token)) {
                Integer expires_in = jsonObject.getInteger("expires_in");
                Integer cacheExpireSec = expires_in * 4 / 5;
                cacheComponent.putRaw(CA_MINI_WECHAT_ACCESS, access_token, cacheExpireSec);
            } else {
                throw new RuntimeException("回复错误:" + accessJson);
            }
        }
        return access_token;
    }

//    public void setValidFormId(UserFormIdDO userFormIdDO) {
//        if (!userFormIdDO.getFormId().contains("mock")) {
//            if (!StringUtils.isEmpty(userFormIdDO.getOpenid())) {
//                userFormIdMapper.insert(userFormIdDO);
//            } else {
//                logger.info("[传入openid为空]" + JSONObject.toJSONString(userFormIdDO));
//            }
//        }
//    }
//
//    public UserFormIdDO getValidFormIdByUserId(Long userId) {
//        List<UserFormIdDO> userFormDOS = userFormIdMapper.selectList(
//                new EntityWrapper<UserFormIdDO>()
//                        .eq("user_id", userId)
//                        .gt("gmt_create", new Date(System.currentTimeMillis() - (1000l * 60 * 60 * 24 * 7 - 1000l * 60 * 30))));
//        if (CollectionUtils.isEmpty(userFormDOS)) {
//            return null;
//        }
//        UserFormIdDO userFormDO = userFormDOS.get(0);
//        userFormIdMapper.deleteById(userFormDO.getId());
//        return userFormDO;
//    }

    /**
     * 抽取 小程序模板消息 公众号模板消息共同代码
     *
     * @param model
     * @param url
     * @throws Exception
     */
    private int wechatCommonTemplateMessage(WeChatCommonTemplateMessageModel model, String url) throws Exception {
        String modelJson = JSONObject.toJSONString(model);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, modelJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String res = okHttpClient.newCall(request).execute().body().string();
        JSONObject jsonObject = JSONObject.parseObject(res);
        Integer errcode = jsonObject.getInteger("errcode");
        if (errcode != 0) {
            logger.error("[模板消息回复] 错误，请求报文=" + modelJson);
            logger.error("[模板消息回复] 错误，回复报文=" + res);
        }
        return errcode;
    }
}
