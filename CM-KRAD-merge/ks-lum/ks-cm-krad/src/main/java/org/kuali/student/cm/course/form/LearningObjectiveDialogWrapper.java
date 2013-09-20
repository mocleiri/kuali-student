/**
 * Copyright 2005-2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 1.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kuali.student.cm.course.form;

import java.io.Serializable;
import java.util.List;

public class LearningObjectiveDialogWrapper implements Serializable {
	
	private static final long serialVersionUID = 5469451568404361391L;

    private List<String> typeKeyFilters;
	
	private String categoryName;
	
	private List<LoCategoryInfoWrapper> learningObjectiveOptions;

    public List<String> getTypeKeyFilters() {
        return typeKeyFilters;
    }

    public void setTypeKeyFilters(List<String> typeKeyFilters) {
        this.typeKeyFilters = typeKeyFilters;
    }

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<LoCategoryInfoWrapper> getLearningObjectiveOptions() {
		return learningObjectiveOptions;
	}

	public void setLearningObjectiveOptions(
			List<LoCategoryInfoWrapper> learningObjectiveOptions) {
		this.learningObjectiveOptions = learningObjectiveOptions;
	}
	
}
