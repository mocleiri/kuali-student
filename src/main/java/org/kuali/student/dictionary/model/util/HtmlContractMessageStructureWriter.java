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

import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.XmlType;
import org.kuali.student.dictionary.writer.HtmlWriter;

/**
 *
 * @author nwright
 */
public class HtmlContractMessageStructureWriter
{

 private XmlType xmlType;
 private HtmlWriter writer;
 private ServiceContractModel model;
 private ModelFinder finder;

 public HtmlContractMessageStructureWriter (XmlType xmlType, String directory,
                                    ServiceContractModel model)
 {
  this.xmlType = xmlType;
  this.writer = new HtmlWriter (directory, xmlType.getName () + ".html",
                                xmlType.getName ());
  this.model = model;
  this.finder = new ModelFinder (this.model);
 }

 public void write ()
 {
  writer.print ("<a href=\"index.html\">home</a>");
  this.writeStyleSheet ();
  writer.writeTag ("h1", xmlType.getName ());

  writer.indentPrintln ("<table id=\"structureMetaTable\">");
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Name");
  writer.writeTag ("td", "id=structureName colspan=2", xmlType.getName ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Version");
  writer.writeTag ("td", "id=structureVersion colspan=2", xmlType.getVersion ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("<tr>");
  writer.writeTag ("th", "class=h", "Usage");
  writer.indentPrint ("<td id=\"structureVersion\">");
  for (String servKey : xmlType.getService ().split (","))
  {
   writer.indentPrintln ("<a href=\"" + servKey + "Service" + ".html" + "\">"
                         + servKey + "Service" + "</a>");
  }
  writer.indentPrint ("</td>");

  writer.indentPrintln ("</tr>");
  writer.writeTag ("th", "class=h", "Type");
  writer.writeTag ("td", "id=structureVersion colspan=2",
                   xmlType.getPrimitive ());
  writer.indentPrintln ("</tr>");
  writer.indentPrintln ("</table>");

  writer.writeTag ("h2", "Description");
  writer.indentPrintln (xmlType.getDesc ());

  if ( ! xmlType.getPrimitive ().equals (XmlType.COMPLEX))
  {
   return;
  }
  writer.indentPrintln (
    "<h2><a name=\"StructureDefinition\"></a>Structure Definition</h2>");


  writer.indentPrintln ("<table class=\"structTable\">");
  writer.indentPrintln ("<tr>");
  writer.indentPrintln ("<th class=\"h\">ShortName</th>");
  writer.indentPrintln ("<th class=\"h\">Name</th>");
  writer.indentPrintln ("<th class=\"h\">Type</th>");
  writer.indentPrintln ("<th class=\"h\">Description</th>");
  writer.indentPrintln ("<th class=\"h\">Required</th>");
  writer.indentPrintln ("<th class=\"h\">Cardinality</th>");
  writer.indentPrintln ("<th class=\"h\">XML Attribute?</th>");
  writer.indentPrintln ("<th class=\"h\">Data Team Status</th>");
  writer.indentPrintln ("<th class=\"h\">Comments/Feedback</th>");
  writer.indentPrintln ("</tr>");

  for (MessageStructure ms : finder.findMessageStructures (xmlType.getName ()))
  {
   this.writeMessageStructure (ms);
  }
  writer.indentPrintln ("</table>");
  writer.writeHeaderBodyAndFooterOutToFile ();
 }

 public void writeMessageStructure (MessageStructure ms)
 {
  writer.indentPrintln ("<tr>");
  writer.writeTag ("td", "class=\"structSName\"", ms.getShortName ());
  writer.writeTag ("td", "class=\"structLName\"", ms.getName ());
  XmlType subType = finder.findXmlType (this.stripListFromType (ms.getType ()));
  if (subType == null)
  {
   for (XmlType xmlt : model.getXmlTypes ())
   {
    System.out.println (this.getClass ().getSimpleName () + ": "
                        + xmlt.getName ());
   }
   throw new NullPointerException (ms.getXmlObject () + "." + ms.getShortName ()
                                   + " has type " + ms.getType ()
                                   + " was not found in list of known types");
  }
  if (subType.getPrimitive ().equals (XmlType.COMPLEX))
  {
   writer.indentPrint ("<td class=\"structType\">");
   writer.indentPrintln ("<a href=\"" + subType.getName () + ".html" + "\">"
                         + ms.getType () + "</a>");
   writer.indentPrint ("</td>");
  }
  else
  {
   writer.writeTag ("td", "class=\"structType\"", ms.getType ());
  }
  writer.writeTag ("td", "class=\"structDesc\"", missingData (ms.getDescription ()));
  writer.writeTag ("td", "class=\"structOpt\"", ms.getOptional ());
  writer.writeTag ("td", "class=\"structCard\"", ms.getCardinality ());
  writer.writeTag ("td", "class=\"structAttr\"", ms.getXmlAttribute ());
  writer.writeTag ("td", "class=\"structStatus\"", ms.getStatus ());
  writer.writeTag ("td", "class=\"commentsDesc\"", ms.getFeedback ());
  writer.indentPrintln ("</tr>");

//  writer.indentPrintln ("</table>");
//  writer.indentPrintln ("<p>");

//  writer.indentPrintln ("<p>");
 }

 private String missingData (String str)
 {
  if (str == null)
  {
   return "???";
  }
  if (str.trim ().isEmpty ())
  {
   return "???";
  }
  return str;
 }
 private String stripListFromType (String type)
 {
  if (type.endsWith ("List"))
  {
   return type.substring (0, type.length () - "List".length ());
  }
  return type;
 }

 public void writeStyleSheet ()
 {
  writer.indentPrintln ("<style type=\"text/css\">");
  writer.indentPrintln ("");
  writer.indentPrintln ("table#structureMetaTable {");
  writer.indentPrintln ("border-collapse:collapse;");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:95%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#structureMetaTable th.h {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#eeeeee;");
  writer.indentPrintln ("width:15%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#structureMetaTable td#structureName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:85%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#structureMetaTable td#structureVersion {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:70%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("table#structureMetaTable td#structureVersionHistory {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:15%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("</style>");

  writer.indentPrintln ("<style type=\"text/css\">");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable {");
  writer.indentPrintln ("border-collapse:collapse;");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("width:95%;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.d {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable th.h {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#eeeeee;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structSName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background-color:#f2f2f2;");
  writer.indentPrintln ("color:#222222;");
  writer.indentPrintln ("text-align:left;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("font-weight:bold;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structLName {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structType {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("font-style:italic;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structOpt {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structReq {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ccccff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structCard {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structAttr {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structElem {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ccccff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.structStatus {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("text-align: center;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("table.structTable td.commentsDesc {");
  writer.indentPrintln ("border:1px solid #000000;");
  writer.indentPrintln ("background:#ffffff;");
  writer.indentPrintln ("vertical-align:top;");
  writer.indentPrintln ("");
  writer.indentPrintln ("}");
  writer.indentPrintln ("");
  writer.indentPrintln ("");
  writer.indentPrintln ("</style>");


 }
}
