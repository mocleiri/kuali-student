package org.kuali.student.ap.academicplan.infc;

import java.util.List;

import org.kuali.student.myplan.academicplan.infc.LearningPlan;

/**
 * Degree Map, which has a list of Degree Map Requirements.
 *
 * @Author mguilla
 */
public interface DegreeMap extends LearningPlan {
	
	/**
     * @name Requirements
     */
    public List<DegreeMapRequirement> getRequirements();
    
}
