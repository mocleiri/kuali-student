package com.sigmasys.kuali.ksa.krad.model;

import com.sigmasys.kuali.ksa.model.Flag;

/**
 * Created by: dmulderink on 10/7/12 at 1:30 PM
 */
public class FlagModel extends Flag {

   private String infoSeverity;

   private String infoAccessLevel;

   public FlagModel() {
   }

   public FlagModel(Flag flag) {
      // populate FlagModel's properties from Flag instance
      setId(flag.getId());
      setType(flag.getType());
      setSeverity(flag.getSeverity());
      setInfoSeverity(flag.getSeverity().toString());
      setAccessLevel(flag.getAccessLevel());
      setInfoAccessLevel(flag.getAccessLevel().toString());
      setText(flag.getText());
      setCreationDate(flag.getCreationDate());
      setEffectiveDate(flag.getEffectiveDate());
      setExpirationDate(flag.getExpirationDate());
      setAccount(flag.getAccount());
      setAccountId(flag.getAccount().getId());
      setTransaction(flag.getTransaction());
      setCreatorId(flag.getCreatorId());
      setEditorId(flag.getEditorId());
      setLastUpdate(flag.getLastUpdate());
   }

   public String getInfoSeverity() {
      return infoSeverity;
   }

   public void setInfoSeverity(String infoSeverity) {
      this.infoSeverity = infoSeverity;
   }

   public String getInfoAccessLevel() {
      return infoAccessLevel;
   }

   public void setInfoAccessLevel(String infoAccessLevel) {
      this.infoAccessLevel = infoAccessLevel;
   }
}
