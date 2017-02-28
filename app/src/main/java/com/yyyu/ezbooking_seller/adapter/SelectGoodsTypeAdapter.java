package com.yyyu.ezbooking_seller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yyyu.barbecue.ezbooking_base.callback.OnRvItemClickListener;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QueryGoodsTypeJson;

import java.util.List;

/**
 * 功能：选择商品类型的时候的Adapter
 *
 * @author yyyu
 * @date 2016/6/1
 */
public class SelectGoodsTypeAdapter<T> extends RecyclerView.Adapter<SelectGoodsTypeAdapter.MyHolder> {

    private OnRvItemClickListener onRvItemClickListener;
    private Context context;
    private List<QueryGoodsTypeJson.GoodsType> data;

    public SelectGoodsTypeAdapter(Context context, List<QueryGoodsTypeJson.GoodsType> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SelectGoodsTypeAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_goods_type, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectGoodsTypeAdapter.MyHolder holder, final int position) {
        QueryGoodsTypeJson.GoodsType bean= data.get(position);
        holder.tv_pop_item.setText(bean.goodsTypeName);
        holder.tv_pop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRvItemClickListener!=null){
                    onRvItemClickListener.onRvItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_pop_item;

        public MyHolder(View itemView) {
            super(itemView);
            tv_pop_item = (TextView) itemView.findViewById(R.id.tv_pop_item);
        }
    }

    public void  setOnRvClickListener(OnRvItemClickListener onRvItemClickListener){
        this.onRvItemClickListener = onRvItemClickListener;
    }

}
