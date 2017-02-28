package com.yyyu.ezbooking_seller.bean.json;

import java.util.List;

/**
 * 功能：查看商家对应商品类型结果封装bean
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class QueryGoodsTypeJson {

    public int RESULT_CODE ;
    public List<GoodsType> RESULT_DATA;

    public static class GoodsType{
        public int goodsTypeId ;
        public String goodsTypeName ;
    }

}
