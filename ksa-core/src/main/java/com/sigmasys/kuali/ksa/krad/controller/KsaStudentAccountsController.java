package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.KsaStudentAccountsForm;
import com.sigmasys.kuali.ksa.krad.model.TransactionModel;
import com.sigmasys.kuali.ksa.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import java.util.List;

/**
 * Created by: dmulderink on 8/29/12 at 12:58 PM
 */
@Controller
@RequestMapping(value = "/ksaStudentAccountsVw")
public class KsaStudentAccountsController extends GenericSearchController {

   private static final Log logger = LogFactory.getLog(KsaStudentAccountsController.class);

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected KsaStudentAccountsForm createInitialForm(HttpServletRequest request) {
        return new KsaStudentAccountsForm();
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

        if (pageId != null && pageId.compareTo("AccountOVTransactionsPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

           // All transactions
           List<Transaction> transactions = transactionService.getTransactions(id);

           logger.info("Transaction Count: " + transactions.size());
           List<TransactionModel> rollUpTransactionModelList = new ArrayList<TransactionModel>();
           List<TransactionModel> unGroupedTransactionModelList = new ArrayList<TransactionModel>();

           form.setRollUpCredit(BigDecimal.ZERO);
           form.setRollUpDebit(BigDecimal.ZERO);
           form.setUnGroupedCredit(BigDecimal.ZERO);
           form.setUnGroupedDebit(BigDecimal.ZERO);

           for(Transaction t : transactions){
              TransactionModel transactionModel = new TransactionModel(t);
               if(t.getRollup() != null){
                   rollUpTransactionModelList.add(transactionModel);
                   if(TransactionTypeValue.CHARGE.equals(t.getTransactionTypeValue())){
                       form.setRollUpCredit(form.getRollUpCredit().add(t.getAmount()));
                   } else {
                       form.setRollUpDebit(form.getRollUpDebit().add(t.getAmount()));
                   }
               } else {
                   unGroupedTransactionModelList.add(transactionModel);
                   if(TransactionTypeValue.CHARGE.equals(t.getTransactionTypeValue())){
                       form.setUnGroupedCredit(form.getUnGroupedCredit().add(t.getAmount()));
                   } else {
                       form.setUnGroupedDebit(form.getUnGroupedDebit().add(t.getAmount()));
                   }
               }
           }

	        form.setRollUpTransactionModelList(rollUpTransactionModelList);
           form.setUnGroupedTransactionModelList(unGroupedTransactionModelList);

        } else if (pageId != null && pageId.compareTo("AccountOVTransactionsRollupDetailPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            Long lId;

            try{
                lId = new Long(id);
            } catch(NumberFormatException e){
                throw new IllegalArgumentException("'id' request parameter must be a number");
            }
            Transaction t = transactionService.getTransaction(lId);
            logger.info("Retrieved transaction with description: " + t.getTransactionType().getDescription());
            TransactionModel transactionModel = new TransactionModel(t);
            ArrayList<TransactionModel> list = new ArrayList<TransactionModel>();
            list.add(transactionModel);
            form.setByRollUpTransactionModelList(list);


        } else if (pageId != null && pageId.compareTo("AccountOVTransactionDetailPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            Long lId;

            try{
                lId = new Long(id);
            } catch(NumberFormatException e){
                throw new IllegalArgumentException("'id' request parameter must be a number");
            }
            Transaction t = transactionService.getTransaction(lId);
            logger.info("Retrieved transaction with description: " + t.getTransactionType().getDescription());

            //TransactionModel transactionModel = new TransactionModel(t);
            //form.setTransactionModel(transactionModel);

            form.setId(t.getId().toString());
            form.setAccountId(t.getAccountId());
            if(t.getRollup() != null){
                Rollup rollup = t.getRollup();
                form.setRollUpDesc(rollup.getDescription());
                //form.setRollUpTag(rollup.get);
                //form.setRollUpPriority();
            }

            form.setEffectiveDate(t.getEffectiveDate());
            form.setRecognitionDate(t.getRecognitionDate());
            //form.setExpirationDate();
            //form.setClearDate(t.get);
            form.setStatementText(t.getStatementText());
            TransactionType tt = t.getTransactionType();
            if(tt != null){
                form.setType(tt.getDescription());
                form.setTypeId(tt.getId().toString());
                //form.setTypeSubCode(t.get);
            }
            form.setAmount(t.getAmount());
            if(t.isInternal() != null){
                form.setInternal(t.isInternal().toString());
            } else {
                form.setInternal(Boolean.FALSE.toString());
            }
            form.setAllocationAmount(t.getAllocatedAmount());
            form.setAmountAllocatedLocked(t.getLockedAllocatedAmount());
            form.setNativeAmountCurr(t.getCurrency().getCode());
            if(t.getDocument() != null){
                form.setDocumentId(t.getDocument().getId().toString());
            }
            //form.setPreviousMemoId(t.get);
            //form.setNextMemoId();
            //form.setMemo(t.get);
            //form.setDeferredId(t.get);
            //form.setRefundable(t.is);
            //form.setRefundRule(t.);
            //form.setPaymentBilling();
            //form.setDerivativeTransactionId(t.get);

            if(t.getGeneralLedgerType() != null){
                form.setGeneralLedgerTypeId(t.getGeneralLedgerType().getId().toString());
            }
            //form.setEntryGenerated(t.get);
            if(t.isGlOverridden() != null){
                form.setOverRidden(t.isGlOverridden().toString());
            } else {
                form.setOverRidden(Boolean.FALSE.toString());
            }
            form.setOriginationDate(t.getOriginationDate());
            //form.setExtensionId(t.getE);


        } /*else {
            throw new IllegalArgumentException("'pageId' of " + pageId + " not understood");
        }*/


        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */

    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") KsaStudentAccountsForm form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {

        // populate model for testing

        return super.start(form, result, request, response);

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
}
