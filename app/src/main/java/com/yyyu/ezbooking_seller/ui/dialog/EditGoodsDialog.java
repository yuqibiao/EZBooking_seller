package com.yyyu.ezbooking_seller.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.ui.dialog.BaseDialog;
import com.yyyu.barbecue.ezbooking_base.ui.pop.PicChoicePop;
import com.yyyu.ezbooking_seller.R;

/**
 * 功能：编辑商品信息的Dialog
 *
 * @author yyyu
 * @date 2016/8/13
 */
public class EditGoodsDialog extends BaseDialog {

    private TextView tv_cancel;
    private TextView tv_confirm;
    private TextView tv_add_goods_icon;

    private Activity mActivity;

    public EditGoodsDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_edit_goods;
    }

    @Override
    protected void initView() {
        tv_cancel = getView(R.id.tv_cancel);
        tv_confirm = getView(R.id.tv_confirm);
        tv_add_goods_icon = getView(R.id.tv_add_goods_icon);
    }

    @Override
    protected void initListener() {

        //---上传图片
        tv_add_goods_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                View popView = View.inflate(mActivity, com.yyyu.barbecue.ezbooking_base.R.layout.pop_pic_select, null);
                popView.measure(0, 0);
                PicChoicePop choicePop = new PicChoicePop(mActivity,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        popView.getMeasuredHeight(), popView){};
                View view =mActivity.findViewById(android.R.id.content);
                choicePop.show(view);
            }
        });
        //---确认
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    protected void initData() {
        super.initData();
    }

}
