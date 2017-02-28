package com.yyyu.ezbooking_seller.bean.json;

import java.util.List;

/**
 * 功能：查询商家服务结果对应的Json
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class QuerySellerServiceJson {

   public Service RESULT_DATA;

    public int RESULT_CODE ;


    public static class Service{
        public int sellerId;
        public String bigImage;
        public String sellerDesc;
        public int isDesk ;
        public int isGoods ;
        public int deskCount ;
        public List<Item> siLstMap;

        public static class Item{
            public int serviceItemId ;
            public String serviceItemPic ;
            public String serviceItemName ;
            public int status ;
        }
    }

}
