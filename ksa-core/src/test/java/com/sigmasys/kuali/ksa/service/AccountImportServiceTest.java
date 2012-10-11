package com.sigmasys.kuali.ksa.service;

import static org.springframework.util.Assert.isTrue;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.transform.StudentProfile;
import com.sigmasys.kuali.ksa.util.CommonUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional(readOnly = false)
public class AccountImportServiceTest extends AbstractServiceTest {

	@Autowired
	private AccountImportService accountImportService;
	
	@Autowired
	private AccountService accountService;
	
	
	@Test
	public void testImportStudentProfileFromObjectNullAccount() throws Exception {
		boolean caughtException = false;
		
		try {
			accountImportService.importStudentProfile((StudentProfile)null);
			isTrue(false);// should not be here
		} catch (Exception e) {
			caughtException = (e instanceof IllegalArgumentException);
		}
		
		isTrue(caughtException);
	}
	
	@Test
	public void testImportStudentProfileFromTextNullInput() throws Exception {
		boolean caughtException = false;
		
		try {
			accountImportService.importStudentProfile((String)null);
			isTrue(false);// should not be here
		} catch (Exception e) {
			caughtException = (e instanceof IllegalArgumentException);
		}
		
		isTrue(caughtException);
	}
	
	@Test
	public void testImportStudentProfileFromTextInvalidSchema() throws Exception {
		// Load contents:
		String content = CommonUtils.getResourceAsString("xmlImport/student-profile-invalid-schema.xml");
		boolean schemaInvalid = true;
		
		try {
			// Call the service:
			accountImportService.importStudentProfile(content);
			isTrue(false); // should not be here
		} catch (Exception e) {
			schemaInvalid = (e instanceof RuntimeException) && StringUtils.containsIgnoreCase(e.getMessage(), "XML schema is invalid");
		}
		
		isTrue(schemaInvalid);
	}
	
	@Test
	public void testImportStudentProfileFromObjectMegNoAccountExists() throws Exception {
		// Create an unmarshalled object from XML text:
		String content = CommonUtils.getResourceAsString("xmlImport/student-profile-unknown-user.xml");
		StringReader reader = new StringReader(content);
		JAXBContext jaxbContext = JAXBContext.newInstance(StudentProfile.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StudentProfile profile = (StudentProfile)unmarshaller.unmarshal(reader);
		
		// Call the service:
		boolean accountDoesNotExist = false;
		
		try {
			accountImportService.importStudentProfile(profile);
			isTrue(false); // should not be here
		} catch (Exception e) {
			accountDoesNotExist = (e instanceof UserNotFoundException);
		}
	
		isTrue(accountDoesNotExist);
	}
	
	@Test
	public void testImportStudentProfileFromTextMegNoAccountExists() throws Exception {
		// Create an unmarshalled object from XML text:
		String content = CommonUtils.getResourceAsString("xmlImport/student-profile-unknown-user.xml");
		
		// Call the service:
		boolean accountDoesNotExist = false;
		
		try {
			accountImportService.importStudentProfile(content);
			isTrue(false); // should not be here
		} catch (Exception e) {
			accountDoesNotExist = (e.getCause() instanceof UserNotFoundException);
		}
	
		isTrue(accountDoesNotExist);
	}
	
	@Test
	public void testImportStudentProfileFromObjectUser3AccountExists() throws Exception {
		// Create an account:
		String userId = "user3";
		accountService.getOrCreateAccount(userId);
		
		// Create an unmarshalled object from XML text:
		String content = CommonUtils.getResourceAsString("xmlImport/student-profile-user3.xml");
		StringReader reader = new StringReader(content);
		JAXBContext jaxbContext = JAXBContext.newInstance(StudentProfile.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StudentProfile profile = (StudentProfile)unmarshaller.unmarshal(reader);
		
		// Call the service:
		accountImportService.importStudentProfile(profile);
	}
}
