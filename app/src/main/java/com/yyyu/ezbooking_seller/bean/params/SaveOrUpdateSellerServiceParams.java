package com.yyyu.ezbooking_seller.bean.params;

import java.util.List;

/**
 * 功能：保存或修改商家的服务信息
 *
 * @author yyyu
 * @date 2016/8/23
 */
public class SaveOrUpdateSellerServiceParams {

    private String sellerId ;
    private int isDesk;
    private int isGoods;
    private int deskCount ;
    private List<ServiceItem> serviceItemIds;

    public SaveOrUpdateSellerServiceParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public int getIsDesk() {
        return isDesk;
    }

    public void setIsDesk(int isDesk) {
        this.isDesk = isDesk;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
    }

    public int getDeskCount() {
        return deskCount;
    }

    public void setDeskCount(int deskCount) {
        this.deskCount = deskCount;
    }

    public List<ServiceItem> getServiceItemIds() {
        return serviceItemIds;
    }

    public void setServiceItemIds(List<ServiceItem> serviceItemIds) {
        this.serviceItemIds = serviceItemIds;
    }

    public static class ServiceItem{

        private int serviceItemId ;

        public ServiceItem(){

        }

        public ServiceItem(int serviceItemId) {
            this.serviceItemId = serviceItemId;
        }

        public int getServiceItemId() {
            return serviceItemId;
        }

        public void setServiceItemId(int serviceItemId) {
            this.serviceItemId = serviceItemId;
        }
    }

}
