package com.yyyu.ezbooking_seller.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yyyu.barbecue.ezbooking_base.ui.activity.BaseActivity;
import com.yyyu.barbecue.ezbooking_base.ui.pop.DatePickerPop;
import com.yyyu.barbecue.ezbooking_base.ui.widget.WrapContentHeightViewPager;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.ui.fragment.TimeMapFragment;

/**
 * 功能：测试Activity
 *
 * @author yyyu
 * @date 2016/8/20
 */
public class TestActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }


    @Override
    protected void initView() {
        WrapContentHeightViewPager wvp_test = (WrapContentHeightViewPager) findViewById(R.id.wvp_test);
        wvp_test.setAdapter(new TestPagerAdapter());
    }

    @Override
    protected void initListener() {

    }

    class TestPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(TestActivity.this).inflate(R.layout.vp_item_statistic , container , false);
            FrameLayout fl_statistic = (FrameLayout) findViewById(R.id.fl_statistic);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fm = fragmentManager.beginTransaction();
            fm.replace(R.id.fl_statistic ,new TimeMapFragment());
            fm.commit();
            container.addView(itemView);
            return itemView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }


}
