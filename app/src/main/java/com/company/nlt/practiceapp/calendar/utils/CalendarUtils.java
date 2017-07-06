package com.company.nlt.practiceapp.calendar.utils;

import java.util.Calendar;

public class CalendarUtils {

    public static boolean isCurrentMonth(Calendar c1, Calendar c2) {
        return !(c1 == null || c2 == null)
                && (c1.get(Calendar.ERA) == c2.get(Calendar.ERA)
                && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH));
    }

    public static boolean areDaysBeforeCurrent(Calendar c1, Calendar c2) {
        int c1Year = c1.get(Calendar.YEAR);
        int c1Month = c1.get(Calendar.MONTH);
        int c1Day = c1.get(Calendar.DAY_OF_MONTH);

        int c2Year = c2.get(Calendar.YEAR);
        int c2Month = c2.get(Calendar.MONTH);
        int c2Day = c2.get(Calendar.DAY_OF_MONTH);

        return compareYears(c1Year, c2Year, c1Month, c2Month, c1Day, c2Day);
    }

    private static boolean compareYears(int y1, int y2, int m1, int m2, int d1, int d2) {
        if (y1 < y2) {
            return true;
        } else if (y1 > y2) {
            return false;
        } else {
            return compareMonth(m1, m2, d1, d2);
        }
    }

    private static boolean compareMonth(int m1, int m2, int d1, int d2) {
        if (m1 > m2) {
            return false;
        } else if (m1 < m2) {
            return true;
        } else {
            return compareDays(d1, d2);
        }
    }

    private static boolean compareDays(int d1, int d2) {
        return d1 < d2;
    }
}
