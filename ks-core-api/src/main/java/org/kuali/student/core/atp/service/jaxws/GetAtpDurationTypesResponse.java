
package org.kuali.student.core.atp.service.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.student.core.atp.dto.AtpDurationTypeInfo;

/**
 * This class was generated by Apache CXF 2.2
 * Tue Mar 31 14:06:06 EDT 2009
 * Generated source version: 2.2
 */

@XmlRootElement(name = "getAtpDurationTypesResponse", namespace = "http://student.kuali.org/core/atp")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAtpDurationTypesResponse", namespace = "http://student.kuali.org/core/atp")

public class GetAtpDurationTypesResponse {

    @XmlElement(name = "return")
    private java.util.List<AtpDurationTypeInfo> _return;

    public java.util.List<AtpDurationTypeInfo> getReturn() {
        return this._return;
    }

    public void setReturn(java.util.List<AtpDurationTypeInfo> new_return)  {
        this._return = new_return;
    }

}

