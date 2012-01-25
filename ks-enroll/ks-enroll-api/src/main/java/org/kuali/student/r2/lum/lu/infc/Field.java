/*
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.r2.lum.lu.infc;

/**
 * Key/value pair, typically used for information which may vary from the common
 * set of information provided about an object.
 *
 * @Version 2.0
 * @Author Sri komandur@uw.edu
 */
public interface Field {
    /**
     * The identifier for this field.
     *
     * @name Id
     * @readOnly
     * @required
     */
    String getId();

    /**
     * The value for this field.
     *
     * @name Value
     */
    String getValue();
}
