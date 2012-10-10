package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaStudentAccountsForm;
import com.sigmasys.kuali.ksa.krad.model.TransactionModel;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.service.CurrencyService;
import com.sigmasys.kuali.ksa.service.InformationService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by: dmulderink on 8/29/12 at 12:58 PM
 */
@Controller
@RequestMapping(value = "/ksaStudentAccountsVw")
public class KsaStudentAccountsController extends GenericSearchController {

    private static final Log logger = LogFactory.getLog(KsaStudentAccountsController.class);

    @Autowired
    CurrencyService currencyService;

    @Autowired
    protected InformationService informationService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaStudentAccountsForm createInitialForm(HttpServletRequest request) {

        KsaStudentAccountsForm form = new KsaStudentAccountsForm();

        String userId = request.getParameter("userId");

        if (userId != null) {

            Account account = accountService.getFullAccount(userId);

            if (account == null) {
                String errMsg = "Cannot find Account by ID = " + userId;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            form.setAccount(account);
        }/* else {
          String errMsg = "'userId' request parameter cannot be null";
          logger.error(errMsg);
          throw new IllegalStateException(errMsg);
       }*/

        return form;
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        // do get stuff...
        String pageId = request.getParameter("pageId");
        String userId = request.getParameter("userId");
        String transactionId = request.getParameter("id");

        if (pageId != null && pageId.equals("AccountOVTransactionsPage")) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            // All transactions
            List<Transaction> transactions = transactionService.getTransactions(userId);

            logger.info("Transaction Count: " + transactions.size());
            List<TransactionModel> rollUpTransactionModelList = new ArrayList<TransactionModel>();
            List<TransactionModel> unGroupedTransactionModelList = new ArrayList<TransactionModel>();

            BigDecimal rollUpCredit = BigDecimal.ZERO;
            BigDecimal rollUpDebit = BigDecimal.ZERO;
            BigDecimal unGroupedCredit = BigDecimal.ZERO;
            BigDecimal unGroupedDebit = BigDecimal.ZERO;

            Transaction singleTransaction = transactions.size() > 0 ? transactions.get(0) : null;
            boolean currencyIsSet = false;

            if (singleTransaction == null) {
               setCurrency(form, singleTransaction);
               currencyIsSet = true;
            }

            for (Transaction t : transactions) {
                if (!currencyIsSet) {
                   setCurrency(form, t);
                   currencyIsSet = true;
                }
                TransactionModel transactionModel = new TransactionModel(t);
                Rollup tmRollup = t.getRollup();
                if (tmRollup != null) {
                    // Check if this rollup is already in there.
                    boolean found = false;
                    for(TransactionModel m : rollUpTransactionModelList){
                        Rollup r = m.getRollup();
                        if(r.getId().equals(t.getId())){
                            m.setAmount(m.getAmount().add(transactionModel.getAmount()));
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        rollUpTransactionModelList.add(transactionModel);
                    }
                    if (TransactionTypeValue.CHARGE.equals(t.getTransactionTypeValue())) {
                       rollUpCredit = rollUpCredit.add(t.getAmount());
                    } else {
                       rollUpDebit = rollUpDebit.add(t.getAmount());
                    }
                } else {
                    unGroupedTransactionModelList.add(transactionModel);
                    if (TransactionTypeValue.CHARGE.equals(t.getTransactionTypeValue())) {
                       unGroupedCredit = unGroupedCredit.add(t.getAmount());
                    } else {
                       unGroupedDebit = unGroupedDebit.add(t.getAmount());
                    }
                }
            }

            form.setRollUpCredit(rollUpCredit.toString());
            form.setRollUpDebit(rollUpDebit.toString());
            form.setUnGroupedCredit(unGroupedCredit.toString());
            form.setUnGroupedDebit(unGroupedDebit.toString());
            form.setRollUpTransactionModelList(rollUpTransactionModelList);
            form.setUnGroupedTransactionModelList(unGroupedTransactionModelList);

        } else if (pageId != null && pageId.equals("AccountOVTransactionsRollupDetailPage")) {
            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            Long rollupId;
            try{
                rollupId = new Long(request.getParameter("rollupId"));
            } catch(NumberFormatException e){
                throw new IllegalArgumentException("'rollupId' request parameter is invalid");
            }

            List<Transaction> transactions = transactionService.getTransactions(userId);
            List<TransactionModel> rollups = new ArrayList<TransactionModel>();

            for(Transaction t : transactions){
                Rollup r = t.getRollup();

                if(r != null && rollupId.equals(r.getId())){
                    logger.info("Retrieved transaction with description: " + t.getTransactionType().getDescription());
                    TransactionModel transactionModel = new TransactionModel(t);
                    rollups.add(transactionModel);
                }
                // the currency in effect for the transaction
                // if no transactions then no need for a currency to convert values ?
                setCurrency(form, t);

            }

            form.setByRollUpTransactionModelList(rollups);


        } else if (pageId != null && pageId.equals("AccountOVTransactionDetailPage")) {

            if (userId == null || userId.isEmpty()) {
                throw new IllegalArgumentException("'userId' request parameter must be specified");
            }

            Transaction t = transactionService.getTransaction(new Long(transactionId));
            logger.info("Retrieved transaction with description: " + t.getTransactionType().getDescription());

            form.setId(t.getId().toString());
            form.setAccountId(t.getAccountId());
            if (t.getRollup() != null) {
                Rollup rollup = t.getRollup();

                //@TODO change this method name.  It isn't part of Rollup.
                form.setRollUpDesc(rollup.getDescription());
            }

            TransactionType tt = t.getTransactionType();
            if (tt != null) {
                List<Tag> tags = tt.getTags();
                if (tags != null) {
                    List<String> names = new ArrayList<String>();
                    for (Tag tag : tags) {
                        names.add(tag.getName());
                    }
                    form.setRollUpTag(StringUtils.collectionToCommaDelimitedString(names));
                }
                form.setType(tt.getDescription());
                form.setTypeId(tt.getId().getId());

            }

            if (tt instanceof DebitType) {
                DebitType dt = (DebitType) tt;
                form.setRollUpPriority(dt.getPriority().toString());
            }

            form.setEffectiveDate(t.getEffectiveDate());
            form.setRecognitionDate(t.getRecognitionDate());
            form.setTypeSubCode(t.getTransactionType().getId().getSubCode().toString());

            if (t instanceof Deferment) {
                Deferment def = (Deferment) t;
                form.setExpirationDate(def.getExpirationDate());

                //form.setDeferredId(t.get);

            } else if (t instanceof Payment) {
                Payment payment = (Payment) t;
                form.setClearDate(payment.getClearDate());
            }

            form.setStatementText(t.getStatementText());

            if (t.getAmount() != null) {
             form.setAmount(t.getAmount().toString());
            }
            form.setInternal(t.isInternal() != null ? t.isInternal().toString() : Boolean.FALSE.toString());

            if (t.getAllocatedAmount() != null) {
               form.setAllocationAmount(t.getAllocatedAmount().toString());
            }

            if (t.getLockedAllocatedAmount() != null) {
               form.setAmountAllocatedLocked(t.getLockedAllocatedAmount().toString());
            }

           // the currency in effect for the transaction
            setCurrency(form, t);

            if (t.getDocument() != null) {
                form.setDocumentId(t.getDocument().getId().toString());
            }

            if (t.getGeneralLedgerType() != null) {
                form.setGeneralLedgerTypeId(t.getGeneralLedgerType().getId().toString());
            }

            form.setOverRidden(t.isGlOverridden() != null ? t.isGlOverridden().toString() : Boolean.FALSE.toString());
            form.setOriginationDate(t.getOriginationDate());

        }

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submit")
    @Transactional(readOnly = false)
    public ModelAndView submit(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
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
    @Transactional(readOnly = false)
    public ModelAndView save(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
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
    public ModelAndView cancel(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do refresh stuff...
        return getUIFModelAndView(form);
    }

   private void setCurrency(KsaStudentAccountsForm form, Transaction transaction) {

      if (transaction != null) {
         Currency currency = transaction.getCurrency();
         if (currency == null) {
            currency = currencyService.getCurrency("USD");
         }
         form.setCurrency(currency);
         form.setNativeAmountCurr(currency.getCode());
      } else {
         form.setCurrency(currencyService.getCurrency("USD"));
         form.setNativeAmountCurr(form.getCurrency().getCode());
      }
   }
}
