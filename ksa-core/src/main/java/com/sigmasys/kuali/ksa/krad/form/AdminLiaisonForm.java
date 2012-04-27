package com.sigmasys.kuali.ksa.krad.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * User: dmulderink
 * Date: 4/24/12
 * Time: 11:16 AM
 */
public class AdminLiaisonForm extends UifFormBase {
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
