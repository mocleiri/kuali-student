package com.sigmasys.kuali.ksa.krad.form;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by: dmulderink on 10/6/12 at 1:57 PM
 */
public class KsaFeeAssessmentForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private MultipartFile uploadXMLFile;

   private String uploadProcessState;

   public MultipartFile getUploadXMLFile() {
      return uploadXMLFile;
   }

   public void setUploadXMLFile(MultipartFile uploadXMLFile) {
      this.uploadXMLFile = uploadXMLFile;
   }

   public String getUploadProcessState() {
      return uploadProcessState;
   }

   public void setUploadProcessState(String uploadProcessState) {
      this.uploadProcessState = uploadProcessState;
   }
}
