package com.yyyu.ezbooking_seller.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.ui.activity.BaseActivity;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

/**
 * 功能：启动界面(在里面可以做一些应用初始化操作)
 * <p/>
 * Created by yyyu on 2016/8/8.
 */
public class SplashActivity extends BaseActivity {

    /** Splash最长显示时间 */
    public static final int MAX_SHOW_TIME = 3*1000;

    private TextView ctv_login;
    private TextView ctv_register;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        ctv_login = getView(R.id.ctv_login);
        ctv_register = getView(R.id.ctv_register);
    }

    @Override
    protected void initListener() {
        ctv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.startAction(SplashActivity.this);
            }
        });
        ctv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.startAction(SplashActivity.this);
            }
        });
    }

    @Override
    protected void afterInit() {
        super.afterInit();
        Animation animation = AnimationUtils.loadAnimation(this , R.anim.splash_anime);
        findViewById(R.id.iv_splash).setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(LogicUtils.getInstance(getApplicationContext()).isUserLogined()){
                    MainActivity.startAction(SplashActivity.this);
                    SplashActivity.this.finish();
                }else{//---未登录
                    ctv_login.setVisibility(View.VISIBLE);
                    ctv_register.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}