package com.icloud.api.web.notify;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.icloud.annotation.AuthIgnore;
import com.icloud.api.web.notify.service.WxNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rize on 2019/7/10.
 */
@RestController
@RequestMapping("/api/cb")
public class CallbackController {

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @Autowired
    private WxNotifyService wxNotifyService;
    @Autowired
    private WxPayService wxPayService;

    @AuthIgnore
    @RequestMapping("/wxpay")
    public Object wxpay(@RequestBody String body) throws Exception {
        WxPayOrderNotifyResult result = null;
        try {
            result = wxPayService.parseOrderNotifyResult(body);
        } catch (WxPayException e) {
            logger.error("[微信解析回调请求] 异常", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
        logger.info("处理微信支付平台的订单支付");
        logger.info(JSONObject.toJSONString(result));
        return wxNotifyService.wxpaynotify(result);
    }

}
