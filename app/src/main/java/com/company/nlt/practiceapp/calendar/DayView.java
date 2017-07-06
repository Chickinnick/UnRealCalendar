package com.company.nlt.practiceapp.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DayView extends TextView {

    private Date date;
    private boolean isDayEnabled;
    private boolean isDayEnabledForLongClick;
    private boolean isDayEnabledForClick;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(Date date) {
        this.date = date;
        isDayEnabled = true;
        isDayEnabledForLongClick = true;
        isDayEnabledForClick = true;
        final SimpleDateFormat df = new SimpleDateFormat("d");
        int day = Integer.parseInt(df.format(date));
        setText(String.valueOf(day));
    }


    public boolean isDayEnabled() {
        return isDayEnabled;
    }

    public void setDayEnabled(boolean dayEnabled) {
        isDayEnabled = dayEnabled;
    }

    public boolean isDayEnabledForClick() {
        return isDayEnabledForClick;
    }

    public void setDayEnabledForClick(boolean dayEnabledForClick) {
        isDayEnabledForClick = dayEnabledForClick;
    }

    public boolean isDayEnabledForLongClick() {
        return isDayEnabledForLongClick;
    }

    public void setDayEnabledForLongClick(boolean dayEnabledForLongClick) {
        isDayEnabledForLongClick = dayEnabledForLongClick;
    }

    public Date getDate() {
        return date;
    }
}