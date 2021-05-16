package com.example.mybiography;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.LocalDate;


public class OneDayDecorator implements DayViewDecorator {
    private CalendarDay date;
    private Drawable highlightDrawable;

    public OneDayDecorator() {
        date = CalendarDay.today();
//        Log.d("OneDayDecorator23", "생성자 : " + String.valueOf(date));
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

//        Log.d("OneDayDecorator", "shouldDecorate : " + String.valueOf(day.equals(date)) + String.valueOf(day));
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.5f)); //확대
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#A86A20"))); //날짜색
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ECF370")));
//        view.addSpan(new DotSpan(5, Color.RED));
    }

    public void setDate(LocalDate date) {
//        Log.d("OneDayDecorator", "setDate" + String.valueOf(date));
        this.date = CalendarDay.from(date);
    }
}
