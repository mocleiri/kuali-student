/*
 * Copyright 2010 Wright Communications, Inc
 *
 * All rights reserved.
 */
package org.sushik.client.component.impl.base;

import java.util.Date;
import java.util.List;
import org.sushik.client.component.infc.base.StringToDateConverter;

/**
 * component configuraiton logic
 */
public class StringToDateConverterImpl extends ConvertLogicImpl
  implements StringToDateConverter
{

 public StringToDateConverterImpl ()
 {
  super ();
 }

 @Override
 public Date asDate (String value)
 {
  if (value == null)
  {
   return null;
  }
  if (value.trim ().equals (""))
  {
   return null;
  }
  value = value.trim ();
  for (String format : formats)
  {
   if (format.equals ("yyyy-MM-dd"))
   {
    if (value.length () == 10)
    {
     // Can't use SimpleDateFormat inside GWT
     // but don't want to use GWT specific code so this is indepdent of GWT
     // so...
     try
     {
      int yyyy = Integer.parseInt (value.substring (0, 4)) - 1900;
      int mm = Integer.parseInt (value.substring (5, 7)) - 1;
      int dd = Integer.parseInt (value.substring (8, 10));
      Date date = new Date (yyyy, mm, dd);
      return date;
     }
     catch (NumberFormatException ex)
     {
      // ignore because we are trying lots of formats until we find one that works
     }
    }
   }
   if (format.equals ("MM-dd-yyyy"))
   {
    if (value.length () == 10)
    {
     try
     {
      // Can't use SimpleDateFormat inside GWT
      // but don't want to use GWT specific code so this is indepdent of GWT
      // so...
      int yyyy = Integer.parseInt (value.substring (6, 10)) - 1900;
      int mm = Integer.parseInt (value.substring (0, 2)) - 1;
      int dd = Integer.parseInt (value.substring (3, 5));
      Date date = new Date (yyyy, mm, dd);
      return date;
     }
     catch (NumberFormatException ex)
     {
      // ignore because we are trying lots of formats until we find one that works
     }
    }
    if (format.equals ("yyyyMMdd"))
    {
     if (value.length () == 8)
     {
      try
      {
       // Can't use SimpleDateFormat inside GWT
       // but don't want to use GWT specific code so this is indepdent of GWT
       // so...
       int yyyy = Integer.parseInt (value.substring (0, 4)) - 1900;
       int mm = Integer.parseInt (value.substring (4, 6)) - 1;
       int dd = Integer.parseInt (value.substring (6, 8));
       Date date = new Date (yyyy, mm, dd);
       return date;
      }
      catch (NumberFormatException ex)
      {
       // ignore because we are trying lots of formats until we find one that works
      }
     }
    }
   }
   throw new UnsupportedOperationException ("format not supported " + format);
  }
  // could not parse
  return null;
 }
 private List<String> formats;

 @Override
 public List<String> getFormats ()
 {
  return this.formats;
 }

 @Override
 public void setFormats (List<String> formats)
 {
  this.formats = formats;
 }
}

