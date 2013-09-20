/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.lum.lu.ui.course.keyvalues;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

/**
 * 
 * This is the helper class for CourseView
 * 
 * @author OpenCollab/rSmart KRAD CM Conversion Alliance!
 * 
 * 
 */
public class FrequencyKeyValueFinder extends UifKeyValuesFinderBase { 
        @Override
        public List<KeyValue> getKeyValues(ViewModel model) {
            List<KeyValue> labels = new ArrayList<KeyValue>();
            labels.add(new ConcreteKeyValue("1", KeyValueConstants.PER_DAY_FREQUENCY_KEY));
            labels.add(new ConcreteKeyValue("2", KeyValueConstants.PER_MONTH_FREQUENCY_KEY));
            labels.add(new ConcreteKeyValue("3", KeyValueConstants.PER_WEEK_FREQUENCY_KEY));
            return labels;
        }
    
}
