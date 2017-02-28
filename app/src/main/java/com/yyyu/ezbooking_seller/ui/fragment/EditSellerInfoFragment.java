package com.yyyu.ezbooking_seller.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.viewexplosion.ExplosionField;
import com.example.administrator.viewexplosion.factory.FallingParticleFactory;
import com.example.administrator.viewexplosion.factory.FlyawayFactory;
import com.squareup.okhttp.Request;
import com.yyyu.barbecue.ezbooking_base.annotate.OnClick;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.net.MyHttpManager;
import com.yyyu.barbecue.ezbooking_base.ui.dialog.ChoicePicDialog;
import com.yyyu.barbecue.ezbooking_base.ui.fragment.BaseFragment;
import com.yyyu.barbecue.ezbooking_base.ui.widget.loading.ShapeLoadingDialog;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyFileOprateUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;
import com.yyyu.barbecue.ezbooking_base.utils.MyNetUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MySnackBar;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.json.GeoCodingJson;
import com.yyyu.ezbooking_seller.bean.json.QuerySellerInfoJson;
import com.yyyu.ezbooking_seller.bean.params.QuerySellerInfoParams;
import com.yyyu.ezbooking_seller.bean.params.UpdateSellerInfoParams;
import com.yyyu.ezbooking_seller.net.MyHttpUrl;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：商家基本信息编辑
 * <p/>
 * Created by yyyu on 2016/8/12.
 */
public class EditSellerInfoFragment extends BaseFragment {

    private final String TAG = "EditSellerInfoFragment";

    @ViewInject(R.id.ib_left)
    private ImageButton ib_left;
    @ViewInject(value = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(value = R.id.tv_right)
    private TextView tv_right;
    @ViewInject(value = R.id.iv_seller_icon)
    private ImageView iv_seller_icon;
    @ViewInject(value = R.id.tv_add_seller_icon)
    private TextView tv_add_seller_icon;
    @ViewInject(value = R.id.et_seller_name)
    private EditText et_seller_name;
    @ViewInject(value = R.id.et_seller_contact)
    private EditText et_seller_contact;
    @ViewInject(value = R.id.et_seller_tel)
    private EditText et_seller_tel;
    @ViewInject(value = R.id.et_seller_location)
    private EditText et_seller_location;
    @ViewInject(value = R.id.tv_edit_finished)
    private TextView tv_edit_finished;
    @ViewInject(value = R.id.rg_seller_type)
    private RadioGroup rg_seller_type;
    @ViewInject(value = R.id.rb_hall)
    private RadioButton rb_hall;
    @ViewInject(value = R.id.rb_snack)
    private RadioButton rb_snack;
    @ViewInject(value = R.id.rb_enter)
    private RadioButton rb_enter;
    @ViewInject(value = R.id.rb_care)
    private RadioButton rb_care;
    @ViewInject(value = R.id.et_seller_desc)
    private EditText et_seller_desc;
    @ViewInject(R.id.map_seller)
    private MapView map_seller;
    @ViewInject(R.id.sv_edit_seller)
    private ScrollView sv_edit_seller;

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isOnce = true;

    private ChoicePicDialog choicePicDialog;
    private UpdateSellerInfoParams updateSellerInfoParams;
    private QuerySellerInfoParams querySellerInfo;
    private int sellerType = 1;
    private DrawerLayout dl_main;
    private ShapeLoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit_seller_info;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        querySellerInfo = new QuerySellerInfoParams();
        updateSellerInfoParams = new UpdateSellerInfoParams();
        choicePicDialog = new ChoicePicDialog(mContext, this);
        loadingDialog = new ShapeLoadingDialog(mContext);

    }

    @Override
    protected void initView() {
        tv_title.setText("基本信息");
        tv_right.setVisibility(View.INVISIBLE);
        dl_main = (DrawerLayout) getActivity().findViewById(R.id.dl_main);
        initMap();
        initLocation();
    }

    /**
     * 初始化基本地图
     */
    private void initMap() {
        //---不显示百度logo
        map_seller.removeViewAt(1);
        //---取消缩放按钮
        map_seller.showZoomControls(false);
        //---得到 baidu map对象
        mBaiduMap = map_seller.getMap();
        //--- 改变地图状态，使地图显示在恰当的缩放大小
        MapStatus mMapStatus = new MapStatus.Builder().zoom(17).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    /**
     * 初始化位置
     */
    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(mContext);
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(10 * 1000);//10s定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }


    @Override
    protected void initListener() {
        //---展开侧边栏
        ib_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dl_main.isDrawerOpen(Gravity.LEFT)) {
                    dl_main.openDrawer(Gravity.LEFT);
                }
            }
        });
        //---餐厅类型切换
        rg_seller_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_hall: {//餐厅
                        sellerType = 1;
                        break;
                    }
                    case R.id.rb_snack: {//小吃
                        sellerType = 2;
                        break;
                    }
                    case R.id.rb_enter: {//娱乐
                        sellerType = 3;
                        break;
                    }
                    case R.id.rb_care: {//便民
                        sellerType = 4;
                        break;
                    }
                }
            }
        });
        //---地图点击回调
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                updateSellerInfoParams.setLng(""+latLng.longitude);
                updateSellerInfoParams.setLat(""+latLng.latitude);
                MyHttpManager.getAsyn(MyHttpUrl.GET_ADRRESS_BY_LATLNG + latLng.latitude + "," + latLng.longitude
                        , new MyHttpManager.ResultCallback<GeoCodingJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e(TAG , "GeoCoding 转换失败！！"+e.getMessage());
                    }
                    @Override
                    public void onResponse(GeoCodingJson response) {
                        if(response.status == 0){
                            et_seller_location.setText(""+response.result.formatted_address);
                            MySnackBar.showShortDef(getActivity() , "选择地址成功！！！");
                        }
                    }
                });
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
        //---地图触摸事件(处理滑动冲突)
        map_seller.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        sv_edit_seller.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        sv_edit_seller.requestDisallowInterceptTouchEvent(false);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        final String sellerId = LogicUtils.getInstance(mContext).getSellerId();
        //---查询商家的信息
        if (!TextUtils.isEmpty(sellerId)) {
            querySellerInfo.setSellerId(sellerId);
            String url = MyHttpUrl.QUERY_SELLER_INFO + gson.toJson(querySellerInfo);
            MyLog.e(TAG, "url====" + url);
            MyHttpManager.getAsyn(MyNetUtils.escapeUrl(url), new MyHttpManager.ResultCallback<QuerySellerInfoJson>() {
                @Override
                public void onError(Request request, Exception e) {
                    MyLog.e(TAG, "网络加载出错==" + e.getMessage());
                }

                @Override
                public void onResponse(QuerySellerInfoJson querySellerInfoJson) {
                    if (querySellerInfoJson.RESULT_CODE == 0) {
                        QuerySellerInfoJson.SellerInfo sellerInfo = querySellerInfoJson.RESULT_DATA;
                        if (!TextUtils.isEmpty(sellerInfo.sellerName)) {
                            et_seller_name.setText("" + sellerInfo.sellerName);
                        }
                        if (!TextUtils.isEmpty(sellerInfo.sellerDesc)) {
                            et_seller_desc.setText("" + sellerInfo.sellerDesc);
                        }
                        if (!TextUtils.isEmpty(sellerInfo.sellerUser)) {
                            et_seller_contact.setText("" + sellerInfo.sellerUser);
                        }
                        if (!TextUtils.isEmpty(sellerInfo.mobile)) {
                            et_seller_tel.setText("" + sellerInfo.mobile);
                        }
                        if (!TextUtils.isEmpty(sellerInfo.bigImage)) {
                            MyBitmapUtils.getInstance(mContext).display(iv_seller_icon, sellerInfo.bigImage);
                        }
                        if (!TextUtils.isEmpty(sellerInfo.sellerAddress)) {
                            et_seller_location.setText("" + sellerInfo.sellerAddress);
                        }
                        switch (sellerInfo.sellerType) {
                            case 1: {
                                rb_hall.setChecked(true);
                                break;
                            }
                            case 2: {
                                rb_snack.setChecked(true);
                                break;
                            }
                            case 3: {
                                rb_enter.setChecked(true);
                                break;
                            }
                            case 4: {
                                rb_care.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 位置监听
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            if (isOnce) {
                //获取经纬度
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(status);//动画的方式到中间
                isOnce = false;
            }
        }

    }


    @OnClick({R.id.tv_add_seller_icon, R.id.tv_edit_finished})
    public void onInjectClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_seller_icon: {//---选择图片
                choicePicDialog.show();
                break;
            }
            case R.id.tv_edit_finished: {//---编辑完成
                ExplosionField explosionField = new ExplosionField(mContext, new FlyawayFactory());
                explosionField.explode(tv_edit_finished);
                tv_edit_finished.setVisibility(View.GONE);
                updateSellerInfoParams.setSellerType(sellerType);
                updateSellerInfoParams.setSellerId(LogicUtils.getInstance(mContext).getSellerId());
                updateSellerInfoParams.setSellerName(et_seller_name.getText().toString());
                updateSellerInfoParams.setSellerUser(et_seller_contact.getText().toString());
                updateSellerInfoParams.setMobile(et_seller_tel.getText().toString());
                updateSellerInfoParams.setSellerDesc(et_seller_desc.getText().toString());
                updateSellerInfoParams.setSellerAddress(et_seller_location.getText().toString());
                saveOrUpdateSellerInfo(updateSellerInfoParams);
                break;
            }
        }
    }

    /**
     * 添加或修改商家信息
     *
     * @param updateSellerInfoParams
     */
    private void saveOrUpdateSellerInfo(UpdateSellerInfoParams updateSellerInfoParams) {
        loadingDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("method", "seller.saveOrUpSellerInfo");
        params.put("data", "" + gson.toJson(updateSellerInfoParams));
        MyHttpManager.postAsyn(MyHttpUrl.SAVE_OR_UPDATE_SELLER_INFO, new MyHttpManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                loadingDialog.dismiss();
                MyLog.e(TAG, "网络加载失败！！" + e.getMessage());
                MySnackBar.showShortDef(getActivity(), "提交失败，检查您的网络！！！");
            }

            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                //MyLog.e(TAG, response);
                MySnackBar.showShortDef(getActivity(), "编辑成功！！！");
                //--设置一系列控件不可编辑
                et_seller_name.setEnabled(false);
                et_seller_desc.setEnabled(false);
                et_seller_contact.setEnabled(false);
                et_seller_tel.setEnabled(false);
                et_seller_location.setEnabled(false);
                rb_hall.setEnabled(false);
                rb_snack.setEnabled(false);
                rb_enter.setEnabled(false);
                rb_care.setEnabled(false);
                tv_add_seller_icon.setVisibility(View.INVISIBLE);
            }
        }, params);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case MediaUtils.PHOTO_REQUEST_CAMERA: {//相机
                    File iconFile = new File(MediaUtils.filePath, MediaUtils.PHOTO_FILE_NAME);
                    MediaUtils.crop(this, Uri.fromFile(iconFile), 2);
                    break;
                }
                case MediaUtils.PHOTO_REQUEST_GALLERY: {//相册
                    if (data != null) {
                        Uri uri = data.getData();
                        MediaUtils.crop(this, uri, 2);
                    }
                    break;
                }
                case MediaUtils.PHOTO_REQUEST_CUT: {
                    Bitmap bitmap = BitmapFactory.decodeFile(MediaUtils.filePath+MediaUtils.cropName);
                    iv_seller_icon.setImageBitmap(bitmap);
                    updateSellerInfoParams.setBigImage(MyFileOprateUtils.imgToBase64(MediaUtils.filePath+MediaUtils.cropName, mContext));
                    break;
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //---开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        map_seller.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map_seller.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map_seller.onDestroy();
    }
}
