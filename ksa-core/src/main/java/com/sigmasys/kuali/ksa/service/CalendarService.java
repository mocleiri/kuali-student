package com.sigmasys.kuali.ksa.service;

import java.util.Calendar;
import java.util.Date;

/**
 * CalendarService. Singleton.
 *
 * @author Michael Ivanov
 *         Date: 4/9/12
 */
public class CalendarService {

    private static final CalendarService singleton = new CalendarService();

    private CalendarService() {
    }

    public static CalendarService getInstance() {
        return singleton;
    }

    public Date addWorkingDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i = 0; i < days; i++) {
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SATURDAY:
                    calendar.add(Calendar.DATE, 3);
                    break;
                case Calendar.SUNDAY:
                    calendar.add(Calendar.DATE, 2);
                    break;
                default:
                    calendar.add(Calendar.DATE, 1);
            }
        }

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                calendar.add(Calendar.DATE, 2);
                break;
            case Calendar.SUNDAY:
                calendar.add(Calendar.DATE, 1);
        }

        return calendar.getTime();
    }

    public Date setTime(Date date, int hours, int minutes, int seconds, int millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);
        cal.set(Calendar.MILLISECOND, millis);
        return cal.getTime();
    }

    public Date truncate(Date date) {
        return setTime(date, 0, 0, 0, 0);
    }

    public Integer getWorkingDaysBetween(Date fromDate, Date toDate) {

        if (fromDate == null || toDate == null) {
            return 0;
        }

        if (fromDate.after(toDate)) {
            Date temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(fromDate);
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(toDate);

        int iNoOfWorkingDays = 0;
        do {
            if (calFrom.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calFrom.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                iNoOfWorkingDays += 1;
            }
            calFrom.add(Calendar.DATE, 1);
        } while (calFrom.getTimeInMillis() < calTo.getTimeInMillis());

        return iNoOfWorkingDays;
    }

    public Integer getCalendarDaysBetween(Date fromDate, Date toDate) {
        Calendar calFrom = Calendar.getInstance();
        calFrom.setTime(fromDate);
        Calendar calTo = Calendar.getInstance();
        calTo.setTime(toDate);
        int days = 0;
        while (calFrom.before(calTo)) {
            calFrom.add(Calendar.DAY_OF_MONTH, 1);
            days++;
        }
        return days;
    }

    public Date addCalendarDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public Date addCalendarMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

}
