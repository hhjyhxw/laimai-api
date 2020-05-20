package com.icloud.modules.lm.enums;

/**
 * Created by rize on 2019/2/13.
 */
public enum OrderStatusType {
    UNPAY(0, "未付款"),
    GROUP_SHOP_WAIT(1,"等待团购活动结束"),
    WAIT_STOCK(2, "待出库"),
    WAIT_CONFIRM(3, "待收货"),
    WAIT_APPRAISE(4, "待评价"),
    COMPLETE(5, "已完成"),
    REFUNDING(6, "退款中"),
    REFUNDED(7, "已退款"),
    CANCELED(8, "已取消"),
    CANCELED_SYS(9, "已取消（系统）");

    OrderStatusType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    /**
     * 判断定订单是否可退款
     * @return
     */
    public static boolean refundable(int orderStauts) {
        if (orderStauts == WAIT_STOCK.getCode() || orderStauts == WAIT_CONFIRM.getCode()) {
            return true;
        } else {
            return false;
        }
    }

}
