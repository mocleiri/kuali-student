package com.sigmasys.kuali.ksa.model;

/**
 * Currency model.
 *
 * User: mike
 * Date: 1/22/12
 * Time: 3:47 PM
 */
public class Currency {

    /**
     * Currency ID
     */
    private Long id;

    /**
     * Currency name
     */
    private String name;

    /**
     * Currency description
     */
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
