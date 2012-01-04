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

import javax.servlet.http.HttpServletRequest;

import org.kuali.rice.krad.web.form.UifFormBase;

/**
 * Form for Test UI Page
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DummyForm extends UifFormBase {
	private static final long serialVersionUID = 4678031668930436995L;

	private String stringField1;
	private String stringField2;
	private String stringField3;
    private String stringField4;

	public DummyForm() {
		super();
	}

	@Override
	public void postBind(HttpServletRequest request) {
		super.postBind(request);
	}

    public String getStringField1() {
        return stringField1;
    }

    public void setStringField1(String stringField1) {
        this.stringField1 = stringField1;
    }

    public String getStringField2() {
        return stringField2;
    }

    public void setStringField2(String stringField2) {
        this.stringField2 = stringField2;
    }

    public String getStringField3() {
        return stringField3;
    }

    public void setStringField3(String stringField3) {
        this.stringField3 = stringField3;
    }

    public String getStringField4() {
        return stringField4;
    }

    public void setStringField4(String stringField4) {
        this.stringField4 = stringField4;
    }
}
