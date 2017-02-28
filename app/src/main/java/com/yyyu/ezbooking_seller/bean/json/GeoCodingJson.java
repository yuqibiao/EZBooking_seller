package com.yyyu.ezbooking_seller.bean.json;

/**
 * 功能：百度地图GeoCoding 转换结果json
 *
 * @author yyyu
 * @date 2016/9/22
 */
public class GeoCodingJson {

    public int status ;
    public Result result;

    public static class Result{
        public String formatted_address ;
        public String business ;
    }

}
