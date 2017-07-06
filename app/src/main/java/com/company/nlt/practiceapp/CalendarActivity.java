package com.company.nlt.practiceapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.company.nlt.practiceapp.calendar.CustomCalendarView;

import java.util.Calendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    CustomCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_calendar);

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.SUNDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);
    }

}