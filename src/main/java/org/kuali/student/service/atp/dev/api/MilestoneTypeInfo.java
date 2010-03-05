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
package org.kuali.student.service.atp.dev.api;


import java.util.Date;
import java.util.Map;


public interface MilestoneTypeInfo
{
	
	/**
	* Set Milestone Type Name
	*
	* Type: string
	*
	* Friendly name of the milestone type.
	*/
	public void setName(String name);
	
	/**
	* Get Milestone Type Name
	*
	* Type: string
	*
	* Friendly name of the milestone type.
	*/
	public String getName();
	
	
	
	/**
	* Set Milestone Type Description
	*
	* Type: string
	*
	* Narrative description of the milestone type.
	*/
	public void setDesc(String desc);
	
	/**
	* Get Milestone Type Description
	*
	* Type: string
	*
	* Narrative description of the milestone type.
	*/
	public String getDesc();
	
	
	
	/**
	* Set Effective Date
	*
	* Type: dateTime
	*
	* Date and time that this milestone type became effective. This is a similar 
	* concept to the effective date on enumerated values. When an expiration date has 
	* been specified, this field must be less than or equal to the expiration date.
	*/
	public void setEffectiveDate(Date effectiveDate);
	
	/**
	* Get Effective Date
	*
	* Type: dateTime
	*
	* Date and time that this milestone type became effective. This is a similar 
	* concept to the effective date on enumerated values. When an expiration date has 
	* been specified, this field must be less than or equal to the expiration date.
	*/
	public Date getEffectiveDate();
	
	
	
	/**
	* Set Expiration Date
	*
	* Type: dateTime
	*
	* Date and time that this milestone type expires. This is a similar concept to the 
	* expiration date on enumerated values. If specified, this must be greater than or 
	* equal to the effective date. If this field is not specified, then no expiration 
	* date has been currently defined and should automatically be considered greater 
	* than the effective date.
	*/
	public void setExpirationDate(Date expirationDate);
	
	/**
	* Get Expiration Date
	*
	* Type: dateTime
	*
	* Date and time that this milestone type expires. This is a similar concept to the 
	* expiration date on enumerated values. If specified, this must be greater than or 
	* equal to the effective date. If this field is not specified, then no expiration 
	* date has been currently defined and should automatically be considered greater 
	* than the effective date.
	*/
	public Date getExpirationDate();
	
	
	
	/**
	* Set Generic/dynamic attributes
	*
	* Type: attributeInfoList
	*
	* List of key/value pairs, typically used for dynamic attributes.
	*/
	public void setAttributes(Map<String,String> attributes);
	
	/**
	* Get Generic/dynamic attributes
	*
	* Type: attributeInfoList
	*
	* List of key/value pairs, typically used for dynamic attributes.
	*/
	public Map<String,String> getAttributes();
	
	
	
	/**
	* Set Milestone Type
	*
	* Type: milestoneTypeKey
	*
	* Unique identifier for a milestone type.
	*/
	public void setKey(String key);
	
	/**
	* Get Milestone Type
	*
	* Type: milestoneTypeKey
	*
	* Unique identifier for a milestone type.
	*/
	public String getKey();
	
	
	
}

