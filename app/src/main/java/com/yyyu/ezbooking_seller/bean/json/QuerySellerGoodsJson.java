package com.yyyu.ezbooking_seller.bean.json;

import java.util.List;

/**
 * 功能：查看商家商品的返回Json
 *
 * @author yyyu
 * @date 2016/8/18
 */
public class QuerySellerGoodsJson {

    public int RESULT_CODE;
    public List<GoodsType> RESULT_DATA;

    public static class GoodsType {
        public int goodsTypeId;
        public String goodsTypeName;
        public List<Goods> goodsList;
    }

    public static class Goods {
        public int goodsId;
        public String goodsName;
        public double goodsPrice ;
        public String bigImage;
        public String goodsDesc;
    }

}
