package com.sigmasys.kuali.ksa.service.hibernate;

import org.hibernate.jdbc.BatchingBatcher;

/**
 * Custom BatchingBatcher that extends Hibernate's BatchingBatcher
 * <p/>
 * @author Michael Ivanov
 *
 */
public class HibernateBatcher extends BatchingBatcher {
	

    private NonBatchingBatcher nonBatchingBatcher;

    public HibernateBatcher(ConnectionManager connectionManager, Interceptor interceptor) {
        super(connectionManager, interceptor);
        nonBatchingBatcher = new NonBatchingBatcher(connectionManager, interceptor);
    }

    @Override
    public void addToBatch(Expectation expectation) throws SQLException, HibernateException {
        if (expectation.canBeBatched()) {
            super.addToBatch(expectation);
        } else {
            nonBatchingBatcher.addToBatch(expectation);
        }
    }

    /**
     * A Hibernate bug fix - calls the Hibernate's closeStatement() only when ps is not closed
     *
     * @param ps PreparedStatement
     */
    @Override
    public void closeStatement(PreparedStatement ps) {
        try {
            if (ps != null && !ps.isClosed()) {
                super.closeStatement(ps);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error occured while trying to close PreparedStatement, " +
                    "it might be due to transaction timeout: " + e.getMessage(), e);
        }
    }

}

