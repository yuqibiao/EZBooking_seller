package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查看商家服务参数封装bean
 *
 * @author yyyu
 * @date 2016/8/23
 */
public class QuerySellerServiceParams {

    private String sellerId;

    public QuerySellerServiceParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
