package com.sigmasys.kuali.ksa.dao.jpa;

import com.sigmasys.kuali.ksa.dao.TransactionDao;

import java.math.BigDecimal;

/**
 * Transaction service JPA implementation.
 *
 * User: mivanov
 * Date: 1/22/12
 * Time: 12:23 PM
 * @author Michael Ivanov
 *
 */
public class TransactionDaoImpl implements TransactionDao {

    // TODO
    // Inject Entity Manager here


    @Override
    public void allocateAmount(String transactionId, BigDecimal amount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void allocateLockedAmount(String transactionId, BigDecimal amount) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void generateTransactionMemo(String transactionId, String memoText) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
