package com.sigmasys.kuali.ksa.model;

import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("D")
public class Deferment extends Credit {

    /**
      * All deferments are set with an expiration date. If the date passes, then the deferment is expired, and the payment application system will
      * remove the allocation of the deferment, and the charge it is applied to will become due.
      */
	@Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    /**
      * A deferment is always issued against a debit. This value shows the system which debit has been deferred.
      * Once a deferment is expired, this value remains to show
      * the original status and intention of the deferment.
      */
	@Column(name = "DEFER_TRN_ID")
    private Long deferredTransactionId;

    /** a deferment is the only type of transaction whose amount can be altered. For audit purposes,
      * the original value of the deferment is set permanently in this attribute,
      * even if the deferment is reduced or expired, the original value will be accessible here.
      */
	@Column(name = "ORIG_DEFER_AMOUNT")
    private BigDecimal originalDefermentAmount;


    public void setDeferredTransactionId(Long deferredTransactionId) {
        this.deferredTransactionId = deferredTransactionId;
    }

    public Long getDeferredTransactionId() {
        return deferredTransactionId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getOriginalDefermentAmount() {
        return originalDefermentAmount;
    }

    public void setOriginalDefermentAmount(BigDecimal originalDefermentAmount) {
        this.originalDefermentAmount = originalDefermentAmount;
    }

    /*
      * This returns the expiration status and is true if the deferment has expired.
      */
    @Transient
    public boolean isExpired() {
        // TODO:
        return false;
    }

}

