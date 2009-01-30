/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.rules.ruleexecution.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.kuali.student.rules.factfinder.dto.FactResultDTO;

/**
 * Proposition report stores the success message and/or failure message after a proposition is applied 
 * 
 * @author Kuali Student Team (kamal.kuali@gmail.com)
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PropositionReportDTO implements java.io.Serializable {
	/** Class serial version uid */
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    private String propositionName;
    
    @XmlAttribute
    private String propositionType;
    
    @XmlElement
    private boolean successful = false;

    @XmlElement
    private String successMessage;

    @XmlElement
    private String failureMessage;
    
    /**
     * Criteria used in the proposition
     */
    @XmlElement
    private FactResultDTO criteria;

    /**
     * Facts used in the proposition
     */
    @XmlElement
    private FactResultDTO fact;

    /**
     * Result of proposition execution
     */
    @XmlElement
    private FactResultDTO propositionResult;

	/**
     * Constructor
     */
    public PropositionReportDTO() {
    }

    /**
     * Gets proposition name.
     * 
     * @return Proposition name
     */
	public String getPropositionName() {
		return this.propositionName;
	}

	/**
	 * Sets proposition name.
	 * 
	 * @param name Proposition name
	 */
	public void setPropositionName(String propositionName) {
		this.propositionName = propositionName;
	}
	
    /**
	 * Returns true if report is successful.
	 * @return True if report is successful; otherwise false
	 */
    public boolean isSuccessful() {
		return successful;
	}
	
    /**
	 * Set success to true if report is successful; other wise set t false.
     * @param success True if report is successful; other false
     */
	public void setSuccessful(final boolean success) {
		this.successful = success;
	}

    /**
     * @return the successMessage
     */
    public String getSuccessMessage() {
        return successMessage;
    }
    /**
     * @param successMessage the successMessage to set
     */
    public void setSuccessMessage(final String successMessage) {
        this.successMessage = successMessage;
    }
    /**
     * @return the failureMessage
     */
    public String getFailureMessage() {
        return failureMessage;
    }
    /**
     * @param failureMessage the failureMessage to set
     */
    public void setFailureMessage(final String failureMessage) {
        this.failureMessage = failureMessage;
    }

    /**
     * Returns the facts used in the proposition.
     * 
     * @return Facts
     */
    public FactResultDTO getFactResult() {
		return fact;
	}

    /**
     * Sets the facts used in the proposition.
     * 
     * @param facts Facts
     */
	public void setFactResult(FactResultDTO facts) {
		this.fact = facts;
	}

	/**
     * Returns the criteria used in the proposition.
	 * 
	 * @return Criteria fact
	 */
	public FactResultDTO getCriteriaResult() {
		return criteria;
	}

	/**
     * Sets the criteria used in the proposition.
	 * 
	 * @param criteria Criteria fact
	 */
	public void setCriteriaResult(FactResultDTO criteria) {
		this.criteria = criteria;
	}

	/**
	 * Gets the proposition type.
	 * 
	 * @return Proposition type
	 */
	public String getPropositionType() {
		return propositionType;
	}

	/**
	 * Sets the proposition type.
	 * 
	 * @param propositionType Proposition type
	 */
	public void setPropositionType(String propositionType) {
		this.propositionType = propositionType;
	}

	/**
	 * Returns the proposition execution results.
	 * 
	 * @return Proposition execution results
	 */
    public FactResultDTO getPropositionResult() {
		return propositionResult;
	}

    /**
	 * Sets the proposition execution results.
     * 
     * @param propositionResult Proposition execution results
     */
	public void setPropositionResult(FactResultDTO propositionResult) {
		this.propositionResult = propositionResult;
	}

	public String toString() {
    	return "PropositionReportDTO[propositionName="+this.propositionName + 
			", propositionType=" + this.propositionType + 
    		", successful=" + this.successful + "]";
    }
}
