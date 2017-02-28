package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查看商店商品请求参数封装bean
 *
 * @author yyyu
 * @date 2016/8/18
 */
public class QuerySellerGoodsParams {

    private String sellerId;

    public QuerySellerGoodsParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
