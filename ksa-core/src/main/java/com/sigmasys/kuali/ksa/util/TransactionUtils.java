package com.sigmasys.kuali.ksa.util;

import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.model.TransactionType;
import com.sigmasys.kuali.ksa.model.TransactionTypeValue;
import com.sigmasys.kuali.ksa.service.TransactionService;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.sigmasys.kuali.ksa.util.TransactionUtils.*;

/**
 * TransactionUtils
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("unchecked")
public class TransactionUtils {

    private static TransactionService transactionService;

    private TransactionUtils() {
    }

    private static TransactionService getTransactionService() {
        if (transactionService == null) {
            transactionService = ContextUtils.getBean(TransactionService.class);
        }
        return transactionService;
    }

    public static BigDecimal getUnallocatedAmount(Transaction transaction) {

        BigDecimal allocatedAmount = transaction.getAllocatedAmount();
        if (allocatedAmount == null) {
            allocatedAmount = BigDecimal.ZERO;
        }

        BigDecimal lockedAllocatedAmount = transaction.getLockedAllocatedAmount();
        if (lockedAllocatedAmount == null) {
            lockedAllocatedAmount = BigDecimal.ZERO;
        }

        BigDecimal amount = transaction.getAmount();
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }

        return amount.subtract(allocatedAmount.add(lockedAllocatedAmount));

    }

    /**
     * Remove all transactions from the list that are now fully allocated (amount = allocatedAmount +
     * lockedAllocationAmount)
     *
     * @param transactions a list of transactions
     * @return a modified list of transactions
     */
    public static List<Transaction> removeFullyAllocatedTransactions(List<Transaction> transactions) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            if (getUnallocatedAmount(iterator.next()).compareTo(BigDecimal.ZERO) == 0) {
                iterator.remove();
            }
        }
        return transactions;
    }

    /**
     * Returns the values of the unallocated amount of payments, deferments and charges in the given list
     *
     * @param transactions a list of transactions
     * @return an instance of Map<TransactionTypeValue, BigDecimal> that contains unallocated values for charges,
     *         payments and deferments
     */
    public static Map<TransactionTypeValue, BigDecimal> getUnallocatedTransactionAmounts(List<Transaction> transactions) {

        BigDecimal chargeAmount = BigDecimal.ZERO;
        BigDecimal paymentAmount = BigDecimal.ZERO;
        BigDecimal defermentAmount = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {

            BigDecimal unallocatedAmount = getUnallocatedAmount(transaction);
            switch (transaction.getTransactionTypeValue()) {
                case CHARGE:
                    chargeAmount = chargeAmount.add(unallocatedAmount);
                    break;
                case PAYMENT:
                    paymentAmount = paymentAmount.add(unallocatedAmount);
                    break;
                case DEFERMENT:
                    defermentAmount = defermentAmount.add(unallocatedAmount);
                    break;
            }

        }

        Map<TransactionTypeValue, BigDecimal> valueMap = new HashMap<TransactionTypeValue, BigDecimal>(3);
        valueMap.put(TransactionTypeValue.CHARGE, chargeAmount);
        valueMap.put(TransactionTypeValue.PAYMENT, paymentAmount);
        valueMap.put(TransactionTypeValue.DEFERMENT, defermentAmount);

        return valueMap;
    }

    /**
     * Returns the value of the unallocated amount of all charges in the list
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the unallocated charge amount
     */
    public static BigDecimal getUnallocatedChargeAmount(List<Transaction> transactions) {
        return getUnallocatedTransactionAmounts(transactions).get(TransactionTypeValue.CHARGE);
    }

    /**
     * Returns the value of the unallocated amount of all payments in the list
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the unallocated payment amount
     */
    public static BigDecimal getUnallocatedPaymentAmount(List<Transaction> transactions) {
        return getUnallocatedTransactionAmounts(transactions).get(TransactionTypeValue.PAYMENT);
    }

    /**
     * Returns the value of the unallocated amount of all deferments in the list
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the unallocated deferment amount
     */
    public static BigDecimal getUnallocatedDefermentAmount(List<Transaction> transactions) {
        return getUnallocatedTransactionAmounts(transactions).get(TransactionTypeValue.DEFERMENT);
    }

    /**
     * Return the unallocated amount of all restricted payments. Unrestricted payments have a
     * permissible debit type mask of “.*”.
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the restricted unallocated payment amount
     */
    public static BigDecimal getRestrictedUnallocatedPaymentAmount(List<Transaction> transactions) {
        return getTransactionService().getUnallocatedAmount(transactions, TransactionTypeValue.PAYMENT, true);
    }

    /**
     * Return the unallocated amount of all unrestricted payments. Unrestricted payments have a
     * permissible debit type mask of “.*”.
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the unrestricted unallocated payment amount
     */
    public static BigDecimal getUnrestrictedUnallocatedPaymentAmount(List<Transaction> transactions) {
        return getTransactionService().getUnallocatedAmount(transactions, TransactionTypeValue.PAYMENT, false);
    }

    /**
     * Return the unallocated amount of all restricted deferments. Unrestricted deferments have a
     * permissible debit type mask of “.*”.
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the restricted unallocated deferment amount
     */
    public static BigDecimal getRestrictedUnallocatedDefermentAmount(List<Transaction> transactions) {
        return getTransactionService().getUnallocatedAmount(transactions, TransactionTypeValue.DEFERMENT, true);
    }

    /**
     * Return the unallocated amount of all unrestricted deferments. Unrestricted deferments have a
     * permissible debit type mask of “.*”.
     *
     * @param transactions a list of transactions
     * @return BigDecimal of the sum of the unrestricted unallocated deferment amount
     */
    public static BigDecimal getUnrestrictedUnallocatedDefermentAmount(List<Transaction> transactions) {
        return getTransactionService().getUnallocatedAmount(transactions, TransactionTypeValue.DEFERMENT, false);
    }


    public static void calculateMatrixScore(List<Transaction> transactions) {
        // TODO
    }

    private static List<Transaction> orderTransactions(List<Transaction> transactions,
                                                       Comparator<Transaction> comparator,
                                                       boolean ascending) {
        if (!ascending) {
            comparator = Collections.reverseOrder(comparator);
        }

        Collections.sort(transactions, comparator);

        return transactions;

    }

    public static List<Transaction> orderByEffectiveDate(List<Transaction> transactions, boolean ascending) {
        return orderTransactions(transactions, new EffectiveDateComparator(), ascending);
    }

    public static List<Transaction> orderByAmount(List<Transaction> transactions, boolean ascending) {
        return orderTransactions(transactions, new AmountComparator(), ascending);
    }

    public static List<Transaction> orderByUnallocatedAmount(List<Transaction> transactions, boolean ascending) {
        return orderTransactions(transactions, new UnallocatedAmountComparator(), ascending);
    }

    public static List<Transaction> reverseOrder(List<Transaction> transactions) {
        Collections.reverse(transactions);
        return transactions;
    }

    public static List<Transaction> getCopy(List<Transaction> transactions) {
        List<Transaction> newList = new ArrayList<Transaction>(transactions.size());
        Collections.copy(newList, transactions);
        return newList;
    }


    // TODO Need to be able to create Matrix Score first
    public static List<Transaction> orderByMatrixScore(List<Transaction> transactions, boolean ascending) {
        return transactions;
    }

    private static List<Transaction> removeTransactions(List<Transaction> transactions, TransactionTypeValue transactionType) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            Transaction transaction = iterator.next();
            if (transactionType == transaction.getTransactionTypeValue()) {
                iterator.remove();
            }
        }
        return transactions;
    }

    public static List<Transaction> removeCharges(List<Transaction> transactions) {
        return removeTransactions(transactions, TransactionTypeValue.CHARGE);
    }

    public static List<Transaction> removePayments(List<Transaction> transactions) {
        return removeTransactions(transactions, TransactionTypeValue.PAYMENT);
    }

    public static List<Transaction> removeDeferments(List<Transaction> transactions) {
        return removeTransactions(transactions, TransactionTypeValue.DEFERMENT);
    }

    public static List<Transaction> union(List<Transaction> transactions1, List<Transaction> transactions2) {
        return new ArrayList<Transaction>(CollectionUtils.union(transactions1, transactions2));
    }

    public static List<Transaction> intersection(List<Transaction> transactions1, List<Transaction> transactions2) {
        return new ArrayList<Transaction>(CollectionUtils.intersection(transactions1, transactions2));
    }

    public static List<Transaction> exclusiveDisjunction(List<Transaction> transactions1, List<Transaction> transactions2) {
        return new ArrayList<Transaction>(CollectionUtils.disjunction(transactions1, transactions2));
    }

    public static List<Transaction> subtract(List<Transaction> transactions1, List<Transaction> transactions2) {
        return new ArrayList<Transaction>(CollectionUtils.subtract(transactions1, transactions2));
    }

    public static List<Transaction> filterByEffectiveDate(List<Transaction> transactions, Date fromDate, Date toDate) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            Date effectiveDate = iterator.next().getEffectiveDate();
            if (effectiveDate != null) {
                if (effectiveDate.compareTo(fromDate) < 0 || effectiveDate.compareTo(toDate) > 0) {
                    iterator.remove();
                }
            }
        }
        return transactions;
    }

    public static List<Transaction> filterByAmount(List<Transaction> transactions, BigDecimal fromAmount,
                                                   BigDecimal toAmount) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            BigDecimal amount = iterator.next().getAmount();
            if (amount != null) {
                if (amount.compareTo(fromAmount) < 0 || amount.compareTo(toAmount) > 0) {
                    iterator.remove();
                }
            }
        }
        return transactions;
    }

    public static List<Transaction> filterByUnallocatedAmount(List<Transaction> transactions, BigDecimal fromAmount,
                                                              BigDecimal toAmount) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            BigDecimal amount = getUnallocatedAmount(iterator.next());
            if (amount.compareTo(fromAmount) < 0 || amount.compareTo(toAmount) > 0) {
                iterator.remove();
            }
        }
        return transactions;
    }

    public static List<Transaction> filterByPriority(List<Transaction> transactions, int fromPriority, int toPriority) {
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext(); ) {
            TransactionType transactionType = iterator.next().getTransactionType();
            if (transactionType == null || transactionType.getPriority() == null) {
                iterator.remove();
                continue;
            }
            int priority = transactionType.getPriority();
            if (priority < fromPriority || priority > toPriority) {
                iterator.remove();
            }
        }
        return transactions;
    }

}


class EffectiveDateComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.getEffectiveDate().compareTo(t2.getEffectiveDate());
    }
}


class AmountComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.getAmount().compareTo(t2.getAmount());
    }
}

class UnallocatedAmountComparator implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return getUnallocatedAmount(t1).compareTo(getUnallocatedAmount(t2));
    }
}

