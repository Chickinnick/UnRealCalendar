package com.company.nlt.practiceapp.calendar;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarPresenter {

    CalendarDataHolder calendarDataHolder;
    private CustomCalendarView customCalendarView;
    ArrayList<DayView> selectedViews;

    public CalendarPresenter(CalendarDataHolder calendarDataHolder, CustomCalendarView customCalendarView) {
        this.calendarDataHolder = calendarDataHolder;
        this.customCalendarView = customCalendarView;
        selectedViews = new ArrayList<>();
    }

    public void add(final Date dateToAdd) {
        calendarDataHolder.tryAdd(dateToAdd , new CalendarDataHolder.AddCallback(){
            @Override public void result(boolean isAdded) {


                ArrayList<Date> selectedDays = calendarDataHolder.getSelectedDays();
                for (int i = 1; i < selectedDays.size() - 2 ; i++) {
                    if (selectedDays.get(i - 1).after()) {

                    }
                }

                    if (isAdded) {
                        compareDates()
                        customCalendarView.updateDayBackground(CustomCalendarView.STATE_SELECTED_BY_EDIT, dayView);
                    } else{
                        customCalendarView.updateDayBackground(CustomCalendarView.STATE_UNSELECTED, dayView);
                    }

            }
        });
    }


}
