package com.sigmasys.kuali.ksa.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * CalendarUtils
 *
 * @author Michael Ivanov
 */
public class CalendarUtils {

    // DatatypeFactory creates new javax.xml.datatype Objects that map XML to/from Java Objects.
    private static DatatypeFactory datatypeFactory = null;

    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(
                    "Error while trying to obtain a new instance of DatatypeFactory", e);
        }
    }

    private CalendarUtils() {
    }

    // Converts a java.util.Date into an instance of XMLGregorianCalendar
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date, boolean removeTime) {
        if (date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setLenient(false);
            gc.setTime(date);
            if (removeTime) {
                gc = removeTime(gc);
            }
            return datatypeFactory.newXMLGregorianCalendar(gc);
        }
        return null;
    }

    // Converts a java.util.Date into an instance of XMLGregorianCalendar
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) {
        return toXmlGregorianCalendar(date, false);
    }

    // Converts an XMLGregorianCalendar to an instance of java.util.Date
    public static Date toDate(XMLGregorianCalendar calendar) {
        return (calendar != null) ? calendar.toGregorianCalendar().getTime() : null;
    }

    public static GregorianCalendar removeTime(GregorianCalendar calendar) {
        // Remove the hours, minutes, seconds and milliseconds.
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // Return the date again.
        return calendar;
    }

    public static Date removeTime(Date date) {
        // Get an instance of the Calendar.
        GregorianCalendar calendar = new GregorianCalendar();
        // Make sure the calendar will not perform automatic correction.
        calendar.setLenient(false);
        // Set the time of the calendar to the given date.
        calendar.setTime(date);
        calendar = removeTime(calendar);
        // Return the date again.
        return calendar.getTime();
    }

}
