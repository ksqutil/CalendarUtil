package com.pattern.calendarutil;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by kangshuaiqiang on 2019/7/23
 */
public class NewCalendar extends LinearLayout {

    private ImageView btnPrve, btnNext;
    private TextView tvDate;
    private GridView grid;
    private Calendar curDate = Calendar.getInstance();

    public NewCalendar(Context context) {
        super(context);
        initControl(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    public NewCalendar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context);
    }

    private void initControl(Context context) {
        bindControl(context);
        bindControlEcent();
        renderCalendar();
    }

    private void bindControlEcent() {
        btnPrve.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, -1);
                renderCalendar();
            }
        });
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                curDate.add(Calendar.MONTH, 1);
                renderCalendar();
            }
        });
    }


    private void bindControl(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calendar_view, this);

        btnPrve = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        tvDate = findViewById(R.id.tvDate);
        grid = findViewById(R.id.grid);

    }


    private void renderCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
        tvDate.setText(sdf.format(curDate.getTime()));

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) curDate.clone();

        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        calendar.add(Calendar.DAY_OF_MONTH, -prevDays);

        int maxCalCount = 6 * 7;


        while (cells.size() < maxCalCount) {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }


        grid.setAdapter(new CalendarAdapter(getContext(), cells));

    }

    private class CalendarAdapter extends ArrayAdapter<Date> {

        LayoutInflater inflater;


        public CalendarAdapter(@NonNull Context context, ArrayList<Date> dates) {
            super(context, R.layout.calender_text_view, dates);
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Date date = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calender_text_view, parent, false);
            }


            int day = date.getDate();

            ((TextView) convertView).setText(String.valueOf(day));


            Date now = new Date();
            boolean isTheMonth = false;

            if (date.getMonth() == now.getMonth()) {
                isTheMonth = true;
            }

            if (isTheMonth) {
                ((TextView) convertView).setTextColor(Color.parseColor("#000000"));

            } else {
                ((TextView) convertView).setTextColor(Color.parseColor("#666666"));

            }


            if (now.getDate() == date.getDate()
                    && now.getMonth() == date.getMonth()
                    && now.getYear() == date.getYear()) {
                ((TextView) convertView).setTextColor(Color.parseColor("#ff0000"));
            }

            return convertView;
        }
    }

}
