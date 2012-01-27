package com.sigmasys.kuali.ksa.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("C")
public class Charge extends Debit {

    // TODO: Do we need Charge if it is the only one Credit transaction type?

    // TODO: implement Charge properties and methods?


}

