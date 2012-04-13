package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.TransOvrForm;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.TransactionService;

import com.sigmasys.kuali.ksa.temp.AccountInfo;
import com.sigmasys.kuali.ksa.temp.AccountTrans;

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

@Controller
@RequestMapping(value = "/transOvrVw")
public class TransOvrController extends UifControllerBase {

   @Autowired
   private AccountService accountService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected TransOvrForm createInitialForm(HttpServletRequest request) {
      TransOvrForm form = new TransOvrForm();
      return form;
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=submit")
   public ModelAndView submit(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do submit stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
   public ModelAndView save(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

/*      List<AccountTrans> accntTransLst = form.getAccntTransLst();

      BigDecimal totalAmnt = form.getTotalAmnt();

      for (int i = 0; i < 3; i++)
      {
         Transaction trns = createTransaction(false);
         accntTransLst.add(new AccountTrans(trns.getLedgerDate(), trns.getStatementText(),
               trns.getExternalId(), trns.getAmount()));

         totalAmnt = totalAmnt.add(trns.getAmount());
      }

      form.setTotalAmnt(totalAmnt);

      form.setAccntTransLst(accntTransLst);

      form.setTransaction(createTransaction(true));*/

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=cancel")
   public ModelAndView cancel(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
      // do cancel stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method=RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute ("KualiForm") TransOvrForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") TransOvrForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      AccountInfo accountInfo = new AccountInfo();
      List<Account> accountList = accountService.getFullAccounts();
      List<PersonName> personNameList = new ArrayList<PersonName>();
      List<PostalAddress> postalAddressList = new ArrayList<PostalAddress>();

      for (int i = 0; i < accountList.size(); i++)
      {
         Set<PersonName> personNameSet = accountList.get(i).getPersonNames();
         Set<PostalAddress > postalAddressSet = accountList.get(i).getPostalAddresses();

         Iterator<PersonName> personNameIterator = personNameSet.iterator();
         while (personNameIterator.hasNext()) {
            PersonName personName = personNameIterator.next();

            if (personName.isDefault()){
               personNameList.add(personName);
            }
         }

         Iterator<PostalAddress> postalAddressIterator = postalAddressSet.iterator();
         while(postalAddressIterator.hasNext()) {
            PostalAddress postalAddress = postalAddressIterator.next();

            if (postalAddress.isDefault()) {
               postalAddressList.add(postalAddress);
            }
         }
      }

      accountInfo.setPersonNames(personNameList); //(accountService.getFullAccounts());
      accountInfo.setPostalAddresses(postalAddressList);

      form.setAccountInfo(accountInfo);
/*
      List<AccountTrans> accntTransLst = form.getAccntTransLst();

      BigDecimal totalAmnt = form.getTotalAmnt();

      for (int i = 0; i < 3; i++)
      {
         Transaction trns = createTransaction(false);
         accntTransLst.add(new AccountTrans(trns.getLedgerDate(), trns.getStatementText(),
               trns.getExternalId(), trns.getAmount()));

         totalAmnt = totalAmnt.add(trns.getAmount());
      }

      form.setTotalAmnt(totalAmnt);

      form.setAccntTransLst(accntTransLst);

      form.setTransaction(createTransaction(false));
*/

      return getUIFModelAndView(form);
   }

   /**
    *
    * @param updated
    * @return
    */
   private Transaction createTransaction(boolean updated) {
      Transaction alertsTransaction = new Charge();
      alertsTransaction.setId(38000L);
      alertsTransaction.setExternalId("134500");
      alertsTransaction.setAmount(new BigDecimal(1050.34));
      alertsTransaction.setLedgerDate(new Date());
      alertsTransaction.setInternal(false);
      alertsTransaction.setEffectiveDate(new Date());
      alertsTransaction.setResponsibleEntity("Entity #2");
      alertsTransaction.setStatementText("Here goes statement text - " + (updated ? "Updated" : "Initial"));

      return alertsTransaction;
   }
}
