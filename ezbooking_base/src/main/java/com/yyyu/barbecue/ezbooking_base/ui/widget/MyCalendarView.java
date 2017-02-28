package com.yyyu.barbecue.ezbooking_base.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yyyu.barbecue.ezbooking_base.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 自定义的日历控件 功能：获得点选的日期区间
 *
 * @author yyyu
 * @version 1.0
 */
public class MyCalendarView extends View implements View.OnTouchListener {


    private final static String TAG = "anCalendar";
    private Date selectedStartDate;
    private Date selectedEndDate;
    private Date curDate; // 当前日历显示的月
    private Date today; // 今天的日期文字显示红色
    private Date downDate; // 手指按下状态时临时日期
    private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期
    private int downIndex; // 按下的格子索引
    private Calendar calendar;
    private Surface surface;
    private int[] date = new int[42]; // 日历显示数字
    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
    private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了
    private boolean isSelectMore = false;
    private int selectedIndex = -1; //选中日期颜色

    // 给控件设置监听事件
    private OnItemClickListener onItemClickListener;

    public MyCalendarView(Context context) {
        super(context);
        init();
        this.surface.width = getResources().getDisplayMetrics().widthPixels;
        this.surface.height = getResources().getDisplayMetrics().heightPixels * 2 / 5;
    }

    /*
     * 向外提供的一个传入日历外观宽高的构造方法
     */
    public MyCalendarView(Context context, int calendarWidth, int calendarHeight) {
        this(context);
        init();
        this.surface.width = calendarWidth;
        this.surface.height = calendarHeight;

    }

    public MyCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /*
     * 初始化方法
     */
    private void init() {
        curDate = selectedStartDate = selectedEndDate = today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();
        surface.init();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
        setOnTouchListener(this);
    }

    /*
     * 控件测量方法的重载
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            width = getResources().getDisplayMetrics().widthPixels;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            height = measureHeight;
        }else{
            height = getResources().getDisplayMetrics().heightPixels * 2 / 5;
        }
        setMeasuredDimension(width, height);

    }

    /*
     * 控件布局方法的重构
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    /*
     * 画出控件方法的重构
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        //--- 画框
        // canvas.drawPath(surface.boxPath, surface.borderPaint);
        //---画星期
        float weekTextY = surface.monthHeight + surface.weekHeight * 3 / 4f;
        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText(surface.weekText[i])) / 2f;
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY, surface.weekPaint);
        }
        // 计算日期
        calculateDate();
        // 按下状态，选择状态背景色
        drawDownOrSelectedBg(canvas);
        // write date number
        int todayIndex = -1;
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        if (curYearAndMonth.equals(todayYearAndMonth)) {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }
        for (int i = 0; i < 42; i++) {
            int color = surface.textColor;
            if (isLastMonth(i)) {
                color = surface.borderColor;
            } else if (isNextMonth(i)) {
                color = surface.borderColor;
            }
            if (todayIndex != -1 && i == todayIndex) {//---当前天的颜色
                color = surface.todayNumberColor;
            }
            if (downIndex != -1 && i == downIndex) {
                color = surface.cellSelectedTextColor;
            }
            drawCellText(canvas, i, date[i] + "", color);
        }
        super.onDraw(canvas);
    }

    /*
     * 日历计算的算法
     */
    private void calculateDate() {
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "day in week:" + dayInWeek);
        int monthStart = dayInWeek;
        if (monthStart == 1) {
            monthStart = 8;
        }
        monthStart -= 1; // 以日为开头-1，以星期一为开头-2
        curStartIndex = monthStart;
        date[monthStart] = 1;
        // last month
        if (monthStart > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
            for (int i = monthStart - 1; i >= 0; i--) {
                date[i] = dayInmonth;
                dayInmonth--;
            }
            calendar.set(Calendar.DAY_OF_MONTH, date[0]);
        }
        showFirstDate = calendar.getTime();
        // this month
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < monthDay; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + monthDay;
        // next month
        for (int i = monthStart + monthDay; i < 42; i++) {
            date[i] = i - (monthStart + monthDay) + 1;
        }
        if (curEndIndex < 42) {
            // 显示了下一月的
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, date[41]);
        showLastDate = calendar.getTime();
    }

    /*
     * 画出每个日历格子里面的日期数值
     */
    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.datePaint.setColor(color);
        float left = surface.cellWidth * (x - 1) + surface.borderWidth + surface.cellWidth / 2;
        float top = surface.monthHeight + surface.weekHeight + (y - 1)
                * surface.cellHeight + surface.borderWidth + surface.cellHeight / 2;
        Rect rect = new Rect();
        surface.datePaint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, left, top + rect.height() / 2, surface.datePaint);
    }

    /*
     * 画出显示日期格子的背景
     */
    private void drawCellBg(Canvas canvas, int index, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.cellBgPaint.setColor(color);
        float left = surface.cellWidth * (x - 1) + surface.borderWidth;
        float top = surface.monthHeight + surface.weekHeight + (y - 1)
                * surface.cellHeight + surface.borderWidth;
     /*   canvas.drawRect(left, top,
                left + surface.cellWidth - surface.borderWidth,
                top + surface.cellHeight - surface.borderWidth,
                surface.cellBgPaint);*/
        float radius = Math.min(surface.cellWidth / 2, surface.cellHeight / 2);
        canvas.drawCircle(left + surface.cellWidth / 2, top + surface.cellHeight / 2, radius, surface.cellBgPaint);
    }

    /*
     * 画出某一日期被选择后或者按下时格子的背景
     */
    private void drawDownOrSelectedBg(Canvas canvas) {
        // down and not up
        if (downDate != null) {
            drawCellBg(canvas, downIndex, surface.cellDownColor);
        }
        // selected bg color
        if (!selectedEndDate.before(showFirstDate)
                && !selectedStartDate.after(showLastDate)) {
            int[] section = new int[]{-1, -1};
            calendar.setTime(curDate);
            calendar.add(Calendar.MONTH, -1);
            findSelectedIndex(0, curStartIndex, calendar, section);
            if (section[1] == -1) {
                calendar.setTime(curDate);
                findSelectedIndex(curStartIndex, curEndIndex, calendar, section);
            }
            if (section[1] == -1) {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, 1);
                findSelectedIndex(curEndIndex, 42, calendar, section);
            }
            if (section[0] == -1) {
                section[0] = 0;
            }
            if (section[1] == -1) {
                section[1] = 41;
            }
            for (int i = section[0]; i <= section[1]; i++) {
                drawCellBg(canvas, i, surface.cellSelectedColor);
            }
        }
    }

    /*
     * 通过index得到选择的索引
     */
    private void findSelectedIndex(int startIndex, int endIndex,
                                   Calendar calendar, int[] section) {
        for (int i = startIndex; i < endIndex; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, date[i]);
            Date temp = calendar.getTime();
            // Log.d(TAG, "temp:" + temp.toLocaleString());
            if (temp.compareTo(selectedStartDate) == 0) {
                section[0] = i;
            }
            if (temp.compareTo(selectedEndDate) == 0) {
                section[1] = i;
                return;
            }
        }
    }

    public Date getSelectedStartDate() {
        return selectedStartDate;
    }

    public Date getSelectedEndDate() {
        return selectedEndDate;
    }

    private boolean isLastMonth(int i) {
        if (i < curStartIndex) {
            return true;
        }
        return false;
    }

    private boolean isNextMonth(int i) {
        if (i >= curEndIndex) {
            return true;
        }
        return false;
    }

    private int getXByIndex(int i) {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i) {
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    /*
     * 获得当前应该显示的年月
     */
    public String getYearAndMonth() {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

    /*
     * 获得当前显示的年月日
     */
    public String getDatetime() {
        StringBuffer dateTimeStr = new StringBuffer();
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        dateTimeStr.append(year);
        if (month < 10) {
            dateTimeStr = dateTimeStr.append("-0").append(month);
        } else {
            dateTimeStr = dateTimeStr.append("-").append(month);
        }
        if (date < 10) {
            dateTimeStr = dateTimeStr.append("-0").append(date);
        } else {
            dateTimeStr = dateTimeStr.append("-").append(date);
        }
        return dateTimeStr.toString();
    }

    /*
     * 上一月
     */
    public String toPreMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndMonth();
    }

    /*
     * 下一月
     */
    public String toNextMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndMonth();
    }

    /*
     * 前一天
	 */
    public String toPreDay() {
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE, -1);
        curDate = calendar.getTime();
        invalidate();
        return getDatetime();
    }

    /*
     * 后一天
     */
    public String toNextDay() {
        calendar.setTime(curDate);
        calendar.add(Calendar.DATE, 1);
        curDate = calendar.getTime();
        invalidate();
        return getDatetime();
    }

    /*
     * 设置日历时间
     */
    public void setCalendarData(Date date) {
        calendar.setTime(date);
        invalidate();
    }

    /**
     * 得到当前选中的月
     *
     * @return
     */
    public int getSelectedMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前的日
     *
     * @return
     */
    public int getSelectedDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到当前的星期
     *
     * @return
     */
    public int getSelectedWeek() {
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        //获取周几
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDay;
    }

    /**
     * 获取日历时间
     */
    public void getCalendarData() {
        calendar.getTime();
    }

    // 设置是否多选
    public boolean isSelectMore() {
        return isSelectMore;
    }

    public void setSelectMore(boolean isSelectMore) {
        this.isSelectMore = isSelectMore;
    }

    /*
     * 设置选择的日期
     */
    private void setSelectedDateByCoor(float x, float y) {
        // cell click down
        if (y > surface.monthHeight + surface.weekHeight) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math
                    .floor((y - (surface.monthHeight + surface.weekHeight))
                            / Float.valueOf(surface.cellHeight)) + 1);
            downIndex = (n - 1) * 7 + m - 1;
            Log.d(TAG, "downIndex:" + downIndex);
            calendar.setTime(curDate);
            if (isLastMonth(downIndex)) {
                calendar.add(Calendar.MONTH, -1);
            } else if (isNextMonth(downIndex)) {
                calendar.add(Calendar.MONTH, 1);
            }
            calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
            downDate = calendar.getTime();
            curDate = calendar.getTime();
        }
        invalidate();
    }

    /*
     * 触摸事件的重构
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setSelectedDateByCoor(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                if (downDate != null) {
                    if (isSelectMore) {
                        if (!completed) {
                            if (downDate.before(selectedStartDate)) {
                                selectedEndDate = selectedStartDate;
                                selectedStartDate = downDate;
                            } else {
                                selectedEndDate = downDate;
                            }
                            completed = true;
                            // 响应监听事件
                            calendar.setTime(selectedEndDate);
                            onItemClickListener.OnItemClick(selectedStartDate, selectedEndDate, downDate);
                        } else {
                            selectedStartDate = selectedEndDate = downDate;
                            completed = false;
                        }
                    } else {
                        selectedStartDate = selectedEndDate = downDate;
                        // 响应监听事件
                        if (onItemClickListener != null) {
                            calendar.setTime(selectedEndDate);
                            onItemClickListener.OnItemClick(selectedStartDate, selectedEndDate, downDate);
                        }
                    }
                    invalidate();
                }

                break;
        }
        return true;
    }

    // 给控件设置监听事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /*
     * 封装的如下的日历属性：
     * 1. 布局尺寸
     * 2. 文字颜色，大小
      * 3. 当前日期的颜色，选择的日期颜色
     */
    public class Surface {

        public float density;
        public int width; // 整个控件的宽度
        public int height; // 整个控件的高度
        public float monthHeight; // 显示月的高度
        public float weekHeight; // 显示星期的高度
        public float cellWidth; // 日期方框宽度
        public float cellHeight; // 日期方框高度
        public float borderWidth;
        public int bgColor = Color.parseColor("#FFFFFF");
        private int textColor = Color.parseColor("#ff424242");
        private int btnColor = Color.parseColor("#999999");
        private int borderColor = Color.parseColor("#CCCCCC");
        public int todayNumberColor = Color.parseColor("#FF4081");
        public int cellDownColor = Color.parseColor("#FEFC37");
        public int cellSelectedColor = Color.parseColor("#F5A92C");
        public int cellSelectedTextColor = Color.parseColor("#ffffff");
        public Paint borderPaint;
        public Paint monthPaint;
        public Paint weekPaint;
        public Paint datePaint;
        public Paint monthChangeBtnPaint;
        public Paint cellBgPaint;
        public Path boxPath; // 边框路径
        public String[] weekText = {"日", "一", "二", "三", "四", "五", "六"};

        public Surface() {

        }

        /*
         * 日历属性的初始化
         */
        public void init() {
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            float temp = height / 7f;
            monthHeight = 0;
            weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
            cellHeight = (height - monthHeight - weekHeight) / 6f;
            cellWidth = width / 7f;
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderWidth = (float) (0.5 * density);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderPaint.setStrokeWidth(borderWidth);
            monthPaint = new Paint();
            monthPaint.setColor(textColor);
            monthPaint.setAntiAlias(true);
            float textSize = cellHeight * 0.4f;
            monthPaint.setTextSize(textSize);
            monthPaint.setTypeface(Typeface.DEFAULT);
            weekPaint = new Paint();
            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = weekHeight * 0.4f;
            weekPaint.setTextSize(weekTextSize);
            weekPaint.setTypeface(Typeface.DEFAULT);
            datePaint = new Paint();
            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            datePaint.setTextAlign(Paint.Align.CENTER);//设置文本中间对其
            float cellTextSize = cellHeight * 0.4f;
            datePaint.setTextSize(cellTextSize);
            datePaint.setTypeface(Typeface.DEFAULT);
            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, monthHeight + weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < 6; i++) {
                boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
                boxPath.moveTo(i * cellWidth, monthHeight);
                boxPath.rLineTo(0, height - monthHeight);
            }
            boxPath.moveTo(6 * cellWidth, monthHeight);
            boxPath.rLineTo(0, height - monthHeight);
            monthChangeBtnPaint = new Paint();
            monthChangeBtnPaint.setAntiAlias(true);
            monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            monthChangeBtnPaint.setColor(btnColor);
            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setStyle(Paint.Style.FILL);
            cellBgPaint.setColor(cellSelectedColor);
        }
    }


    /**
     * Item 点击事件回调接口
     */
    public interface OnItemClickListener {
        void OnItemClick(Date selectedStartDate, Date selectedEndDate,
                         Date downDate);
    }


}
