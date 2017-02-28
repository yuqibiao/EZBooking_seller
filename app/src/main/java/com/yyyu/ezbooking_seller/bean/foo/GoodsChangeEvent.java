package com.yyyu.ezbooking_seller.bean.foo;

/**
 * 功能：商品被修改了Evnet事件
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class GoodsChangeEvent {

    private String msg;

    public GoodsChangeEvent(String msg) {
        this.msg = msg;
    }

}
