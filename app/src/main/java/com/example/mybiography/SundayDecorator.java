package com.example.mybiography;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SundayDecorator implements DayViewDecorator {
    private static final String TAG = "SundayDecorator";
    private CalendarDay day;
        private final Calendar calendar = Calendar.getInstance();
    //달력불러오기
//    private Calendar calendar = Calendar.getInstance();

    public SundayDecorator() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, 0);
//        calendar.set(Calendar.MONTH, 0);
//        calendar.set(Calendar.DATE, 0);
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        day.copyTo(calendar);
        Calendar calendar = Calendar.getInstance(); //달력불러오기
//        LocalDate datee = day.getDate(); //2021-06-09
        calendar.set(Calendar.YEAR, day.getYear());
        calendar.set(Calendar.MONTH, day.getMonth());
        calendar.set(Calendar.DATE, day.getDay());
        // check if weekday is sunday
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK); //요일구하기
        Log.d("SundayTextView", String.valueOf(weekDay == Calendar.SUNDAY));
        Log.d(TAG, "Day: " + day + ",day.getDay() : " + day.getDay() + ", day.getDate() : " + day.getDate());
//        return date != null && day.equals(date);

        return weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        // add red foreground span
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
