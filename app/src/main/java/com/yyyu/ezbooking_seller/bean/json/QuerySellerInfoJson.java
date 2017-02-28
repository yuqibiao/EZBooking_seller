package com.yyyu.ezbooking_seller.bean.json;

/**
 * 功能：查询商家信息返回结果Json bean
 *
 * Created by yyyu on 2016/8/18.
 */
public class QuerySellerInfoJson {

    public int RESULT_CODE ;
    public SellerInfo RESULT_DATA;

    public static  class SellerInfo{
        public String sellerId ;
        public int sellerType ;
        public String sellerName;
        public String sellerUser ;
        public String bigImage ;
        public String mobile ;
        public String sellerDesc;
        public String sellerAddress;
    }

}
