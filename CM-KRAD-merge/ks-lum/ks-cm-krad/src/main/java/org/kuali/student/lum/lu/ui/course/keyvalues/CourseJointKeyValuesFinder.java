package org.kuali.student.lum.lu.ui.course.keyvalues;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

public class CourseJointKeyValuesFinder extends UifKeyValuesFinderBase {
	
	private static final long serialVersionUID = 1394806282459489941L;

	public enum SearchByKeys {
		COURSES_AND_PROPOSALS("Courses and Proposals"), COURSES_ONLY("Courses Only"), PROPOSALS_ONLY("Proposals Only");
		
		private String display;
		
		SearchByKeys(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}
	}

	@Override
	public List<KeyValue> getKeyValues(ViewModel model) {
		final List<KeyValue> keyValues = new ArrayList<KeyValue>();
		keyValues.add(new ConcreteKeyValue(SearchByKeys.COURSES_AND_PROPOSALS.toString(), SearchByKeys.COURSES_AND_PROPOSALS.getDisplay()));
		keyValues.add(new ConcreteKeyValue(SearchByKeys.COURSES_ONLY.toString(), SearchByKeys.COURSES_ONLY.getDisplay()));
		keyValues.add(new ConcreteKeyValue(SearchByKeys.PROPOSALS_ONLY.toString(), SearchByKeys.PROPOSALS_ONLY.getDisplay()));
        return keyValues;
	}

}
