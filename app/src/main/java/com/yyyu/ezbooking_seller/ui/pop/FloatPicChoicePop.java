package com.yyyu.ezbooking_seller.ui.pop;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;

import com.yyyu.barbecue.ezbooking_base.ui.pop.BasePopupWindow;
import com.yyyu.barbecue.ezbooking_base.utils.DimensChange;
import com.yyyu.barbecue.ezbooking_base.utils.MediaUtils;
import com.yyyu.ezbooking_seller.R;

/**
 * 功能：浮动的图片选择pop
 *
 * Created by yyyu on 2016/8/15.
 */
public class FloatPicChoicePop extends BasePopupWindow{

    public FloatPicChoicePop(Activity act, int width, int height, View popView) {
        super(act, width, height, popView);
        setAnimationStyle(R.style.float_pic_choice);
    }

    public FloatPicChoicePop(Fragment fragment, int width, int height, View popView) {
        super(fragment, width, height, popView);
        setAnimationStyle(R.style.float_pic_choice);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.tv_to_photo , R.id.tv_to_camera);
    }

    public void show(View target){
        super.showAsDropDown(target,target.getWidth()/2,
                DimensChange.dp2px(mAct , 3));
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_to_photo){//---从相册
            if (mFragment != null) {
                MediaUtils.toGallery(mFragment);
            } else {
                MediaUtils.toGallery(mAct);
            }
            dismiss();
        }else if(v.getId()==R.id.tv_to_camera){//---从相机
            if (mFragment != null) {
                MediaUtils.toCamera(mFragment);
            } else {
                MediaUtils.toCamera(mAct);
            }
        }
        dismiss();
    }
}
