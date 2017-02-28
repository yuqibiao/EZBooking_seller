package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：添加、修改商家参数
 *
 * @author yyyu
 * @date 2016/8/14
 */
public class UpdateSellerInfoParams {

    private String sellerId;
    private String sellerName;
    private String sellerUser;
    private String mobile;
    private Integer sellerType;
    private String bigImage;
    private String sellerDesc;
    private String sellerAddress;
    private String lng;//经度
    private String lat;//纬度

    public UpdateSellerInfoParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerUser() {
        return sellerUser;
    }

    public void setSellerUser(String sellerUser) {
        this.sellerUser = sellerUser;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSellerType() {
        return sellerType;
    }

    public void setSellerType(Integer sellerType) {
        this.sellerType = sellerType;
    }

    public String getBigImage() {
        return bigImage;
    }

    public void setBigImage(String bigImage) {
        this.bigImage = bigImage;
    }

    public String getSellerDesc() {
        return sellerDesc;
    }

    public void setSellerDesc(String sellerDesc) {
        this.sellerDesc = sellerDesc;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
