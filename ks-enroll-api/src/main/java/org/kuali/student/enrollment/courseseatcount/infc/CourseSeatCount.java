/**
 * Copyright 2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.enrollment.courseseatcount.infc;


import org.kuali.student.r2.common.infc.IdNamelessEntity;

import java.util.Date;

public interface CourseSeatCount extends IdNamelessEntity {


    /**
     * The activityOffering related to this courseSeatCount
     * @name  ActivityOffering Id
     */
    String getActivityOfferingId();
    /**
     * Total number of seats
     * @name Total Seats
     */
    Integer getSeats();

    /**
     * Number of seats taken by registered students
     * @name Used Seats
     */
    Integer getTakenSeats();

    /**
     * Number of unused seats.  In general, the sum of available and used
     * seats equal the total seats though that definition is most clear
     * for activity offerings
     * @name Available Seats
     */
    Integer getAvailableSeats();

    /**
     * The approximate timestamp of when the seatcount was taken
     * @name Timestamp
     */
    Date getTimestamp();

}
