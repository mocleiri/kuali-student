package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

/**
 * This class represents a subclass of the KeyPair class that is capable of
 * referring to a certain time period. Per the existing design, 
 * "For example, for a simple three-term system, the periods might be stored as 2012-1, 2012-2, 2012-3."
 * 
 * @author Sergey
 * @version 1.0
 */
@Entity
@DiscriminatorValue(KeyPairType.PERIOD_KEYPAIR_TYPE_CODE)
public class PeriodKeyPair extends KeyPair {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Due to the need to store different values by period, this is a key
	 * pair value that also has a period indicator. Set at instantiation.
	 */
	private String period;

	
	@Column(name="PERIOD", length=45)
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public boolean equals(Object o) {
		if (o instanceof PeriodKeyPair) {
			PeriodKeyPair pkpAnother = (PeriodKeyPair)o;
			
			return super.equals(o) && StringUtils.equals(pkpAnother.period, this.period);
		}
		
		return false;
	}
	
	public int hashCode() {
		return 31 * (super.hashCode() + (StringUtils.isNotBlank(period) ? period.hashCode() : 1));
	}
}
