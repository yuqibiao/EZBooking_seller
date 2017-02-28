package com.yyyu.ezbooking_seller;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/5/17
 */
public class MyApplication extends Application{

    private static final String TAG = "MyApplication";

    public Context aContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.aContext = getApplicationContext();
        SDKInitializer.initialize(aContext);
    }

}
