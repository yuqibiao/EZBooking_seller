package com.yyyu.ezbooking_seller.net;

/**
 * 功能：URL地址
 * Created by yyyu on 2016/6/28.
 */
public class MyHttpUrl {

    public static final String IP_PORT = "http://120.25.220.179:8089";
    public static final String BASE_ADDRESS = IP_PORT + "/dbLife/rest/";
    public static final String BASE_IMAGE_PATH=IP_PORT+"/dbLife/";
    public static final String MAP_KEY = "enErnF9NYsXX9bjro82xTSUOdWGv1GiY";
    //---查看订单
    public static final String QUERY_SELLER_ORDER=BASE_ADDRESS+"rest?method=seller.querySellerOrder&data=";
    //---添加、修改商家
    public static final String SAVE_OR_UPDATE_SELLER_INFO =BASE_ADDRESS+ "rest";
    //---查看商家的信息
    public static final String QUERY_SELLER_INFO = BASE_ADDRESS+"rest?method=seller.querySellerInfo&data=";
    //---添加。修改商家服务
    public static final String SAVE_OR_UPDATE_SELLER_SERVICE=BASE_ADDRESS+"rest?method=seller.saveOrUpdateSellerService&data=";
    //---查看商家的服务
    public static final String QUERY_SELLER_SERVICE=BASE_ADDRESS+"rest?method=seller.querySellerService&data=";
    //---给商家添加商品
    public static final String SAVE_OR_UPDATE_SELLER_GOODS=BASE_ADDRESS+ "rest";
    //---查看商家商品
    public static final String QUERY_SELLER_GOODS=BASE_ADDRESS+"rest?method=seller.querySellerGoods&data=";
    //---查看商家商品类型
    public  static final String QUERY_SELLER_GOODS_TYPE=BASE_ADDRESS+"rest?method=seller.querySellerGoodsType&data=";
    //---添加商品类型
    public static final String ADD_SELLER_GOODS_TYPE = BASE_ADDRESS+"rest?method=seller.addSellerGoodsType&data=";
    //---删除商品
    public static final String DELETE_SELLER_GOODS = BASE_ADDRESS+"rest?method=seller.delSellerGoods&data=";
    //---商家接单
    public static final String ACCEPT_ORDER = BASE_ADDRESS+"rest?method=seller.acceptOrder&data=";
    //---查询统计信息
    public static final String QUERY_SELLER_STATISTIC = BASE_ADDRESS+"rest?method=seller.querySellerStatistic&data=";
    //---百度地图 根据经纬度获取地址
    public static final String GET_ADRRESS_BY_LATLNG = "http://api.map.baidu.com/geocoder/v2/?" +
            "ak=" + "" +MAP_KEY+
            "&output=json&pois=0" +
            "&location=";//39.983424,116.322987

}
