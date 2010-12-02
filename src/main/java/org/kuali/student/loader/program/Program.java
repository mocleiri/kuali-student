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
package org.kuali.student.loader.program;

/**
 *
 * @author nwright
 */
public class Program
{
 private String id;
 private String code;
 private String type;
 private String variationOf;
 private String shortTitle;
 private String longTitle;
 private String transcriptTitle;
 private String universityClassification;
 private String descr;

 public String getId ()
 {
  return id;
 }

 public void setId (String id)
 {
  this.id = id;
 }

 
 public String getCode ()
 {
  return code;
 }

 public void setCode (String code)
 {
  this.code = code;
 }

 public String getVariationOf ()
 {
  return variationOf;
 }

 public void setVariationOf (String variationOf)
 {
  this.variationOf = variationOf;
 }

 
 public String getLongTitle ()
 {
  return longTitle;
 }

 public void setLongTitle (String longTitle)
 {
  this.longTitle = longTitle;
 }

 public String getShortTitle ()
 {
  return shortTitle;
 }

 public void setShortTitle (String shortTitle)
 {
  this.shortTitle = shortTitle;
 }

 public String getTranscriptTitle ()
 {
  return transcriptTitle;
 }

 public void setTranscriptTitle (String transcriptTitle)
 {
  this.transcriptTitle = transcriptTitle;
 }

 public String getType ()
 {
  return type;
 }

 public void setType (String type)
 {
  this.type = type;
 }

 public String getUniversityClassification ()
 {
  return universityClassification;
 }

 public void setUniversityClassification (String universityClassification)
 {
  this.universityClassification = universityClassification;
 }

 public String getDescr ()
 {
  return descr;
 }

 public void setDescr (String descr)
 {
  this.descr = descr;
 }

 
}
