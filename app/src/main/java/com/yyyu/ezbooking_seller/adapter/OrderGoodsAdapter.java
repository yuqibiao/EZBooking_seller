package com.yyyu.ezbooking_seller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerOrderJson;

import java.util.List;

/**
 * 功能：OrderManagerFragment中某个订单的商品ListView对应的Adapter
 *
 *
 * Created by yyyu on 2016/8/22.
 */
public class OrderGoodsAdapter extends BaseAdapter{

    private Context mContext;
    private List<QuerySellerOrderJson.OrderGoods> mData;

    public OrderGoodsAdapter(Context context , List<QuerySellerOrderJson.OrderGoods> data){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        SubOrderGoodsHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_order_goods , parent , false);
            holder = new SubOrderGoodsHolder();
            holder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            holder.tv_goods_num = (TextView) convertView.findViewById(R.id.tv_goods_num);
            convertView.setTag(holder);
        }else{
            holder = (SubOrderGoodsHolder) convertView.getTag();
        }
        holder.tv_goods_name.setText(""+mData.get(position).goodsName);
        holder.tv_goods_num.setText("x"+mData.get(position).goodsCount);
        return convertView;
    }

    public static class SubOrderGoodsHolder{
        public TextView tv_goods_name;
        public TextView tv_goods_num;
    }

}
