package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：删除商品的请求封装bean
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class DeleteGoodsParams {

    private String goodsId;

    public DeleteGoodsParams(){

    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
