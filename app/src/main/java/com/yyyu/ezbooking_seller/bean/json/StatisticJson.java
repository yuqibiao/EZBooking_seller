package com.yyyu.ezbooking_seller.bean.json;

import java.util.List;

/**
 * 功能：统计数据对应的Json
 *
 * Created by yyyu on 2016/10/8.
 */
public class StatisticJson {

    public int RESULT_CODE ;
    public ResultData RESULT_DATA;

    public static class ResultData{
        public List<Sale> saleMap;
        public List<TimeMapBean> hotTimeMap;
        public List<TimeMapBean>surpriseTimeMap;
        public List<GoodsMapBean> hotGoodsMap;
        public List<GoodsMapBean> surpriseGoodsMap;

    }

    public static class GoodsMapBean{
        public int goodsId ;
        public String bigImage ;
        public String goodsName ;
    }

    public static class TimeMapBean{
        public String orderDate ;
        public String orderTime ;
    }

    public static class Sale{
        public int amount ;//总数
        public int ydCount ;//预定
        public int yyCount;//预约
        public int date ;
    }

}
