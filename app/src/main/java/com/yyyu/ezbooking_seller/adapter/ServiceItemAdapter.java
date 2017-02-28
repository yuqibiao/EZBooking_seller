package com.yyyu.ezbooking_seller.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerServiceJson;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;

import java.util.HashMap;
import java.util.List;

/**
 * 功能：EditSellerServiceFragment 中服务项Adapter
 *
 * @author yyyu
 * @date 2016/9/17
 */
public class ServiceItemAdapter extends BaseAdapter{

    private Context mContext;
    private List<QuerySellerServiceJson.Service.Item> mSiLstMap;
    /**Item中checkbox状态的维护*/
    private SparseArray<Boolean> ItemcheckedHolder;

    public ServiceItemAdapter(Context context , List<QuerySellerServiceJson.Service.Item> siLstMap){
        this.mContext = context;
        this.mSiLstMap = siLstMap;
        ItemcheckedHolder = new SparseArray<>();
    }

    @Override
    public int getCount() {
        return mSiLstMap.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return mSiLstMap.get(position).serviceItemId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemView  = LayoutInflater.from(mContext).inflate(R.layout.pt_service_item , parent , false);
        ImageView iv_service_item = (ImageView) itemView.findViewById(R.id.iv_service_item);
        CheckBox cb_service_item = (CheckBox) itemView.findViewById(R.id.cb_service_item);
        cb_service_item.setText(mSiLstMap.get(position).serviceItemName);
        if(mSiLstMap.get(position).status==0){
            cb_service_item.setChecked(false);
            ItemcheckedHolder.put(mSiLstMap.get(position).serviceItemId , false);
        }else{
            cb_service_item.setChecked(true);
            ItemcheckedHolder.put(mSiLstMap.get(position).serviceItemId , true);
        }
        MyBitmapUtils.getInstance(mContext).display(iv_service_item
                , MyHttpUrl.BASE_IMAGE_PATH+ mSiLstMap.get(position).serviceItemPic);
        cb_service_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ItemcheckedHolder.put(mSiLstMap.get(position).serviceItemId , isChecked);
            }
        });
        return itemView;
    }

    public SparseArray getItemCheckedHolder(){

        return ItemcheckedHolder;
    }

}
