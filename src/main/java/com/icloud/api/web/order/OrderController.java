package com.icloud.api.web.order;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.category.CategoryBizService;
import com.icloud.api.sevice.freight.FreightBizService;
import com.icloud.api.sevice.groupshop.GroupShopBizService;
import com.icloud.api.sevice.notify.AdminNotifyBizService;
import com.icloud.api.sevice.order.OrderBizService;
import com.icloud.api.sevice.user.UserBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.util.GeneratorUtil;
import com.icloud.basecommon.util.lang.StringUtils;
import com.icloud.common.IpUtil;
import com.icloud.config.global.MyPropertitys;
import com.icloud.exceptions.ApiException;
import com.icloud.executor.GlobalExecutor;
import com.icloud.modules.lm.componts.LockComponent;
import com.icloud.modules.lm.dto.UserCouponDTO;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.dto.freight.ShipTraceDTO;
import com.icloud.modules.lm.dto.goods.GroupShopDTO;
import com.icloud.modules.lm.dto.goods.SkuDTO;
import com.icloud.modules.lm.dto.order.OrderDTO;
import com.icloud.modules.lm.dto.order.OrderRequestDTO;
import com.icloud.modules.lm.dto.order.OrderRequestSkuDTO;
import com.icloud.modules.lm.entity.*;
import com.icloud.modules.lm.enums.*;
import com.icloud.modules.lm.model.Page;
import com.icloud.modules.lm.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private static final String TAKE_ORDER_LOCK = "TAKE_ORDER_";


    @Autowired
    private MyPropertitys myPropertitys;
    @Autowired
    private LmSkuService lmSkuService;

    @Autowired
    private LmUserCouponService lmUserCouponService;

    @Autowired
    private LmOrderService lmOrderService;

    @Autowired
    private LmOrderSkuService lmOrderSkuService;

    @Autowired
    private LmAddressService lmAddressService;

    @Autowired
    private LmCartService lmCartService;

    @Autowired
    private LmCategoryService lmCategoryService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private LockComponent lockComponent;

    @Autowired
    private OrderBizService orderBizService;

    @Autowired
    private FreightBizService freightBizService;

    @Autowired
    private UserBizService userBizService;

    @Autowired
    private GroupShopBizService groupShopBizService;

    @Autowired
    private AdminNotifyBizService adminNotifyBizService;
    @Autowired
    private CategoryBizService categoryBizService;

//    @Value("${com.iotechn.unimall.machine-no}")
//    private String MACHINE_NO;
//
//    @Value("${com.iotechn.unimall.env}")
//    private String ENV;
//
//    @Value("${com.iotechn.unimall.wx.mini.app-id}")
//    private String wxMiNiAppid;
//
//    @Value("${com.iotechn.unimall.wx.app.app-id}")
//    private String wxAppAppid;
//
//    @Value("${com.iotechn.unimall.wx.h5.app-id}")
//    private String wxH5Appid;


    /**
     * 提交订单
     * @param orderRequest
     * @param channel
     * @return
     * @throws ApiException
     */
    @RequestMapping("/takeOrder")
    @ResponseBody
    public ApiResponse takeOrder(OrderRequestDTO orderRequest, String channel,@LoginUser UserDTO user) throws ApiException {
        if (lockComponent.tryLock(TAKE_ORDER_LOCK + user.getId(), 20)) {
            //加上乐观锁，防止用户重复提交订单
            try {
                //用户会员等级
                Integer userLevel = user.getLevel();
                //参数强校验 START
                List<OrderRequestSkuDTO> skuList = orderRequest.getSkuList();
                if (CollectionUtils.isEmpty(skuList) || orderRequest.getTotalPrice() == null) {
                    throw new ApiException("商品参数为空");
                }
                if (orderRequest.getTotalPrice() <= 0) {
                    throw new ApiException("商品价格参数不能小于0");
                }
                // 若是卖虚拟物品，不需要收货地址，可以将此行注释掉
                if (orderRequest.getAddressId() == null) {
                    throw new ApiException("地址不能为空");
                }
                Long groupShopId = orderRequest.getGroupShopId();
                Integer groupShopPrice = null;
                if (groupShopId != null) {
                    //校验团购参数
                    if (skuList.size() > 1) {
                        throw new ApiException("财团商品不能大于0");
                    }
                    GroupShopDTO groupShopDTO = groupShopBizService.getGroupShopById(groupShopId);
                    if (groupShopDTO == null || groupShopDTO.getStatus() == StatusType.LOCK.getCode()) {
                        throw new ApiException("财团商品不存在或活动已结束");
                    }
                    List<LmGroupShopSku> groupShopSkuList = groupShopDTO.getGroupShopSkuList();
                    for (LmGroupShopSku groupShopSkuDO : groupShopSkuList) {
                        if (groupShopSkuDO.getSkuId().equals(groupShopSkuList.get(0).getSkuId())) {
                            //若找到交集
                            groupShopPrice = groupShopSkuDO.getSkuGroupShopPrice();
                        }
                    }
                }
                //商品价格
                int skuPrice = 0;
                int skuOriginalPrice = 0;
                //稍后用于优惠券作用范围校验
                Map<Long, Integer> categoryPriceMap = new HashMap<>();
                //稍后用于插入OrderSku
                Map<Long, SkuDTO> skuIdDTOMap = new HashMap<>();
                for (OrderRequestSkuDTO orderRequestSkuDTO : skuList) {
                    SkuDTO skuDTO = lmSkuService.getSkuDTOById(orderRequestSkuDTO.getSkuId());
                    skuIdDTOMap.put(skuDTO.getId(), skuDTO);
                    if (skuDTO.getStatus() == SpuStatusType.STOCK.getCode()) {
                        throw new ApiException(skuDTO.getSpuTitle() + "." + skuDTO.getTitle() + " 已经下架!");
                    }
                    if (skuDTO == null) {
                        throw new ApiException("商品不存在");
                    }
                    if (skuDTO.getStock() < orderRequestSkuDTO.getNum()) {
                        throw new ApiException(skuDTO.getSpuTitle() + "." + skuDTO.getTitle() + " 库存不足!");
                    }
                    int p;//商品价格
                    //团购id不为空
                    if (groupShopId != null && groupShopPrice != null) {
                        p = groupShopPrice;
                    } else if (userLevel == UserLevelType.VIP.getCode()) {
                        p = skuDTO.getVipPrice() * orderRequestSkuDTO.getNum();
                    } else {
                        p = skuDTO.getPrice() * orderRequestSkuDTO.getNum();
                    }
                    skuPrice += p;
                    skuOriginalPrice += skuDTO.getOriginalPrice() * orderRequestSkuDTO.getNum();
                    List<Long> categoryFamily = categoryBizService.getCategoryFamily(skuDTO.getCategoryId());
                    for (Long cid : categoryFamily) {
                        Integer price = categoryPriceMap.get(cid);
                        if (price == null) {
                            price = p;
                        } else {
                            price += p;
                        }
                        categoryPriceMap.put(cid, price);
                    }
                }

                if (skuPrice != orderRequest.getTotalPrice()) {
                    throw new ApiException("商品总额校验失败");
                }

                //优惠券折扣价格
                int couponPrice = 0;
                //优惠券校验
                UserCouponDTO userCouponFromFront = orderRequest.getCoupon();
                if (userCouponFromFront != null) {
                    if (userCouponFromFront.getId() == null || userCouponFromFront.getDiscount() == null) {
                        throw new ApiException("优化券参数校验失败");
                    }
                    UserCouponDTO userCouponFromDB = lmUserCouponService.getUserCouponById(userCouponFromFront.getId(), user.getId());

                    if (userCouponFromDB == null) {
                        throw new ApiException("下单时查找用户优惠券失败");
                    }

                    if (userCouponFromDB.getGmtUsed() != null) {
                        throw new ApiException("优惠券已使用");
                    }

                    if (!userCouponFromDB.getDiscount().equals(userCouponFromFront.getDiscount())) {
                        throw new ApiException("优惠优惠券与发布的优惠券金额不一致");
                    }

                    //校验优惠券策略是否满足
                    Long categoryId = userCouponFromDB.getCategoryId();
                    if (categoryId != null) {
                        Integer p = categoryPriceMap.get(categoryId);
                        if (p < userCouponFromDB.getMin()) {
                            throw new ApiException("优惠券金额未达到指定值");
                        }
                    } else {
                        if (skuPrice < userCouponFromDB.getMin()) {
                            throw new ApiException("优惠券金额未达到指定值");
                        }
                    }
                    couponPrice = userCouponFromDB.getDiscount();
                }

                Integer freightPrice = freightBizService.getFreightMoney(orderRequest);
                //参数强校验 END
                //???是否校验actualPrice??强迫校验？
                int actualPrice = skuPrice - couponPrice + freightPrice;
                Date now = new Date();
                LmOrder orderDO = new LmOrder();
                orderDO.setSkuTotalPrice(skuPrice);//订单总额
                orderDO.setSkuOriginalTotalPrice(skuOriginalPrice);//订单原始总额
                orderDO.setChannel(channel);//下单渠道
                orderDO.setActualPrice(actualPrice);//订单实付总额
                orderDO.setGroupShopId(groupShopId);//团购Id
                if (couponPrice != 0) {//优惠券价格
                    orderDO.setCouponId(orderRequest.getCoupon().getId());//优惠券id
                    orderDO.setCouponPrice(couponPrice);//优惠金额
                }
                orderDO.setMono(orderRequest.getMono());//
                orderDO.setFreightPrice(freightPrice);//邮费
                orderDO.setOrderNo(GeneratorUtil.genOrderId(myPropertitys.getMachineNo(), myPropertitys.getEnv()));
                orderDO.setUserId(user.getId());
                orderDO.setStatus(String.valueOf(OrderStatusType.UNPAY.getCode()));
                orderDO.setCreatedTime(now);
                orderDO.setUpdatedTime(now);

                if (orderRequest.getAddressId() != null) {
                    LmAddress addressDO = (LmAddress) lmAddressService.getById(orderRequest.getAddressId());
                    if (user.getId().longValue()!=addressDO.getUserId().longValue()) {
                        throw new ApiException("传入地址与用户地址不一致");
                    }
                    orderDO.setConsignee(addressDO.getConsignee());
                    orderDO.setPhone(addressDO.getPhone());
                    orderDO.setProvince(addressDO.getProvince());
                    orderDO.setCity(addressDO.getCity());
                    orderDO.setCounty(addressDO.getCounty());
                    orderDO.setAddress(addressDO.getAddress());
                }
                lmOrderService.save(orderDO);

                //扣除用户优惠券
                if (orderDO.getCouponId() != null) {
                    LmUserCoupon updateUserCouponDO = new LmUserCoupon();
                    updateUserCouponDO.setId(orderDO.getCouponId());
                    updateUserCouponDO.setUseredTime(now);
                    updateUserCouponDO.setOrderId(orderDO.getId());
                    lmUserCouponService.updateById(updateUserCouponDO);
                }

                //插入OrderSku
                skuList.forEach(item -> {
                    SkuDTO skuDTO = skuIdDTOMap.get(item.getSkuId());
                    LmOrderSku orderSkuDO = new LmOrderSku();
                    orderSkuDO.setBarCode(skuDTO.getBarCode());
                    orderSkuDO.setTitle(skuDTO.getTitle());
                    orderSkuDO.setUnit(skuDTO.getUnit());
                    orderSkuDO.setSpuTitle(skuDTO.getSpuTitle());
                    orderSkuDO.setImg(skuDTO.getImg() == null ? skuDTO.getSpuImg() : skuDTO.getImg());
                    orderSkuDO.setNum(item.getNum());
                    orderSkuDO.setOriginalPrice(skuDTO.getOriginalPrice());
                    orderSkuDO.setPrice(skuDTO.getPrice());
                    if (userLevel == UserLevelType.VIP.getCode()) {
                        orderSkuDO.setPrice(skuDTO.getVipPrice());
                    } else {
                        orderSkuDO.setPrice(skuDTO.getPrice());
                    }
                    orderSkuDO.setSkuId(skuDTO.getId());
                    orderSkuDO.setSpuId(skuDTO.getSpuId());
                    orderSkuDO.setOrderNo(orderDO.getOrderNo());
                    orderSkuDO.setOrderId(orderDO.getId());
                    orderSkuDO.setCreatedTime(now);
                    orderSkuDO.setUpdatedTime(now);
                    lmOrderSkuService.save(orderSkuDO);
                    //扣除库存
                    lmSkuService.decSkuStock(item.getSkuId(), item.getNum());
                });

                if (!StringUtils.isEmpty(orderRequest.getTakeWay())) {
                    String takeWay = orderRequest.getTakeWay();
                    if ("cart".equals(takeWay)) {
                        //扣除购物车
                        List<Long> skuIds = skuList.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
                        lmCartService.remove(new QueryWrapper<LmCart>().in("sku_id", skuIds).eq("user_id", user.getId()));
                    }
                    //直接购买传值为 "buy"
                }
                return new ApiResponse().ok(orderDO.getOrderNo());
//                return orderDO.getOrderNo();

            } catch (ApiException e) {
                throw e;
            } catch (Exception e) {
                logger.error("[提交订单] 异常", e);
                throw new ApiException("[提交订单] 异常:"+e.getMessage());
            } finally {
                lockComponent.release(TAKE_ORDER_LOCK + user.getId());
            }
        }
        throw new ApiException("系统繁忙,请稍后");
    }

    @RequestMapping("/getOrderPage")
    @ResponseBody
    public ApiResponse getOrderPage(Integer pageNo, Integer pageSize, String status,@LoginUser UserDTO user) throws ApiException {
        List<OrderDTO> orderDTOList = lmOrderService.selectOrderPage(status, (pageNo - 1) * pageSize, pageSize, user.getId());
        Long count = lmOrderService.countOrder(status, (pageNo - 1) * pageSize, pageSize, user.getId());
        //封装SKU
        orderDTOList.forEach(item -> {
            item.setSkuList(lmOrderSkuService.list(new QueryWrapper<LmOrderSku>().eq("order_id", item.getId())));
        });
        Page<OrderDTO> page = new Page<>(orderDTOList, pageNo, pageSize, count);
        return new ApiResponse().ok(page);
    }

    @RequestMapping("/getOrderDetail")
    @ResponseBody
    public ApiResponse getOrderDetail(Long orderId, @LoginUser UserDTO user) throws ApiException {
        return new ApiResponse().ok(orderBizService.getOrderDetail(orderId, user.getId()));
    }

    /**
     * 小程序预支付
     * @param orderNo
     * @param ip
     * @param user
     * @return
     * @throws ApiException
     */
    @RequestMapping("/wxPrepay")
    @ResponseBody
    public ApiResponse wxPrepay(String orderNo, String ip,@LoginUser UserDTO user) throws ApiException {
        Date now = new Date();
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        // 检测订单状态
        int status = Integer.parseInt(orderDO.getStatus());
        if (status != OrderStatusType.UNPAY.getCode()) {
            logger.info("订单状态 status=="+status);
            throw new ApiException("订单状态不能支付");
        }
        Integer loginType = user.getLoginType();
        String appId;
        String tradeType;
        if (UserLoginType.MP_WEIXIN.getCode() == loginType) {
            appId = myPropertitys.getWx().getMini().getAppid();
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else if (UserLoginType.APP_WEIXIN.getCode() == loginType || UserLoginType.REGISTER.getCode() == loginType) {
            appId =  myPropertitys.getWx().getApp().getAppid();;
            tradeType = WxPayConstants.TradeType.APP;
        } else if (UserLoginType.H5_WEIXIN.getCode() == loginType) {
            appId = myPropertitys.getWx().getH5().getAppid();
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else {
            logger.info("不支持的支付方式 loginType=="+loginType);
            throw new ApiException("不支持的支付方式");
        }

        Object result = null;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setAppid(appId);
            orderRequest.setOutTradeNo(orderNo);
            orderRequest.setOpenid(user.getOpenId());
            orderRequest.setBody("订单：" + orderNo);
            orderRequest.setTotalFee(orderDO.getActualPrice());
            orderRequest.setSpbillCreateIp(IpUtil.getIpAddr(request));
            orderRequest.setTradeType(tradeType);

            result = wxPayService.createOrder(orderRequest);
            logger.info("wxPrepay获取预支付单请求结果=="+ JSON.toJSONString(result));
            // 微信已经取消模板消息
//            if (result instanceof  WxPayMpOrderResult) {
//                String prepayId = ((WxPayMpOrderResult)result).getPackageValue();
//                prepayId = prepayId.replace("prepay_id=", "");
//                UserFormIdDO userFormIdDO = new UserFormIdDO();
//                userFormIdDO.setFormId(prepayId);
//                userFormIdDO.setUserId(userId);
//                userFormIdDO.setOpenid(SessionUtil.getUser().getOpenId());
//                userFormIdDO.setGmtUpdate(now);
//                userFormIdDO.setGmtCreate(now);
//                userBizService.setValidFormId(userFormIdDO);
//            }
        } catch (WxPayException e) {
            logger.error("[微信支付] 异常1", e);
            throw new ApiException("预付款异常:"+e.getErrCodeDes());
        } catch (Exception e) {
            logger.error("[预付款异常]2", e);
            throw new ApiException(e.getMessage());
        }
        return new ApiResponse().ok(result);
    }

    @RequestMapping("/offlinePrepay")
    @ResponseBody
    public ApiResponse offlinePrepay(String orderNo,@LoginUser UserDTO user) throws ApiException {
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        // 检测订单状态
        Integer status = Integer.valueOf(orderDO.getStatus());
        if (status != OrderStatusType.UNPAY.getCode()) {
            throw new ApiException("不可用支付订单");
        }
        LmOrder updateOrderDO = new LmOrder();
        updateOrderDO.setPayChannel(PayChannelType.OFFLINE.getCode());
        updateOrderDO.setStatus(String.valueOf(OrderStatusType.WAIT_STOCK.getCode()));
        updateOrderDO.setUpdatedTime(new Date());
        boolean succ = orderBizService.changeOrderStatus(orderNo, String.valueOf(OrderStatusType.UNPAY.getCode()), updateOrderDO);
        if (succ) {
            return new ApiResponse().ok("ok");
        }
        throw new ApiException("线下支付更新失败");
    }

    @RequestMapping("/refund")
    @ResponseBody
    public ApiResponse refund(String orderNo, String reason,@LoginUser UserDTO user) throws ApiException {
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        if (PayChannelType.OFFLINE.getCode().equals(orderDO.getPayChannel())) {
            throw new ApiException("线下支付不能退款");
        }
        if (OrderStatusType.refundable(Integer.parseInt(orderDO.getStatus()))) {
            LmOrder updateOrderDO = new LmOrder();
            updateOrderDO.setRefundReason(reason);
            updateOrderDO.setStatus(String.valueOf(OrderStatusType.REFUNDING.getCode()));
            orderBizService.changeOrderStatus(orderNo, orderDO.getStatus() , updateOrderDO);
            GlobalExecutor.execute(() -> {
                OrderDTO orderDTO = new OrderDTO();
                BeanUtils.copyProperties(orderDO, orderDTO);
                List<LmOrderSku> orderSkuList = lmOrderSkuService.list(new QueryWrapper<LmOrderSku>().eq("order_no", orderDO.getOrderNo()));
                orderDTO.setSkuList(orderSkuList);
                //退款通知
                adminNotifyBizService.refundOrder(orderDTO);
            });
            return new ApiResponse().ok("ok");
        }
        throw new ApiException("该订单不支持退款");
    }

    /**
     * 取消订单
     * @param orderNo
     * @return
     * @throws ApiException
     */
    @RequestMapping("/cancel")
    @ResponseBody
    public ApiResponse cancel(String orderNo,@LoginUser UserDTO user) throws ApiException {
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        if (Integer.parseInt(orderDO.getStatus()) != OrderStatusType.UNPAY.getCode()) {
            throw new ApiException("订单状态不支持取消");
        }
        LmOrder updateOrderDO = new LmOrder();
        updateOrderDO.setStatus(String.valueOf(OrderStatusType.CANCELED.getCode()));
        updateOrderDO.setUpdatedTime(new Date());
        List<LmOrderSku> orderSkuList = lmOrderSkuService.list(new QueryWrapper<LmOrderSku>().eq("order_id", orderDO.getId()));
        orderSkuList.forEach(item -> {
            lmSkuService.returnSkuStock(item.getSkuId(), item.getNum());
        });
        orderBizService.changeOrderStatus(orderNo, String.valueOf(OrderStatusType.UNPAY.getCode()), updateOrderDO);
        return new ApiResponse().ok("ok");
    }

    /**
     * 确认收货
     * @param orderNo
     * @param user
     * @return
     * @throws ApiException
     */
    @RequestMapping("/confirm")
    @ResponseBody
    public ApiResponse confirm(String orderNo, @LoginUser UserDTO user) throws ApiException {
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        if (Integer.parseInt(orderDO.getStatus()) != OrderStatusType.WAIT_CONFIRM.getCode()) {
            throw new ApiException("不是待确认收货状态");
        }
        LmOrder updateOrderDO = new LmOrder();
        updateOrderDO.setStatus(String.valueOf(OrderStatusType.WAIT_APPRAISE.getCode()));
        updateOrderDO.setUpdatedTime(new Date());
        List<LmOrderSku> orderSkuList = lmOrderSkuService.list(new QueryWrapper<LmOrderSku>().eq("order_id", orderDO.getId()));
        orderSkuList.forEach(item -> {
//            lmSkuService.decSkuFreezeStock(item.getSkuId(), item.getNum());//不能释放冻结库存
        });
        orderBizService.changeOrderStatus(orderNo, String.valueOf(OrderStatusType.WAIT_CONFIRM.getCode()), updateOrderDO);
        return new ApiResponse().ok("ok");
    }

    /**
     * 查看物流
     * @param orderNo
     * @param user
     * @return
     * @throws ApiException
     */
    @RequestMapping("/queryShip")
    @ResponseBody
    public ShipTraceDTO queryShip(String orderNo, @LoginUser UserDTO user) throws ApiException {
        LmOrder orderDO = orderBizService.checkOrderExist(orderNo, user.getId());
        if (Integer.parseInt(orderDO.getStatus()) < OrderStatusType.WAIT_CONFIRM.getCode()) {
            throw new ApiException("不是待收货状态");
        }
        if (StringUtils.isEmpty(orderDO.getShipCode()) || StringUtils.isEmpty(orderDO.getShipNo())) {
            throw new ApiException("运单号为空");
        }
        ShipTraceDTO shipTraceList = freightBizService.getShipTraceList(orderDO.getShipNo(), orderDO.getShipCode());
        if (CollectionUtils.isEmpty(shipTraceList.getTraces())) {
            throw new ApiException("没用物流详细信息");
        }
        return shipTraceList;
    }


}
