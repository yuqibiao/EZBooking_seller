package com.yyyu.ezbooking_seller.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.widget.NoScrollListView;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.barbecue.ezbooking_base.utils.MyToast;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.foo.OrderAcceptEvent;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerOrderJson;
import com.yyyu.ezbooking_seller.bean.params.AcceptOrderParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：OrderManagerFragment中订单ListView的Adapter
 *
 * Created by yyyu on 2016/8/22.
 */
public class OrderAdapter extends BaseAdapter{

    private static final String TAG = "OrderAdapter";

    private Context mContext;
    private List<QuerySellerOrderJson.Order> mData;

    public OrderAdapter(Context context  , List<QuerySellerOrderJson.Order> data){
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MyLog.e(TAG , "OrderAdapter====getView");

        final OrderHolder orderHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_order , parent , false);
            orderHolder = new OrderHolder();
            orderHolder.civ_user_icon = (CircleImageView) convertView.findViewById(R.id.civ_user_icon);
            orderHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            orderHolder.tv_user_tel = (TextView) convertView.findViewById(R.id.tv_user_tel);
            orderHolder.tv_desk_num = (TextView) convertView.findViewById(R.id.tv_desk_num);
            orderHolder.tv_person_num = (TextView) convertView.findViewById(R.id.tv_person_num);
            orderHolder.tv_order_tip = (TextView) convertView.findViewById(R.id.tv_order_tip);
            orderHolder.ll_order = (LinearLayout) convertView.findViewById(R.id.ll_order);
            orderHolder.lv_order_goods = (NoScrollListView) convertView.findViewById(R.id.lv_order_goods);
            orderHolder.tv_pay_num = (TextView) convertView.findViewById(R.id.tv_pay_num);
            orderHolder.tv_words_left = (TextView) convertView.findViewById(R.id.tv_words_left);
            orderHolder.tv_order_option = (TextView) convertView.findViewById(R.id.tv_order_option);
            orderHolder.tv_order_status = (TextView) convertView.findViewById(R.id.tv_order_status);
            convertView.setTag(orderHolder);
        }else{
            orderHolder = (OrderHolder) convertView.getTag();
        }
        final QuerySellerOrderJson.Order order = mData.get(position);
        MyBitmapUtils.getInstance(mContext).display(orderHolder.civ_user_icon , order.userPic);
        orderHolder.tv_username.setText(""+order.userName);
        orderHolder.tv_desk_num.setText(""+order.deskCount);
        orderHolder.tv_person_num.setText(""+order.personCount);
        if(TextUtils.isEmpty(order.orderDesc)){
            orderHolder.tv_words_left.setText("留言：无");
        }else{
            orderHolder.tv_words_left.setText("留言："+order.orderDesc);
        }
        if(order.orderType == 1 ){//仅预约
            orderHolder.tv_order_status.setText("仅预约");
            orderHolder.tv_order_tip.setVisibility(View.VISIBLE);
            orderHolder.ll_order.setVisibility(View.GONE);
        }else if(order.orderType ==2){//预约+预定
            orderHolder.tv_order_status.setText("预约+预定");
            orderHolder.tv_order_tip.setVisibility(View.GONE);
            orderHolder.ll_order.setVisibility(View.VISIBLE);
            orderHolder.lv_order_goods.setAdapter(new OrderGoodsAdapter(mContext , order.subOrderList));
            orderHolder.tv_pay_num.setText(""+order.orderPrice+"RMB"+"  已支付完成");
        }
        if (order.orderStatus == 1){
            orderHolder.tv_order_option.setTextColor(mContext.getResources().getColor(R.color.my_blue));
            orderHolder.tv_order_option.setText("接单");
            orderHolder.tv_order_option.setEnabled(true);
        }else if(order.orderStatus == 2){
            orderHolder.tv_order_option.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            orderHolder.tv_order_option.setText("已接单");
            orderHolder.tv_order_option.setEnabled(false);
        }
        orderHolder.tv_order_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptOrderParams params = new AcceptOrderParams();
                params.setOrderId(order.orderId);
                String url = MyHttpUrl.ACCEPT_ORDER + new Gson().toJson(params);
                MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {

                    }
                    @Override
                    public void onResponse(String response) {
                        MyLog.e(TAG , "response==="+response);
                        order.orderStatus = 2; // 将数据集合中订单的状态改为已接单
                        orderHolder.tv_order_option.setText("已接单");
                        orderHolder.tv_order_option.setEnabled(false);
                        MyToast.showShort(mContext , "接单成功！！！");
                        EventBus.getDefault().post(new OrderAcceptEvent("Order Accepted"));
                    }
                });
            }
        });


        return convertView;
    }

    private class OrderHolder{

        public OrderHolder(){

        }

        public CircleImageView civ_user_icon;
        public TextView tv_username;//---接口没返回
        public TextView tv_user_tel;///---接口没返回
        public TextView tv_desk_num;//没返回
        public TextView tv_person_num;//没返回
        public TextView tv_order_tip;//---默认gone
        public LinearLayout ll_order;
        public NoScrollListView lv_order_goods;
        public TextView tv_pay_num;
        public TextView tv_words_left;
        public TextView tv_order_option;
        public TextView tv_order_status;

    }

}
