package com.company.nlt.practiceapp.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.nlt.practiceapp.R;
import com.company.nlt.practiceapp.calendar.utils.CalendarUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout implements View.OnClickListener {

    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";
    private static final String TAG = CustomCalendarView.class.getName();

    private final int CLICK_KEY_CODE = 1;
    private final int LONG_CLICK_KEY_CODE = 2;

    private Context mContext;
    private View view;
    private Calendar currentCalendar;
    private Locale locale;
    private ArrayList<Date> selectedDaysByClick;
    private ArrayList<Integer> neighboringPositionsByClick;
    private ArrayList<Date> selectedDaysByLongClick;
    private ArrayList<Integer> borders;
    private int disabledDayBackgroundColor;
    private int disabledDayTextColor;
    private int calendarBackgroundColor;

    private int selectedDayBackgroundYellow;
    private int selectedDayStartBorderYellow;
    private int selectedDayEndBorderYellow;
    private int selectedDayBackgroundRangeYellow;

    private int selectedDayBackgroundGrey;
    private int selectedDayStartBorderGrey;
    private int selectedDayEndBorderGrey;
    private int selectedDayBackgroundRangeGrey;

    private int selectedDayBackgroundBlack;
    private int selectedDayStartBorderBlack;
    private int selectedDayEndBorderBlack;
    private int selectedDayBackgroundRangeBlack;
    private int selectedDayTextColorWhite;

    private int weekLayoutBackgroundColor;
    private int calendarTitleBackgroundColor;
    private int calendarTitleTextColor;
    private int dayOfWeekTextColor;
    private int dayOfMonthTextColor;
    private int beforeCurrentDayColor;
    private int currentMonthIndex;
    private int firstDayOfWeek = Calendar.SUNDAY;
    private boolean isOverflowDateVisible = true;

    public CustomCalendarView(Context mContext) {
        this(mContext, null);
    }

    public CustomCalendarView(Context mContext, AttributeSet attrs) {
        super(mContext, attrs);
        this.mContext = mContext;
        selectedDaysByClick = new ArrayList<>();
        neighboringPositionsByClick = new ArrayList<>();
        selectedDaysByLongClick = new ArrayList<>();
        borders = new ArrayList<>();

        getAttributes(attrs);
        initializeCalendarLayout();
    }

    private void getAttributes(AttributeSet attrs) {
        final TypedArray typedArray = mContext.obtainStyledAttributes(
                attrs, R.styleable.CustomCalendarView, 0, 0);

        calendarBackgroundColor = typedArray.getColor(
                R.styleable.CustomCalendarView_calendarBackgroundColor,
                getResources().getColor(R.color.white));
        calendarTitleBackgroundColor = typedArray.getColor(
                R.styleable.CustomCalendarView_titleLayoutBackgroundColor,
                getResources().getColor(R.color.white));
        calendarTitleTextColor = typedArray.getColor(
                R.styleable.CustomCalendarView_calendarTitleTextColor,
                getResources().getColor(R.color.black));
        weekLayoutBackgroundColor = typedArray.getColor(
                R.styleable.CustomCalendarView_weekLayoutBackgroundColor,
                getResources().getColor(R.color.white));
        dayOfWeekTextColor = typedArray.getColor(
                R.styleable.CustomCalendarView_dayOfWeekTextColor,
                getResources().getColor(R.color.day_week_color));
        dayOfMonthTextColor = typedArray.getColor(
                R.styleable.CustomCalendarView_dayOfMonthTextColor,
                getResources().getColor(R.color.black));
        disabledDayBackgroundColor = typedArray.getColor(
                R.styleable.CustomCalendarView_disabledDayBackgroundColor,
                getResources().getColor(R.color.day_disabled_background_color));
        disabledDayTextColor = typedArray.getColor(
                R.styleable.CustomCalendarView_disabledDayTextColor,
                getResources().getColor(R.color.day_disabled_text_color));

        selectedDayBackgroundYellow = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayBackgroundYellow,
                R.drawable.selected_day_yellow);
        selectedDayStartBorderYellow = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayStartBorderYellow,
                R.drawable.border_start_yellow);
        selectedDayEndBorderYellow = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayEndBorderYellow,
                R.drawable.border_end_yellow);
        selectedDayBackgroundRangeYellow = typedArray.getColor(
                R.styleable.CustomCalendarView_selectedDayBackgroundRangeYellow,
                getResources().getColor(R.color.range_yellow));

        selectedDayBackgroundBlack = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayBackgroundBlack,
                R.drawable.selected_day_black);
        selectedDayStartBorderBlack = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayStartBorderBlack,
                R.drawable.border_start_black);
        selectedDayEndBorderBlack = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayEndBorderBlack,
                R.drawable.border_end_black);
        selectedDayBackgroundRangeBlack = typedArray.getColor(
                R.styleable.CustomCalendarView_selectedDayBackgroundRangeBlack,
                getResources().getColor(R.color.range_black));
        selectedDayTextColorWhite = typedArray.getColor(
                R.styleable.CustomCalendarView_disabledDayTextColor,
                getResources().getColor(R.color.white));

        selectedDayBackgroundGrey = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayBackgroundGrey,
                R.drawable.selected_day_grey);
        selectedDayStartBorderGrey = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayStartBorderGrey,
                R.drawable.border_start_grey);
        selectedDayEndBorderGrey = typedArray.getResourceId(
                R.styleable.CustomCalendarView_selectedDayEndBorderGrey,
                R.drawable.border_end_grey);
        selectedDayBackgroundRangeGrey = typedArray.getColor(
                R.styleable.CustomCalendarView_selectedDayBackgroundRangeGrey,
                getResources().getColor(R.color.grey));

        beforeCurrentDayColor = typedArray.getColor(
                R.styleable.CustomCalendarView_beforeCurrentDayColor,
                getResources().getColor(R.color.day_week_color));
        typedArray.recycle();
    }

    private void initializeCalendarLayout() {
        final LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.custom_calendar_layout, this, true);

        ImageView previousMonthButton = (ImageView) view.findViewById(R.id.leftButton);
        ImageView nextMonthButton = (ImageView) view.findViewById(R.id.rightButton);
        previousMonthButton.setOnClickListener(this);
        nextMonthButton.setOnClickListener(this);
        initializeCalendarData();
    }

    /**
     * Initialize calendar for current month
     */
    private void initializeCalendarData() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        setFirstDayOfWeek(Calendar.SUNDAY);
        refreshCalendar(currentCalendar);
    }

    /**
     * Display calendar title with next/previous month button
     */
    private void initializeTitleLayout() {
        View titleLayout = view.findViewById(R.id.titleLayout);
        titleLayout.setBackgroundColor(calendarTitleBackgroundColor);

        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendar.get(
                Calendar.MONTH)];

        TextView dateTitle = (TextView) view.findViewById(R.id.dateTitle);
        dateTitle.setTextColor(calendarTitleTextColor);
        dateTitle.setText(dateText + " " + currentCalendar.get(Calendar.YEAR));
        dateTitle.setTextColor(calendarTitleTextColor);
        dateTitle.setTypeface(setFont("Fonts/Roboto-Bold.ttf"));
    }

    /**
     * Initialize the calendar week layout, considers start day
     */
    private void initializeWeekLayout() {
        TextView dayOfWeek;
        String dayOfTheWeekString;

        View titleLayout = view.findViewById(R.id.weekLayout);
        titleLayout.setBackgroundColor(weekLayoutBackgroundColor);

        final String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfTheWeekString = weekDaysArray[i];
            if (dayOfTheWeekString.length() > 1) {
                dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();
            }

            dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(
                    i,
                    currentCalendar));
            dayOfWeek.setText(dayOfTheWeekString);
            dayOfWeek.setTextColor(dayOfWeekTextColor);
            dayOfWeek.setTypeface(setFont("Fonts/Roboto-Bold.ttf"));
        }
    }

    private void setDaysInCalendar() {
        Calendar calendar = getCalendarInstance();
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, calendar);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        final Calendar startCalendar = (Calendar) calendar.clone();
        startCalendar.add(Calendar.DATE, -(dayOfMonthIndex - 1));
        int monthEndIndex = 42 - (actualMaximum + dayOfMonthIndex - 1);

        for (int i = 1; i < 43; i++) {
            ViewGroup dayOfMonthContainer = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_CONTAINER + i);
            DayView dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            dayView.setTypeface(setFont("Fonts/Roboto-Bold.ttf"));
            dayView.bind(startCalendar.getTime());
            dayView.setVisibility(View.VISIBLE);

            checkIsCurrentMonth(calendar, startCalendar, monthEndIndex,
                                i, dayOfMonthContainer, dayView);
            startCalendar.add(Calendar.DATE, 1);
            dayOfMonthIndex++;
        }
        setVisibilityLastWeekRow();
    }


    private DayView getDayOfMonthText(Calendar currentCalendar) {
        return (DayView) getView(DAY_OF_MONTH_TEXT, currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        return currentDay + monthOffset;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {
            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();
        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {
            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        return view.findViewWithTag(key + index);
    }

    public void refreshCalendar(Calendar currentCalendar) {
        this.currentCalendar = currentCalendar;
        this.currentCalendar.setFirstDayOfWeek(getFirstDayOfWeek());
        locale = mContext.getResources().getConfiguration().locale;

        initializeTitleLayout();
        initializeWeekLayout();
        setDaysInCalendar();
    }

    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    private void markDaysBeforeCurrent(Calendar calendar) {
        if (calendar != null && CalendarUtils.areDaysBeforeCurrent(
                calendar,
                Calendar.getInstance())) {
            DayView dayOfMonth = getDayOfMonthText(calendar);
            dayOfMonth.setDayEnabled(false);
            dayOfMonth.setTextColor(beforeCurrentDayColor);
        }
    }

    private void markDayAsSelectedByClick(Date currentDate) {
        borders.clear();

        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);

        int indexOfSelectedDate = getDayIndexByDate(currentCalendar);
        storeNeighboringPositions(indexOfSelectedDate);

        DayView dayView = getDayOfMonthText(currentCalendar);
        if (!dayView.isDayEnabledForClick()) {
            return;
        }

        if (!dateWasSelected(currentDate, selectedDaysByClick)) {
            setDayViewBackgroundResource(
                    dayView,
                    selectedDayBackgroundBlack,
                    selectedDayTextColorWhite);
            storeLastValuesByClick(currentDate);
        } else {
            removeDayByRepeatClick(currentDate, selectedDaysByClick);
            dayView.setDayEnabledForLongClick(true);
        }
        setSingleClicksRange(CLICK_KEY_CODE);

        Log.d(TAG, "single clicks " + selectedDaysByClick.toString());
    }

    private void storeNeighboringPositions(int indexOfSelectedDate) {
        neighboringPositionsByClick.add(indexOfSelectedDate);
    }

    private void setSingleClicksRange(int keyCode) {
        if (neighboringPositionsByClick.size() >= 2) {
            int first = neighboringPositionsByClick.get(0);
            int last = neighboringPositionsByClick.get(neighboringPositionsByClick.size() - 1);
            int diff;

            if (neighboringPositionsByClick.size() == 2) {
                diff = calcDifference(first, last);
                determineRangeDirection(diff, first, last, keyCode);
            } else if (neighboringPositionsByClick.size() > 2) {
                int preLast = neighboringPositionsByClick.get(neighboringPositionsByClick.size() - 2);
                diff = calcDifference(preLast, last);
                determineRangeDirection(diff, first, last, keyCode);
            } else {
                clearAllNeighboringPositions();
                storeNeighboringPositions(last);
            }
        }
    }

    private void clearAllNeighboringPositions() {neighboringPositionsByClick.clear();}

    private void determineRangeDirection(int diff, int first, int last, int keyCode) {
        switch (diff) {
            case 1:
                setForwardRange(first, last, keyCode);
                break;
            case -1:
                setBackRange(first, last, keyCode);
                break;
            default:
                clearAllNeighboringPositions();
                storeNeighboringPositions(last);
        }
    }

    private void setForwardRange(int first, int last, int keyCode) {
        for (int i = first; i <= last; i++) {
            setDayViewRange(i, first, last, keyCode);

        }
    }

    private void setBackRange(int first, int last, int keyCode) {
        for (int i = last; i <= first; i++) {
            setDayViewRange(i, last, first, keyCode);
        }
    }

    private int calcDifference(int first, int last) {
        return last - first;
    }

    private void markDayAsSelectedDayByLongClick(Date currentDate) {
        clearAllNeighboringPositions();

        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        currentCalendar.setTime(currentDate);

        int indexOfSelectedDate = getDayIndexByDate(currentCalendar);
        DayView dayView = getDayOfMonthText(currentCalendar);
        if (!dayView.isDayEnabledForLongClick()) {
            return;
        }
        if (!dateWasSelected(currentDate, selectedDaysByLongClick) && borders.size() < 2) {
            borders.add(indexOfSelectedDate);
            setDayViewBackgroundResource(
                    dayView,
                    selectedDayBackgroundBlack,
                    selectedDayTextColorWhite);
        } else {
            removeDayByRepeatClick(currentDate, selectedDaysByLongClick);
            dayView.setDayEnabledForClick(true);
        }

        if (borders.size() == 2) {

            calculateRange(LONG_CLICK_KEY_CODE);
            borders.clear();
        }

        Log.d(TAG, "long clicks: " + selectedDaysByLongClick.toString());
    }

    private void clearSelectedDaysByClick() {selectedDaysByClick.clear();}

    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            String tagId = getTagId((ViewGroup) view);
            final DayView dayOfMonthText = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);
            final Calendar calendar = getCalendar(dayOfMonthText);

            if (!dayOfMonthText.isDayEnabled()) {
                return;
            }

            markDayAsSelectedByClick(calendar.getTime());
        }
    };

    private OnLongClickListener onDayOfMonthLongClickListener = new OnLongClickListener() {
        @Override public boolean onLongClick(View view) {
            String tagId = getTagId((ViewGroup) view);
            final DayView dayOfMonthText = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);
            final Calendar calendar = getCalendar(dayOfMonthText);

            if (!dayOfMonthText.isDayEnabled()) {
                return false;
            }

            markDayAsSelectedDayByLongClick(calendar.getTime());
            return true;
        }
    };

    private void calculateRange(int keyCode) {
        int start = borders.get(0);
        int end = borders.get(1);
        int diff = end - start;
        if (diff > 0) {
            setForwardRange(start, end, keyCode);
        } else if (diff < 0) {
            setBackRange(start, end, keyCode);
        }
    }

    private void setDayViewRange(int i, int start, int end, int keyCode) {
        DayView dayView = (DayView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
        dayView.setDayEnabledForClick(false);

        if (keyCode == CLICK_KEY_CODE) {
            dayView.setDayEnabledForLongClick(false);
        } else if (keyCode == LONG_CLICK_KEY_CODE) {
            dayView.setDayEnabledForClick(false);
        }

        if (i == start) {
            setDayViewBackgroundResource(
                    dayView,
                    selectedDayStartBorderBlack,
                    selectedDayTextColorWhite);
        } else if (i == end) {
            setDayViewBackgroundResource(
                    dayView,
                    selectedDayEndBorderBlack,
                    selectedDayTextColorWhite);
        } else {
            setDayViewColor(
                    dayView,
                    selectedDayBackgroundRangeBlack,
                    selectedDayTextColorWhite);
        }
    }

    private boolean dateWasSelected(Date currentDate, ArrayList<Date> selectedDays) {
        for (Date date : selectedDays) {
            if (date.equals(currentDate)) {
                return true;
            }
        }
        return false;
    }

    private void removeDayByRepeatClick(Date currentDate, ArrayList<Date> selectedDays) {
        try {
            for (Date date : selectedDays) {
                if (date.equals(currentDate)) {
                    selectedDays.remove(currentDate);
                }
            }
        } catch (ConcurrentModificationException cme) {
            Log.e(TAG, "removeDayByRepeatClick: ", cme.fillInStackTrace());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftButton:
                currentMonthIndex--;
                onMonthButtonClick(currentMonthIndex);
                break;
            case R.id.rightButton:
                currentMonthIndex++;
                onMonthButtonClick(currentMonthIndex);
                break;
        }
    }

    private void onMonthButtonClick(int monthIndex) {
        currentCalendar = Calendar.getInstance(Locale.getDefault());
        currentCalendar.add(Calendar.MONTH, monthIndex);
        refreshCalendar(currentCalendar);
    }

    private void checkIsCurrentMonth(
            Calendar calendar, Calendar startCalendar,
            float monthEndIndex, int i, ViewGroup dayOfMonthContainer,
            DayView dayView) {

        if (CalendarUtils.isCurrentMonth(calendar, startCalendar)) {
            dayOfMonthContainer.setOnLongClickListener(onDayOfMonthLongClickListener);
            dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            setDayViewColor(dayView, calendarBackgroundColor, calendarTitleTextColor);
            markDaysBeforeCurrent(startCalendar);
        } else {
            setDayViewColor(dayView, disabledDayBackgroundColor, disabledDayTextColor);

            if (!isOverflowDateVisible()) {
                dayView.setVisibility(View.GONE);
            } else if (i >= 36 && (monthEndIndex / 7.0f) >= 1) {
                dayView.setVisibility(View.GONE);
            }
        }
    }

    private void setVisibilityLastWeekRow() {
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
        DayView dayView = (DayView) view.findViewWithTag("dayOfMonthText36");
        if (dayView.getVisibility() != VISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }
    }

    private void setDayViewColor(DayView dayView, int colorBackground, int colorText) {
        dayView.setBackgroundColor(colorBackground);
        dayView.setTextColor(colorText);
    }

    private void setDayViewBackgroundResource(
            DayView dayView, int backgroundResource,
            int colorText) {
        dayView.setBackgroundResource(backgroundResource);
        dayView.setTextColor(colorText);
    }

    @NonNull private Calendar getCalendarInstance() {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        return calendar;
    }

    @NonNull private Calendar getCalendar(DayView dayOfMonthText) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(getFirstDayOfWeek());
        calendar.setTime(currentCalendar.getTime());
        calendar.set(
                Calendar.DAY_OF_MONTH,
                Integer.valueOf(dayOfMonthText.getText().toString()));
        return calendar;
    }

    @NonNull private String getTagId(ViewGroup view) {
        String tagId = (String) view.getTag();
        tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length(), tagId.length());
        return tagId;
    }

    private void storeLastValuesByClick(Date currentDate) {
        selectedDaysByClick.add(currentDate);
    }

    private void storeLastValuesByLongClick(Date currentDate) {
        selectedDaysByLongClick.add(currentDate);
    }

    public void setShowOverflowDate(boolean isOverFlowEnabled) {
        isOverflowDateVisible = isOverFlowEnabled;
    }

    private Typeface setFont(String font) {
        return Typeface.createFromAsset(getContext().getAssets(), font);
    }

    public boolean isOverflowDateVisible() {
        return isOverflowDateVisible;
    }
}
