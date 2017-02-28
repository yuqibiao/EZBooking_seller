package com.yyyu.barbecue.ezbooking_base.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.yyyu.barbecue.ezbooking_base.R;
import com.yyyu.barbecue.ezbooking_base.annotate.ViewInjectUtils;

/**
 * 功能：
 * Created by yyyu on 2016/8/15.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    private View rootView;
    protected Context mContext;
    protected Gson gson;
    protected Dialog mDialog;

    public BaseDialogFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        rootView = inflater.from(getActivity()).inflate(getLayoutId(), container, false);
        mContext = getContext();
        gson = new Gson();
        ViewInjectUtils.injectView(this, rootView);
        ViewInjectUtils.injectEvents(this, rootView);
        mDialog = getDialog();
        mDialog.getWindow().setWindowAnimations(R.style.dialog_anim);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setTitle("修改商品信息");
        beforeInit();
        initView();
        initListener();
        initData();
        afterInit();
        return rootView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 得到fragment布局文件id的抽像方法
     */
    public abstract int getLayoutId();

    protected void beforeInit() {
    }

    protected abstract void initView();

    protected abstract void initListener();

    protected void initData() {
    }

    protected void afterInit() {
    }

    /**
     * findViewById
     */
    public <T extends View> T getView(int viewId){
        return (T) rootView.findViewById(viewId);
    }

}
