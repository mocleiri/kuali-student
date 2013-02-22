package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.model.TransactionTypeValue;
import com.sigmasys.kuali.ksa.service.brm.BrmContext;

import java.math.BigDecimal;

/**
 * BrmContext-aware Payment service, typically used by BRM engine.
 * <p/>
 *
 * @author Michael Ivanov
 */
public interface BrmPaymentService {

    /**
     * An overridden version of applyPayments() that takes the names of in-transaction and out-GL-transaction lists
     * as well as maxAmount and BRM context
     *
     * @param maxAmount          Maximum amount allowed
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output GL transaction list stored in the BRM context
     * @param context            BRM context
     */
    void applyPayments(BigDecimal maxAmount, String inTransactionLists, String outTransactionList, BrmContext context);

    /**
     * An overridden version of applyPayments() that takes the names of in-transaction and out-GL-transaction lists
     * as well as maxAmount and BRM context
     *
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output GL transaction list stored in the BRM context
     * @param context            BRM context
     */
    void applyPayments(String inTransactionLists, String outTransactionList, BrmContext context);

    /**
     * Removes all allocations for the given transaction list(s) and stores the result in "outTransactionList" list.
     *
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output GL transaction list stored in the BRM context
     * @param context            BRM context
     */
    void removeAllocations(String inTransactionLists, String outTransactionList, BrmContext context);

    /**
     * Allocates reversals for the given transaction list(s) and stores the result in "outTransactionList" list.
     *
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output GL transaction list stored in the BRM context
     * @param context            BRM context
     */
    void allocateReversals(String inTransactionLists, String outTransactionList, BrmContext context);

    /**
     * Filters the lists of transactions by the given parameters and stores the result in "outTransactionList" list.
     *
     * @param type               Transaction type
     * @param year               Year
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output transaction list stored in the BRM context
     * @param context            BRM context
     */
    void filterTransactions(TransactionTypeValue type, int year, String inTransactionLists, String outTransactionList,
                            BrmContext context);

    /**
     * Filters the lists of transactions by the given parameters and stores the result in "outTransactionList" list.
     *
     * @param type               Transaction type
     * @param year               Year
     * @param tag                Transaction tag
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output transaction list stored in the BRM context
     * @param context            BRM context
     */
    void filterTransactions(TransactionTypeValue type, int year, String tag, String inTransactionLists,
                            String outTransactionList, BrmContext context);

    /**
     * Filters the lists of transactions by the given parameters and stores the result in "outTransactionList" list.
     *
     * @param type               Transaction type
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param outTransactionList The name of the output transaction list stored in the BRM context
     * @param context            BRM context
     */
    void filterTransactions(TransactionTypeValue type, String inTransactionLists, String outTransactionList,
                            BrmContext context);

    /**
     * Calculates matrix scores for the given transaction lists.
     *
     * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
     * @param context            BRM context
     */
    void calculateMatrixScores(String inTransactionLists, BrmContext context);

    /**
     * Sorts the given transaction list by transaction matrix score in the specified order.
     *
     * @param inTransactionList The name of the input transaction list stored in the BRM context
     * @param order             Indicates ascending order if true and descending otherwise
     * @param context           BRM context
     */
    void sortByMatrixScore(String inTransactionList, boolean order, BrmContext context);

    /**
         * Summarizes the given lists of GL transactions and stores the result in "outTransactionList" list.
         *
         * @param inTransactionLists The name(s) of the input transaction list stored in the BRM context
         * @param outTransactionList The name of the output GL transaction list stored in the BRM context
         * @param context            BRM context
         */
    void summarizeGlTransactions(String inTransactionLists, String outTransactionList, BrmContext context);

}
