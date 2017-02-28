package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查看商家商品类型请求参数封装bean
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class QueryGoodsTypeParams {

    private  String sellerId;

    public QueryGoodsTypeParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
