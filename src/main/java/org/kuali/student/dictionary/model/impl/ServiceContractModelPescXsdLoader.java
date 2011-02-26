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
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
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
   for (XSSimpleType st : schema.getSimpleTypes ().values ())
   {
    System.out.println ("SimpleType =" + st.getName () + " namespace=" + st.getTargetNamespace ());
    addSimpleType (st);
   }
   for (XSComplexType ct : schema.getComplexTypes ().values ())
   {
    if ( ! shouldInclude (ct))
    {
     System.out.println ("Skipping ComplexType =" + ct.getName () + " namespace=" + ct.getTargetNamespace ());
     continue;
    }
    System.out.println ("ComplexType =" + ct.getName () + " namespace=" + ct.getTargetNamespace ());
    addComplexType (ct);
   }
  }
 }

 private boolean shouldInclude (XSComplexType ct)
 {
  if (ct.getTargetNamespace ().equals ("urn:org:pesc:core:CoreMain:v1.8.0"))
  {
   return true;
  }
  return false;
 }

 private void addSimpleType (XSSimpleType simpleType)
 {
  XmlType xmlType = xmlTypeMap.get (simpleType.getName ());
  if (xmlType != null)
  {
   System.out.println ("Already processed simple Type="
                       + simpleType.getName ());
   return;
  }
  xmlType = new XmlType ();
  xmlTypeMap.put (simpleType.getName (), xmlType);
  xmlType.setName (simpleType.getName ());
  xmlType.setDesc (calcMissing (calcDesc (simpleType.getAnnotation ())));
  xmlType.setComments ("???");
  xmlType.setExamples ("???");
  xmlType.setService ("Pesc");
  xmlType.setPrimitive ("Primitive");
 }

 private void addComplexType (XSComplexType complexType)
 {
  addComplexType (complexType, complexType.getName ());
 }

 private void addComplexType (XSComplexType complexType, String name)
 {
  XmlType xmlType = xmlTypeMap.get (name);
  if (xmlType != null)
  {
   System.out.println ("Already processed complex Type="  + name);
   return;
  }
  xmlType = new XmlType ();
  xmlTypeMap.put (name, xmlType);
  xmlType.setName (name);
  xmlType.setDesc (calcMissing (calcDesc (complexType.getAnnotation ())));
  xmlType.setComments ("???");
  xmlType.setExamples ("???");
  xmlType.setService ("Pesc");
  xmlType.setPrimitive (XmlType.COMPLEX);

  boolean found = false;
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
      found = true;
     }
    }
   }
  }
  if ( ! found)
  {
   System.out.println ("*** WARNING *** Complex Type, " + xmlType.getName () + ", has no message structure fields");
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
  ms.setShortName (element.getName ());
  ms.setName ("???");
  ms.setId (xmlObject + "." + ms.getShortName ());
  ms.setType (calcType (element, xmlObject + "" + ms.getShortName ()));
  ms.setDescription (calcMissing (calcDesc (element.getAnnotation ())));
  System.out.println ("Element " + ms.getId () + " " + ms.getType ());
  ms.setRequired (calcRequired (element));
  ms.setCardinality (calcCardinality (element));
 }

 private String calcType (XSElementDecl element, String inLinePrefix)
 {
  String type = calcType (element.getType (), inLinePrefix);
  if (type != null)
  {
   return type;
  }
  return type;
 }

 private String calcType (XSType xsType, String inLinePrefix)
 {
  if (xsType.isSimpleType ())
  {
   XSSimpleType st = xsType.asSimpleType ();
   return st.getBaseType ().getName ();
//   if (st.isRestriction ())
//   {
//    XSRestrictionSimpleType res = st.asRestriction ();
//    return res.getBaseType ().getName ();
//   }
  }
  String type = xsType.getName ();
  if (type != null)
  {
   return type;
  }
  if ((xsType.isComplexType ()))
  {
   XSComplexType ct = xsType.asComplexType ();
   String baseType = ct.getBaseType ().getName ();
   if (baseType.equals ("anyType"))
   {
    baseType = "";
   }
   String inlineTypeName =  inLinePrefix + baseType;
   addComplexType (ct, inlineTypeName);
   return inlineTypeName;
  }
  throw new IllegalArgumentException ("cannot calculate the type of the field " + inLinePrefix);
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
