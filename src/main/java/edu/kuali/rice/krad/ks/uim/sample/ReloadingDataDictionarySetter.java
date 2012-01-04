/*
 * Copyright 2006-2011 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kuali.rice.krad.ks.uim.sample;

import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class ReloadingDataDictionarySetter implements BeanPostProcessor {
    private DataDictionary dataDictionary;

    public void setDataDictionary(DataDictionary dataDictionary) {
        this.dataDictionary = dataDictionary;
    }

    public Object postProcessAfterInitialization(Object bean, String name) {
        if ("dataDictionary".equals(name)) {
            bean = dataDictionary;
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String name) {
        return bean;
    }
}
