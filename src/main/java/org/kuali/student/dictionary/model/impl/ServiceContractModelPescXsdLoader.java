/*
 * Copyright 2010 The Kuali Foundation
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
package org.kuali.student.dictionary.model.impl;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethodReturnValue;
import org.kuali.student.dictionary.model.XmlType;
import org.xml.sax.SAXException;

/**
 *
 * @author nwright
 */
public class ServiceContractModelPescXsdLoader implements
  ServiceContractModel
{

 private String xsdFileName;
 private List<Service> services = null;
 private List<ServiceMethod> serviceMethods = null;
 private Map<String, XmlType> xmlTypeMap = null;
 private List<MessageStructure> messageStructures;

 public ServiceContractModelPescXsdLoader (String xsdFileName)
 {
  this.xsdFileName = xsdFileName;
 }

 @Override
 public List<ServiceMethod> getServiceMethods ()
 {
  if (this.serviceMethods == null)
  {
   this.parse ();
  }
  return this.serviceMethods;
 }

 @Override
 public List<String> getSourceNames ()
 {
  List<String> list = new ArrayList ();
  list.add (this.xsdFileName);
  return list;
 }

 @Override
 public List<Service> getServices ()
 {
  if (services == null)
  {
   this.parse ();
  }
  return services;
 }

 @Override
 public List<XmlType> getXmlTypes ()
 {
  if (xmlTypeMap == null)
  {
   this.parse ();
  }
  return new ArrayList (xmlTypeMap.values ());
 }

 @Override
 public List<MessageStructure> getMessageStructures ()
 {
  if (messageStructures == null)
  {
   this.parse ();
  }
  return this.messageStructures;
 }

 private void parse ()
 {
  System.out.println ("ServiceContractModelQDoxLoader: Starting parse");
  services = new ArrayList ();
  Service service = new Service ();
  services.add (service);
  service.setKey ("Pesc");
  service.setName ("PescService");
  service.setComments ("Derived from pesc CoreMain");
  serviceMethods = new ArrayList ();
  ServiceMethod method = new ServiceMethod ();
  serviceMethods.add (method);
  method.setName ("dummy");
  method.setDescription ("Dummy method so validation won't fail");
  method.setService ("Pesc");
  method.setParameters (new ArrayList ());
  ServiceMethodReturnValue rv = new ServiceMethodReturnValue ();
  rv.setType ("void");
  rv.setDescription ("returns nothing");
  method.setReturnValue (rv);
  xmlTypeMap = new LinkedHashMap ();
  messageStructures = new ArrayList ();

  XSOMParser parser = new XSOMParser ();
  parser.setAnnotationParser (new XsdAnnotationParserFactory ());
  try
  {
   parser.parse (new File (this.xsdFileName));
  }
  catch (SAXException ex)
  {
   throw new IllegalArgumentException (ex);
  }
  catch (IOException ex)
  {
   throw new IllegalArgumentException (ex);
  }

  XSSchemaSet schemaSet;
  try
  {
   schemaSet = parser.getResult ();
  }
  catch (SAXException ex)
  {
   throw new IllegalArgumentException (ex);
  }

  for (XSSchema schema : schemaSet.getSchemas ())
  {
   System.out.println ("Schema Namespace=" + schema.getTargetNamespace ());
   for (XSComplexType ct : schema.getComplexTypes ().values ())
   {
    System.out.println ("ComplexType =" + ct.getName ());
    addComplexType (ct);
   }
  }
 }

 private void addComplexType (XSComplexType complexType)
 {
  XmlType xmlType = xmlTypeMap.get (complexType.getName ());
  if (xmlType != null)
  {
   System.out.println ("Already processed complex Type="
                       + complexType.getName ());
   return;
  }
  xmlType = new XmlType ();
  xmlTypeMap.put (complexType.getName (), xmlType);
  xmlType.setName (complexType.getName ());
  xmlType.setDesc (calcMissing (calcDesc (complexType.getAnnotation ())));
  xmlType.setComments ("???");
  xmlType.setExamples ("???");
  xmlType.setService ("Pesc");

  XSContentType contentType = complexType.getContentType ();
  XSParticle particle = contentType.asParticle ();
  if (particle != null)
  {
   XSTerm term = particle.getTerm ();
   if (term.isModelGroup ())
   {
    XSModelGroup xsModelGroup = term.asModelGroup ();
    XSParticle[] particles = xsModelGroup.getChildren ();
    for (XSParticle p : particles)
    {
     XSTerm pterm = p.getTerm ();
     if (pterm.isElementDecl ())
     { //xs:element inside complex type
      XSElementDecl element = pterm.asElementDecl ();
      addMessageStructure (xmlType.getName (), element);
     }
    }
   }
  }
 }

 private String calcMissing (String str)
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

 private String calcDesc (XSAnnotation annotation)
 {
  if (annotation == null)
  {
   return null;
  }
  if (annotation.getAnnotation () == null)
  {
   return null;
  }
  return annotation.getAnnotation ().toString ();
 }

 private void addMessageStructure (String xmlObject, XSElementDecl element)
 {
  MessageStructure ms = new MessageStructure ();
  this.messageStructures.add (ms);
  ms.setXmlObject (xmlObject);
  ms.setName (element.getName ());
  ms.setId (xmlObject + "." + ms.getName ());
  ms.setType (calcType (element));
  ms.setDescription (calcMissing (calcDesc (element.getAnnotation ())));
  System.out.println ("Element " + ms.getId () + " " + ms.getType ());
  ms.setRequired (calcRequired (element));
  ms.setCardinality (calcCardinality (element));
 }

 private String calcType (XSElementDecl element)
 {
  return calcType (element.getType ());
 }

 private String calcType (XSType xsType)
 {
  String type = xsType.getName ();
  return type;
 }

 private String calcRequired (XSElementDecl element)
 {
  return "No";
 }

 private String calcCardinality (XSElementDecl element)
 {
  return "One";
 }
}
