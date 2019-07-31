package com.pattern.calendarutil.oneviewgroup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pattern.calendarutil.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by kangshuaiqiang on 2019/7/25
 */
public class MonthGroup extends ViewGroup {

    private static final int DEFAULT_DAYS_WEEK = 7;

    private Context context;
    private DisplayMetrics metrics;
    private Calendar calendar = Calendar.getInstance();
    /**
     * 选中的日期
     */
    private Date selectDate;
    private int index;
    private DayClickListener listener;
    private GetDateInterface getDateInterface;
    private GestureDetector gestureDetector;


    private void init() {
        //默认设置为今天
        this.selectDate = new Date();
        gestureDetector = new GestureDetector(context, onGestureListener);
        if (getDateInterface != null) {
            getDateInterface.getDate(selectDate);
        }
        /**
         * 绘制
         */
        renderCalender();
    }

    /**
     * 绘制
     */
    private void renderCalender() {
        removeAllViews();
        addWeek();
        Calendar dayCalendar = (Calendar) this.calendar.clone();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");

        Log.i("MonthGroup", sdf.format(dayCalendar.getTime()));
        dayCalendar.set(Calendar.DAY_OF_MONTH, 1);

        this.index = dayCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int actualMaximum = this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < actualMaximum; i++) {
            DayView dayView = new DayView(context, dayCalendar.getTime());
            dayView.setOnClickListener(this.listener);
            addView(dayView);
            dayCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        resetCalenderView();
    }

    private void resetCalenderView() {
        Calendar selectDate = (Calendar) calendar.clone();
        selectDate.setTime(this.selectDate);

        int childCount = getChildCount();
        for (int i = DEFAULT_DAYS_WEEK; i < childCount; i++) {
            DayView childAt = (DayView) getChildAt(i);
            Calendar calendar = childAt.getCalendar();
            boolean identical = identical(selectDate, calendar);
            if (identical) {
                childAt.setSelected(true);
            } else {
                childAt.setSelected(false);
            }
        }
    }

    private boolean identical(Calendar selectDate, Calendar calendar) {
        if (selectDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                selectDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                selectDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return true;
        } else return false;
    }

    private void addWeek() {
        for (int i = 0; i < DEFAULT_DAYS_WEEK; i++) {
            TextView week = new TextView(context);
            week.setText(getWeekName(i));
            week.setGravity(Gravity.CENTER);
            week.setTypeface(null, Typeface.BOLD);
            week.setTextColor(ContextCompat.getColor(this.getContext(), R.color.white));
            week.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f);
            week.setAllCaps(true);
            addView(week);
        }
    }

    public MonthGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthGroup(Context context) {
        this(context, null);

    }

    public MonthGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.metrics = getResources().getDisplayMetrics();
        this.listener = new DayClickListener();
        setBackgroundColor(Color.RED);
        init();
    }


    public void addMonth() {
        calendar.add(Calendar.MONTH, 1);
        renderCalender();
//        invalidate();
    }

    public void reduceMonth() {
        calendar.add(Calendar.MONTH, -1);
        renderCalender();
//        invalidate();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int weekHeight = 0;
        for (int j = 0; j < DEFAULT_DAYS_WEEK; j++) {
            View child = getChildAt(j);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            weekHeight = height;
            int childLeft;
            if (child instanceof TextView) {
                childLeft = (width * j);
                child.layout(childLeft, 0, childLeft + width, height);
            }
        }
        int count = getChildCount();
        int offset = this.index;
        int childTop = weekHeight;
        for (int j = DEFAULT_DAYS_WEEK; j < count; j++) {
            View child = getChildAt(j);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int childLeft;
            childLeft = width * offset;
            child.layout(childLeft, childTop, childLeft + width, height + childTop);
            offset++;
            if (offset >= DEFAULT_DAYS_WEEK) {
                offset = 0;
                childTop += height;
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            throw new IllegalStateException("CalendarPagerView should never be left to decide it's size");
        }

        int measureTileWidth = specWidthSize / DEFAULT_DAYS_WEEK;
        int measureTileHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35.0f, metrics);
//        + ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0f, this.metrics))
        setMeasuredDimension(specWidthSize, (measureTileHeight * DEFAULT_DAYS_WEEK));

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(measureTileWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(measureTileHeight, MeasureSpec.EXACTLY));
        }
    }

    private String getWeekName(int week) {
        switch (week) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return "无";
        }
    }

    class DayClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Log.i("onTouchEvent", "view点击");
            DayView dayView = (DayView) view;
            if (dayView == null) {
                return;
            }
            MonthGroup.this.selectDate = dayView.getSelectDate();
            dayView.setSelected(true);
            resetCalenderView();
            if (getDateInterface != null) {
                getDateInterface.getDate(MonthGroup.this.selectDate);
            }
        }
    }

    public void setGetDateInterface(GetDateInterface getDateInterface) {
        this.getDateInterface = getDateInterface;
        this.getDateInterface.getDate(selectDate);
    }

    public interface GetDateInterface {
        void getDate(Date date);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("onTouchEvent", "onInterceptTouchEvent");
        return gestureDetector.onTouchEvent(ev);
    }


    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (x > 100) {
                calendar.add(Calendar.MONTH, 1);
                renderCalender();
            } else if (x < -100) {
                calendar.add(Calendar.MONTH, -1);
                renderCalender();
            }
            return true;
        }
    };
}
