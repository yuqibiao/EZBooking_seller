package com.yyyu.ezbooking_seller.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.viewexplosion.ExplosionField;
import com.example.administrator.viewexplosion.factory.FlyawayFactory;
import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.dialog.ChoicePicDialog;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseFragment;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.adapter.ServiceItemAdapter;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerServiceJson;
import com.yyyu.ezbooking_seller.bean.params.QuerySellerServiceParams;
import com.yyyu.ezbooking_seller.bean.params.SaveOrUpdateSellerServiceParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：商家服务设置
 * <p/>
 * Created by yyyu on 2016/8/12.
 */
public class EditSellerServiceFragment extends BaseFragment {

    private static final String TAG = "EditSellerServiceFragment";

    @ViewInject(R.id.ib_left)
    private ImageButton ib_left;
    @ViewInject(value = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(value = R.id.tv_right)
    private TextView tv_right;
    @ViewInject(value = R.id.tv_add_service_icon)
    private TextView tv_add_service_icon;
    @ViewInject(value = R.id.iv_service_icon)
    private ImageView iv_service_icon;
    @ViewInject(value = R.id.tv_seller_desc)
    private TextView tv_seller_desc;
    @ViewInject(value = R.id.tv_edit_finished)
    private TextView tv_edit_finished;
    @ViewInject(R.id.gv_service_item)
    private GridView gv_service_item;
    @ViewInject(R.id.cb_desk)
    private CheckBox cb_desk;
    @ViewInject(value = R.id.cb_goods)
    private CheckBox cb_goods;
    @ViewInject(R.id.et_desk_limit)
    private EditText et_desk_limit;

    private SaveOrUpdateSellerServiceParams saveOrUpdateParams;
    private QuerySellerServiceParams querySellerServiceParams;
    private DrawerLayout dl_main;
    private ServiceItemAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_seller_service;
    }

    @Override
    protected void beforeInit() {
        saveOrUpdateParams = new SaveOrUpdateSellerServiceParams();
        querySellerServiceParams = new QuerySellerServiceParams();
        String sellerId = LogicUtils.getInstance(mContext).getSellerId();
        saveOrUpdateParams.setSellerId(sellerId);
        querySellerServiceParams.setSellerId(sellerId);
    }

    @Override
    protected void initView() {
        tv_title.setText("服务");
        tv_right.setText("服务/菜单");
        dl_main = (DrawerLayout) getActivity().findViewById(R.id.dl_main);
    }

    @Override
    protected void initListener() {
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dl_main.isDrawerOpen(Gravity.LEFT)){
                    dl_main.openDrawer(Gravity.LEFT);
                }
            }
        });
        //---编辑完成
        tv_edit_finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isDigitsOnly(et_desk_limit.getText().toString())){
                    MySnackBar.showShortDef(getActivity() , "输入的桌数不合法");
                    return;
                }
                List<SaveOrUpdateSellerServiceParams.ServiceItem> serviceItemList = new ArrayList<>();
                SparseArray<Boolean> ItemCheckedHolder = adapter.getItemCheckedHolder();
                for (int i=0 ; i<ItemCheckedHolder.size() ; i++){
                    Boolean isChecked = ItemCheckedHolder.valueAt(i);
                    if(isChecked){
                        SaveOrUpdateSellerServiceParams.ServiceItem item = new SaveOrUpdateSellerServiceParams.ServiceItem();
                        item.setServiceItemId(ItemCheckedHolder.keyAt(i));
                        serviceItemList.add(item);
                    }
                }
                saveOrUpdateParams.setServiceItemIds(serviceItemList);
                if (cb_desk.isChecked()){
                    saveOrUpdateParams.setIsDesk(1);
                    saveOrUpdateParams.setDeskCount(Integer.parseInt(et_desk_limit.getText().toString()));
                }else{
                    saveOrUpdateParams.setIsDesk(0);
                }
                if (cb_goods.isChecked()){
                    saveOrUpdateParams.setIsGoods(1);
                }else{
                    saveOrUpdateParams.setIsGoods(0);
                }
                String url = MyHttpUrl.SAVE_OR_UPDATE_SELLER_SERVICE+gson.toJson(saveOrUpdateParams);

                MyLog.e(TAG , "url=修改服务==="+url);

                MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e(TAG , "网络请求出错=="+e.getMessage());
                        MySnackBar.showShortDef(getActivity() , "提交失败，检查你的网络！！！");
                    }
                    @Override
                    public void onResponse(String response) {
                        ExplosionField explosionField = new ExplosionField(mContext, new FlyawayFactory());
                        explosionField.explode(tv_edit_finished);
                        tv_edit_finished.setVisibility(View.GONE);
                        //---设置一系列控件不可编辑
                        cb_desk.setEnabled(false);
                        cb_goods.setEnabled(false);
                        et_desk_limit.setEnabled(false);
                        MyLog.e(TAG , "response=="+response);
                        MySnackBar.showShortDef(getActivity() , "商家服务设置成功！！！");
                    }
                });

            }

        });
    }

    @Override
    protected void initData() {
        String url = MyHttpUrl.QUERY_SELLER_SERVICE+gson.toJson(querySellerServiceParams);

        MyLog.e(TAG , "查询服务信息URL=="+url);

        MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QuerySellerServiceJson>() {

            @Override
            public void onError(Request request, Exception e) {
                MyLog.e(TAG , "网络错误=="+e.getMessage());
            }

            @Override
            public void onResponse(QuerySellerServiceJson sellerServiceJson) {
                if(sellerServiceJson.RESULT_CODE==0){
                    QuerySellerServiceJson.Service service = sellerServiceJson.RESULT_DATA;
                    MyBitmapUtils.getInstance(mContext).display(iv_service_icon , service.bigImage);
                    tv_seller_desc.setText(""+service.sellerDesc);
                    for(QuerySellerServiceJson.Service.Item item :service.siLstMap){

                    }
                    if(service.isDesk==1){
                        cb_desk.setChecked(true);
                    }else{
                        cb_desk.setChecked(false);
                    }
                    et_desk_limit.setText(""+service.deskCount);
                    if(service.isGoods==1){
                        cb_goods.setChecked(true);
                    }else{
                        cb_goods.setChecked(false);
                    }
                    //---init service item gridView
                    int length = DimensChange.dp2px(mContext , 50);
                    int space = DimensChange.dp2px(mContext , 2);
                    int gridViewWidth = service.siLstMap.size() * (length+space)+DimensChange.dp2px(mContext,20);
                    int itemWidth = length;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                    gv_service_item.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
                    gv_service_item.setColumnWidth(itemWidth); // 设置列表项宽
                    gv_service_item.setHorizontalSpacing(space); // 设置列表项水平间距
                    gv_service_item.setStretchMode(GridView.NO_STRETCH);
                    gv_service_item.setNumColumns( service.siLstMap.size()); // 设置列数量=列表集合数
                    adapter = new ServiceItemAdapter(mContext ,service.siLstMap );
                    gv_service_item.setAdapter(adapter);
                }
            }
        });
    }


}
