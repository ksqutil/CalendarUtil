package com.pattern.calendarutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pattern.calendarutil.oneviewgroup.MonthGroup;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MonthGroup monthGroup;
    private TextView showDate;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        findView();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        //这个月一共多少天
        int dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int prevDays = calendar.get(Calendar.DAY_OF_WEEK) - 1;


        calendar.add(Calendar.MONTH, 1);


        //这个月一共多少天
        int dayCount1 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int prevDays1 = calendar.get(Calendar.DAY_OF_WEEK) - 1;

    }

    private void findView() {
        findViewById(R.id.select_one).setOnClickListener(this);
        findViewById(R.id.select_two).setOnClickListener(this);
        showDate = findViewById(R.id.show_date);
        monthGroup = findViewById(R.id.month_group);
        monthGroup.setGetDateInterface(new MonthGroup.GetDateInterface() {
            @Override
            public void getDate(Date date) {
                showDate.setText(sdf.format(date));
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_one:
                monthGroup.addMonth();
                break;
            case R.id.select_two:
                monthGroup.reduceMonth();
                break;
            default:
        }
    }
}
