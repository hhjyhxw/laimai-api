package com.icloud.api.sevice.order;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.icloud.config.global.MyPropertitys;
import com.icloud.exceptions.AdminServiceException;
import com.icloud.exceptions.ApiException;
import com.icloud.exceptions.ServiceException;
import com.icloud.modules.lm.componts.LockComponent;
import com.icloud.modules.lm.dao.LmOrderMapper;
import com.icloud.modules.lm.dao.LmOrderSkuMapper;
import com.icloud.modules.lm.dao.LmUserMapper;
import com.icloud.modules.lm.dto.order.OrderDTO;
import com.icloud.modules.lm.entity.LmOrder;
import com.icloud.modules.lm.entity.LmOrderSku;
import com.icloud.modules.lm.entity.LmUser;
import com.icloud.modules.lm.enums.OrderStatusType;
import com.icloud.modules.lm.enums.UserLoginType;
import com.icloud.modules.lm.service.LmOrderSkuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by rize on 2019/7/10.
 */
@Service
@Transactional
public class OrderBizService {

    private static final String ORDER_STATUS_LOCK = "ORDER_STATUS_LOCK_";

    //订单退款乐观锁
    public static final String ORDER_REFUND_LOCK = "ORDER_REFUND_LOCK_";

    private static final Logger logger = LoggerFactory.getLogger(OrderBizService.class);

    @Autowired
    private MyPropertitys myPropertitys;

    @Autowired
    private LockComponent lockComponent;

    @Autowired
    private LmOrderMapper orderMapper;

    @Autowired
    private LmOrderSkuMapper orderSkuMapper;

    @Autowired
    private LmOrderSkuService lmOrderSkuService;

    @Autowired
    private LmUserMapper userMapper;



    @Autowired
    private WxPayService wxPayService;

//    private String wxMiNiAppid = myPropertitys.getWx().getMini().getAppid();
//
//    private String wxAppAppid = myPropertitys.getWx().getApp().getAppid();

    public boolean changeOrderStatus(String orderNo, int nowStatus, LmOrder orderDO) throws ApiException {
        try {
            // 防止传入值为空,导致其余订单被改变
            if(orderNo == null || orderDO == null){
                throw new ApiException("修改订单状态:订单编号"+orderNo+",查询不到订单");
            }

            if (lockComponent.tryLock(ORDER_STATUS_LOCK + orderNo, 30)) {
                if (orderMapper.update(orderDO,
                        new UpdateWrapper<LmOrder>()
                                .eq("order_no", orderNo)
                                .eq("status", nowStatus)) > 0) {
                    return true;
                }
                throw new ApiException("修改订单状态失败");
            } else {
                throw new ApiException("获取订单锁失败");
            }
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[订单状态扭转] 异常", e);
            throw new ApiException("[订单状态扭转] 异常");
        } finally {
            lockComponent.release(ORDER_STATUS_LOCK + orderNo);
        }
    }

    public LmOrder checkOrderExist(String orderNo, Long userId) throws ApiException {
        QueryWrapper<LmOrder> wrapper = new QueryWrapper<LmOrder>().eq("order_no", orderNo);
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        List<LmOrder> orderDOS = orderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderDOS)) {
            throw new ApiException("订单不存在");
        }
        return orderDOS.get(0);
    }

    public OrderDTO getOrderDetail(Long orderId, Long userId) throws Exception {
        QueryWrapper<LmOrder> wrapper = new QueryWrapper<LmOrder>()
                .eq("id", orderId);
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        List<LmOrder> orderDOS = orderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(orderDOS)) {
            throw new ApiException("订单不存在");
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderDOS.get(0), orderDTO);
        orderDTO.setSkuList(lmOrderSkuService.list(new QueryWrapper<LmOrderSku>().eq("order_id", orderId)));
        return orderDTO;
    }

//    @Transactional(rollbackFor = Exception.class) 外面加了事务
    public String groupShopStatusRefund(String orderNo) throws ServiceException {
        if (lockComponent.tryLock(OrderBizService.ORDER_REFUND_LOCK + orderNo, 30)) {
            try {
                //1.校验订单状态是否处于团购状态中
                LmOrder orderDO = checkOrderExist(orderNo, null);
                if (Integer.parseInt(orderDO.getStatus()) != OrderStatusType.GROUP_SHOP_WAIT.getCode()) {
                    throw new ApiException("订单状态不是团购状态");
                }
                //2.退款处理
                //2.1.1 先流转状态
                LmOrder updateOrderDO = new LmOrder();
                updateOrderDO.setStatus(String.valueOf(OrderStatusType.REFUNDED.getCode()));
                updateOrderDO.setUpdatedTime(new Date());
                changeOrderStatus(orderNo, OrderStatusType.GROUP_SHOP_WAIT.getCode(), updateOrderDO);
                Long userId = orderDO.getUserId();
                LmUser userDO = (LmUser)userMapper.selectById(userId);
                int loginType = Integer.parseInt(userDO.getLoginType());
                //2.1.2 向微信支付平台发送退款请求
                WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                wxPayRefundRequest.setAppid(loginType == UserLoginType.MP_WEIXIN.getCode() ? myPropertitys.getWx().getMini().getAppid() : myPropertitys.getWx().getApp().getAppid());
                wxPayRefundRequest.setOutTradeNo(orderNo);//订单号
                wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);//退款单号
                wxPayRefundRequest.setRefundDesc("团购失败退款");
                wxPayRefundRequest.setTotalFee(orderDO.getPayPrice() - orderDO.getFreightPrice());//订单金额-运费金额
                wxPayRefundRequest.setRefundFee(orderDO.getPayPrice() - orderDO.getFreightPrice());//退款金额
                WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
                if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new AdminServiceException(wxPayRefundResult.getReturnMsg(),
                           600);
                }
                if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new AdminServiceException(wxPayRefundResult.getReturnMsg(),
                            600);
                }
                return "ok";
            } catch (ServiceException e) {
                throw e;
            } catch (Exception e) {
                logger.error("[微信退款] 异常", e);
                throw new AdminServiceException("[微信退款] 异常");
            } finally {
                lockComponent.release(OrderBizService.ORDER_REFUND_LOCK + orderNo);
            }
        } else {
            throw new AdminServiceException("微信退款获取锁失败");
        }
    }

}
