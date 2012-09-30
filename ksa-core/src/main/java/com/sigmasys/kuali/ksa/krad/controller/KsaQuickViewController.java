package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaQuickViewForm;
import com.sigmasys.kuali.ksa.krad.util.AlertsFlagsMemos;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: dmulderink on 9/28/12 at 2:25 PM
 */
@Controller
@RequestMapping(value = "/ksaQuickVw")
public class KsaQuickViewController extends GenericSearchController {

   @Autowired
   private InformationService informationService;

   /**
    * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
    */
   @Override
   protected KsaQuickViewForm createInitialForm(HttpServletRequest request) {
      KsaQuickViewForm form  = new KsaQuickViewForm();
      form.setCompositeDefaultPersonName("Thomas Brudenell-Bruce, 1st Earl of Ailesbury");
      form.setCompositeDefaultPostalAddress("Winchester College\n" +
            "College Street\n" +
            "Winchester SO23 9NA\n" +
            "United Kingdom");
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
   @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
   public ModelAndView get(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                           HttpServletRequest request, HttpServletResponse response) {

      // do get stuff...

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

   @RequestMapping(params = "methodToCall=start")
   public ModelAndView start(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                             HttpServletRequest request, HttpServletResponse response) {

      // populate model for testing

      return super.start(form, result, request, response);

   }

   /**
    *
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=submit")
   @Transactional(readOnly = false)
   public ModelAndView submit(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
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
   @Transactional(readOnly = false)
   public ModelAndView save(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

      // do save stuff...

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
   public ModelAndView cancel(@ModelAttribute ("KualiForm") KsaQuickViewForm form, BindingResult result,
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
   @RequestMapping(method= RequestMethod.POST, params="methodToCall=refresh")
   public ModelAndView refresh(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {
      // do refresh stuff...
      return getUIFModelAndView(form);
   }

   /**
    * @param form
    * @param result
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = "methodToCall=ageDebt")
   public ModelAndView ageDebt(@ModelAttribute("KualiForm") KsaQuickViewForm form, BindingResult result,
                               HttpServletRequest request, HttpServletResponse response) {

      // do aging of transactions stuff...
      String accountId = form.getSearchValue();
      boolean ignoreDeferment =  Boolean.parseBoolean(form.getIgnoreDeferment());

      if (accountId != null && !accountId.trim().isEmpty()) {
         // age the indexed Account Transactions
         ChargeableAccount chargeableAccount = accountService.ageDebt(accountId, ignoreDeferment);
         // populate the form using the id
         populateForm(accountId, form);
      }

      return getUIFModelAndView(form);
   }

   /**
    * Populate the form per business needs for a single account by the account identifier
    *
    * @param id
    * @param form
    */
   private void populateForm(String id, KsaQuickViewForm form) {

      // store the selected account ID
      //form.setSelectedId(id);

      boolean ignoreDeferment =  Boolean.parseBoolean(form.getIgnoreDeferment());

      Account accountById = accountService.getFullAccount(id);
      if (accountById == null) {
         throw new IllegalStateException("Cannot find Account by ID = " + id);
      }

      ChargeableAccount chargeableAccount = (ChargeableAccount) accountById;

      List<Account> accountList = new ArrayList<Account>();
      accountList.add(accountById);

      // no session scope
      //form.setStudentLookupByName(accountById.getDefaultPersonName().getLastName());
      // a list of one
      //form.setAccountBrowseList(accountList);
      form.setCompositeDefaultPersonName(accountById.getCompositeDefaultPersonName());
      form.setCompositeDefaultPostalAddress(accountById.getCompositeDefaultPostalAddress());

      // Account Status summation totals
      // charges by ID
      //List<Charge> charges = transactionService.getCharges(id);

      // payments by ID
      //List<Payment> payments = transactionService.getPayments(id);

      // deferments by ID
      //List<Deferment> deferments = transactionService.getDeferments(id);

      // set the form data

      //form.setCharges(charges);
      //form.setPayments(payments);
      //form.setDeferments(deferments);

      BigDecimal pastDue = accountService.getOutstandingBalance(id, ignoreDeferment) != null ? accountService.getOutstandingBalance(id, ignoreDeferment) : BigDecimal.ZERO;
      BigDecimal balance = accountService.getDueBalance(id, ignoreDeferment) != null ? accountService.getDueBalance(id, ignoreDeferment) : BigDecimal.ZERO;
      BigDecimal future = accountService.getUnallocatedBalance(id) != null ? accountService.getUnallocatedBalance(id) : BigDecimal.ZERO;
      BigDecimal deferment = accountService.getDeferredAmount(id) != null ? accountService.getDeferredAmount(id) : BigDecimal.ZERO;

      // Aging

      Date lastAgeDate = chargeableAccount.getLateLastUpdate();
      form.setLastAgeDate(lastAgeDate);

      form.setAged30(chargeableAccount.getAmountLate1());
      form.setAged60(chargeableAccount.getAmountLate2());
      form.setAged90(chargeableAccount.getAmountLate3());

      BigDecimal agedTotal = BigDecimal.ZERO;

      if (chargeableAccount.getAmountLate1() != null &&
            chargeableAccount.getAmountLate2() != null &&
            chargeableAccount.getAmountLate3() != null) {
         agedTotal = agedTotal.add(chargeableAccount.getAmountLate1());
         agedTotal = agedTotal.add(chargeableAccount.getAmountLate2());
         agedTotal = agedTotal.add(chargeableAccount.getAmountLate3());
      }

      form.setAgedTotal(agedTotal);

      form.setPastDueAmount(pastDue);
      form.setBalanceAmount(balance);
      form.setFutureAmount(future);
      form.setDefermentAmount(deferment);

      // Alerts, Flags and Memos
      List<Alert> alerts = informationService.getAlerts(id);

      AlertsFlagsMemos afm = new AlertsFlagsMemos();
      // Alerts
      for (Alert alert : alerts) {

         alert.setCompositeInfo(afm.CreateCompositeAlert(alert));
      }

      form.setAlerts(alerts);

      // Flags
      // Flags do not have a Text field and throws an exception when there are flag records TODO
      List<Flag> flags = informationService.getFlags(id);

      for (Flag flag : flags) {

         flag.setCompositeInfo(afm.CreateCompositeFlag(flag));
      }

      form.setFlags(flags);

      List<Memo> memos = informationService.getMemos(id);

      // Alerts
      for (Memo memo : memos) {

         memo.setCompositeInfo(afm.CreateCompositeMemo(memo));
      }

      form.setMemos(memos);
   }
}
