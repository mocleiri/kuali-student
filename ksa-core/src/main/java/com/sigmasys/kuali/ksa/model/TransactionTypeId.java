package com.sigmasys.kuali.ksa.model;

import java.io.Serializable;

import javax.persistence.Column;

@SuppressWarnings("serial")
public class TransactionTypeId implements Serializable {
	
	    private Long id;    
	    private String subCode;
	    
	    
	    public TransactionTypeId() {
	    }
	    
	    public TransactionTypeId(Long id, String subCode) {
	        setId(id);
	        setSubCode(subCode);
	    }
	    
	    @Column(name = "ID")
	    public Long getId() {
	        return id;
	    }
	    public void setId(Long id) {
	        this.id = id;
	    }
	    
	    @Column(name = "SUBCODE")
	    public String getSubCode() {
	        return subCode;
	    }
	    public void setSubCode(String subCode) {
	        this.subCode = subCode;
	    }
	    
	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((id == null) ? 0 : id.hashCode());
	        result = prime * result + ((subCode == null) ? 0 : subCode.hashCode());
	        return result;
	    }
	    
	    @Override
	    public boolean equals(Object obj) {
	        
	    	if (this == obj) {
	            return true;
	    	}    
	        
	        if (obj == null) {
	            return false;
	        }    
	        
	        if (getClass() != obj.getClass()) {
	            return false;
	        }    
	        
	        TransactionTypeId other = (TransactionTypeId) obj;
	        
	        if (getId() == null) {
	            if (other.getId() != null) {
	                return false;
	            }    
	        } else if (!getId().equals(other.getId())) {
	            return false;
	        }    
	        
	        if (getSubCode() == null) {
	            if (other.getSubCode() != null) {
	                return false;
	            }    
	        } else if (!getSubCode().equals(other.getSubCode())) {
	            return false;
	        }    
	        
	        return true;
	    }

}
