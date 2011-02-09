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

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.Annotation;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class ServiceContractModelQDoxLoader implements
  ServiceContractModel
{

 private List<String> sourceDirectories = null;
 private List<Service> services = null;
 private List<ServiceMethod> serviceMethods = null;
 private Map<String, XmlType> xmlTypeMap = null;
 private List<MessageStructure> messageStructures;

 public ServiceContractModelQDoxLoader (List<String> sourceDirectories)
 {
  this.sourceDirectories = sourceDirectories;
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
  List<String> list = new ArrayList (this.sourceDirectories.size ());
  for (String javaFile : this.sourceDirectories)
  {
   list.add (javaFile);
  }
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

 private String dump (DocletTag tag)
 {
  if (tag == null)
  {
   return null;
  }
  StringBuilder bldr = new StringBuilder ();
  bldr.append (tag.getName ());
  bldr.append ("=");
  if (tag.getNamedParameterMap () == null
      || tag.getNamedParameterMap ().isEmpty ())
  {
   bldr.append (tag.getValue ());
  }
  else
  {
   for (Object key : tag.getNamedParameterMap ().keySet ())
   {
    Object value = tag.getNamedParameterMap ().get (key);
    bldr.append ("(");
    bldr.append (key);
    bldr.append ("=");
    bldr.append (value);
    bldr.append (")");
   }
  }
  return bldr.toString ();
 }

 private void parse ()
 {
  System.out.println ("ServiceContractModelQDoxLoader: Starting parse");
  services = new ArrayList ();
  serviceMethods = new ArrayList ();
  JavaDocBuilder builder = new JavaDocBuilder ();
  for (String sourceDirectory : sourceDirectories)
  {
   builder.addSourceTree (new File (sourceDirectory));
  }
  for (JavaClass javaClass : builder.getClasses ())
  {
   if ( ! this.isServiceToProcess (javaClass))
   {
    continue;
   }
   Service service = new Service ();
   services.add (service);
   service.setKey (javaClass.getName ().substring (0, javaClass.getName ().length ()
                                                      - "Service".length ()));
   service.setName (javaClass.getName ());
   service.setComments (javaClass.getComment ());
   service.setUrl (this.calcServiceUrl (javaClass));
   service.setVersion ("???");
   service.setStatus ("???");
   service.setIncludedServices (calcIncludedServices (javaClass));

//   for (DocletTag tag : javaClass.getTags ())
//   {
//    System.out.println ("ServiceContractModelQDoxLoader: Class: "
//                        + javaClass.getName () + " has tag=" + dump (
//      tag));
//   }
   for (JavaMethod javaMethod : javaClass.getMethods ())
   {

    ServiceMethod serviceMethod = new ServiceMethod ();
    serviceMethods.add (serviceMethod);
    serviceMethod.setService (service.getKey ());
    serviceMethod.setName (javaMethod.getName ());
    serviceMethod.setDescription (calcMissing (javaMethod.getComment ()));
    serviceMethod.setParameters (new ArrayList ());
//    for (DocletTag tag : javaMethod.getTags ())
//    {
//     System.out.println ("ServiceContractModelQDoxLoader: Method: "
//                         + service.getName () + "."
//                         + javaMethod.getName ()
//                         + " has tag=" + dump (tag));
//    }
    // parameters
    for (JavaParameter parameter : javaMethod.getParameters ())
    {
     ServiceMethodParameter param = new ServiceMethodParameter ();
     serviceMethod.getParameters ().add (param);
     param.setName (parameter.getName ());
     param.setType (calcType (parameter.getType ()));
     param.setDescription (calcMissing (
       calcParameterDescription (javaMethod,
                                 param.getName ())));
     addXmlTypeAndMessageStructure (calcRealJavaClass (parameter.getType ()),
                                    serviceMethod.getService ());
    }
    // errors
    serviceMethod.setErrors (new ArrayList ());
    for (Type exception : javaMethod.getExceptions ())
    {
     ServiceMethodError error = new ServiceMethodError ();
     error.setType (this.calcType (exception.getJavaClass ()));
     error.setDescription (calcMissing (
       calcExceptionDescription (javaMethod,
                                 error.getType ())));
     error.setPackageName (exception.getJavaClass ().getPackageName ());
     error.setClassName (exception.getJavaClass ().getName ());
     serviceMethod.getErrors ().add (error);
    }
    // return values
    ServiceMethodReturnValue rv = new ServiceMethodReturnValue ();
    serviceMethod.setReturnValue (rv);
    rv.setType (this.calcType (javaMethod.getReturnType ()));
    rv.setDescription (calcMissing (this.calcReturnDescription (javaMethod)));
    addXmlTypeAndMessageStructure (calcRealJavaClass (
      javaMethod.getReturnType ()),
                                   serviceMethod.getService ());
   }
  }
 }

 private boolean isServiceToProcess (JavaClass javaClass)
 {
  if ( ! javaClass.getName ().endsWith ("Service"))
  {
   return false;
  }
  if (javaClass.getPackageName ().contains (".old."))
  {
   return false;
  }
  if (javaClass.getPackageName ().endsWith (".old"))
  {
   return false;
  }
  return true;
 }

 private String calcIncludedServices (JavaClass javaClass)
 {
  // The QDox parser is broken
  // it says that CommentService does not:
  // (1) have a superclass
  // (2) implement anything
  // (3) implement any intefaces anything
  // Even the code block has the implements stripped out!
//  CodeBlock=/**
// * @Author KSContractMojo
// * @Author Neerav Agrawal
// * @Since Fri Jun 05 14:27:10 EDT 2009
// * @See <a href="https://test.kuali.org/confluence/display/KULSTR/Comment+Service+v1.0-rc1">CommentService</>
// */
//public interface CommentService {
//
//        /**
//         * Retrieves the list of types which can be tagged or commented.
//         *
//         * @return the list of types which can be tagged or commented
//         * @throws OperationFailedException unable to complete request
//         */
//        public java.util.List getReferenceTypes() throws org.kuali.student.core.exceptions.OperationFailedException;
//
//  when in reality it has stuff
//
//    /**
// *
// * @Author KSContractMojo
// * @Author Neerav Agrawal
// * @Since Fri Jun 05 14:27:10 EDT 2009
// * @See <a href="https://test.kuali.org/confluence/display/KULSTR/Comment+Service+v1.0-rc1">CommentService</>
// *
// */
//@WebService(name = "CommentService", targetNamespace = "http://student.kuali.org/wsdl/comment")
//@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
////@XmlSeeAlso({org.kuali.student.core.dto.ReferenceTypeInfo.class})
//public interface CommentService extends DictionaryService {
//    /**
//     * Retrieves the list of types which can be tagged or commented.
//     * @return the list of types which can be tagged or commented
//     * @throws OperationFailedException unable to complete request
//	 */
//    public List<ReferenceTypeInfo> getReferenceTypes() throws OperationFailedException;

  System.out.println ("ServiceContractModelQDoxLoader:" + javaClass.getName ()
                      + " extends " + javaClass.getSuperClass ());
  System.out.println (javaClass.getName () + " implmenets "
                      + javaClass.getImplements ().length + " things");
  System.out.println (javaClass.getName () + " implmenets "
                      + javaClass.getImplementedInterfaces ().length
                      + " interfaces");
//  System.out.println ("CodeBlock=" + javaClass.getCodeBlock ());
//   StringBuilder includedServices = new StringBuilder ();
//   String comma = "";
//   for (Type type : javaClass.getImplements ())
//   {
//    System.out.println ("ServiceContractModelQDoxLoader:" + javaClass.getName ()
//                        + " implements " + type);
//   }
//   for (JavaClass interfaceClass : javaClass.getImplementedInterfaces ())
//   {
//    System.out.println ("ServiceContractModelQDoxLoader:" + javaClass.getName ()
//                        + " implements " + interfaceClass.getName ());
//    includedServices.append (comma);
//    comma = ", ";
//    includedServices.append (interfaceClass.getName ());
//   }
  return null;
 }

 private String calcParameterDescription (JavaMethod method,
                                          String parameterName)
 {
  for (DocletTag tag : method.getTags ())
  {
   if (tag.getName ().equals ("param"))
   {
    if (tag.getValue ().startsWith (parameterName + " "))
    {
     return tag.getValue ().substring (parameterName.length () + 1);
    }
   }
  }
  return null;
 }

 private String calcExceptionDescription (JavaMethod serviceMethod,
                                          String exceptionType)
 {
  for (DocletTag tag : serviceMethod.getTags ())
  {
   if (tag.getName ().equals ("throws"))
   {
    if (tag.getValue ().startsWith (exceptionType + " "))
    {
     return tag.getValue ().substring (exceptionType.length () + 1);
    }
   }
  }
  return null;
 }

 private String calcReturnDescription (JavaMethod serviceMethod)
 {
  for (DocletTag tag : serviceMethod.getTags ())
  {
   if (tag.getName ().equals ("return"))
   {
    return tag.getValue ();
   }
  }
  return null;
 }

 private String calcServiceUrl (JavaClass serviceClass)
 {
  for (DocletTag tag : serviceClass.getTags ())
  {
   if (tag.getName ().equals ("See"))
   {
    return tag.getValue ();
   }
  }
  return null;
 }

 private void addXmlTypeAndMessageStructure (JavaClass messageStructureJavaClass,
                                             String serviceKey)
 {
  if (xmlTypeMap == null)
  {
   xmlTypeMap = new LinkedHashMap ();
  }
  XmlType xmlType = xmlTypeMap.get (messageStructureJavaClass.getName ());
  if (xmlType == null)
  {
   xmlType = new XmlType ();
   xmlTypeMap.put (messageStructureJavaClass.getName (), xmlType);
   xmlType.setName (messageStructureJavaClass.getName ());
   xmlType.setDesc (messageStructureJavaClass.getComment ());
   xmlType.setService (serviceKey);
   xmlType.setVersion ("???");
   xmlType.setPrimitive (calcPrimitive (messageStructureJavaClass));
   if (xmlType.getPrimitive ().equals (XmlType.COMPLEX))
   {
    addMessageStructure (messageStructureJavaClass, serviceKey);
   }
  }
  else
  {
   addServiceToList (xmlType, serviceKey);
  }
 }

 private String calcPrimitive (JavaClass javaClass)
 {
  if (this.isComplex (javaClass))
  {
   return XmlType.COMPLEX;
  }
  return "Primitive";
 }

 private void addMessageStructure (JavaClass messageStructureJavaClass,
                                   String serviceKey)
 {
  if (this.messageStructures == null)
  {
   this.messageStructures = new ArrayList ();
  }
  Set<JavaClass> subObjectsToAdd = new LinkedHashSet ();
  Set<String> fieldsAdded = new HashSet ();
  for (JavaMethod setterMethod : messageStructureJavaClass.getMethods (true))
  {
   if ( ! isSetterMethod (setterMethod, messageStructureJavaClass.getName ()))
   {
    continue;
   }
   String shortName = this.calcShortName (setterMethod);
   JavaMethod getterMethod = findGetterMethod (messageStructureJavaClass,
                                               shortName);
   if (getterMethod == null)
   {
    throw new IllegalArgumentException ("Setter method has no corresponding getter method: "
                                        + messageStructureJavaClass.getName ()
                                        + "." + setterMethod.getName ());
   }
   JavaField beanField = this.findField (messageStructureJavaClass,
                                         shortName);
   if (beanField == null)
   {
    throw new IllegalArgumentException ("Setter method has no corresponding bean field: "
                                        + messageStructureJavaClass.getName ()
                                        + "." + setterMethod.getName ());
   }
   // overide the shortName if the bean field has an XmlAttribute name=xxx
   // this catches the key=id switch
   for (Annotation annotation : beanField.getAnnotations ())
   {
    if (annotation.getType ().getJavaClass ().getName ().equals ("XmlAttribute"))
    {
     Object nameValue = annotation.getNamedParameter ("name");
     if (nameValue != null)
     {
      shortName = stripQuotes (nameValue.toString ());
     }
    }
   }
   if (fieldsAdded.contains (shortName))
   {
    continue;
   }
   fieldsAdded.add (shortName);
   MessageStructure ms = new MessageStructure ();
   messageStructures.add (ms);
   ms.setXmlObject (messageStructureJavaClass.getName ());
   ms.setShortName (shortName);
   ms.setId (ms.getXmlObject () + "." + ms.getShortName ());
   ms.setName ("????");
   ms.setType (calcTypeOfSetterMethodFirstParam (setterMethod));
   if (ms.getType ().equals ("Object"))
   {
    System.out.println (ms.getId ()
                        + " has Object as it's type ==> Changing to String");
    ms.setType ("String");
   }
   else if (ms.getType ().equals ("ObjectList"))
   {
    System.out.println (
      ms.getId ()
      + " has a list of Objects as it's type ==> Changing to List of String");
    ms.setType ("StringList");
   }
   ms.setXmlAttribute (this.calcXmlAttribute (beanField));
   ms.setOptional ("???");
   ms.setCardinality (this.calcCardinality (setterMethod));
   ms.setDescription (calcMissing (getterMethod.getComment ()));
   ms.setFeedback ("???");
   ms.setStatus ("???");
   JavaClass subObjToAdd = this.calcRealJavaClassOfSetterFirstParam (
     setterMethod);
   if ( ! subObjToAdd.isEnum ())
   {
    if ( ! subObjToAdd.getName ().equals ("Object"))
    {
     if ( ! subObjToAdd.getName ().equals ("LocaleKeyList"))
     {
      if ( ! subObjToAdd.getName ().equals ("MessageGroupKeyList"))
      {
       subObjectsToAdd.add (subObjToAdd);
      }
     }
    }
   }
  }
  // now add all it's complex sub-objects if they haven't already been added
  for (JavaClass subObjectToAdd : subObjectsToAdd)
  {
   XmlType xmlType = xmlTypeMap.get (subObjectToAdd.getName ());
   if (xmlType == null)
   {
    addXmlTypeAndMessageStructure (subObjectToAdd, serviceKey);
   }
   else
   {
    addServiceToList (xmlType, serviceKey);
   }
  }
  return;
 }


 private String stripQuotes (String str)
 {
  if (str.startsWith ("\""))
  {
   str = str.substring (1);
  }
  if (str.endsWith ("\""))
  {
   str = str.substring (0, str.length () - 1);
  }
  return str;
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

 private void addServiceToList (XmlType xmlType, String serviceKey)
 {
  if ( ! xmlType.getService ().contains (serviceKey))
  {
   xmlType.setService (xmlType.getService () + ", " + serviceKey);
  }
 }

 private String calcXmlAttribute (JavaField beanField)
 {
  for (Annotation annotation : beanField.getAnnotations ())
  {
   if (annotation.getType ().getJavaClass ().getName ().equals ("XmlAttribute"))
   {
    return "Yes";
   }
  }
  return "No";
 }

 private JavaField findField (JavaClass javaClass, String shortName)
 {
  for (JavaField field : javaClass.getFields ())
  {
   if (field.getName ().equalsIgnoreCase (shortName))
   {
    return field;
   }
   // TODO: check for shortNames that already start with is so we don't check for isIsEnrollable
   if (field.getName ().equals ("is" + shortName))
   {
    return field;
   }
  }
  JavaClass superClass = javaClass.getSuperJavaClass ();
  if (superClass == null)
  {
   return null;
  }
  return findField (superClass, shortName);
 }

 private JavaMethod findGetterMethod (JavaClass msClass, String shortName)
 {
  for (JavaMethod method : msClass.getMethods (true))
  {
   if (method.getName ().equals ("get" + shortName))
   {
    return method;
   }
   // TODO: check for shortNames that already start with is so we don't check for isIsEnrollable
   if (method.getName ().equals ("is" + shortName))
   {
    return method;
   }
  }
  return null;
 }
 private static final String[] SETTER_METHODS_TO_SKIP =
 {
  // Somebody put "convenience" methods on the validation result info
  "ValidationResultInfo.setWarning",
  "ValidationResultInfo.setError",
  // not on original wiki but still defined as a method but not backed by a field so not in wsdl
  "CredentialProgramInfo.setDiplomaTitle",
  // synonym for the official of setCredentialType
  "CredentialProgramInfo.setType",
  // not on original wiki but still defined as a method but not backed by a field so not in wsdl
  "CredentialProgramInfo.setHegisCode",
  "CredentialProgramInfo.setCip2000Code",
  "CredentialProgramInfo.setCip2010Code",
  "CredentialProgramInfo.setSelectiveEnrollmentCode",
  "CoreProgramInfo.setDiplomaTitle",
  // synonym for the official of setCredentialType
  //  "CoreProgramInfo.setType",
  // not on original wiki but still defined as a method but not backed by a field so not in wsdl
  "CoreProgramInfo.setHegisCode",
  "CoreProgramInfo.setCip2000Code",
  "CoreProgramInfo.setCip2010Code",
  "CoreProgramInfo.setSelectiveEnrollmentCode",
  "WhenConstraint.setValue"
 };

 private boolean isSetterMethod (JavaMethod method, String className)
 {
  if ( ! method.getName ().startsWith ("set"))
  {
   return false;
  }
  if (method.getParameters ().length != 1)
  {
   return false;
  }
  String fullName = className + "." + method.getName ();
  for (String skip : SETTER_METHODS_TO_SKIP)
  {
   if (skip.equals (fullName))
   {
    return false;
   }
  }
  return true;
 }

 private String calcShortName (JavaMethod method)
 {
  return method.getName ().substring (3);
 }

 private String calcCardinality (JavaMethod setterMethod)
 {
  if (isList (setterMethod))
  {
   return "Many";
  }
  return "One";
 }

 private boolean isList (JavaMethod method)
 {
  JavaParameter param = method.getParameters ()[0];
  Type type = param.getType ();
  return isList (type);
 }

 private boolean isList (Type type)
 {
  JavaClass setterParamClass = type.getJavaClass ();
  return this.isList (setterParamClass);
 }

 private boolean isList (JavaClass setterParamClass)
 {
  if (setterParamClass.getName ().equals (List.class.getSimpleName ()))
  {
   return true;
  }
  if (setterParamClass.getName ().equals (ArrayList.class.getSimpleName ()))
  {
   return true;
  }
  return false;
 }

 private String calcTypeOfSetterMethodFirstParam (JavaMethod setterMethod)
 {
  JavaParameter param = setterMethod.getParameters ()[0];
  return calcType (param);
 }

 private String calcType (JavaParameter parameter)
 {
  return calcType (parameter.getType ());
 }

 private String calcType (Type type)
 {
  if (isList (type.getJavaClass ()))
  {
   return calcType (calcRealJavaClass (type)) + "List";
  }
  return calcType (calcRealJavaClass (type));
 }

 private String calcType (JavaClass javaClass)
 {
  if (javaClass.isEnum ())
  {
   if (javaClass.getName ().equals ("ErrorLevel"))
   {
    return "Integer";
   }
   if (javaClass.getName ().equals ("StatementOperatorTypeKey"))
   {
    return "String";
   }
   if (javaClass.getName ().equals ("WriteAccess"))
   {
    return "String";
   }
   if (javaClass.getName ().equals ("Widget"))
   {
    return "String";
   }
   if (javaClass.getName ().equals ("DataType"))
   {
    return "String";
   }
   if (javaClass.getName ().equals ("SortDirection"))
   {
    return "String";
   }
   if (javaClass.getName ().equals ("Usage"))
   {
    return "String";
   }
  }
  // this is messed up instead of list of strings it is an object with a list of strings
  if (javaClass.getName ().equals ("LocaleKeyList"))
  {
   return "StringList";
  }
  if (javaClass.getName ().equals ("MessageGroupKeyList"))
  {
   return "StringList";
  }
  return javaClass.getName ();
 }

 private JavaClass calcRealJavaClassOfSetterFirstParam (JavaMethod setterMethod)
 {
  JavaParameter param = setterMethod.getParameters ()[0];
  return this.calcRealJavaClass (param);
 }

 private JavaClass calcRealJavaClass (JavaParameter param)
 {
  Type type = param.getType ();
  return calcRealJavaClass (type);
 }

 private JavaClass calcRealJavaClass (Type type)
 {
  JavaClass javaClass = type.getJavaClass ();
  if ( ! this.isList (javaClass))
  {
   return javaClass;
  }

//  for (Type t : type.getActualTypeArguments ())
//  {
//   System.out.println ("ServiceContractModelQDoxLoader: type arguments = "
//                       + t.toString ());
//  }

  Type t = type.getActualTypeArguments ()[0];
  return t.getJavaClass ();
 }

 private boolean isComplex (JavaClass javaClass)
 {
  if (javaClass.isEnum ())
  {
   return false;
  }
  if (javaClass.getName ().equals (String.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Integer.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Date.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Long.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Boolean.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Double.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Float.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (int.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (long.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (boolean.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (double.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (float.class.getSimpleName ()))
  {
   return false;
  }
  if (javaClass.getName ().equals (Map.class.getSimpleName ()))
  {
   return false;
  }
  return true;
 }
}
