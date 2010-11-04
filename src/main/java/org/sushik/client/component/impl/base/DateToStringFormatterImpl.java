/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base;

import java.util.Date;
import java.util.List;
import org.sushik.client.component.infc.base.DateToStringFormatter;

/**
 * component configuraiton logic
 */
public class DateToStringFormatterImpl extends ConvertLogicImpl
  implements DateToStringFormatter
{

 public DateToStringFormatterImpl ()
 {
  super ();
 }
 private String format;

 @Override
 public String getFormat ()
 {
  return this.format;
 }

 @Override
 public void setFormat (String format)
 {
  this.format = format;
 }

 @Override
 public String format (Date date)
 {
  if (date == null)
  {
   return null;
  }

  if (format.equals ("yyyy-MM-dd"))
  {
   // Can't use SimpleDateFormat inside GWT
   // but don't want to use GWT specific code so this is indepdent of GWT
   // so...
   int yyyy = date.getYear () + 1900;
   int mm = date.getMonth () + 1;
   int dd = date.getDate ();
   String yr = yyyy + "-";
   String mnth = mm + "-";
   if (mm < 10)
   {
    mnth = "0" + mnth;
   }
   String dom = "" + dd;
   if (dd < 10)
   {
    dom = "0" + dom;
   }
   return yr + "-" + mnth + "-" + dom;
  }
  if (format.equals ("MM-dd-yyyy"))
  {
   // Can't use SimpleDateFormat inside GWT
   // but don't want to use GWT specific code so this is indepdent of GWT
   // so...
   int yyyy = date.getYear () + 1900;
   int mm = date.getMonth () + 1;
   int dd = date.getDate ();
   String yr = yyyy + "-";
   String mnth = mm + "-";
   if (mm < 10)
   {
    mnth = "0" + mnth;
   }
   String dom = "" + dd;
   if (dd < 10)
   {
    dom = "0" + dom;
   }
   return mnth + "-" + dom + "-" + yr;
  }
  if (format.equals ("yyyyMMdd"))
  {
   // Can't use SimpleDateFormat inside GWT
   // but don't want to use GWT specific code so this is indepdent of GWT
   // so...
   int yyyy = date.getYear () + 1900;
   int mm = date.getMonth () + 1;
   int dd = date.getDate ();
   String yr = yyyy + "-";
   String mnth = mm + "-";
   if (mm < 10)
   {
    mnth = "0" + mnth;
   }
   String dom = "" + dd;
   if (dd < 10)
   {
    dom = "0" + dom;
   }
   return yr + mnth + dom;
  }
  throw new UnsupportedOperationException ("format not supported " + format);
 }
}

