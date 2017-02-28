package com.yyyu.ezbooking_seller.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.OnClick;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.dialog.ChoicePicDialog;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseFragment;
import com.yyyu.barbecue.ezbooking_base.ui.widget.NoScrollListView;
import com.yyyu.barbecue.ezbooking_base.ui.widget.RoundImageView;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.FormValidationUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyFileOprateUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.adapter.GoodsTypeAdapter;
import com.yyyu.ezbooking_seller.bean.foo.GoodsChangeEvent;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerGoodsJson;
import com.yyyu.ezbooking_seller.bean.params.AddGoodsTypeParams;
import com.yyyu.ezbooking_seller.bean.params.QuerySellerGoodsParams;
import com.yyyu.ezbooking_seller.bean.params.SaveOrUpdateSellerGoodsParams;
import com.yyyu.ezbooking_seller.callback.OnGoodsTypeSelectedListener;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.ui.pop.GoodsTypePop;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：商家商品设置
 * <p/>
 * Created by yyyu on 2016/8/12.
 */
public class EditSellerGoodsFragment extends BaseFragment {

    private static final String TAG = "EditSellerGoodsFragment";

    @ViewInject(R.id.ib_left)
    private ImageButton ib_left;
    @ViewInject(value = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(value = R.id.tv_right)
    private TextView tv_right;
    @ViewInject(value = R.id.lv_type_goods)
    private NoScrollListView lv_type_goods;
    @ViewInject(value = R.id.pt_add_goods)
    private View pt_add_goods;
    @ViewInject(value = R.id.tv_add_finished)
    private TextView tv_add_finished;
    @ViewInject(value = R.id.et_goods_name)
    private EditText et_goods_name;
    @ViewInject(value = R.id.et_goods_price)
    private EditText et_goods_price;
    @ViewInject(R.id.et_add_goods_type)
    private EditText et_add_goods_type;
    @ViewInject(value = R.id.tv_select_goods_type)
    private TextView tv_select_goods_type;
    @ViewInject(value = R.id.tv_add_goods_type)
    private TextView tv_add_goods_type;
    @ViewInject(value = R.id.riv_goods_icon)
    private RoundImageView riv_goods_icon;

    private QuerySellerGoodsParams querySellerGoodsParams;
    private SaveOrUpdateSellerGoodsParams saveOrUpdateSellerGoodsParams;
    private String sellerId;
    private int goodsType = -1;
    private GoodsTypePop pop;
    private DrawerLayout dl_main;

    @Override
    protected void beforeInit() {
        super.beforeInit();
        sellerId = LogicUtils.getInstance(mContext).getSellerId();
        saveOrUpdateSellerGoodsParams = new SaveOrUpdateSellerGoodsParams();
        querySellerGoodsParams = new QuerySellerGoodsParams();
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_seller_goods;
    }

    @Override
    protected void initView() {
        tv_title.setText("商品设置");
        tv_right.setText("设置/商品");
        View viewPop = LayoutInflater.from(mContext).inflate(R.layout.pop_goods_type_show , null);
        pop = new GoodsTypePop(
                EditSellerGoodsFragment.this ,
                DimensChange.dp2px(mContext , 120),
                DimensChange.dp2px(mContext,150),
                viewPop);
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
        //---选择或者新增商品分类
        tv_select_goods_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int[] location = new int[2];
                    View target = tv_select_goods_type;
                    target.getLocationOnScreen(location);
                    pop.showAtLocation(target, Gravity.LEFT | Gravity.TOP,
                            location[0] - pop.getWidth() + target.getWidth(),
                            location[1] - pop.getHeight() - 5);
            }
        });
        //---设置商品分类EditText visible
        tv_add_goods_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_add_goods_type.getVisibility()==View.VISIBLE && !TextUtils.isEmpty(et_add_goods_type.getText().toString())) {
                    AddGoodsTypeParams params = new AddGoodsTypeParams();
                    params.setSellerId(sellerId);
                    params.setGoodsTypeName(et_add_goods_type.getText().toString());
                    String url = MyHttpUrl.ADD_SELLER_GOODS_TYPE + gson.toJson(params);
                    MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            MyLog.e(TAG, "网路错误==" + e.getMessage());
                            MySnackBar.showShortDef(getActivity() , "请检查你的网络！！！");
                        }
                        @Override
                        public void onResponse(String response) {
                            MyLog.e(TAG , "response==="+response);
                            et_add_goods_type.setVisibility(View.GONE);
                            tv_select_goods_type.setVisibility(View.VISIBLE);
                            MySnackBar.showShortDef(getActivity() , "添加商品类型成功！！！");
                        }
                    });
                } else {
                    et_add_goods_type.setVisibility(View.VISIBLE);
                    tv_select_goods_type.setVisibility(View.GONE);
                    MySnackBar.showShortDef(getActivity(), "现在可以点击编辑框新增类型了！！！");
                }
            }
        });

        //---选择商品类型回调
        pop.setOnGoodsTypeSelectedListener(new OnGoodsTypeSelectedListener() {
            @Override
            public void onGoodsTypeSelected(int goodsTypeId, String goodsTypename) {
                goodsType = goodsTypeId;
                tv_select_goods_type.setText(""+goodsTypename);
            }
        });
    }

    @Override
    protected void initData() {
        String sellerId = LogicUtils.getInstance(mContext).getSellerId();
        if (!TextUtils.isEmpty(sellerId)) {
            //---查询店家商品
            querySellerGoodsParams.setSellerId(sellerId);
            querySellerGoods(querySellerGoodsParams);
        }
    }

    /**
     * 查询商家商品
     */
    private void querySellerGoods(QuerySellerGoodsParams querySellerGoodsParams) {
        String url = MyHttpUrl.QUERY_SELLER_GOODS + gson.toJson(querySellerGoodsParams);
        MyLog.e(TAG, "url===" + url);
        MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QuerySellerGoodsJson>() {
            @Override
            public void onError(Request request, Exception e) {
                MyLog.e(TAG, "网络请求出错===" + e.getMessage());
            }

            @Override
            public void onResponse(QuerySellerGoodsJson querySellerGoodsJson) {
                if (querySellerGoodsJson.RESULT_CODE == 0) {
                    lv_type_goods.setAdapter(new GoodsTypeAdapter(getActivity(),
                            querySellerGoodsJson.RESULT_DATA));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case MediaUtils.PHOTO_REQUEST_CAMERA: {//相机
                    File iconFile = new File(MediaUtils.filePath, MediaUtils.PHOTO_FILE_NAME);
                    MediaUtils.crop(this, Uri.fromFile(iconFile));
                    break;
                }
                case MediaUtils.PHOTO_REQUEST_GALLERY: {//相册
                    if (data != null) {
                        Uri uri = data.getData();
                        MediaUtils.crop(this, uri);
                    }
                    break;
                }
                case MediaUtils.PHOTO_REQUEST_CUT: {
                    Bitmap bitmap = BitmapFactory.decodeFile(MediaUtils.filePath+MediaUtils.cropName);
                    riv_goods_icon.setImageBitmap(bitmap);
                    saveOrUpdateSellerGoodsParams.setBigImage(MyFileOprateUtils.imgToBase64(MediaUtils.filePath+MediaUtils.cropName, mContext));
                    break;
                }
            }
        }
    }

    @OnClick({R.id.ib_add_goods, R.id.tv_add_goods_icon,
            R.id.tv_add_finished})
    public void onInjectClick(View view) {
        switch (view.getId()) {
            case R.id.ib_add_goods: {//---显示添加商品
                pt_add_goods.setVisibility(View.VISIBLE);
                tv_add_finished.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.expand_show);
                pt_add_goods.setAnimation(animation);
                break;
            }
            case R.id.tv_add_goods_icon: {//---选择图片
                ChoicePicDialog choicePicDialog = new ChoicePicDialog(mContext, this);
                choicePicDialog.show();
                break;
            }
            case R.id.tv_add_finished: {//---完成新增

                if(TextUtils.isEmpty(et_goods_name.getText().toString())
                        ||TextUtils.isEmpty(et_goods_price.getText().toString())){
                    MySnackBar.showShortDef(getActivity() , "商品名称或价格不能为空");
                    return ;
                }
                if (goodsType == -1){
                    MySnackBar.showShortDef(getActivity() , "请选择商品类型");
                }
                //---save goods
                saveOrUpdateSellerGoodsParams.setSellerId(LogicUtils.getInstance(mContext).getSellerId());
                saveOrUpdateSellerGoodsParams.setGoodsName(et_goods_name.getText().toString());
                String goodsPrice = et_goods_price.getText().toString();
                if (FormValidationUtils.isDecimal(goodsPrice) || TextUtils.isDigitsOnly(goodsPrice)) {
                    saveOrUpdateSellerGoodsParams.setGoodsPrice(Double.parseDouble(goodsPrice));
                } else {
                    MySnackBar.showShortDef(getActivity(), "请输入合法的金额格式");
                    return;
                }
                saveOrUpdateSellerGoodsParams.setGoodsTypeId(goodsType);
                Map<String, String> params = new HashMap<>();
                params.put("method", "seller.saveOrUpSellerGoods");
                params.put("data", "" + gson.toJson(saveOrUpdateSellerGoodsParams));
                MyHttpManager.postAsyn(MyHttpUrl.SAVE_OR_UPDATE_SELLER_GOODS, new MyHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e(TAG, "网络请求异常===" + e.getMessage());
                    }
                    @Override
                    public void onResponse(String response) {
                        MyLog.e(TAG, "response===" + response);
                        initData();
                    }
                }, params);

                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.expand_hidden);
                pt_add_goods.setAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        animation.setFillAfter(false);
                        pt_add_goods.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                tv_add_finished.setVisibility(View.GONE);
                break;
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goodsChange(GoodsChangeEvent goodsChangeEvent) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
