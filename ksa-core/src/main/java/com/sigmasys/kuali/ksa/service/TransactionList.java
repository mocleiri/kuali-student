package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.gwt.client.model.TransactionType;
import com.sigmasys.kuali.ksa.model.Transaction;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: timb
 * Date: 8/26/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionList {

    public static int getNumberOfTransactions(List<Transaction> transactions){
        return transactions.size();
    }

    /**
     * Remove all transactions from the list that are now fully allocated (amount = allocatedAmount +
     * lockedAllocationAmount)
     */
    public static List<Transaction> refreshList(List<Transaction> transactions){
       if(transactions == null){ return null;}

       for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
           Transaction t = it.next();
           if(t.getAmount().equals(t.getAllocatedAmount().add(t.getLockedAllocatedAmount()))){
               it.remove();
           }
       }

       return transactions;
    }

    /**
     * Returns the value of the unallocated amount of all payments in the list
     * @return BigDecimal of the sum of the Unallocated Payment Value
     */
    public static BigDecimal getUnallocatedPaymentValue(List<Transaction> transactions){
        BigDecimal amt = new BigDecimal(0);

        for(Transaction t : transactions){
            if(! t.getTransactionType().equals(TransactionType.PAYMENT)){ continue; }
            amt.add(t.getAmount().subtract(t.getAllocatedAmount()).subtract(t.getLockedAllocatedAmount()));
        }

        return amt;
    }

    /**
     * Returns the value of the unallocated amount of all charges in the list
     * @return BigDecimal of the sum of the Unallocated Charge Value
     */
    public static BigDecimal getUnallocatedChargeValue(List<Transaction> transactions){
        BigDecimal amt = new BigDecimal(0);

        for(Transaction t : transactions){
            if(! t.getTransactionType().equals(TransactionType.CHARGE)){ continue; }
            amt.add(t.getAmount().subtract(t.getAllocatedAmount()).subtract(t.getLockedAllocatedAmount()));
        }

        return amt;
    }

    /**
     * Return the unallocated amount of all restricted or unrestricted payments. Unrestricted payments have a
     * permissableDebitType of “*”.
     * @return BigDecimal of the sum of the Restricted Payment Value
     */
    public static BigDecimal getRestrictedPaymentValue(List<Transaction> transactions){
        BigDecimal amt = new BigDecimal(0);

        for(Transaction t : transactions){
            // @TODO Need to figure out how to determine if this is a restricted payment
        }

        return amt;
    }

    /**
     * Return the unallocated amount of all restricted or unrestricted payments. Unrestricted payments have a
     * permissableDebitType of “*”.
     * @return BigDecimal of the sum of the Unrestricted Payment Value
     */
    public static BigDecimal getUnrestrictedPaymentValue(List<Transaction> transactions){
        BigDecimal amt = new BigDecimal(0);

        for(Transaction t : transactions){
            // @TODO Need to figure out how to determine if this is an unrestricted payment
        }

        return amt;
    }

    /**
     * @TODO
     * @param transactions
     */
    public static void calculateMatrixScore(List<Transaction> transactions){

    }

    /**
     * Order list by priority
     * @param transactions
     * @return List<Transaction> of sorted transactions
     */
    public static List<Transaction> orderByPriority(List<Transaction> transactions){
        return transactions;
    }

    public static List<Transaction> orderByPriority(List<Transaction> transactions, boolean ascending){
        if(ascending){
            PriorityComparatorAscending comparator = new PriorityComparatorAscending();
            Collections.sort(transactions, comparator);
            return transactions;
        } else {
            PriorityComparatorDescending comparator = new PriorityComparatorDescending();
            Collections.sort(transactions, comparator);
            return transactions;
        }
    }

    public static List<Transaction> orderByDate(List<Transaction> transactions, boolean ascending){
        if(ascending){
            DateComparatorAscending comparator = new DateComparatorAscending();
            Collections.sort(transactions, comparator);
            return transactions;
        } else {
            DateComparatorDescending comparator = new DateComparatorDescending();
            Collections.sort(transactions, comparator);
            return transactions;
        }
    }

    public static List<Transaction> orderByAmount(List<Transaction> transactions, boolean ascending){
        if(ascending){
            AmountComparatorAscending comparator = new AmountComparatorAscending();
            Collections.sort(transactions, comparator);
            return transactions;
        } else {
            AmountComparatorDescending comparator = new AmountComparatorDescending();
            Collections.sort(transactions, comparator);
            return transactions;
        }
    }

    public static List<Transaction> orderByUnallocatedAmount(List<Transaction> transactions, boolean ascending){
        if(ascending){
            UnallocatedAmountComparatorAscending comparator = new UnallocatedAmountComparatorAscending();
            Collections.sort(transactions, comparator);
            return transactions;
        } else {
            UnallocatedAmountComparatorDescending comparator = new UnallocatedAmountComparatorDescending();
            Collections.sort(transactions, comparator);
            return transactions;
        }
    }

    // @TODO Need to be able to create Matrix Score first
    public static List<Transaction> orderByMatrixScore(List<Transaction> transactions, boolean ascending){
        return transactions;
    }

    public static List<Transaction> reverseList(List<Transaction> transactions){
        Collections.reverse(transactions);
        return transactions;
    }

    public static List<Transaction> getNewList(List<Transaction> transactions){
        List<Transaction> newList = new ArrayList<Transaction>(transactions);
        Collections.copy(newList,transactions);

        return newList;
    }

    public static void removeCharges(List<Transaction> transactions){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            if(TransactionType.CHARGE.equals(t.getTransactionType())){
                it.remove();
            }
        }
    }

    public static void removePayments(List<Transaction> transactions){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            if(TransactionType.PAYMENT.equals(t.getTransactionType())){
                it.remove();
            }
        }
    }

    public static Set<Transaction> performUnion(List<Transaction> transactions1, List<Transaction> transactions2){
        Set<Transaction> union = new HashSet<Transaction>(transactions1);

        union.addAll(transactions2);

        return union;
    }

    public static Set<Transaction> performIntersection(List<Transaction> transactions1, List<Transaction> transactions2){
        Set<Transaction> intersection = new HashSet<Transaction>(transactions1);

        intersection.retainAll(transactions2);

        return intersection;
    }

    public static Set<Transaction> performCombination(List<Transaction> transactions1, List<Transaction> transactions2){
        Set<Transaction> symmetricDiff = new HashSet<Transaction>(transactions1);
        symmetricDiff.addAll(transactions2);
        Set<Transaction> tmp = new HashSet<Transaction>(transactions1);
        tmp.retainAll(transactions2);
        symmetricDiff.removeAll(tmp);

        return symmetricDiff;
    }

    public static Set<Transaction> performSubtract(List<Transaction> transactions1, List<Transaction> transactions2){
        Set<Transaction> intersection = new HashSet<Transaction>(transactions1);

        intersection.removeAll(transactions2);

        return intersection;
    }

    // @TODO - Need to implement TransactionType.getPriority
    public static void filterByPriority(List<Transaction> transactions, int from, int to){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            //int priority = t.getTransactionType().getPriority();
            //if(priority < from || priority > to){
            //    it.remove();
            //}
        }
    }

    public static void filterByDate(List<Transaction> transactions, Date from, Date to){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            Date d = t.getEffectiveDate();

            if(d.before(from) || d.after(to)){
                it.remove();
            }
        }
    }

    public static void filterByAmount(List<Transaction> transactions, BigDecimal from, BigDecimal to){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            BigDecimal d = t.getAmount();

            if((d.compareTo(from) < 0) || (d.compareTo(to) > 0)){
                it.remove();
            }
        }
    }

    public static void filterByUnallocatedAmount(List<Transaction> transactions, BigDecimal from, BigDecimal to){
        for(Iterator<Transaction> it = transactions.iterator(); it.hasNext(); ){
            Transaction t = it.next();
            BigDecimal d = t.getAmount().subtract(t.getAllocatedAmount());

            if((d.compareTo(from) < 0) || (d.compareTo(to) > 0)){
                it.remove();
            }
        }
    }

    // @TODO
    public static void filterByMatrixScore(List<Transaction> transactions, int from, int to){
        return;
    }

    // @TODO
    public void applyPayments(){}

    // @TODO
    public void applyPaymente(boolean isQueued){}
}

// @TODO - Need to implement the Priority column within the TransactionType model
class PriorityComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return 0;
    }
}

// @TODO - Need to implement the Priority column within the TransactionType model
class PriorityComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return 0;
    }
}

class DateComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.getEffectiveDate().compareTo(t2.getEffectiveDate());
    }
}

class DateComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t2.getEffectiveDate().compareTo(t1.getEffectiveDate());
    }
}

class AmountComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t1.getAmount().compareTo(t2.getAmount());
    }
}
class AmountComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        return t2.getAmount().compareTo(t1.getAmount());
    }
}

class UnallocatedAmountComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        BigDecimal v1 = t1.getAmount().subtract(t1.getAllocatedAmount());
        BigDecimal v2 = t2.getAmount().subtract(t2.getAllocatedAmount());

        return v1.compareTo(v2);
    }
}
class UnallocatedAmountComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction t1, Transaction t2) {
        BigDecimal v1 = t1.getAmount().subtract(t1.getAllocatedAmount());
        BigDecimal v2 = t2.getAmount().subtract(t2.getAllocatedAmount());

        return v2.compareTo(v1);
    }
}
