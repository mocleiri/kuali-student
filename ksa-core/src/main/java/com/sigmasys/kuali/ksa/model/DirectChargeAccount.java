package com.sigmasys.kuali.ksa.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * A direct charge account
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue(AccountTypeValue.DIRECT_CHARGE_CODE)
public class DirectChargeAccount extends ChargeableAccount {


}

