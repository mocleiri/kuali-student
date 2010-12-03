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
package org.kuali.student.loader.util;

import java.util.HashMap;
import java.util.Map;
import org.kuali.student.core.dto.TimeAmountInfo;

/**
 *
 * @author nwright
 */
public class AttributeInfoHelper
{

 public Map<String, String> setValue (String key, Object value,
                                      Map<String, String> attributes)
 {
  if (value == null)
  {
   if (attributes == null)
   {
    return null;
   }
   attributes.remove (key);
   return attributes;
  }
  if (attributes == null)
  {
   attributes = new HashMap ();
  }
  String valStr = value.toString ();
  attributes.put (key, valStr);
  return attributes;
 }
}
