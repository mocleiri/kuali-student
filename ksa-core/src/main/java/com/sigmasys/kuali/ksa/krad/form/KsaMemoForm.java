package com.sigmasys.kuali.ksa.krad.form;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Memo;

import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 10/4/12 at 7:52 AM
 */
public class KsaMemoForm extends AbstractViewModel {

   private static final long serialVersionUID = -7525378097732916418L;

   private Account account;

   private Date fromDate;

   private Date toDate;

   private List<Memo> memos;

   private Memo memo;

   /*
     Get / Set methods
   */

   public Account getAccount() {
      return account;
   }

   public void setAccount(Account account) {
      this.account = account;
   }

   public Date getFromDate() {
      return fromDate;
   }

   public void setFromDate(Date fromDate) {
      this.fromDate = fromDate;
   }

   public Date getToDate() {
      return toDate;
   }

   public void setToDate(Date toDate) {
      this.toDate = toDate;
   }

   public List<Memo> getMemos() {
      return memos;
   }

   public void setMemos(List<Memo> memos) {
      this.memos = memos;
   }

   public Memo getMemo() {
      return memo;
   }

   public void setMemo(Memo memo) {
      this.memo = memo;
   }
}
