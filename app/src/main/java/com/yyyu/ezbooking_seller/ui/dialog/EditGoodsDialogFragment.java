package com.yyyu.ezbooking_seller.ui.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.EventBase;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseDialogFragment;
import com.yyyu.barbecue.ezbooking_base.ui.widget.RoundImageView;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.FormValidationUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyFileOprateUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.foo.GoodsChangeEvent;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerGoodsJson;
import com.yyyu.ezbooking_seller.bean.params.AddGoodsTypeParams;
import com.yyyu.ezbooking_seller.bean.params.QuerySellerGoodsParams;
import com.yyyu.ezbooking_seller.bean.params.SaveOrUpdateSellerGoodsParams;
import com.yyyu.ezbooking_seller.callback.OnGoodsTypeSelectedListener;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.ui.pop.FloatPicChoicePop;
import com.yyyu.ezbooking_seller.ui.pop.GoodsTypePop;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：编辑商品信息的Dialog
 *
 * @author yyyu
 * @date 2016/8/13
 */
public class EditGoodsDialogFragment extends BaseDialogFragment {

    private static final String TAG = "EditGoodsDialogFragment";

    @ViewInject(value = R.id.et_goods_name)
    private EditText et_goods_name;
    @ViewInject(value = R.id.et_goods_price)
    private EditText et_goods_price;
    @ViewInject(value = R.id.riv_goods_icon)
    private RoundImageView riv_goods_icon;
    @ViewInject(value = R.id.tv_select_goods_type)
    private TextView tv_select_goods_type;
    @ViewInject(value = R.id.et_add_goods_type)
    private EditText et_add_goods_type;
    @ViewInject(value = R.id.tv_add_goods_type)
    private TextView tv_add_goods_type;
    @ViewInject(value = R.id.tv_cancel)
    private TextView tv_cancel;
    @ViewInject(value = R.id.tv_confirm)
    private TextView tv_confirm;
    @ViewInject(value = R.id.tv_add_goods_icon)
    private TextView tv_add_goods_icon;

    private QuerySellerGoodsJson.Goods mGoods;
    private String goodsTypeName;
    private SaveOrUpdateSellerGoodsParams saveOrUpdateSellerGoodsParams;
    private String sellerId;
    private int goodsType = -1;
    private GoodsTypePop pop;

    public EditGoodsDialogFragment(QuerySellerGoodsJson.Goods goods , String goodsTypeName , int goodsTypeId){
        this.mGoods = goods;
        this.goodsTypeName = goodsTypeName;
        this.goodsType = goodsTypeId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_edit_goods;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        sellerId = LogicUtils.getInstance(mContext).getSellerId();
        saveOrUpdateSellerGoodsParams = new SaveOrUpdateSellerGoodsParams();
    }

    @Override
    protected void initView() {
        et_goods_name.setText(""+mGoods.goodsName);
        et_goods_price.setText(""+mGoods.goodsPrice);
        MyBitmapUtils.getInstance(mContext).display(riv_goods_icon , mGoods.bigImage);
        tv_select_goods_type.setText(""+goodsTypeName);
        View viewPop = LayoutInflater.from(mContext).inflate(R.layout.pop_goods_type_show , null);
        pop = new GoodsTypePop(
                EditGoodsDialogFragment.this ,
                DimensChange.dp2px(mContext , 120),
                DimensChange.dp2px(mContext,150),
                viewPop);
    }

    @Override
    protected void initListener() {
        {
            //---选择或者新增商品分类
            tv_select_goods_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] location = new int[2];
                    View target = et_add_goods_type;
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
        //---上传图片
        tv_add_goods_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popView = View.inflate(mContext, R.layout.pop_float_pic_choice, null);
                popView.measure(0, 0);
                FloatPicChoicePop floatPicChoicePop = new FloatPicChoicePop(
                        EditGoodsDialogFragment.this ,
                        DimensChange.dp2px(mContext , 100),DimensChange.dp2px(mContext , 70) ,
                        popView);
                floatPicChoicePop.show(tv_add_goods_icon);

            }
        });
        //---确认
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et_goods_name.getText().toString())
                        ||TextUtils.isEmpty(et_goods_price.getText().toString())){
                    MySnackBar.showShortDef(getActivity() , "商品名称或价格不能为空");
                    return ;
                }
                if (goodsType == -1){
                    MySnackBar.showShortDef(getActivity() , "请选择商品类型");
                    return;
                }
                //---save goods
                saveOrUpdateSellerGoodsParams.setGoodsId(""+mGoods.goodsId);
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

                MyLog.e(TAG , "data=="+gson.toJson(saveOrUpdateSellerGoodsParams));

                MyHttpManager.postAsyn(MyHttpUrl.SAVE_OR_UPDATE_SELLER_GOODS, new MyHttpManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e(TAG, "网络请求异常===" + e.getMessage());
                    }
                    @Override
                    public void onResponse(String response) {
                        EventBus.getDefault().post(new GoodsChangeEvent("goods changed"));
                    }
                }, params);
                dismiss();
            }
        });
        //---取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
                        MediaUtils.crop(this, uri );
                    }
                    break;
                }
                case MediaUtils.PHOTO_REQUEST_CUT:{
                    Bitmap bitmap = data.getParcelableExtra("data");
                    String savePath = MyFileOprateUtils.saveBitmap(getActivity(), bitmap, "crop_temp.jpg");
                    saveOrUpdateSellerGoodsParams.setBigImage(MyFileOprateUtils.imgToBase64(savePath, mContext));
                    riv_goods_icon.setImageBitmap(bitmap);
                    break;
                }
            }
        }

    }
}
