package com.sigmasys.kuali.ksa.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(TransactionTypeValue.CHARGE_CODE)
public class Charge extends Debit {


}

