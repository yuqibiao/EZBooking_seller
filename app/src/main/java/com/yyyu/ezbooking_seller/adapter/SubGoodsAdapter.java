package com.yyyu.ezbooking_seller.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.viewexplosion.ExplosionField;
import com.example.administrator.viewexplosion.factory.FallingParticleFactory;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.widget.RoundImageView;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.barbecue.ezbooking_base.utils.MyToast;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerGoodsJson;
import com.yyyu.ezbooking_seller.bean.params.DeleteGoodsParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.ui.dialog.EditGoodsDialogFragment;

import java.util.List;

/**
 * 功能：商品设置中，每个类别中GridView的Adapter
 *
 * @author yyyu
 * @date 2016/8/13
 */
public class SubGoodsAdapter extends BaseAdapter {

    private Activity mContext;
    private List<QuerySellerGoodsJson.Goods> mData;
    private String goodsTypeName;
    private int goodsTypeId;


    public SubGoodsAdapter(Activity context , List<QuerySellerGoodsJson.Goods> data ,
                           String goodsTypeName , int goodsTypeId) {
        this.mContext = context;
        this.mData = data;
        this.goodsTypeName = goodsTypeName;
        this.goodsTypeId = goodsTypeId;
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
        final GoodsHolder goodsHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gv_item_goods, parent, false);
            goodsHolder = new GoodsHolder();
            goodsHolder.rriv_goods_icon = (RoundImageView) convertView.findViewById(R.id.rriv_goods_icon);
            goodsHolder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
            goodsHolder.tv_goods_price = (TextView) convertView.findViewById(R.id.tv_goods_price);
            goodsHolder.tv_goods_edit = (TextView) convertView.findViewById(R.id.tv_goods_edit);
            goodsHolder.tv_goods_delete = (TextView) convertView.findViewById(R.id.tv_goods_delete);
            convertView.setTag(goodsHolder);
        } else {
            goodsHolder = (GoodsHolder) convertView.getTag();
        }
        //---set
        final QuerySellerGoodsJson.Goods goods = mData.get(position);
        goodsHolder.tv_goods_name.setText(""+goods.goodsName);
        goodsHolder.tv_goods_price.setText(""+goods.goodsPrice);
        MyBitmapUtils.getInstance(mContext).display(goodsHolder.rriv_goods_icon , goods.bigImage);
        //---编辑
        goodsHolder.tv_goods_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditGoodsDialogFragment editGoodsDialogFragment = new EditGoodsDialogFragment(
                        goods , goodsTypeName ,goodsTypeId );
                if (mContext instanceof FragmentActivity) {
                    editGoodsDialogFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), "edit_seller_goods");
                } else {
                    throw new UnsupportedOperationException("该Activity要继承自FragmentActivity");
                }
            }
        });
        //---删除
        final View finalConvertView = convertView;
        goodsHolder.tv_goods_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("删除")
                        .setMessage("确认删除本件商品？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //---TODO
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //----删除商品，更新数据
                                DeleteGoodsParams params = new DeleteGoodsParams();
                                params.setGoodsId(""+goods.goodsId);
                                String url = MyHttpUrl.DELETE_SELLER_GOODS+new Gson().toJson(params);
                                MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<String >() {
                                    @Override
                                    public void onError(Request request, Exception e) {
                                        MyToast.showShort(mContext , "删除失败，检查你的网络");
                                    }
                                    @Override
                                    public void onResponse(String response) {
                                        ExplosionField explosionField = new ExplosionField(mContext, new FallingParticleFactory());
                                        explosionField.explode(finalConvertView);
                                        explosionField.setOnExplosionFinishedListener(new ExplosionField.OnExplosionFinishedListener() {
                                            @Override
                                            public void onExplosionFinished() {
                                                mData.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        });
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }

    public static class GoodsHolder {
        public RoundImageView rriv_goods_icon;
        public TextView tv_goods_name;
        public TextView tv_goods_price;
        public TextView tv_goods_edit;
        public TextView tv_goods_delete;
    }

}
