package com.yyyu.ezbooking_seller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.ui.widget.NoScrollListView;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerOrderJson;

import java.util.List;

/**
 * 功能：OrderManagerFragment中最外层RecyclerView对应的Item
 *
 *
 * Created by yyyu on 2016/8/22.
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder>{

    private static final String TAG = "OrderItemAdapter";

    private Context mContext;
    private List<QuerySellerOrderJson.OrderItem> mData;

    public OrderItemAdapter(Context context , List<QuerySellerOrderJson.OrderItem> data){
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public OrderItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_item_order_show  , parent , false);

        return new OrderItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderItemHolder holder, int position) {

        MyLog.e(TAG , "onBindViewHolder==============position："+position);
        
        QuerySellerOrderJson.OrderItem orderItem = mData.get(position);
        holder.tv_order_date.setText("时间："+orderItem.orderTime);
        holder.tv_order_num.setText("预订数："+orderItem.orderCount);
        holder.tv_desk_num.setText("桌数："+orderItem.deskCount);
        holder.tv_person_num.setText("人数："+orderItem.personCount);
        holder.lv_order_item.setAdapter(new OrderAdapter(mContext ,orderItem.orderList));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setMData(List<QuerySellerOrderJson.OrderItem> data){
        this.mData = data;
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public TextView tv_order_date;
        public TextView tv_order_num;
        public TextView tv_desk_num;
        public TextView tv_person_num;
        public NoScrollListView lv_order_item;

        public OrderItemHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_order_date = (TextView) itemView.findViewById(R.id.tv_order_date);
            tv_order_num = (TextView) itemView.findViewById(R.id.tv_order_num);
            tv_desk_num = (TextView) itemView.findViewById(R.id.tv_desk_num);
            tv_person_num = (TextView) itemView.findViewById(R.id.tv_person_num);
            lv_order_item = (NoScrollListView) itemView.findViewById(R.id.lv_order_item);
        }
    }

}
