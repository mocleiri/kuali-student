/*
 * Copyright 2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.dictionary.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.XmlType;
import org.kuali.student.dictionary.writer.HtmlWriter;

/**
 *
 * @author nwright
 */
public class HtmlContractWriter
{

 private HtmlWriter writer;
 private ServiceContractModel model;
 private ModelFinder finder;
 private String directory;

 public HtmlContractWriter (String directory,
                            ServiceContractModel model)
 {
  this.writer = new HtmlWriter (directory, "index.html",
                                "Service Contracts Index");
  this.directory = directory;
  this.model = model;
  this.finder = new ModelFinder (this.model);
 }

 public void write ()
 {
  this.writeIndexPage ();
  for (Service service : model.getServices ())
  {
   HtmlContractServiceWriter swriter = new HtmlContractServiceWriter (service,
                                                                      directory,
                                                                      model);
   swriter.write ();
  }
  for (XmlType xmlType : model.getXmlTypes ())
  {
   HtmlContractMessageStructureWriter msWriter =
                                      new HtmlContractMessageStructureWriter (
     xmlType,
     directory,
     model);
   msWriter.write ();
  }
 }
 private static final Comparator<XmlType> XML_TYPE_NAME_COMPARATOR =
                                          new Comparator<XmlType> ()
 {

  @Override
  public int compare (XmlType e1, XmlType e2)
  {
   return e1.getName ().compareTo (e2.getName ());
  }
 };
 private static final Comparator<Service> SERVICE_NAME_COMPARATOR =
                                          new Comparator<Service> ()
 {

  @Override
  public int compare (Service e1, Service e2)
  {
   return e1.getName ().compareTo (e2.getName ());
  }
 };

 private void writeIndexPage ()
 {
  writer.print ("<a href=\"index.html\">home</a>");
  writer.writeTag ("h1", "Service Contracts");
  writer.indentPrintln (
    "<div class=\"panel\" style=\"background-color: rgb(255, 255, 255); border: 1px solid rgb(204, 204, 204);\">");
  writer.indentPrintln (
    "<div class=\"panelHeader\" style=\"border-bottom: 1px solid rgb(204, 204, 204); background-color: rgb(238, 238, 238);\">");
  writer.indentPrintln ("<b><a name=\"Services\"></a>Services</b>");
  writer.indentPrintln (
    "</div><div class=\"panelContent\" style=\"background-color: rgb(255, 255, 255);\">");
  writer.indentPrintln ("<ul>");
  List<Service> services = new ArrayList (model.getServices ());
  Collections.sort (services, SERVICE_NAME_COMPARATOR);
  for (Service service : services)
  {
   writer.indentPrint ("<li>");
   writer.print ("<a href=\"" + service.getName () + ".html"
                 + "\">" + service.getName () + "</a>");
   writer.print ("</li>");
  }
  writer.indentPrintln ("</ul>");
  writer.indentPrintln ("</div>");
  writer.indentPrintln ("</div>");

  writer.indentPrintln (
    "<div class=\"panel\" style=\"background-color: rgb(255, 255, 255); border: 1px solid rgb(204, 204, 204);\">");
  writer.indentPrintln (
    "<div class=\"panelHeader\" style=\"border-bottom: 1px solid rgb(204, 204, 204); background-color: rgb(238, 238, 238);\">");
  writer.indentPrintln (
    "<b><a name=\"MessageStructures\"></a>Message Structures</b>");
  writer.indentPrintln (
    "</div><div class=\"panelContent\" style=\"background-color: rgb(255, 255, 255);\">");
  writer.indentPrintln ("<ul>");
  List<XmlType> types = new ArrayList (model.getXmlTypes ());
  Collections.sort (types, XML_TYPE_NAME_COMPARATOR);
  for (XmlType type : types)
  {
   if (type.getPrimitive () == null)
   {
    throw new NullPointerException (type.getName () + " has no primitive flag set");
   }
   if (type.getPrimitive ().equals (XmlType.COMPLEX))
   {
    writer.indentPrint ("<li>");
    writer.print ("<a href=\"" + type.getName () + ".html"
                  + "\">" + type.getName () + "</a>");
    writer.print ("</li>");
   }
  }
  writer.indentPrintln ("</ul>");
  writer.indentPrintln ("</div>");
  writer.indentPrintln ("</div>");

  writer.writeHeaderBodyAndFooterOutToFile ();
 }
}
