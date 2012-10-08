package com.sigmasys.kuali.ksa.krad.model;

import com.sigmasys.kuali.ksa.model.Memo;

/**
 * Created by: dmulderink on 10/8/12 at 9:43 AM
 */
public class MemoModel extends Memo {

   private String infoAccessLevel;

   public MemoModel() {
   }

   public MemoModel(Memo memo) {
      // populate MemoModel's properties from Memo instance
      setId(memo.getId());
      setPreviousMemo(memo.getPreviousMemo());
      setNextMemo(memo.getNextMemo());

      Integer accessLevel = memo.getAccessLevel();
      if (memo.getAccessLevel() == null) {
         setAccessLevel(0);
         setInfoAccessLevel("0");
      } else {
         setAccessLevel(accessLevel);
         setInfoAccessLevel(accessLevel.toString());
      }

      setText(memo.getText());
      setCreationDate(memo.getCreationDate());
      setEffectiveDate(memo.getEffectiveDate());
      setExpirationDate(memo.getExpirationDate());
      setAccount(memo.getAccount());
      setAccountId(memo.getAccount().getId());
      setTransaction(memo.getTransaction());
      setCreatorId(memo.getCreatorId());
      setEditorId(memo.getEditorId());
      setLastUpdate(memo.getLastUpdate());
   }

   public String getInfoAccessLevel() {
      return infoAccessLevel;
   }

   public void setInfoAccessLevel(String infoAccessLevel) {
      this.infoAccessLevel = infoAccessLevel;
   }
}
