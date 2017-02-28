package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：商家接单请求参数
 *
 * @author yyyu
 * @date 2016/8/28
 */
public class AcceptOrderParams {

    public int orderId;

    public AcceptOrderParams(){

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
