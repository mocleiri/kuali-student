package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Debit;
import com.sigmasys.kuali.ksa.model.Pair;
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
 * @author Tim Bornholtz, Michael Ivanov
 */
@WebService(name = "AccountService", targetNamespace = "http://ksa.kuali.org/wsdl/account")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface AccountService {

    /**
     * This process creates a temporary subset of the account as if the account were being administered
     * as a balance forward account. This permits aging the account in a way that is not affected by the
     * payment application methodology. This temporary array is passed to the ageDebt() method.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return a list of pairs [Debit, BigDecimal]
     */
    List<Pair<Debit, BigDecimal>> rebalance(String userId, boolean ignoreDeferment);

    /**
     * @param ignoreDeferment
     */
    void ageDebt(boolean ignoreDeferment);

    /**
     * Returns the total balance due of all active transactions.
     *
     * @param userId          Account ID
     * @param ignoreDeferment boolean value
     * @return total amount of balance due
     */
    BigDecimal getDueBalance(String userId, boolean ignoreDeferment);

    /**
     * @param ignoreDeferment
     * @return
     */
    BigDecimal getOutstandingBalance(boolean ignoreDeferment);

    /**
     * @return
     */
    BigDecimal getUnallocatedBalance();

    /**
     * @return
     */
    BigDecimal getDeferredAmount();

    /**
     * moving a transaction from a pre-effective state to an effective state.
     */
    void makeEffective();

    /**
     * Simple Balance Forward Only
     * <p/>
     * Notes:
     * This is a simple, balance-forward payment application routine.
     * “Allocate” requires both the transaction allocationAmount to be updated, as well as updating/instantiating
     * the Allocation objects.
     * Deferments, when created, are allocated to their charge using lockedAllocationAmount.
     * A more complex, rule-based payment application system, that takes into account payment
     * divisions, priority codes, etc. will be developed to supplement this algorithm.
     */

    void paymentApplication();

    void createAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount);

    void createLockedAllocation(Transaction transaction1, Transaction transaction2, BigDecimal allocationAmount);

    /**
     * This method is used to verify that an account exists before a transaction or other operations are
     * performed on the account. There is an initial inquiry into the KSA store. If no account exist, then there is
     * an inquiry into KIM. If KIM also returns no result, then false is returned. If a KIM account does exist, then
     * a KSA account is created, using the KIM information as a template.
     *
     * @param accountId Account ID
     * @return the account instance or null if the account does not exist
     */
    Account getOrCreateAccount(String accountId);

    /**
     * This methods fetches Account and all its associations by account ID.
     *
     * @param accountId Account ID
     * @return the account instance or null if the account does not exist
     */
    Account getFullAccount(String accountId);

    /**
     * This methods fetches all KSA accounts and all their associations.
     *
     * @return the list account instances
     */
    List<Account> getFullAccounts();

}
