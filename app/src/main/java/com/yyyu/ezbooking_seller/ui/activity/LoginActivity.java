package com.yyyu.ezbooking_seller.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.yyyu.barbecue.ezbooking_base.callback.OnDataSubmitListener;
import com.yyyu.barbecue.ezbooking_base.ui.activity.BaseRegAndLoginActivity;

/**
 * 功能：登录和注册界面
 *
 * @author yyyu
 * @date 2016/8/14
 */
public class LoginActivity extends BaseRegAndLoginActivity {

    @Override
    protected Integer setUserType() {
        return 2;
    }

    @Override
    protected Integer setOptionType() {
        return 1;
    }

    @Override
    protected void initListener() {
        super.initListener();
        setOnDataSubmitListener(new OnDataSubmitListener() {
            @Override
            public void onSuccess(String response) {
                MainActivity.startAction(LoginActivity.this);
                LoginActivity.this.finish();
            }
            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 跳转到Activity界面
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }
}
