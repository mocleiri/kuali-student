package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.model.GeneralLedgerType;

import java.util.List;

public class GeneralLedgerTypeForm extends AbstractViewModel {

   private Account account;

   private List<GeneralLedgerType> types;

   private GeneralLedgerType type;

   private String statusMessage;

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public String getStatusMessage() {
      return statusMessage;
   }

   public void setStatusMessage(String statusMessage) {
      this.statusMessage = statusMessage;
   }

    public List<GeneralLedgerType> getGeneralLedgerTypes() {
        return types;
    }

    public void setGeneralLedgerTypes(List<GeneralLedgerType> types) {
        this.types = types;
    }

    public GeneralLedgerType getGeneralLedgerType() {
        return type;
    }

    public void setGeneralLedgerType(GeneralLedgerType type) {
        this.type = type;
    }
}
