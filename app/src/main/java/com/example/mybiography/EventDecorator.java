package com.example.mybiography;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class EventDecorator  implements DayViewDecorator {
    private static final float DEFAULT_DOT_RADIUS = 4;
    //Note that negative values indicate a relative offset to the LEFT
    private static final int[] xOffsets = new int[]{0,-10,10,-20};
    private final int[] colors;
//    private final ArrayList<CalendarDay> datesList;
//    private final HashSet<CalendarDay> dates;
//    private final Collection<CalendarDay> dates;
    private final CalendarDay dates;

//    public EventDecorator(int[] colors, Collection<CalendarDay> dates ) {
    public EventDecorator(int[] colors, CalendarDay dates ) {
        this.colors = colors;
        this.dates = dates;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
//        Log.d("EventDecorator", "shouldDecorate: " + String.valueOf(day));
//        Log.d("EventDecorator", "dates" + String.valueOf(dates));
//        return dates.contains(day);
        return dates.equals(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        Log.d("EventDecorator", view.toString());
//        view.addSpan(new DotSpan(5, color));
        view.addSpan((new CustmMultipleDotSpan(5,colors)));
    }

}
