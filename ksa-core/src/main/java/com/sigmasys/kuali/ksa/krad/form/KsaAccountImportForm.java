package com.sigmasys.kuali.ksa.krad.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * The form object behind the Account Import controller.
 * 
 * @author Sergey
 * @version 1.0
 */
public class KsaAccountImportForm extends AbstractViewModel {

	private static final long serialVersionUID = 2944579192309358211L;

	private MultipartFile uploadXMLFile;

	private String uploadProcessState;

	/**
	 * Get the upload XML file
	 * 
	 * @return
	 */
	public MultipartFile getUploadXMLFile() {
		return uploadXMLFile;
	}

	/**
	 * Set the upload XML file
	 * 
	 * @param uploadXMLFile
	 */
	public void setUploadXMLFile(MultipartFile uploadXMLFile) {
		this.uploadXMLFile = uploadXMLFile;
	}

	/**
	 * Get the file upload processing state
	 * 
	 * @return
	 */
	public String getUploadProcessState() {
		return uploadProcessState;
	}

	/**
	 * Set the file upload processing state
	 * 
	 * @param uploadProcessState
	 */
	public void setUploadProcessState(String uploadProcessState) {
		this.uploadProcessState = uploadProcessState;
	}
}
