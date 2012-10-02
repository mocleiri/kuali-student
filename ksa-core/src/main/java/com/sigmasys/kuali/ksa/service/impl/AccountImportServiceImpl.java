package com.sigmasys.kuali.ksa.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.service.AccountImportService;
import com.sigmasys.kuali.ksa.transform.StudentProfile;

@Service("accountImportService")
@Transactional(readOnly=true)
public class AccountImportServiceImpl implements AccountImportService {

	/**
	 * Performs import of a student profile. 
	 * Refer to the "Process Diagrams" design document for a detailed depiction of the process and logic.
	 * 
	 * @param studentProfile A student's profile object. 
	 * @throws IllegalArgumentException If "studentProfile" is invalid.
	 * @throws RuntimeException If student profile import encountered an error.
	 */
	@Override
	public void importStudentProfile(StudentProfile studentProfile) {
		// TODO Auto-generated method stub

	}

}
