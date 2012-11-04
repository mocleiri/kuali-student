package com.sigmasys.kuali.ksa.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sigmasys.kuali.ksa.exception.InvalidRefundTypeException;
import com.sigmasys.kuali.ksa.exception.RefundCancelledException;
import com.sigmasys.kuali.ksa.exception.RefundFailedException;
import com.sigmasys.kuali.ksa.exception.RefundNotFoundException;
import com.sigmasys.kuali.ksa.exception.RefundNotVerifiedException;
import com.sigmasys.kuali.ksa.exception.UserNotFoundException;
import com.sigmasys.kuali.ksa.model.Account;
import com.sigmasys.kuali.ksa.model.Activity;
import com.sigmasys.kuali.ksa.model.Allocation;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.PostalAddress;
import com.sigmasys.kuali.ksa.model.Refund;
import com.sigmasys.kuali.ksa.model.RefundStatus;
import com.sigmasys.kuali.ksa.model.Transaction;
import com.sigmasys.kuali.ksa.service.AccountService;
import com.sigmasys.kuali.ksa.service.RefundService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.transform.Ach;
import com.sigmasys.kuali.ksa.util.RequestUtils;

/**
 * The refund service is the core of KSA-RM-RM. It handles the production of refunds from the system. 
 * At its heart is the Refund object, which acts as a queue for refunds to be processed.
 * <p>
 * The refund service will try to determine if a transaction can be paid as "cash" 
 * (that is, without restrictions, not going to a third party, or being repaid in a specific way.) 
 * If a refund (or part of a refund) can be paid as "cash" then it will be paid with the refund type as 
 * defined in "refund.method" system property, which is a student-specific attribute, unless 
 * the "override.refund.method" property is set to a different refund type, in which case, this takes precedence. 
 * If neither are set, then the "default.refund.method" property is used.
 * <p>
 * This allows a default method for all students (likely paper check) with students able to sign up for other 
 * refund types (bank transfer, etc.) and it allows a counselor to override that choice in certain cases 
 * (for example, they may require certain students to pick up their refund check at the office.) 
 * <p>
 * @author Sergey Godunov
 * @version 1.0
 */
@Service("refundService")
@Transactional
@SuppressWarnings("unchecked")
public class RefundServiceImpl extends GenericPersistenceService implements RefundService {
	
	/**
     * The logger.
     */
    private static final Log logger = LogFactory.getLog(RefundServiceImpl.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;
    

	@Override
	public List<Refund> checkForRefund(String accountId, Date dateFrom,
			Date dateTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Refund> checkForRefund(List<String> accountIds, Date dateFrom,
			Date dateTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Refund> checkForRefunds(Date dateFrom, Date dateTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Refund> checkForRefunds() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This actually creates the refund transaction, in addition to allocating the refund to the original charge. 
	 * It marks the refund object as refunded.
	 * 
	 * @param refundId ID of a Refund object.
	 * @return The Refund object.
	 */
	@Override
	public Refund performRefund(Long refundId) {
		return performRefund(refundId, null);
	}

	/**
	 * This actually creates the refund transaction as a part of a batch transaction, in addition to allocating the refund to the original charge. 
	 * It marks the refund object as refunded.
	 * 
	 * @param refundId ID of a Refund object.
	 * @param batch ID of a batch transaction.
	 * @return The Refund object.
	 */
	@Override
	public Refund performRefund(Long refundId, String batch) {
		return performRefundInternal(refundId, batch, null);
	}

	/**
	 * Performs refund validation.
	 * Sets the refundStatus to {@link RefundStatus#VERIFIED} and the authorizedBy to the current user.
	 * @param refundId
	 * @return
	 */
	@Override
	public Refund validateRefund(Long refundId) {
		// Get the Refund object by its identifier:
		Refund refund = getRefund(refundId, false);
		
		// Set the Refund status to VERIFIED:
		String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
		Account currentUserAccount = accountService.getOrCreateAccount(currentUserId);
		
		refund.setStatus(RefundStatus.VERIFIED);
		refund.setAuthorizedBy(currentUserAccount);
		persistEntity(refund);
		
		return refund;
	}

	/**
	 * Performs a refund to a student account. Calls <code>performRefund</code> for the actual refund action.
	 * 
	 * @param refundId ID of a refund to apply to a student account.
	 * @return The <code>Refund</code> object that was generated during this process.
	 */
	@Override
	public Refund doAccountRefund(Long refundId) {
		return doAccountRefund(refundId, null);
	}

	/**
	 * Performs a batched refund to a student account. Calls <code>performRefund</code> for the actual refund action.
	 * 
	 * @param refundId ID of a refund to apply to a student account.
	 * @param batch Batch of transactions.
	 * @return The <code>Refund</code> object that was generated during this process.
	 */
	@Override
	public Refund doAccountRefund(Long refundId, String batch) {
		// Get the Refund object and check that it's in the VERIFIED status:
		Refund refund = getRefund(refundId, true);
		
		// Check that it's an Account refund type using the Refund's "attribute" attribute:
		String refundRule = refund.getAttribute();
		
		if (!StringUtils.startsWith(refundRule, "A")) {
			throw new InvalidRefundTypeException("Invalid Refund type for an Account refund. Refund rule (attribute) is [" + refundRule + "].");
		}
		
		// Validate the refund rule:
		if (!isRefundRuleValid(refundRule)) {
			// Check if the refund rule is invalid because of a non-existing Account:
			String[] refundRuleParams = StringUtils.substringsBetween(refundRule, "(", ")");
			
			if ((refundRuleParams != null) && (refundRuleParams.length == 2)) {
				String accountId = refundRuleParams[1];
				boolean accountExists = accountService.accountExists(accountId);
				
				if (!accountExists) {
					// Fail the Refund:
					refund.setStatus(RefundStatus.FAILED);
					
					// Create an Activity to record this occurrence:
					createInvalidAccountActivity(accountId);
					
					throw new UserNotFoundException("Refund attribute contains an invalid Account identifier. Refund attribute was [" + refundRule + "]");
				}
			}
			
			// If, however, the refund rule is invalid for another reason, throw an exception:
			throw new InvalidRefundTypeException("Invalid Refund attribute [" + refundRule + "].");
		}
		
		// Perform refund:
		String accountId = StringUtils.substringsBetween(refundRule, "(", ")")[1];
		
		refund = performRefundInternal(refundId, batch, accountId);
		
		// Set the Refund system:
		String systemName = configService.getInitialParameter(Constants.REFUND_ACCOUNT_SYSTEM_NAME);
		
		refund.setSystem(systemName);
		
		// Set the Transaction's statement text to that of the Refund's:
		String overrideStatement = refund.getStatement();
		
		if (StringUtils.isNotBlank(overrideStatement)) {
			Transaction refundTransaction = refund.getRefundTransaction();
			
			refundTransaction.setStatementText(overrideStatement);
		}
		
		return refund;
	}

	/**
	 * Go through the Refund objects in a batch. For each validated refund with type set to account refund
	 * (account.refund.type). For each one that is found, call doAccountRefund (refundId, batch).
	 * 
	 * @param batch ID of a transaction batch.
	 * @return A <code>List</code> of <code>Refund</code> objects created as a result of this operation.
	 */
	@Override
	public List<Refund> doAccountRefunds(String batch) {
		// Find all Refund object with the given value of "batch":
        String sql = "select r from Refund r where r.batchId = :batchId and r.status = :status and r.refundType.creditTypeId = :refundTypeId";
        String refundTypeId = configService.getInitialParameter(Constants.REFUND_ACCOUNT_TYPE);
        Query query = em.createQuery(sql)
        		.setParameter("batchId", batch)
        		.setParameter("status", RefundStatus.VERIFIED)
        		.setParameter("refundTypeId", refundTypeId);
        List<Refund> match = query.getResultList();
		
		// For each matching Refund, perform account refund:
        for (Refund refund : match) {
        	doAccountRefund(refund.getId(), batch);
        }
		
		return null;
	}

	@Override
	public String doCheckRefund(Long refundId, Date checkDate, String checkMemo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doCheckRefund(Long refundId, String batch, Date checkDate,
			String checkMemo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doCheckRefunds(String batch, Date checkDate, String checkMemo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String produceXMLCheck(String identifier, String payee,
			PostalAddress postalAddress, BigDecimal amount, Date checkDate,
			String memo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doAchRefund(Long refundId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doAchRefund(Long refundId, String batch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doAchRefunds(String batch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String produceAchTransmission(Ach ach, BigDecimal amount,
			String reference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRefundRuleValid(String refundRule) {
        if (StringUtils.isBlank(refundRule)) {
            return true;
        }

        // example A(100)(pheald) or S(45)
        // transactionService.isRefundRule(refundRule);
        // applies to credit types
        // drill down on the refund rule provided
        if (refundRule.startsWith("A") || refundRule.startsWith("S")) {
            // does the rule have a number 0 - 65535 after/in parenthesis
            int openRuleIndex = refundRule.indexOf("(");
            int closeRuleIndex = refundRule.indexOf(")");
            if ((openRuleIndex > 0) && (closeRuleIndex > 0) && (openRuleIndex < closeRuleIndex)) {
                try {
                    int value = Integer.valueOf(refundRule.substring(openRuleIndex + 1, closeRuleIndex));
                    if ((value < 0) || (value > 0xffff)) {
                        return false;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    return false;
                }
                if (refundRule.startsWith("S")) {
                    return true;
                } else {
                    // There should be a second parameter in parenthesis for "A" which appears to be the accountId
                    // which must exist in KSA
                    int openAccountIndex = refundRule.indexOf("(", closeRuleIndex+1);
                    int closeAccountIndex = refundRule.indexOf(")", closeRuleIndex+1);
                    if ((openAccountIndex > 0) && (closeAccountIndex > 0) && (openAccountIndex < closeAccountIndex)) {
                        try {
                            String accountId = refundRule.substring(openAccountIndex + 1, closeAccountIndex);
                            return (accountService.getOrCreateAccount(accountId) != null);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            return false;
                        }
                    }
                }
            }
        }

        return false;
	}

	/**
	 * Performs refund cancellation. 
	 * Reverses the associated <code>Transaction</code> and sets the appropriate refund status.
	 * Refer to the "Process Diagrams" document for the refund cancellation logic.
	 * 
	 * @param refundId ID of a <code>Refund</code> to be cancelled.
	 * @param memo An explanation of the reasons for refund cancellation.
	 * @return <code>Refund</code> that was cancelled.
	 */
	@Override
	public Refund cancelRefund(Long refundId, String memo) {
		// Get the Refund object:
		Refund refund = getRefund(refundId, false);
		RefundStatus status = refund.getStatus();
		
		// Check different statuses that lead to different results:
		if (status == RefundStatus.CANCELED) {
			throw new RefundCancelledException("Refund with ID [" + refundId + "] is already canceled.");
		} else if (status == RefundStatus.FAILED) {
			throw new RefundFailedException("Refund with ID [" + refundId + "] is already failed.");
		} else if ((status == RefundStatus.VERIFIED) || (status == RefundStatus.UNVERIFIED)) {
			refund.setStatus(RefundStatus.CANCELED);
		} else if (status == RefundStatus.REFUNDED) {
			// Check all Refunds belonging to the same refund group, cancel them and reverse all Transactions.
			// If there is no refund group, cancel only the refund and reverse its Transaction:
			String refundGroup = refund.getRefundGroup();
			List<Refund> refundsToCancel = new ArrayList<Refund>();
			
			refundsToCancel.add(refund);
			
			if (StringUtils.isNotBlank(refundGroup)) {
				// Get all Refunds with the same group ID:
				String sql = "select r from Refund r where r.refundGroup = :refundGroup and r.id <> :id";
				Query query = em.createQuery(sql).setParameter("refundGroup", refundGroup).setParameter("id", refund.getId());

				refundsToCancel.addAll(query.getResultList());
			}
			
			// Cancel all Refunds and reverse their Transactions:
			for (Refund refundToCancel : refundsToCancel) {
				Transaction refundTransaction = refundToCancel.getRefundTransaction();
				BigDecimal amount = refund.getAmount();
				
				transactionService.reverseTransaction(refundTransaction.getId(), memo, amount, "Refund Cancellation");
				refundToCancel.setStatus(RefundStatus.CANCELED);
				persistEntity(refundToCancel);
			}
		}
		
		return refund;
	}

	@Override
	public Refund payoffWithRefund(String accountId, BigDecimal maxPayoff) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund doPayoffRefund(Long refundId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Refund doPayoffRefund(Long refundId, String batch) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Refund> doPayoffRefunds(String batch) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	/* ****************************************************************
	 * 
	 * Utility methods.
	 * 
	 * ****************************************************************/
	
	/**
	 * Finds a <code>Refund</code> object. Optionally, asserts that it's in the VERIFIED state.
	 * 
	 * @param refundId ID of a <code>Refund</code> to be located.
	 * @param checkVerified Flag to check if the <code>Refund</code> is in the VERIFIED state.
	 * @return The <code>Refund</code> object with the matching ID.
	 * @throws RefundNotFoundException If no such <code>Refund</code> is found by the given ID.
	 * @throws RefundNotVerifiedException If <code>checkVerified</code> flag is <code>true</code> 
	 * 	and the <code>Refund</code> has a status different than {@link RefundStatus#VERIFIED}
	 */
	private Refund getRefund(Long refundId, boolean checkVerified) {
		// Get the Refund object by its identifier:
		Refund refund = getEntity(refundId, Refund.class);
		
		if (refund == null) {
			throw new RefundNotFoundException(refundId, "Refund with ID [" + refundId + "] was found in the system.");
		}
		
		// Check the status of the Refund:
		if (checkVerified) {
			RefundStatus refundStatus = refund.getStatus();
			
			if (refundStatus != RefundStatus.VERIFIED) {
				throw new RefundNotVerifiedException("Refund with ID [" + refundId + "] is not verified.");
			}
		}
		
		return refund;
	}
	
	/**
	 * A utility method that performs refund and accepts an Account ID optionally.
	 * 
	 * @param refundId Refund ID.
	 * @param batch Batch ID.
	 * @param accountId Optional Account ID. If null, the original Transaction's Account ID is used.
	 * @return The Refund object.
	 */
	private Refund performRefundInternal(Long refundId, String batch, String accountId) {
		// Get the Refund object and check that it's in the VERIFIED status:
		Refund refund = getRefund(refundId, true);
		
		// Create a charge transaction on the account referenced through the original transaction
		// Of type referenced in the refundType, of the amount of the refund:
		Transaction originalTransaction = refund.getTransaction();
		Account account = originalTransaction.getAccount();
		String userId = StringUtils.isNotBlank(accountId) ? accountId : account.getId();
		String refundTransactionTypeId = refund.getRefundType().getCreditTypeId();
		Date effectiveDate = new Date();
		BigDecimal amount = refund.getAmount();
		Transaction refundTransaction = transactionService.createTransaction(refundTransactionTypeId, userId, effectiveDate, amount);
		
		// Create a lockedAllocation between the refund and the original transaction in the amount of the refund:
        Allocation allocation = new Allocation();
        
        allocation.setAccount(account);
        allocation.setFirstTransaction(originalTransaction);
        allocation.setSecondTransaction(refundTransaction);
        allocation.setAmount(amount);
        allocation.setLocked(true);
        persistEntity(allocation);
		
        // Update the Refund object with the modified values:
        refund.setRefundDate(effectiveDate);
        refund.setRefundTransaction(refundTransaction);
        refund.setStatus(RefundStatus.REFUNDED);
        
        if (StringUtils.isNotBlank(batch)) {
        	refund.setBatchId(batch);
        }
        
        persistEntity(refund);
		
		return refund;
	}

	/**
	 * Creates an <code>Activity</code> that records an occurrence of an invalid <code>Account</code>
	 * 
	 * @param accountId An invalid <code>Account</code> identifier.
	 */
	private void createInvalidAccountActivity(String accountId) {
        Activity activity = new Activity();
        String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());
        
        activity.setAccountId(accountId);
        activity.setEntityId(accountId);
        activity.setEntityType(Account.class.getSimpleName());
        activity.setCreatorId(currentUserId);
        activity.setIpAddress("127.0.0.1");
        activity.setLogDetail("Invalid account identifier [" + accountId + "] in processing Account Refunds.");
        activity.setTimestamp(new Date());
        
        persistEntity(activity);
	}
}
