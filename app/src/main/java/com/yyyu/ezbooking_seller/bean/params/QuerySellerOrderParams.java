package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查看商家订单请求的参数bean
 *
 * @author yyyu
 * @date 2016/8/20
 */
public class QuerySellerOrderParams {

    private String sellerId ;
    private String orderDate ;
    private int currentPage;
    private int pageSize ;

    public QuerySellerOrderParams(){

    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
