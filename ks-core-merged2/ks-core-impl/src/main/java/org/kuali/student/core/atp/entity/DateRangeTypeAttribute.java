/**
 * Copyright 2010 The Kuali Foundation Licensed under the
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

package org.kuali.student.core.atp.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.kuali.student.common.entity.Attribute;;

@Deprecated
@Entity
@Table(name = "KSAP_DT_RANGE_TYPE_ATTR")
public class DateRangeTypeAttribute extends Attribute<DateRangeType> {
	@ManyToOne
	@JoinColumn(name = "OWNER")
	private DateRangeType owner;

	@Override
	public DateRangeType getOwner() {
		return owner;
	}

	@Override
	public void setOwner(DateRangeType owner) {
		this.owner = owner;
	}
}
