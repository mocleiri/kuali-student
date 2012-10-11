package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.transform.StudentProfile;

/**
 * Represents a facility to import student profiles based on a StudentProfile entity data.
 * 
 * The logic of importing of student profiles is explained by Paul H. in the
 * "Process Diagrams" design document.
 * 
 * @author Sergey G.
 *
 */
public interface AccountImportService {

	/**
	 * Performs import of a student profile. 
	 * Refer to the "Process Diagrams" design document for a detailed depiction of the process and logic.
	 * 
	 * @param studentProfile A student's profile object. 
	 */
	void importStudentProfile(StudentProfile studentProfile);
	
	/**
	 * Performs import of a student profile from the specified XML content of the profile.
	 * 
	 * @param xmlContent XML content of a Student profile.
	 */
	void importStudentProfile(String xml);
}
