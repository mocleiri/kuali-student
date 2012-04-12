package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Deferment;
import com.sigmasys.kuali.ksa.model.Transaction;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.math.BigDecimal;
import java.util.List;

/**
 * Account service declares business operations on Student Accounts and related
 * objects
 * <p/>
 *
 * @author Tim Bornholtz
 */
@WebService(name = "AccountService", targetNamespace = "http://ksa.kuali.org/wsdl/account")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface AccountService {
    /**
     *     This process creates a temporary subset of the account as if the account were being administered
     *     as a balance forward account. This permits aging the account in a way that is not affected by the
     *     payment application methodology. This temporary array is passed to the ageDebt() method.
     * @param ignoreDeferment
     */
    void rebalance(Boolean ignoreDeferment);

    /**
     *
     * @param ignoreDeferment
     */
    void ageDebt(Boolean ignoreDeferment);

    /**
     *
     * @param ignoreDeferment
     * @return
     */
    BigDecimal getDueBalance(Boolean ignoreDeferment);

    /**
     *
     * @param ignoreDeferment
     * @return
     */
    BigDecimal getOutstandingBalance(Boolean ignoreDeferment);

    /**
     *
     * @return
     */
    BigDecimal getUnallocatedBalance();

    /**
     *
     * @return
     */
    BigDecimal getDeferredAmount();

    /**
     * moving a transaction from a pre-effective state to an effective state.
     *
    */
    void makeEffective();

    /**
     * Simple Balance Forward Only
     *
     * Notes:
     * This is a simple, balance-forward payment application routine.
     * “Allocate” requires both the transaction allocationAmount to be updated, as well as updating/instantiating
     * the Allocation objects.
     * Deferments, when created, are allocated to their charge using lockedAllocationAmount.
     * A more complex, rule-based payment application system, that takes into account payment
     * divisions, priority codes, etc. will be developed to supplement this algorithm.
     */

    void paymentApplication();

    void createAllocation (Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount);

    void createLockedAllocation (Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount);

    /**
     *     This method is used to verify that an account exists before a transaction or other operations are
     *     performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     *     an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     *     a KSA account is created, using the KIM information as a template.
     *
     * @param accountId
     * @return the account instance or null if the account does not exist
     */
    Account getAccount(String accountId);

}
