package com.yyyu.ezbooking_seller.ui.fragment;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseFragment;
import com.yyyu.barbecue.ezbooking_base.ui.pop.DatePickerPop;
import com.yyyu.barbecue.ezbooking_base.ui.widget.MyCalendarView;
import com.yyyu.barbecue.ezbooking_base.ui.widget.RefreshLayout;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.adapter.OrderItemAdapter;
import com.yyyu.ezbooking_seller.bean.foo.OrderAcceptEvent;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerOrderJson;
import com.yyyu.ezbooking_seller.bean.params.QuerySellerOrderParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能：订单管理
 * <p/>
 * Created by yyyu on 2016/8/12.
 */
public class OrderManagerFragment extends BaseFragment {

    private static final String TAG = "OrderManagerFragment";

    @ViewInject(R.id.ib_left)
    private ImageButton ib_left;
    @ViewInject(R.id.ib_pre_day)
    private ImageButton ib_pre_day;
    @ViewInject(R.id.ib_next_day)
    private ImageButton ib_next_day;
    @ViewInject(value = R.id.tv_order_date)
    private TextView tv_order_date;
    @ViewInject(value = R.id.rl_order_item)
    private RefreshLayout rl_order_show;
    @ViewInject(value = R.id.rv_order_item)
    private RecyclerView rv_order_item;

    private DatePickerPop datePickerPop;
    private QuerySellerOrderParams querySellerOrderParams;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 3;
    private List<QuerySellerOrderJson.OrderItem> orderItems;
    private String sellerId;
    private DrawerLayout dl_main;
    private OrderItemAdapter orderItemAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_manager;
    }

    @Override
    protected void beforeInit() {
        sellerId = LogicUtils.getInstance(mContext).getSellerId();
        orderItems = new ArrayList<>();
        //---init params
        querySellerOrderParams = new QuerySellerOrderParams();
        querySellerOrderParams.setCurrentPage(currentPage);
        querySellerOrderParams.setPageSize(PAGE_SIZE);
        //---init date picker pop
        datePickerPop = new DatePickerPop(
                this,
                getResources().getDisplayMetrics().widthPixels,
                DimensChange.dp2px(getActivity(), 250),
                LayoutInflater.from(getActivity()).inflate(R.layout.pop_calendar, null));
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        tv_order_date.setText("" + datePickerPop.getSelectedInfo());
        rl_order_show.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        rv_order_item.setLayoutManager(new LinearLayoutManager(mContext));
        dl_main = (DrawerLayout) getActivity().findViewById(R.id.dl_main);
    }

    @Override
    protected void initListener() {
        //---展开侧滑菜单
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dl_main.isDrawerOpen(Gravity.LEFT)){
                    dl_main.openDrawer(Gravity.LEFT);
                }
            }
        });
        //---显示日期选择
        tv_order_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerPop.showAsDropDown(tv_order_date);
            }
        });
        //---日历日期选择回调
        datePickerPop.getMyCalendar().setOnItemClickListener(new MyCalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
                tv_order_date.setText("" + datePickerPop.getSelectedInfo());
                querySellerOrderParams.setOrderDate(datePickerPop.getMyCalendar().getDatetime());
                getDataFromNet(0 , querySellerOrderParams);
                datePickerPop.dismiss();
            }
        });
        //---前一天
        ib_pre_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerPop.getMyCalendar().toPreDay();
                tv_order_date.setText("" + datePickerPop.getSelectedInfo());
                querySellerOrderParams.setOrderDate(datePickerPop.getMyCalendar().getDatetime());
                getDataFromNet(0 , querySellerOrderParams);
            }
        });
        //---后一天
        ib_next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerPop.getMyCalendar().toNextDay();
                tv_order_date.setText("" + datePickerPop.getSelectedInfo());
                querySellerOrderParams.setOrderDate(datePickerPop.getMyCalendar().getDatetime());
                getDataFromNet(0 , querySellerOrderParams);
            }
        });
        //---刷新数据
        rl_order_show.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(0 , querySellerOrderParams);
            }
        });
        //---加载更多
        rl_order_show.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                MyLog.e(TAG , "j加载更多============");
                getDataFromNet(1 , querySellerOrderParams);
            }
        });
    }

    @Override
    protected void initData() {
        querySellerOrderParams.setSellerId(sellerId);
        querySellerOrderParams.setOrderDate(datePickerPop.getMyCalendar().getDatetime());
        getDataFromNet(0 , querySellerOrderParams);

    }

    /**
     * 从网络获取订单数据
     *
     * @param type 0：刷新 1：加载更多
     * @param querySellerOrderParams
     */
    private void getDataFromNet(int type, QuerySellerOrderParams querySellerOrderParams) {
        if (type == 0) {
            currentPage=1;
            querySellerOrderParams.setCurrentPage(currentPage);
            String url = MyHttpUrl.QUERY_SELLER_ORDER + gson.toJson(querySellerOrderParams);
            MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QuerySellerOrderJson>() {
                @Override
                public void onError(Request request, Exception e) {
                    MyLog.e(TAG , "网络加载出错"+e.getMessage());
                    MySnackBar.showShortDef(getActivity() , "获取订单信息失败，检查你的网络！！！");
                }
                @Override
                public void onResponse(QuerySellerOrderJson querySellerOrderJson) {
                    MyLog.e(TAG , "RESULT_CODE======="+querySellerOrderJson.RESULT_CODE);
                    if(querySellerOrderJson.RESULT_CODE==0){
                        orderItems = querySellerOrderJson.RESULT_DATA;
                        rv_order_item.setAdapter( orderItemAdapter = new OrderItemAdapter(mContext , orderItems));
                        rl_order_show.setRefreshing(false);
                    }
                }
            });
        } else if (type == 1) {
            currentPage ++;
            querySellerOrderParams.setCurrentPage(currentPage);
            String url = MyHttpUrl.QUERY_SELLER_ORDER + gson.toJson(querySellerOrderParams);
            MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QuerySellerOrderJson>() {
                @Override
                public void onError(Request request, Exception e) {
                    MyLog.e(TAG , "网络加载出错"+e.getMessage());
                    MySnackBar.showShortDef(getActivity() , "获取订单信息失败，检查你的网络！！！");
                }
                @Override
                public void onResponse(QuerySellerOrderJson querySellerOrderJson) {
                    MyLog.e(TAG , "RESULT_CODE======="+querySellerOrderJson.RESULT_CODE);
                    if(querySellerOrderJson.RESULT_CODE==0){
                        if(orderItemAdapter != null){
                            orderItems.addAll(querySellerOrderJson.RESULT_DATA);
                            orderItemAdapter.setMData(orderItems);
                            orderItemAdapter.notifyDataSetChanged();
                            rl_order_show.setLoadFinished();
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderAccepted(OrderAcceptEvent acceptEvent){
        if(orderItemAdapter != null){
            orderItemAdapter.notifyDataSetChanged();
        }
    }


}








