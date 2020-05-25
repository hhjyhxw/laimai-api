package com.icloud.api.web.notify.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.icloud.api.sevice.notify.AdminNotifyBizService;
import com.icloud.api.sevice.order.OrderBizService;
import com.icloud.api.sevice.user.UserBizService;
import com.icloud.modules.lm.entity.LmOrder;
import com.icloud.modules.lm.entity.LmOrderSku;
import com.icloud.modules.lm.enums.OrderStatusType;
import com.icloud.modules.lm.service.LmGroupShopService;
import com.icloud.modules.lm.service.LmOrderService;
import com.icloud.modules.lm.service.LmOrderSkuService;
import com.icloud.modules.lm.service.LmSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WxNotifyService {

    @Autowired
    private OrderBizService orderBizService;

    @Autowired
    private UserBizService userBizService;

    @Autowired
    private LmSpuService lmSpuService;

    @Autowired
    private LmOrderSkuService lmOrderSkuService;

    @Autowired
    private LmGroupShopService lmGroupShopService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private LmOrderService lmOrderService;


    @Autowired
    private AdminNotifyBizService adminNotifyBizService;


    public Object wxpaynotify(WxPayOrderNotifyResult result) throws Exception {

        String orderNo = result.getOutTradeNo();
        String payId = result.getTransactionId();


        List<LmOrder> orderDOList = lmOrderService.list(
                new QueryWrapper<LmOrder>()
                        .eq("order_no", orderNo));

        if (CollectionUtils.isEmpty(orderDOList)) {
            return WxPayNotifyResponse.fail("订单不存在 orderNo=" + orderNo);
        }
        LmOrder order = orderDOList.get(0);

        // 检查这个订单是否已经处理过
        if (Integer.parseInt(order.getStatus()) != OrderStatusType.UNPAY.getCode()) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        Integer totalFee = result.getTotalFee();

        // 检查支付订单金额
        if (!totalFee.equals(order.getActualPrice())) {
            return WxPayNotifyResponse.fail(order.getOrderNo() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        //**************** 在此之前都没有 数据库修改 操作 所以前面是直接返回错误的 **********************//

        LmOrder updateOrderDO = new LmOrder();
        updateOrderDO.setPayId(payId);
        updateOrderDO.setPayChannel("WX");
        updateOrderDO.setPayPrice(result.getTotalFee());
        updateOrderDO.setPayTime(new Date());
        updateOrderDO.setUpdatedTime(order.getUpdatedTime());
        if (order.getGroupShopId() != null) {
            updateOrderDO.setStatus(String.valueOf(OrderStatusType.GROUP_SHOP_WAIT.getCode()));
        } else {
            updateOrderDO.setStatus(String.valueOf(OrderStatusType.WAIT_STOCK.getCode()));
        }
        orderBizService.changeOrderStatus(orderNo, String.valueOf(OrderStatusType.UNPAY.getCode()), updateOrderDO);
        List<LmOrderSku> orderSkuDOList = lmOrderSkuService.list(
                new QueryWrapper<LmOrderSku>()
                        .eq("order_no", orderNo));
        orderSkuDOList.forEach(item -> {
            //增加销量
            lmSpuService.incSales(item.getSpuId(), item.getNum());
            if (order.getGroupShopId() != null) {
                //增加团购人数, 若想算商品数这里就获取orderSku的数量，若想算人数，这里就写1
                lmGroupShopService.incCurrentNum(order.getGroupShopId(), item.getNum());
            }
        });

//        OrderDTO orderDTO = new OrderDTO();
//        BeanUtils.copyProperties(order, orderDTO);
//        orderDTO.setPayChannel(updateOrderDO.getPayChannel());
//        orderDTO.setSkuList(orderSkuDOList);
//
//        List<IPluginPaySuccess> plugins = pluginsManager.getPlugins(IPluginPaySuccess.class);
//        if (!CollectionUtils.isEmpty(plugins)) {
//            String formId = userBizService.getValidFormIdByUserId(orderDTO.getUserId()).getFormId();
//            for (IPluginPaySuccess paySuccess : plugins) {
//                orderDTO = paySuccess.invoke(orderDTO, formId);
//            }
//        }
//        //通知管理员发货
//        OrderDTO finalOrderDTO = orderDTO;
//        GlobalExecutor.execute(() -> {
//            adminNotifyBizService.newOrder(finalOrderDTO);
//        });
        return WxPayNotifyResponse.success("支付成功");
    }

}
