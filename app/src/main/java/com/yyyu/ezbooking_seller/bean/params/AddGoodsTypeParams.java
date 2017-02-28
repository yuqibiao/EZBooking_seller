package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：添加商店商品类型请求参数封装bean
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class AddGoodsTypeParams {

    private String sellerId;
    private String goodsTypeName;

    public AddGoodsTypeParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }
}
