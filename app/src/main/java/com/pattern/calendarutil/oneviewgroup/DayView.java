package com.pattern.calendarutil.oneviewgroup;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pattern.calendarutil.R;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by kangshuaiqiang on 2019/7/25
 */
public class DayView extends FrameLayout {

    private static final float DEFAULT_TEXT_SIZE = 12.0f;
    public static final float DEFAULT_MEASURED = 30.0f;


    private static Integer _measured;

    static {
        _measured = null;
    }

    private Calendar calendar = Calendar.getInstance();

    /**
     * 获取屏幕信息
     */
    private DisplayMetrics metrics;
    /**
     * 是否选中
     */
    private boolean selected;
    /**
     * 日期显示文本
     */
    private TextView tvDay;

    private boolean isToday = false;

    private Date date;


    public DayView(Context context) {
        super(context);
    }

    public DayView(Context context, Date day) {
        super(context);
        this.calendar.setTime(day);
        this.date = day;
        Calendar today = (Calendar) this.calendar.clone();
        today.setTime(new Date());
        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
            isToday = true;
        }
        init(context);
    }

    public DayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init(Context context) {

        this.metrics = getResources().getDisplayMetrics();
        this.selected = false;
        this.tvDay = new TextView(context);
        this.tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        this.tvDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE);
        this.tvDay.setGravity(Gravity.CENTER);

        LayoutParams params = new LayoutParams(getDefaultMeasured(), getDefaultMeasured());
        params.gravity = Gravity.CENTER;
        this.tvDay.setLayoutParams(params);

        if (isToday) {
            setBackgroundColor(Color.GRAY);
            this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        } else {
            setBackgroundColor(Color.BLUE);
            this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }


        addView(this.tvDay);
    }

    private int getDefaultMeasured() {
        if (_measured == null) {
            _measured = Integer.valueOf((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MEASURED, this.metrics));
        }
        return _measured.intValue();
    }

    @Override
    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
        }
        if (this.selected) {
            this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            return;
        }
        this.tvDay.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    public Date getSelectDate() {
        return this.date;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

}
