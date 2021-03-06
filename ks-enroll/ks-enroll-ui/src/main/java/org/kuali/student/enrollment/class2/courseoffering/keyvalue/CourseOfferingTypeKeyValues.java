/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.kuali.student.enrollment.class2.courseoffering.keyvalue;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingManagementUtil;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  Retrieves a complete list of type and creates a KeyValue for it.
 */
public class CourseOfferingTypeKeyValues extends UifKeyValuesFinderBase implements Serializable {

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        QueryByCriteria.Builder qBuilder = QueryByCriteria.Builder.create();
        try{
            List<TypeInfo> types = CourseOfferingManagementUtil.getTypeService().searchForTypes(qBuilder.build(), new ContextInfo());
            for(TypeInfo type : types){
                keyValues.add(new ConcreteKeyValue(type.getKey(),type.getName()));
            }
        }catch(Exception e){
           throw new RuntimeException("Error getting CourseOfferingTypeKeyValues", e);
        }

        return keyValues;
    }
}
