/*
 * Copyright 2010 The Kuali Foundation Licensed under the Educational
 * Community License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.osedu.org/licenses/ECL-2.0 Unless
 * required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.common.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.kuali.student.common.infc.KeyEntity;



@SuppressWarnings("serial")
@XmlTransient
public abstract class KeyEntityInfo extends EntityInfo implements KeyEntity, Serializable {

    @XmlAttribute
    private String key;

    public KeyEntityInfo() {
    	super();
        key = null;
    }

    public KeyEntityInfo(KeyEntity kEntity) {
        super(kEntity);
        if (null != kEntity) {
	        this.key = kEntity.getKey();
        }
    }

    @Override
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
}
