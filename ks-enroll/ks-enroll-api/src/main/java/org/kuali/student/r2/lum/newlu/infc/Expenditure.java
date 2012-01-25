/*
 * Copyright 2011 The Kuali Foundation
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
package org.kuali.student.r2.lum.lu.infc;

import java.util.List;
import org.kuali.student.r2.common.infc.HasAttributesAndMeta;
import org.kuali.student.r2.common.infc.HasId;

/**
 * Detailed information about organizations responsible for expenditures
 *
 * @author nwright
 */
public interface Expenditure extends HasId, HasAttributesAndMeta {

    /**
     * List of affiliated organizations.
     * @name Affiliated Organizations
     */
    public List<? extends AffiliatedOrg> getAffiliatedOrgs();
}