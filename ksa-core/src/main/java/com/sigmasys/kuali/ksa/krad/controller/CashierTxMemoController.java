package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.CashierTxMemoForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.InformationService;
import com.sigmasys.kuali.ksa.service.TransactionService;

import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: dmulderink
 * Date: 4/16/12
 * Time: 2:50 PM
 */
@Controller
@RequestMapping(value = "/cashierTxMemo")
public class CashierTxMemoController extends UifControllerBase {

   @Autowired
   private AccountService accountService;

   @Autowired
   private InformationService informationService;

   @Autowired
   private TransactionService transactionService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected CashierTxMemoForm createInitialForm(HttpServletRequest request) {
      return new CashierTxMemoForm();
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
   public ModelAndView submit(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...

      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
   public ModelAndView save(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {
      // do save stuff...
      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
   public ModelAndView cancel(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do cancel stuff...
      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // just for the transactions by person page
      String pageId = request.getParameter("pageId");

      if (pageId != null && pageId.compareTo("CashierTxListByPersonPage") == 0) {
         String id = request.getParameter("id");
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("'id' request parameter must be specified");
         }

         // store the selected account ID
         form.setSelectedId(id);
         // charges by ID
         List<Charge> charges = transactionService.getCharges(id);
         // payments by ID
         List<Payment> payments = transactionService.getPayments(id);

         form.setChargeList(charges);

         form.setPaymentList(payments);
      }


      // for the bio, aging tx alerts and
      if (pageId != null && pageId.compareTo("CashierTxBioAgeOvrVwPage") == 0) {
         String id = request.getParameter("id");
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("'id' request parameter must be specified");
         }

         // store the selected account ID
         form.setSelectedId(id);

         Account accountById = accountService.getFullAccount(id);
         ChargeableAccount chargeableAccount = null;
         if (accountById != null) {
            chargeableAccount = (ChargeableAccount) accountById;
         }

         PersonName personName = accountById.getDefaultPersonName();
         PostalAddress postalAddress = accountById.getDefaultPostalAddress();

         accountById.setCompositeDefaultPersonName(CreatePersonName(personName));
         accountById.setCompositeDefaultPostalAddress(CreateCompositePostalAddress(postalAddress));
         List<Account> accountList = new ArrayList<Account>();
         accountList.add(accountById);
         // no session scope
         form.setStudentLookupByName(accountById.getDefaultPersonName().getLastName());
         // a list of one
         form.setAccountBrowseList(accountList);
         form.setCompositePersonName(accountById.getCompositeDefaultPersonName());
         form.setCompositePostalAddress(accountById.getCompositeDefaultPostalAddress());

/*
         for (Account account : accountList) {

            if (account.getId().matches(id)) {
               form.setCompositePersonName(account.getCompositeDefaultPersonName());
               form.setCompositePostalAddress(account.getCompositeDefaultPostalAddress());
            }
         }
*/

         // Account Status summation totals
         // charges by ID
         List<Charge> charges = transactionService.getCharges(id);

         // payments by ID
         List<Payment> payments = transactionService.getPayments(id);
         // deferments by ID
         List<Deferment> deferments = transactionService.getDeferments(id);

         // set the form data

         form.setChargeList(charges);
         form.setPaymentList(payments);
         form.setDefermentList(deferments);

         // stubbed in data
         BigDecimal pastDue = BigDecimal.ZERO;
         BigDecimal balance = BigDecimal.ZERO;
         BigDecimal future = BigDecimal.ZERO;
         BigDecimal deferment = BigDecimal.ZERO;

         for (Charge chrg : charges) {
            balance = balance.add(chrg.getAmount());
         }

         for (Payment paymnt : payments) {
            balance = balance.subtract(paymnt.getAmount());
            future = future.add(paymnt.getAmount());
         }

         for (Deferment dfrmnt : deferments) {
            pastDue = pastDue.add(dfrmnt.getAmount());
            deferment = deferment.add(dfrmnt.getAmount());
         }

         form.setPastDue(pastDue);
         form.setBalance(balance);
         form.setFuture(future);
         form.setDefermentTotal(deferment);

         // Alerts and Flags
         List<Memo> informationList = new ArrayList<Memo>();
         Memo information1 = new Memo();
         information1.setId(1L);
         information1.setAccount(accountById);
         information1.setText("04/12/2012 - Please contact the university Customer Service Representative Hal Kanni-Helfew at (555) 867-5309.");

         Memo information2 = new Memo();
         information2.setId(2L);
         information2.setAccount(accountById);
         information2.setText("03/26/2012 - Check #199 bounced due to insufficient funds.");

         Memo information3 = new Memo();
         information3.setId(3L);
         information3.setAccount(accountById);
         information3.setText("03/11/2012 - Permanent address does not appear to be valid.");

         Memo information4 = new Memo();
         information4.setId(4L);
         information4.setAccount(accountById);
         information4.setText("03/11/2012 - Please contact Supervising Cashier Bill Peymaster.");

         informationList.add(information1);
         informationList.add(information2);
         informationList.add(information3);
         informationList.add(information4);

         form.setAlertFlagList(informationList);


         // Aging
         Calendar calendar = Calendar.getInstance();
         // 10 days ago
         calendar.add(Calendar.DAY_OF_YEAR, - 10);
         Date lastAgeDate = calendar.getTime();
         form.setLastAgeDate(lastAgeDate);

         form.setAged30(new BigDecimal(68.96));
         form.setAged60(new BigDecimal(28.88));
         form.setAged90(BigDecimal.ZERO);
         form.setAgedTotal(new BigDecimal(97.84));

         // Memo (Information table)

/*
         List<Memo> memoList = new ArrayList<Memo>();
         Memo memo = new Memo();
         memo.setId(Long.valueOf(id));
         memo.setPreviousMemo(memo);
         memo.setNextMemo(memo);
         memo.setText("04/05/2012 - Student advised that Direct Loan financial aid submission cutoff date is 04/25/2012.");
         memoList.add(memo);
*/
         form.setMemoList(informationService.getMemos(id));
      }

      return getUIFModelAndView(form);
   }

   /**
    * User searches on a (last) name. The result set is iterated over to create the composite PersonName
    * and composite address using default records, eventfully creating a browse list that can be displayed
    * and selected from for further processing as desired.
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=searchByName")
   public ModelAndView searchByName(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {

      // we do not have a query by name or partial name via last name or contains yet
      // if no result set from getting full accounts than the List is empty
      // otherwise the lit contains records and a compsite person name and postal address

      String studentLookupByName = form.getStudentLookupByName();

      // query for all accounts

      List<Account> accountSearchList = accountService.getFullAccounts();

      // create a a list of Account objects for display requirements

      List<Account> accountList = new ArrayList<Account>();

      // if we have a result set of Accounts from the query

      for (Account account : accountSearchList) {

         PersonName personName = account.getDefaultPersonName();

         if (personName != null && personName.getLastName().contains(studentLookupByName)) {

            // an account should have a default PersonName and default PostalAddress

            PostalAddress postalAddress = account.getDefaultPostalAddress();

            Account accountCopy = account.getCopy();

            StringBuilder personNameBuilder = new StringBuilder();
            StringBuilder postalAddressBuilder = new StringBuilder();

            // create the composite default person name

            personNameBuilder.append(personName.getLastName());
            personNameBuilder.append(", ");
            personNameBuilder.append(personName.getFirstName());

            accountCopy.setCompositeDefaultPersonName(personNameBuilder.toString());

            // create the composite default postal address
            if (postalAddress != null) {
               postalAddressBuilder.append(postalAddress.getStreetAddress1());
               postalAddressBuilder.append(", ");
               postalAddressBuilder.append(postalAddress.getState());
               postalAddressBuilder.append(" ");
               postalAddressBuilder.append(postalAddress.getPostalCode());
               postalAddressBuilder.append(" ");
               postalAddressBuilder.append(postalAddress.getCountry());

               accountCopy.setCompositeDefaultPostalAddress(postalAddressBuilder.toString());
            }

            // add each account copy to a list

            accountList.add(accountCopy);
         }
      }

      // set the account list derived from the full search list

      form.setAccountBrowseList(accountList);

      // do a search by name returning account info
      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=ageDebit")
   public ModelAndView ageDebit(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

      // do aging of transactions stuff...
      //String id = request.getParameterMap().get("id").toString();
      ChargeableAccount chargeableAccount = accountService.ageDebt("1", form.getIgnoreDeferment());

      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addMemo")
   public ModelAndView addMemo(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do addMemo stuff...

      String memoType = form.getMemoType();

      String selId = form.getSelectedId();

      InformationTypeValue informationType = Enum.valueOf(InformationTypeValue.class, memoType);

      Information info = null;
      switch(informationType) {
         case ALERT:
            Alert alert = new Alert();
            alert.setId(1L);
            alert.setText(form.getMemoText());
            info = alert;
            break;
         case FLAG:
            Flag flag = new Flag();
            flag.setId(1L);
            info = flag;
            break;
         case MEMO:
            Memo memo = new Memo();
            memo.setId(1L);
            memo.setText(form.getMemoText());
            //informationService.persistMemo(memo);
            break;
         default:
            break;
      }

      if (info != null)
      {
         informationService.persistInformation(info);
      }
      return getUIFModelAndView(form);
   }

   /**
    * Create a composite person name from PersonName record fields
    * @param personName
    * @return
    */
   private String CreatePersonName(PersonName personName) {

      StringBuilder personNameBuilder = new StringBuilder();

      if (personName != null) {

         // create the composite default person name

         personNameBuilder.append(personName.getLastName());
         personNameBuilder.append(", ");
         personNameBuilder.append(personName.getFirstName());
      }

      return personNameBuilder.toString();
   }

   /**
    * Create a composite postal address from PostalAddress record fields
    * @param postalAddress
    * @return
    */
   private String CreateCompositePostalAddress(PostalAddress postalAddress) {

      StringBuilder postalAddressBuilder = new StringBuilder();

      // create the composite default postal address

      if (postalAddress != null) {
         postalAddressBuilder.append(postalAddress.getStreetAddress1());
         postalAddressBuilder.append(", ");
         postalAddressBuilder.append(postalAddress.getState());
         postalAddressBuilder.append(" ");
         postalAddressBuilder.append(postalAddress.getPostalCode());
         postalAddressBuilder.append(" ");
         postalAddressBuilder.append(postalAddress.getCountry());
      }

      return postalAddressBuilder.toString();
   }
}
