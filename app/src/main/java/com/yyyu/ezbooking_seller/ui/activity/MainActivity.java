package com.yyyu.ezbooking_seller.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.annotate.ViewInject;
import com.yyyu.barbecue.ezbooking_base.bean.json.LoginJson;
import com.yyyu.barbecue.ezbooking_base.gobal.Constant;
import com.yyyu.barbecue.ezbooking_base.net.MyBitmapUtils;
import com.yyyu.barbecue.ezbooking_base.ui.activity.BaseActivity;
import com.yyyu.barbecue.ezbooking_base.ui.widget.AdapterLinearLayout;
import com.yyyu.barbecue.ezbooking_base.utils.ActivityHolder;
import com.yyyu.barbecue.ezbooking_base.utils.MySPUtils;
import com.yyyu.barbecue.ezbooking_base.utils.MyToast;
import com.yyyu.ezbooking_seller.R;
import com.yyyu.ezbooking_seller.bean.foo.LeftMenuBean;
import com.yyyu.ezbooking_seller.ui.fragment.EditSellerGoodsFragment;
import com.yyyu.ezbooking_seller.ui.fragment.EditSellerInfoFragment;
import com.yyyu.ezbooking_seller.ui.fragment.EditSellerServiceFragment;
import com.yyyu.ezbooking_seller.ui.fragment.OrderManagerFragment;
import com.yyyu.ezbooking_seller.ui.fragment.StatisticFragment;
import com.yyyu.ezbooking_seller.utils.LogicUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity {

    @ViewInject(value = R.id.dl_main)
    private DrawerLayout dl_main;
    @ViewInject(value = R.id.ll_menu)
    private AdapterLinearLayout ll_menu;
    @ViewInject(value = R.id.civ_seller_icon)
    private CircleImageView civ_seller_icon;
    @ViewInject(value = R.id.tv_seller_name)
    private TextView tv_seller_name;
    @ViewInject(value = R.id.tv_seller_tel)
    private TextView tv_seller_tel;
    @ViewInject(R.id.tv_logout)
    private TextView tv_logout;

    private List<LeftMenuBean> leftMenus;
    private LoginJson.UserInfo userInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeInit() {
        super.beforeInit();
        leftMenus = new ArrayList<>();
        leftMenus.add(new LeftMenuBean(R.mipmap.icon_order, "订单管理"));
        leftMenus.add(new LeftMenuBean(R.mipmap.icon_seller, "商家基本信息"));
        leftMenus.add(new LeftMenuBean(R.mipmap.icon_service, "商家服务设置"));
        leftMenus.add(new LeftMenuBean(R.mipmap.icon_goods, "商家商品设置"));
        leftMenus.add(new LeftMenuBean(R.mipmap.icon_statistics , "预约统计"));

    }

    @Override
    protected void initView() {
        initMenu();
        //---init content
        replaceFragmentByContentId(R.id.fl_content, new OrderManagerFragment());
        if(!LogicUtils.getInstance(this).isUserLogined()){//未登录
            tv_seller_name.setText("请先登录");
            tv_seller_tel.setVisibility(View.GONE);
        }else{
            LoginJson.UserInfo userInfo =  LogicUtils.getInstance(this).getUser();
            tv_seller_name.setText(userInfo.userName);
            tv_seller_tel.setText("电话  "+userInfo.mobile);
            MyBitmapUtils.getInstance(this).display(civ_seller_icon , userInfo.userImages);
        }
    }

    @Override
    protected void initListener() {

        //---设置侧滑菜单点击事件
        ll_menu.setOnItemClickListener(new AdapterLinearLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0: {//---订单管理
                        replaceFragmentByContentId(R.id.fl_content, new OrderManagerFragment());
                        MyToast.showShort(MainActivity.this, "订单管理");
                        break;
                    }
                    case 1: {//---商家基本信息
                        replaceFragmentByContentId(R.id.fl_content, new EditSellerInfoFragment());
                        break;
                    }
                    case 2: {//---商家服务设置
                        replaceFragmentByContentId(R.id.fl_content, new EditSellerServiceFragment());
                        break;
                    }
                    case 3: {//---商家商品设置
                        replaceFragmentByContentId(R.id.fl_content, new EditSellerGoodsFragment());
                        break;
                    }
                    case 4:{//---统计
                        replaceFragmentByContentId(R.id.fl_content , new StatisticFragment());
                        break;
                    }
                }
                if (dl_main.isDrawerOpen(Gravity.LEFT)) {
                    dl_main.closeDrawer(Gravity.LEFT);
                }
            }
        });
        //---注销用户
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("注销用户")
                        .setMessage("确定要残忍的退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MySPUtils.remove(MainActivity.this, Constant.USER_INFO);
                                startActivity(new Intent(MainActivity.this , SplashActivity.class));
                                ActivityHolder.finishedAll();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 初始化侧滑菜单项
     */
    private void initMenu() {
        ll_menu.setAdapter(new AdapterLinearLayout.LinearAdapter(){
            @Override
            public int getItemCount() {
                return leftMenus.size();
            }
            @Override
            public View getView(ViewGroup parent, int position) {
                View mView = LayoutInflater.from(MainActivity.this).inflate(R.layout.menu_left_item, parent, false);
                ImageView iv_menu_icon = (ImageView) mView.findViewById(R.id.iv_menu_icon);
                TextView tv_menu = (TextView) mView.findViewById(R.id.tv_menu);
                iv_menu_icon.setImageResource(leftMenus.get(position).getMenuIcon());
                tv_menu.setText(leftMenus.get(position).getMenuTitle());
                return mView;
            }
        });

    }

    /**
     * 替换Fragment
     */
    public void replaceFragmentByContentId(int contentId, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(contentId, fragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        if(dl_main.isDrawerOpen(Gravity.LEFT)){
            dl_main.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }

    /**
     * 跳转到MainActivity
     */
    public static void startAction(Activity activity){
        Intent intent = new Intent(activity ,MainActivity.class);
        activity.startActivity(intent);
    }

}
