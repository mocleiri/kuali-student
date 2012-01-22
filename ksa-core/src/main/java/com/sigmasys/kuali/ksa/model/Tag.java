package com.sigmasys.kuali.ksa.model;

/**
 * Tag.
 * //TODO: provide a detail description of it
 * 
 * User: mike
 * Date: 1/22/12
 * Time: 4:40 PM
 */
public class Tag {
    
    private String code;
    
    private String name;

    public Tag() {
    }

    public Tag(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
