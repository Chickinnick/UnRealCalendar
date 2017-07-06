package com.company.nlt.practiceapp.calendar;


import java.util.ArrayList;
import java.util.Date;

public class CalendarDataHolder {

    private ArrayList<Date> selectedDays;

    public CalendarDataHolder() {
        this.selectedDays = new ArrayList<>();
    }

    public void tryAdd(Date date, AddCallback addCallback) {
        if(!selectedDays.contains(date)){
            selectedDays.add(date);
            addCallback.result(true);
        }else {
            selectedDays.remove(date);
            addCallback.result(false);
        }
    }

    public ArrayList<Date> getSelectedDays() {
        return selectedDays;
    }

    public interface AddCallback {

        void result(boolean isAdded);
    }
}
