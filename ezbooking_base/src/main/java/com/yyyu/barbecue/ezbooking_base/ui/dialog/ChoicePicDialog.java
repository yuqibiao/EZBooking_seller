package com.yyyu.barbecue.ezbooking_base.ui.dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.yyyu.barbecue.ezbooking_base.R;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;

/**
 * 功能：选择图片Dialog
 *
 * @author yyyu
 * @date 2016/8/6
 */
public class ChoicePicDialog extends BaseDialog {

    private Fragment fragment;

    public ChoicePicDialog(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
        lp.width = DimensChange.dp2px(context, 280);
        lp.dimAmount = 0.7f;
        getWindow().setAttributes(lp);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_choice_pic;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.tv_to_photo, R.id.tv_to_camera);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_to_photo) {
            MediaUtils.toGallery(fragment);
            dismiss();
        } else if (v.getId() == R.id.tv_to_camera) {
            MediaUtils.toCamera(fragment);
            dismiss();
        }
    }
}
