package com.yyyu.barbecue.ezbooking_base.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.yyyu.barbecue.ezbooking_base.R;
import com.yyyu.barbecue.ezbooking_base.utils.MyLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能：自定义折线图
 * <p/>
 * 要点：
 * 1.设置数据集并排序(按x轴方向)
 * 2.封装一个话折线的方法（lineTo）,根据数据集合画出折线
 * <p/>
 * 自定义View的步骤
 * 1.根据分析抽取出这个view的一些外部特征（比如这里的折线图字体的大小），自定义属性
 * 2.重写 onMeasure 和 onDraw 方法
 * <p/>
 * <p/>
 * Created by yyyu on 2016/9/29.
 */
public class MyLineChart extends View {

    private static final String TAG = "MyLineChart";

    /**
     * 标注字体的大小
     */
    private int labelTextSize;
    /**
     * 标注字体的颜色
     */
    private int labelTextColor;
    /**
     * 线的宽度
     */
    private int lineWidth;
    /**
     * 默认颜色
     */
    private int defLabelTextColor = Color.parseColor("#8C8C8C");
    /**
     * 画标注画笔
     */
    private Paint mLabelPaint;
    /**
     * 画线的画笔
     */
    private Paint mLinePaint;
    /**
     * 画X轴方向线的画笔
     */
    private Paint mXLinePaint;
    /**
     * 数据集
     */
    private List<List<Point>> mDataHolder;
    /**
     * 颜色集
     */
    private List<Integer> mColorHolder;
    /**
     * 折线区域的宽度
     */
    private int lineAreaWidth;
    /**
     * 折线区域的高度
     */
    private int lineAreaHeight;
    /**
     * 左边距（用来画Y方向标注）
     */
    private int leftMargin ;
    /**
     * 下边距（用来画Y方向标注）
     */
    private int bottomMargin;

    private OnSwitchListener mOnSwitchListener;


    public MyLineChart(Context context) {
        this(context, null);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDataHolder = new ArrayList<>();
        mColorHolder = new ArrayList<>();
        initAttr(context, attrs, defStyleAttr);
        initPaint();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mLabelPaint = new Paint();
        mLabelPaint.setAntiAlias(true);
        mLabelPaint.setColor(labelTextColor);
        mLabelPaint.setTextSize(labelTextSize);
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mXLinePaint = new Paint();
        mXLinePaint.setAntiAlias(true);
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyLineChart, defStyleAttr, 0);
        labelTextSize = ta.getDimensionPixelSize(R.styleable.MyLineChart_label_text_size
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        labelTextColor = ta.getColor(R.styleable.MyLineChart_label_text_color, defLabelTextColor);
        lineWidth = ta.getDimensionPixelOffset(R.styleable.MyLineChart_line_width,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("width must be EXACTLY,you should set like android:width=\"200dp\"");
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            throw new IllegalArgumentException("height must be EXACTLY,you should set like android:height=\"200dp\"");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataHolder == null || mDataHolder.size() == 0) return;
        canvas.translate(leftMargin, bottomMargin);//---移动画布
        lineAreaWidth = getWidth() -  leftMargin;
        lineAreaHeight = getHeight() -2* bottomMargin;
        //---所有数据集合中最大的x和y
        int total_max_x = -1;
        int total_max_y = -1;
        for (int i = 0; i < mDataHolder.size(); i++) {
            List<Point> data = mDataHolder.get(i);
            int max_x = -1;
            int max_y = -1;
            for (Point point : data) {
                if (max_x < point.x) {
                    max_x = point.x;
                }
                if (max_y < point.y) {
                    max_y = point.y;
                }
            }
            if (total_max_x < max_x) {
                total_max_x = max_x;
            }
            if (total_max_y < max_y) {
                total_max_y = max_y;
            }
        }

        mLabelPaint.setTextAlign(Paint.Align.CENTER);
        /*背景线和y轴方向的标注*/
        for (int i = 0; i <= 5; i++) {
            if(i== 0){
                mXLinePaint.setColor(Color.parseColor("#8C8C8C"));
            }else{
                mXLinePaint.setColor(Color.parseColor("#D8D8D8"));
            }
            canvas.drawLine(lineAreaWidth *1/ 30, lineAreaHeight - lineAreaHeight * i / 5,
                    lineAreaWidth, lineAreaHeight - lineAreaHeight * i / 5,
                    mXLinePaint);
            canvas.drawText("" + total_max_y * i / 5, -leftMargin / 2, lineAreaHeight - lineAreaHeight * i / 5, mLabelPaint);
        }
        /*x轴方向的标注*/
        for (int i = 1; i <= 10; i++) {
            canvas.drawText("" + i, lineAreaWidth * i / 30, lineAreaHeight + bottomMargin/2, mLabelPaint);
        }
        //---得到每份在X和Y轴方向上占的坐标
        float partX = lineAreaWidth / (float) total_max_x;
        float partY = lineAreaHeight / (float) total_max_y;
        //---画折线
        for (int i = 0; i < mDataHolder.size(); i++) {
            drawFoldLine(mDataHolder.get(i), mColorHolder.get(i), canvas, partX, partY);
        }
    }

    /**
     * 最小的滑动距离
     */
    private  final float  MIN_DISTANCE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
    float dowX ;
    float upX ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                dowX = event.getRawX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                upX = event.getRawX();
                if(upX -dowX >MIN_DISTANCE){
                    if(mOnSwitchListener != null){
                        mOnSwitchListener.toPre();
                    }
                    MyLog.e(TAG , "向右滑动=============");
                }else if(dowX -upX >MIN_DISTANCE){
                    if(upX -dowX >MIN_DISTANCE) {
                        if (mOnSwitchListener != null) {
                            mOnSwitchListener.toNext();
                        }
                    }
                    MyLog.e(TAG , "向左滑动=============");
                }
                break;
            }
        }
        return true;
    }

    /**
     * 添加数据集
     *
     * @param data
     */
    public void addData(List<Point> data, int color) {
        mDataHolder.add(data);
        mColorHolder.add(color);
        invalidate();
    }

    /**
     * 画折线的方法
     *
     * @param data   点的数据集
     * @param color
     * @param canvas
     * @param partX  x 方向 所占的像素比
     * @param partY  y方向 所占的像素比
     */
    private void drawFoldLine(List<Point> data, int color, Canvas canvas, float partX, float partY) {
        canvas.save();
        Path linePath = new Path();
        /*按照x大小给集合排序*/
        Collections.sort(data, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                if (lhs.x > rhs.x) {
                    return 1;
                } else if (lhs.x < lhs.x) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        for (int i = 0; i < data.size(); i++) {

            /*画点*/
         /*   mLinePaint.setColor(Color.parseColor("#00BCD4"));
            mLinePaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(data.get(i).x * partX, lineAreaHeight - data.get(i).y * partY, 5, mLinePaint);*/

            /*画线*/
            mLinePaint.setColor(color);
            mLinePaint.setStyle(Paint.Style.STROKE);//设置画笔为线条模式
            mLinePaint.setStrokeWidth(lineWidth);//设置线宽
            if (i == data.size() - 1) {
                break;
            }
            linePath.moveTo(data.get(i).x * partX, lineAreaHeight - data.get(i).y * partY);
            linePath.lineTo(data.get(i + 1).x * partX, lineAreaHeight - data.get(i + 1).y * partY);
        }
        canvas.drawPath(linePath, mLinePaint);
        canvas.restore();
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener){
        this.mOnSwitchListener = onSwitchListener;
    }

    public interface OnSwitchListener{
        /*下一页（向左滑动）*/
        void toNext();
        /*上一页（向右滑动）*/
        void toPre();
    }


}




