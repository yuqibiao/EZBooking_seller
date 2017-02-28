package com.yyyu.barbecue.ezbooking_base.ui.pop;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.yyyu.barbecue.ezbooking_base.R;
import com.yyyu.barbecue.ezbooking_base.ui.widget.MyCalendarView;
import com.yyyu.barbecue.ezbooking_base.utils.MyStrUtils;

import java.util.Date;

/**
 * 功能：日期选择pop
 *
 * @author yyyu
 * @date 2016/8/21
 */
public class DatePickerPop extends BasePopupWindow{

    private MyCalendarView mcv_pop;
    private TextView tv_date;

    public DatePickerPop(Activity activity, int width, int height, View popView) {
        super(activity, width, height, popView);
        setAnimationStyle(R.style.pop_date_picker);
    }

    public DatePickerPop(Fragment fragment, int width, int height, View popView) {
        super(fragment, width, height, popView);
        setAnimationStyle(R.style.pop_date_picker);
    }

    @Override
    protected void initView() {
        tv_date = getView(R.id.tv_date);
        mcv_pop = getView(R.id.mcv_pop);
        tv_date.setText(""+ mcv_pop.getYearAndMonth());
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.ib_pre_month , R.id.ib_next_month);
    }

    /**
     * 得到选中的月份+日期+星期
     * @return
     */
    public String getSelectedInfo(){
        return mcv_pop.getSelectedMonth()+"月"
                +mcv_pop.getSelectedDay()+"日" +
                "  周"+ MyStrUtils.intToWeekNum(mcv_pop.getSelectedWeek());
    }

    public MyCalendarView getMyCalendar(){

        return mcv_pop;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_pre_month){//前一个月
            tv_date.setText(""+mcv_pop.toPreMonth());
        }else if(v.getId() == R.id.ib_next_month){//后一个月
            tv_date.setText(""+mcv_pop.toNextMonth());
        }
    }
}
