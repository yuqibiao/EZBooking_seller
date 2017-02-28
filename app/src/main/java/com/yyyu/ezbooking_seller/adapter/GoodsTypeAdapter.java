package com.yyyu.ezbooking_seller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.ui.widget.NoScrollGridView;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerGoodsJson;

import java.util.List;

/**
 * 功能：商品设置中分类商品的Adapter
 *
 * @author yyyu
 * @date 2016/8/13
 */
public class GoodsTypeAdapter extends BaseAdapter{

    private Activity mContext;
    private List<QuerySellerGoodsJson.GoodsType> mData;

    public GoodsTypeAdapter(Activity context , List<QuerySellerGoodsJson.GoodsType> data){
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
        GoodsTypeHolder goodsTypeHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item_goods_show , parent , false);
            goodsTypeHolder = new GoodsTypeHolder();
            goodsTypeHolder.tv_goods_type_name = (TextView) convertView.findViewById(R.id.tv_goods_type_name);
            goodsTypeHolder.gv_type_goods_show = (NoScrollGridView) convertView.findViewById(R.id.gv_type_goods_show);
            convertView.setTag(goodsTypeHolder);
        }else{
            goodsTypeHolder = (GoodsTypeHolder) convertView.getTag();
        }
        QuerySellerGoodsJson.GoodsType goodsType = mData.get(position);
        goodsTypeHolder.tv_goods_type_name.setText(""+goodsType.goodsTypeName);
        goodsTypeHolder.gv_type_goods_show.setAdapter(new SubGoodsAdapter(
                mContext , goodsType.goodsList , goodsType.goodsTypeName , goodsType.goodsTypeId));
        return convertView;
    }

    public static class GoodsTypeHolder {
        public TextView tv_goods_type_name;
        public NoScrollGridView gv_type_goods_show;
    }

}
