package com.sigmasys.kuali.ksa.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * A direct charge account
 *
 * @author Michael Ivanov
 */
@Entity
@DiscriminatorValue("ACD")
public class DirectChargeAccount extends ChargeableAccount {


}

