package com.yyyu.ezbooking_seller.bean.params;

/**
 * 功能：查看商家的统计信息参数
 *
 * Created by yyyu on 2016/10/8.
 */
public class QueryStatisticParams {

    private String sellerId;
    private String month;

    public QueryStatisticParams(){

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
