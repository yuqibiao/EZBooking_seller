package com.yyyu.ezbooking_seller.ui.pop;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.callback.OnRvItemClickListener;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.pop.BasePopupWindow;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.adapter.SelectGoodsTypeAdapter;
import com.yyyu.ezbooking_seller.bean.json.QueryGoodsTypeJson;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerGoodsJson;
import com.yyyu.ezbooking_seller.bean.params.QueryGoodsTypeParams;
import com.yyyu.ezbooking_seller.callback.OnGoodsTypeSelectedListener;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import java.util.List;

/**
 * 功能：显示商品类型的Pop
 *
 * @author yyyu
 * @date 2016/8/27
 */
public class GoodsTypePop extends BasePopupWindow{

    private static final String TAG = "GoodsTypePop";

    private RecyclerView rv_goods_type;

    private OnGoodsTypeSelectedListener mOnGoodsTypeSelectedListener;

    public GoodsTypePop(Fragment fragment, int width, int height, View popView) {
        super(fragment, width, height, popView);
        initData();
    }

    public GoodsTypePop(Activity act, int width, int height, View popView) {
        super(act, width, height, popView);
        initData();
    }

    @Override
    protected void initView() {
        rv_goods_type = getView(R.id.rv_goods_type);
        rv_goods_type.setLayoutManager(new LinearLayoutManager(mAct));
    }

    @Override
    protected void initListener() {

    }

    private void initData() {
        QueryGoodsTypeParams params = new QueryGoodsTypeParams();
        params.setSellerId(LogicUtils.getInstance(mAct).getSellerId());
        String url = MyHttpUrl.QUERY_SELLER_GOODS_TYPE+new Gson().toJson(params);
        MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QueryGoodsTypeJson>() {
            @Override
            public void onError(Request request, Exception e) {
                MyLog.e(TAG , "网络请求失败=="+e.getMessage());
            }
            @Override
            public void onResponse(QueryGoodsTypeJson queryGoodsTypeJson) {
                if(queryGoodsTypeJson.RESULT_CODE==0){
                    final List<QueryGoodsTypeJson.GoodsType> types = queryGoodsTypeJson.RESULT_DATA;
                    SelectGoodsTypeAdapter adapter = new SelectGoodsTypeAdapter(mAct , types);
                    rv_goods_type.setAdapter(adapter);
                    adapter.setOnRvClickListener(new OnRvItemClickListener() {
                        @Override
                        public void onRvItemClick(int position) {
                            if(mOnGoodsTypeSelectedListener != null){
                                mOnGoodsTypeSelectedListener.onGoodsTypeSelected(
                                        types.get(position).goodsTypeId ,
                                        types.get(position).goodsTypeName);
                                dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void showAtLocation(View target, int gravity, int x, int y) {
        super.showAtLocation(target, gravity, x, y);
        initData();
    }

    public void setOnGoodsTypeSelectedListener(OnGoodsTypeSelectedListener onGoodsTypeSelectedListener){
        this.mOnGoodsTypeSelectedListener = onGoodsTypeSelectedListener;
    }

}
