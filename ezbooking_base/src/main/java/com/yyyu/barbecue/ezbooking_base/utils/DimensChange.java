package com.yyyu.barbecue.ezbooking_base.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * 功能：尺寸转换工具类
 *
 * @author yyyu
 * @date 2016/5/22
 */
public class DimensChange {

    private  DimensChange(){
        throw new UnsupportedOperationException("该类不能被实例化");
    }

    /**
     * dp转px
     */
    public static int dp2px (Context ctx , float dpValue){
        float  scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue*scale);
    }
    /**
     * px转dp
     */
    public static float px2dp(Context ctx , int pxValue){
        float  scale = ctx.getResources().getDisplayMetrics().density;
        return pxValue/scale;
    }

}
