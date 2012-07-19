package com.sigmasys.kuali.ksa.util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by: dmulderink on 6/29/12 at 12:41 PM
 */
public class CalendarUtils {

    // DatatypeFactory creates new javax.xml.datatype Objects that map XML to/from Java Objects.
    private static DatatypeFactory df = null;

    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(
                    "Error while trying to obtain a new instance of DatatypeFactory", e);
        }
    }

    private CalendarUtils() {
    }

    // Converts a java.util.Date into an instance of XMLGregorianCalendar
    public static XMLGregorianCalendar toXmlGregorianCalendar(Date date) {
        if (date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
        return null;
    }

    // Converts an XMLGregorianCalendar to an instance of java.util.Date
    public static Date toDate(XMLGregorianCalendar calendar) {
        return (calendar != null) ? calendar.toGregorianCalendar().getTime() : null;
    }
}
