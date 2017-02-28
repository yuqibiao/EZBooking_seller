package com.yyyu.ezbooking_seller.bean.json;

import java.util.List;

/**
 * 功能：查看商家订单返回数据的Json bean
 * <p/>
 * <p/>
 * Created by yyyu on 2016/8/22.
 */
public class QuerySellerOrderJson {

    public int RESULT_CODE;
    public List<OrderItem> RESULT_DATA;

    public static class OrderItem {

        public int orderCount;
        public String orderTime;
        public int deskCount;
        public int personCount;

        public List<Order> orderList;

    }

    public static class Order {

        public int orderId;
        public String orderNo;
        public int orderType;
        public int orderStatus;
        public String orderTime;
        public String userPic;
        public String userName ;
        public int deskCount;
        public int personCount ;
        public double orderPrice;
        public String orderDesc;

        public List<OrderGoods> subOrderList;

    }

    public class OrderGoods {

        public int subOrderId;
        public int goodsId;
        public int goodsCount;
        public String goodsName;

    }


}
