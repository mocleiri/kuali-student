package org.kuali.rice.krms.dto;

import org.kuali.rice.krms.api.repository.term.TermDefinitionContract;
import org.kuali.rice.krms.api.repository.term.TermParameterDefinitionContract;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinitionContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: SW
 * Date: 2013/03/05
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TermEditor implements TermDefinitionContract {

    private String id;
    private TermSpecificationDefinition specification;
    private String description;
    private List<TermParameterEditor> parameters;
    private Long versionNumber;

    public TermEditor(){
        super();
    }

    public TermEditor(TermDefinitionContract contract){
        this.id = contract.getId();
        //this.specification = contract.getSpecification();
        this.description = contract.getDescription();
        if(contract.getParameters() != null){
            parameters = new ArrayList<TermParameterEditor>();
            for (TermParameterDefinitionContract parameter : contract.getParameters()){
                parameters.add(new TermParameterEditor(parameter));
            }
        }
        this.versionNumber = contract.getVersionNumber();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSpecification(TermSpecificationDefinition specification) {
        this.specification = specification;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParameters(List<TermParameterEditor> parameters) {
        this.parameters = parameters;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    @Override
    public TermSpecificationDefinitionContract getSpecification() {
        return this.specification;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<? extends TermParameterDefinitionContract> getParameters() {
        return this.parameters;
    }

    public List<TermParameterEditor> getEditorParameters() {
        return this.parameters;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }
}
