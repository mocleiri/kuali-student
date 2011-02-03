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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jws.WebParam;
import org.kuali.student.dictionary.model.MessageStructure;
import org.kuali.student.dictionary.model.Service;
import org.kuali.student.dictionary.model.ServiceMethod;
import org.kuali.student.dictionary.model.ServiceContractModel;
import org.kuali.student.dictionary.model.ServiceMethodError;
import org.kuali.student.dictionary.model.ServiceMethodParameter;
import org.kuali.student.dictionary.model.ServiceMethodReturnValue;
import org.kuali.student.dictionary.model.XmlType;

/**
 *
 * @author nwright
 */
public class ServiceContractModelAnnotationLoader implements
  ServiceContractModel
{

 private List<Class<?>> serviceClasses = null;
 private List<ServiceMethod> serviceMethods = null;
 private Map<String, XmlType> xmlTypeMap = null;
 private List<MessageStructure> messageStructures;

 public ServiceContractModelAnnotationLoader (List<Class<?>> serviceClasses)
 {
  this.serviceClasses = serviceClasses;
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
  List<String> list = new ArrayList (this.serviceClasses.size ());
  for (Class<?> serviceClass : this.serviceClasses)
  {
   list.add (serviceClass.getSimpleName ());
  }
  return list;
 }

 @Override
 public List<Service> getServices ()
 {
  List<Service> list = new ArrayList (this.serviceClasses.size ());
  for (Class<?> serviceClass : this.serviceClasses)
  {
   Service service = new Service ();
   service.setKey (serviceClass.getSimpleName ());
   service.setName ("????");
   list.add (service);
  }
  return list;
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
  System.out.println ("STarting parse");
  serviceMethods = new ArrayList ();
  for (Class<?> serviceClass : serviceClasses)
  {
   for (Method method : serviceClass.getDeclaredMethods ())
   {
    for (Annotation annotation : method.getAnnotations ())
    {
     System.out.println (method.getName () + " has annotation=" + annotation);
    }
    ServiceMethod serviceMethod = new ServiceMethod ();
    serviceMethod.setService (serviceClass.getSimpleName ());
    serviceMethod.setName (method.getName ());
    serviceMethod.setDescription ("???");
    serviceMethod.setParameters (new ArrayList ());
//    Paranamer paranamer = new CachingParanamer (new BytecodeReadingParanamer ());
//    String[] paramNames = paranamer.lookupParameterNames (method);
    Class<?>[] paramClasses = method.getParameterTypes ();
    for (int i = 0; i < paramClasses.length; i ++)
    {
     Class<?> paramClass = paramClasses[i];
     Annotation[] paramAnnotations = method.getParameterAnnotations ()[i];
     for (Annotation annotation : paramAnnotations)
     {
      System.out.println ("Method " + method.getName () + "param#" + i
                          + " has annotation=" + annotation);
     }
//     String paramName = paramNames[i];
     ServiceMethodParameter param = new ServiceMethodParameter ();
     param.setName ("???");
     param.setType (paramClass.getSimpleName ());
     param.setDescription ("????");
     serviceMethod.getParameters ().add (param);
     addXmlTypeAndMessageStructure (paramClass, serviceMethod.getService ());
    }
    serviceMethod.setErrors (new ArrayList ());
    for (Class<?> exceptionClass : method.getExceptionTypes ())
    {
     ServiceMethodError error = new ServiceMethodError ();
     error.setType (exceptionClass.getSimpleName ());
     error.setDescription ("????");
     serviceMethod.getErrors ().add (error);
    }
    ServiceMethodReturnValue rv = new ServiceMethodReturnValue ();
    serviceMethod.setReturnValue (rv);
    rv.setType (method.getReturnType ().getSimpleName ());
    rv.setDescription ("???");
    addXmlTypeAndMessageStructure (method.getReturnType (),
                                   serviceMethod.getService ());
    serviceMethods.add (serviceMethod);
   }
  }
 }

 private String calcParameterName (Annotation[] paramAnnotations)
 {
  for (Annotation annotation : paramAnnotations)
  {
   if (annotation instanceof WebParam)
   {
    WebParam webParam = (WebParam) annotation;
    return webParam.name ();
   }
  }
  return "????";
 }

 private void addXmlTypeAndMessageStructure (Class<?> msClass, String service)
 {
  if (xmlTypeMap == null)
  {
   xmlTypeMap = new LinkedHashMap ();
  }
  XmlType xmlType = xmlTypeMap.get (msClass.getName ());
  if (xmlType == null)
  {
   xmlType = new XmlType ();
   xmlTypeMap.put (msClass.getName (), xmlType);
   xmlType.setName (msClass.getSimpleName ());
   xmlType.setDesc ("???");
   xmlType.setComments ("???");
   xmlType.setExamples ("???");
   xmlType.setService (service);
   addMessageStructure (msClass, service);
  }
  else
  {
   if ( ! xmlType.getService ().contains (service))
   {
    xmlType.setService (xmlType.getService () + ", " + service);
   }
  }
 }

 private void addMessageStructure (Class<?> msClass, String service)
 {
  if (this.messageStructures == null)
  {
   this.messageStructures = new ArrayList ();
  }
  Set<Class<?>> complexSubObjectsToAdd = new LinkedHashSet ();
  for (Method method : msClass.getDeclaredMethods ())
  {
   String shortName = this.calcShortName (method);
   if (shortName == null)
   {
    continue;
   }
   MessageStructure ms = new MessageStructure ();
   ms.setXmlObject (msClass.getSimpleName ());
   ms.setShortName (shortName);
   ms.setName ("????");
   ms.setType (calcType (method));
   ms.setXmlAttribute ("??");
   ms.setCardinality (this.calcCardinality (method));
   ms.setDescription ("???");
   ms.setFeedback ("");
   ms.setStatus ("");
   if (isComplex (method))
   {
    complexSubObjectsToAdd.add (this.calcSetterClass (method));
   }
  }
  // now add all it's complex sub-objects if they haven't already been added
  for (Class<?> clazzToAdd : complexSubObjectsToAdd)
  {
   XmlType xmlType = xmlTypeMap.get (clazzToAdd.getName ());
   if (xmlType == null)
   {
    addXmlTypeAndMessageStructure (clazzToAdd, service);
   }
  }
  return;
 }

 private String calcShortName (Method method)
 {
  if ( ! method.getName ().startsWith ("set"))
  {
   return null;
  }
  if (method.getParameterTypes ().length != 1)
  {
   return null;
  }
  return method.getName ().substring (3);
 }

 private String calcCardinality (Method setterMethod)
 {
  if (isList (setterMethod))
  {
   return "Many";
  }
  return "One";
 }

 private boolean isList (Method method)
 {
  Class<?> setterParamClass = method.getParameterTypes ()[0];
  if (setterParamClass.equals (List.class))
  {
   return true;
  }
  return false;
 }

 private String calcType (Method setterMethod)
 {
  if (isList (setterMethod))
  {
   return calcSetterClass (setterMethod).getSimpleName () + "List";
  }
  return calcSetterClass (setterMethod).getSimpleName ();
 }

 private Class calcSetterClass (Method setterMethod)
 {
  if ( ! this.isList (setterMethod))
  {
   return setterMethod.getParameterTypes ()[0];
  }
  Class<?> listClazz = setterMethod.getParameterTypes ()[0];

  for (Type type : listClazz.getGenericInterfaces ())
  {
   System.out.println ("type = " + type.toString ());
  }
  ParameterizedType paramType =
                    (ParameterizedType) listClazz.getGenericInterfaces ()[0];
  Class clazz = (Class) paramType.getActualTypeArguments ()[0];
  return clazz;
 }

 private boolean isComplex (Method setterMethod)
 {
  Class clazz = calcSetterClass (setterMethod);
  if (clazz.equals (String.class))
  {
   return false;
  }
  if (clazz.equals (Integer.class))
  {
   return false;
  }
  if (clazz.equals (Date.class))
  {
   return false;
  }
  if (clazz.equals (Long.class))
  {
   return false;
  }
  if (clazz.equals (Boolean.class))
  {
   return false;
  }
  if (clazz.equals (Double.class))
  {
   return false;
  }
  if (clazz.equals (Float.class))
  {
   return false;
  }
  if (clazz.equals (int.class))
  {
   return false;
  }
  if (clazz.equals (long.class))
  {
   return false;
  }
  if (clazz.equals (boolean.class))
  {
   return false;
  }
  if (clazz.equals (double.class))
  {
   return false;
  }
  if (clazz.equals (float.class))
  {
   return false;
  }
  return true;
 }
}
