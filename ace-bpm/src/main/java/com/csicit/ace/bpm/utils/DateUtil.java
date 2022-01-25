package com.csicit.ace.bpm.utils;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class DateUtil {
    public static void main(String[] args) {
        Calendar cal = new GregorianCalendar();
        // cal now contains current date
        System.out.println(cal.getTime());

        // add the working days
        int workingDaysToAdd = 5;
        for (int i=0; i<workingDaysToAdd; i++)
            do {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            } while (! isWorkingDay(cal));
        System.out.println(cal.getTime());
    }
    private static boolean isWorkingDay(Calendar cal) {
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)
            return false;
        // tests for other holidays here
        // ...
        return true;
    }
}