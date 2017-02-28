package com.yyyu.ezbooking_seller.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yyyu.barbecue.ezbooking_base.bean.json.LoginJson;
import com.yyyu.barbecue.ezbooking_base.gobal.Constant;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MySPUtils;

/**
 * 功能：业务逻辑相关的工具类
 * <p/>
 * Created by yyyu on 2016/8/8.
 */
public class LogicUtils {

    public static LogicUtils mInstance;

    private Context mContext;
    private Gson gson;

    private LogicUtils(Context context) {
        this.mContext = context;
        gson = new Gson();
    }

    public static LogicUtils getInstance(Context context) {
        synchronized (LogicUtils.class) {
            while (mInstance == null) {
                mInstance = new LogicUtils(context);
            }
        }
        return mInstance;
    }

    /**
     * 得到sellerId
     *
     * @return 未登录返回""
     */
    public String getSellerId(){
        if(!isUserLogined()){
            return "";
        }
        LoginJson loginJson = gson.fromJson(getUserInfo() , LoginJson.class);
        return loginJson.RESULT_DATA.sellerId;
    }


    /**
     * 得到用户的Id
     * @return 未登录返回-1
     */
    public int  getUserId(){
        if(!isUserLogined()){
            return -1;
        }
        LoginJson loginJson = gson.fromJson(getUserInfo() , LoginJson.class);
        return loginJson.RESULT_DATA.userId;
    }


    /**
     * 判断用户是否登录
     */
    public boolean isUserLogined(){
        return !TextUtils.isEmpty(getUserInfo());
    }

    /**
     * 得到用户的信息Json串
     *
     * @return
     */
    public String getUserInfo() {
        return (String) MySPUtils.get(mContext, Constant.USER_INFO, "");
    }

    public LoginJson.UserInfo getUser(){
        Gson gson = new Gson();
        return gson.fromJson(getUserInfo() , LoginJson.class).RESULT_DATA;
    }

}
