package com.yyyu.ezbooking_seller.ui.fragment;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseFragment;
import com.yyyu.barbecue.ezbooking_base.ui.widget.MyLineChart;
import com.yyyu.barbecue.ezbooking_base.ui.widget.WrapContentHeightViewPager;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.foo.TabBean;
import com.yyyu.ezbooking_seller.bean.json.StatisticJson;
import com.yyyu.ezbooking_seller.bean.params.QueryStatisticParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：统计Fragment
 *
 *
 * Created by yyyu on 2016/10/8.
 */
public class StatisticFragment extends BaseFragment{

    private static final String TAG = "StatisticFragment";

    @ViewInject(R.id.ib_left)
    private ImageButton ib_left;
    @ViewInject(value = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(value = R.id.tv_right)
    private TextView tv_right;
    @ViewInject(R.id.lc_statistic)
    private MyLineChart lc_statistic;
    @ViewInject(R.id.dot_left)
    private View dot_left;
    @ViewInject(R.id.dot_right)
    private View dot_right;
    @ViewInject(R.id.vp_statistic)
    private WrapContentHeightViewPager vp_statistic;

    private DrawerLayout dl_main;
    private QueryStatisticParams queryStatisticParams;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_statistic;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        queryStatisticParams = new QueryStatisticParams();
    }

    @Override
    protected void initView() {
        tv_title.setText("预约统计");
        tv_right.setVisibility(View.INVISIBLE);
        dl_main = (DrawerLayout) getActivity().findViewById(R.id.dl_main);
        initTab();
    }

    /**
     * 热门/冷门 tab
     */
    private void initTab() {
        final  List<TabBean> tabs = new ArrayList<>(2);
        tabs.add(new TabBean(1 , new GoodsMapFragment()) );
        tabs.add(new TabBean(2 , new TimeMapFragment()));
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return tabs.get(position).getFragment();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return super.isViewFromObject(view, object);
            }

            @Override
            public int getCount() {
                return tabs.size();
            }
            @Override
            public void finishUpdate(ViewGroup container) {
                super.finishUpdate(container);
                /*设置Viewpager的高度*/
                vp_statistic.setCurrentPagerHeight();
            }
            @Override
            public long getItemId(int position) {
                return tabs.get(position).getId();
            }
        };
        vp_statistic.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        //---展开侧边栏
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dl_main.isDrawerOpen(Gravity.LEFT)) {
                    dl_main.openDrawer(Gravity.LEFT);
                }
            }
        });
        //---冷
        vp_statistic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    dot_left.setBackgroundResource(R.drawable.red_round);
                    dot_right.setBackgroundResource(R.drawable.gray_round);
                }else if (position == 1){
                    dot_left.setBackgroundResource(R.drawable.gray_round);
                    dot_right.setBackgroundResource(R.drawable.red_round);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //---得到统计数据
        if(!LogicUtils.getInstance(mContext).isUserLogined()){
            return;
        }
        queryStatisticParams.setSellerId("53");
        queryStatisticParams.setMonth("2");
        String url = MyHttpUrl.QUERY_SELLER_STATISTIC+gson.toJson(queryStatisticParams);
        MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<StatisticJson>() {
            @Override
            public void onResponse(StatisticJson statisticJson) {
                if (statisticJson.RESULT_CODE == 0){
                    List<Point> amountData = new ArrayList<>() ;
                    List<Point> yyData = new ArrayList<>() ;
                    List<Point> ydData = new ArrayList<>() ;
                    for (StatisticJson.Sale sale : statisticJson.RESULT_DATA.saleMap){
                        amountData.add(new Point(sale.date , sale.amount));
                        yyData.add(new Point(sale.date , sale.yyCount));
                        ydData.add(new Point(sale.date , sale.ydCount));
                        lc_statistic.addData(amountData , getResources().getColor(R.color.sta_amount));
                        lc_statistic.addData(yyData , getResources().getColor(R.color.sta_yy));
                        lc_statistic.addData(ydData , getResources().getColor(R.color.sta_yd));
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                MyLog.e(TAG , "获取统计信息异常=="+e.getMessage());
            }
        });
    }
}









