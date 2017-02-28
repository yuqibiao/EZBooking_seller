package com.yyyu.barbecue.ezbooking_base.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.yyyu.barbecue.ezbooking_base.utils.MyLog;

/**
 * ViewPager wrapContent解决方案
 *
 * 如果是结合ViewPager使用在FragmentPagerAdapter
 * 的finishUpdate方法中调用setVariableHeight()
 *
 */
public class WrapContentHeightViewPager extends ViewPager {

    private static final String TAG = "WrapContentHeightViewPager";
    
    private boolean isSettingHeight;


    /**
     * Constructor
     *
     * @param context the context
     */
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs   the attribute set
     */
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //setVariableHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 动态设置ViewPager的高度
     */
    public void setVariableHeight() {
        if (!this.isSettingHeight) {
            this.isSettingHeight = true;
            int maxChildHeight = 0;
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                maxChildHeight = child.getMeasuredHeight() > maxChildHeight ? child.getMeasuredHeight() : maxChildHeight;
            }
            int height = maxChildHeight + getPaddingTop() + getPaddingBottom();
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
            super.measure(widthMeasureSpec, heightMeasureSpec);
            requestLayout();
            this.isSettingHeight = false;
        }
    }

    /**
     * 设置Viewpager的高度为当前页的高度
     */
    public void setCurrentPagerHeight(){
        if (!this.isSettingHeight) {
            this.isSettingHeight = true;
            int currentChildHeight;
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
            int currentPosition = getCurrentItem();
            if(currentPosition>getChildCount() || getChildAt(currentPosition) ==null){
                return;
            }
            MyLog.e(TAG , "currentPosition===="+currentPosition);
            View child = getChildAt(currentPosition);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            currentChildHeight = child.getMeasuredHeight();
            int heightMeasureSpec = MeasureSpec.makeMeasureSpec(currentChildHeight, MeasureSpec.EXACTLY);
            super.measure(widthMeasureSpec, heightMeasureSpec);
            MyLog.e(TAG , "currentChildHeight==="+currentChildHeight);
            requestLayout();
            this.isSettingHeight = false;
        }
    }

}