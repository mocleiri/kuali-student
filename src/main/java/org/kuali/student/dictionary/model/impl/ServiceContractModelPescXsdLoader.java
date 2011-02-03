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

import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethodParameter;
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

 public ServiceContractModelPescXsdLoader (String xsdFileName)
 {
  this.xsdFileName = xsdFileName;
 }

 @Override
 public List<ServiceMethod> getServiceMethods ()
 {
  List<ServiceMethod> list = new ArrayList ();
  return list;
 }

 @Override
 public List<String> getSourceNames ()
 {
  return Arrays.asList (this.xsdFileName);
 }

 @Override
 public List<Service> getServices ()
 {
  List<Service> list = new ArrayList ();
  return list;
 }

 @Override
 public List<XmlType> getXmlTypes ()
 {
  Map<String, XmlType> map = new HashMap ();
  XSOMParser parser = new XSOMParser ();
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
//   ServiceMethodReturnValue rv = method.getReturnValue ();
//   if (rv != null)
//   {
//    XmlType xmlType = map.get (rv.getType ());
//    if (xmlType == null)
//    {
//     xmlType = new XmlType ();
//     map.put (rv.getType (), xmlType);
//     xmlType.setName (rv.getType ());
//     xmlType.setDesc ("???");
//     xmlType.setComments ("???");
//     xmlType.setExamples ("???");
//     xmlType.setService (method.getService ());
//    }
//    else
//    {
//     if ( ! xmlType.getService ().contains (method.getService ()))
//     {
//      xmlType.setService (xmlType.getService () + ", " + method.getService ());
//     }
//    }
//   }
//   for (ServiceMethodParameter parm : method.getParameters ())
//   {
//    XmlType xmlType = map.get (parm.getType ());
//    if (xmlType == null)
//    {
//     xmlType = new XmlType ();
//     map.put (parm.getType (), xmlType);
//     xmlType.setName (parm.getType ());
//     xmlType.setDesc ("???");
//     xmlType.setComments ("???");
//     xmlType.setExamples ("???");
//     xmlType.setService (method.getService ());
//    }
//    else
//    {
//     if ( ! xmlType.getService ().contains (method.getService ()))
//     {
//      xmlType.setService (xmlType.getService () + ", " + method.getService ());
//     }
//    }
//   }
  }
  return new ArrayList (map.values ());
 }

 @Override
 public List<MessageStructure> getMessageStructures ()
 {
  List<MessageStructure> list = new ArrayList ();

  return list;
 }
}
