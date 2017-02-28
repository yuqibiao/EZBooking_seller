package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查询商家信息的封装Bean
 *
 * Created by yyyu on 2016/8/18.
 */
public class QuerySellerInfoParams {

    private String sellerId;

    public QuerySellerInfoParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
