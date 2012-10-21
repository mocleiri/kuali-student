package com.sigmasys.kuali.ksa.krad.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by: dmulderink on 9/26/12 at 9:09 AM
 */
public class BatchTransactionsForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private MultipartFile uploadXMLFile;

   private String uploadProcessState;


   /**
    * Get the upload XML file
    * @return
    */
   public MultipartFile getUploadXMLFile() {
      return uploadXMLFile;
   }

   /**
    * Set the upload XML file
    * @param uploadXMLFile
    */
   public void setUploadXMLFile(MultipartFile uploadXMLFile) {
      this.uploadXMLFile = uploadXMLFile;
   }

   /**
    * Get the file upload processing state
    * @return
    */
   public String getUploadProcessState() {
      return uploadProcessState;
   }

   /**
    * Set the file upload processing state
    * @param uploadProcessState
    */
   public void setUploadProcessState(String uploadProcessState) {
      this.uploadProcessState = uploadProcessState;
   }
}
