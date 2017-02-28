package com.yyyu.ezbooking_seller.bean.foo;

/**
 * 功能：商品被修改了Evnet事件
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class OrderAcceptEvent {

    private String msg;

    public OrderAcceptEvent(String msg) {
        this.msg = msg;
    }

}
