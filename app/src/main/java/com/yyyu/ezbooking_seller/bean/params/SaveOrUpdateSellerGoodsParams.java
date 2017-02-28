package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：添加或修改商家商品的请求参数封装bean
 *
 * Created by yyyu on 2016/8/16.
 */
public class SaveOrUpdateSellerGoodsParams {

    private String goodsId ;
    private String  sellerId ;
    private int goodsTypeId ;
    private String goodsName ;
    private double  goodsPrice ;
    private String bigImage ;

    public SaveOrUpdateSellerGoodsParams(){

    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsTypeId() {
        return goodsTypeId;
    }

    public void setGoodsTypeId(int goodsTypeId) {
        this.goodsTypeId = goodsTypeId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
