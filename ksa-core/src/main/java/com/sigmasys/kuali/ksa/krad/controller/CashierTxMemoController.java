package com.sigmasys.kuali.ksa.krad.controller;

import com.sigmasys.kuali.ksa.krad.form.CashierTxMemoForm;
import com.sigmasys.kuali.ksa.krad.util.AlertsFlagsMemos;
import com.sigmasys.kuali.ksa.krad.util.PersonPostal;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.Currency;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.CurrencyService;
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
    private CurrencyService currencyService;

    @Autowired
    private InformationService informationService;

    @Autowired
    private TransactionService transactionService;

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected CashierTxMemoForm createInitialForm(HttpServletRequest request) {
        CashierTxMemoForm form = new CashierTxMemoForm();
        form.setInfoType(InformationTypeValue.MEMO.name());
        return form;
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refresh")
    public ModelAndView refresh(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do refresh stuff...
        String accountId = form.getSelectedId();
        populateForm(accountId, form);

        return getUIFModelAndView(form);
    }

    /**
     * @param form
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addCharge")
    public ModelAndView addCharge(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                  HttpServletRequest request, HttpServletResponse response) {
        // do addCharge stuff...
        String accountId = form.getSelectedId();
        if (accountId != null && !accountId.trim().isEmpty()) {

            Date dtNow = new Date();

            Transaction transaction =
                    transactionService.createTransaction(form.getChargeTransTypeValue(), accountId, dtNow,
                            form.getCharge().getAmount());

            if (transaction != null) {
                form.setTransactionStatus("Success");
            } else {
                form.setTransactionStatus("Failed to add charge");
            }

            // populate the form using the id
            populateForm(accountId, form);
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=makePayment")
    public ModelAndView makePayment(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        // do makePayment stuff...
        String accountId = form.getSelectedId();
        if (accountId != null && !accountId.trim().isEmpty()) {

            Date dtNow = new Date();

            Transaction transaction =
                    transactionService.createTransaction(form.getPaymentTransTypeValue(), accountId, dtNow,
                            form.getPayment().getAmount());

            if (transaction != null) {
                form.setTransactionStatus("Success");
            } else {
                form.setTransactionStatus("Failed to add payment");
            }

            // populate the form using the id
            populateForm(accountId, form);
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=submitPayAge")
    public ModelAndView submitPayAge(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                     HttpServletRequest request, HttpServletResponse response) {
        // do make payment and age stuff...

        String accountId = form.getSelectedId();

        if (accountId != null && !accountId.trim().isEmpty()) {

            Date dtNow = new Date();

            Transaction transaction =
                    transactionService.createTransaction(form.getPaymentTransTypeValue(), accountId, dtNow,
                            form.getPayment().getAmount());

            if (transaction != null) {
                ChargeableAccount chargeableAccount = accountService.ageDebt(accountId, form.getIgnoreDeferment());

                if (chargeableAccount != null) {
                    form.setTransactionStatus("Success");
                } else {
                    form.setTransactionStatus("Failed to age transactions");
                }
            } else {
                form.setTransactionStatus("Failed to add payment");
            }

            // populate the form using the id
            populateForm(accountId, form);
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
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=get")
    public ModelAndView get(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                            HttpServletRequest request, HttpServletResponse response) {

        // just for the transactions by person page
        String pageId = request.getParameter("pageId");

        // Deprecated
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

        // for the bio, aging tx alerts flags and memo
        if (pageId != null && pageId.compareTo("CashierTxBioAgeOvrVwPage") == 0) {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("'id' request parameter must be specified");
            }

            populateForm(id, form);
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
        PersonPostal personPostal = new PersonPostal();
        for (Account account : accountSearchList) {

            PersonName personName = account.getDefaultPersonName();

            // for each default PersonName in the query list that contains the selected person
            if (personName != null && personName.getLastName().contains(studentLookupByName)) {

                // an account should have a default PersonName and default PostalAddress

                PostalAddress postalAddress = account.getDefaultPostalAddress();

                Account accountCopy = account.getCopy();

                // format the name and address as a single string of each

                accountCopy.setCompositeDefaultPersonName(personPostal.CreateCompositePersonName(personName));
                accountCopy.setCompositeDefaultPostalAddress(personPostal.CreateCompositePostalAddress(postalAddress));

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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=ageDebt")
    public ModelAndView ageDebt(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {

        // do aging of transactions stuff...
        String accountId = form.getSelectedId();

        if (accountId != null && !accountId.trim().isEmpty()) {
            // age the indexed Account Transactions
            ChargeableAccount chargeableAccount = accountService.ageDebt(accountId, form.getIgnoreDeferment());
            // populate the form using the id
            populateForm(accountId, form);
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
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addMemo")
    public ModelAndView addMemo(@ModelAttribute("KualiForm") CashierTxMemoForm form, BindingResult result,
                                HttpServletRequest request, HttpServletResponse response) {
        // do addMemo stuff...

        String accountId = form.getSelectedId();
        String infoType = InformationTypeValue.MEMO.name();
        form.setInfoType(infoType);

        if (accountId != null && !accountId.trim().isEmpty()) {

            Account account = accountService.getFullAccount(accountId);

            InformationTypeValue informationType = Enum.valueOf(InformationTypeValue.class, infoType);

            Information info;
            switch (informationType) {
                case ALERT:
                    Alert alert = new Alert();
                    alert.setText(form.getInfoText());
                    info = alert;
                    break;
                case FLAG:
                    Flag flag = new Flag();
                    flag.setSeverity(0);
                    info = flag;
                    break;
                case MEMO:
                    Memo memo = new Memo();
                    memo.setText(form.getInfoText());
                    info = memo;
                    break;
                default:
                    throw new IllegalStateException("Unknown Information Type '" + informationType);
            }

            info.setAccount(account);
            info.setCreationDate(new Date());
            info.setEffectiveDate(form.getInfoEffectiveDate());
            info.setLastUpdate(new Date());
            //info.setCreatorId();
            //info.setResponsibleEntity();
            Long infoId = informationService.persistInformation(info);

            form.setInfoAddStatus(infoId > 0 ? "Success" : "Unable to add");
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
    private void populateForm(String id, CashierTxMemoForm form) {

        // store the selected account ID
        form.setSelectedId(id);

        boolean ignoreDeferments = form.getIgnoreDeferment();

        Account accountById = accountService.getFullAccount(id);
        if (accountById == null) {
            throw new IllegalStateException("Cannot find Account by ID = " + id);
        }

        ChargeableAccount chargeableAccount = (ChargeableAccount) accountById;

        PersonName personName = accountById.getDefaultPersonName();
        PostalAddress postalAddress = accountById.getDefaultPostalAddress();

        // format the name and address as a single string of each
        PersonPostal personPostal = new PersonPostal();
        accountById.setCompositeDefaultPersonName(personPostal.CreateCompositePersonName(personName));
        accountById.setCompositeDefaultPostalAddress(personPostal.CreateCompositePostalAddress(postalAddress));

        List<Account> accountList = new ArrayList<Account>();
        accountList.add(accountById);

        // no session scope
        form.setStudentLookupByName(accountById.getDefaultPersonName().getLastName());
        // a list of one
        form.setAccountBrowseList(accountList);
        form.setCompositePersonName(accountById.getCompositeDefaultPersonName());
        form.setCompositePostalAddress(accountById.getCompositeDefaultPostalAddress());

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

        BigDecimal pastDue = accountService.getOutstandingBalance(id, ignoreDeferments) != null ? accountService.getOutstandingBalance(id, ignoreDeferments) : BigDecimal.ZERO;
        BigDecimal balance = accountService.getDueBalance(id, ignoreDeferments) != null ? accountService.getDueBalance(id, ignoreDeferments) : BigDecimal.ZERO;
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

        form.setPastDue(pastDue);
        form.setBalance(balance);
        form.setFuture(future);
        form.setDefermentTotal(deferment);

        // Alerts, Flags and Memos
        List<Alert> alerts = informationService.getAlerts(id);

        AlertsFlagsMemos afm = new AlertsFlagsMemos();
        // Alerts
        for (Alert alert : alerts) {

            alert.setCompositeInfo(afm.CreateCompositeAlert(alert));
        }

        form.setAlertList(alerts);

        // Flags
        // Flags do not have a Text field and throws an exception when there are flag records TODO
        //form.setFlagList(informationService.getFlags(id));

        List<Memo> memos = informationService.getMemos(id);

        // Alerts
        for (Memo memo : memos) {

            memo.setCompositeInfo(afm.CreateCompositeMemo(memo));
        }

        form.setMemoList(memos);
    }
}
