/**
 * Copyright 2013 The Kuali Foundation Licensed under the
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
 *
 * Created by David Yin on 3/4/13
 */
package org.kuali.student.enrollment.class2.autogen.form;

import org.kuali.student.enrollment.class2.courseoffering.dto.*;
import org.kuali.student.enrollment.class2.courseoffering.form.CourseOfferingManagementForm;
import org.kuali.student.enrollment.uif.form.KSUifForm;
import org.kuali.student.r2.core.acal.dto.TermInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class //TODO ...
 *
 * @author Kuali Student Team
 */
public class ARGCourseOfferingManagementForm extends KSUifForm {
    //TODO: do we still need this for manage theCO page?
    //for authorization purpose
    private String adminOrg;

    /**
     * Search input field with the label of *Term
     */
    private String termCode;
    /**
     * Search input filed with the label of *Course
     * This is the user entered field data when they search for Course/Activity Offerings.
     * For a term and subjectArea/CourseOffering code, search will be performed.        *
     */
    private String inputCode;
    /**
     * This is used to display CO search result list in manage COs page
     */
    private List<CourseOfferingListSectionWrapper> courseOfferingResultList;
    /**
     * This is used to display AO list under a specified CO in manage the CO page
     */
    private List<ActivityOfferingWrapper> activityWrapperList;
    /**
     * This is used to display cluster list under a specified CO in manage the CO page
     * was filteredAOClusterWrapperList in RGManagementForm
     */
    private List<ActivityOfferingClusterWrapper> clusterResultList;
    /**
     * This is used to display all registration group list under a specified CO in
     * manage the CO page.
     */
    private List<RegistrationGroupWrapper> rgResultList;

    private boolean hasMoreThanOneFormat = false;
    private boolean hasMoreThanOneCluster = false;

    private TermInfo termInfo;

    /**
     * @see #setSubjectCode(String)
     */
    private String subjectCode;

    /**
     * @see #setSubjectCodeDescription(String)
     */
    private String subjectCodeDescription;


    //For Adding Activity
    private String formatIdForNewAO;
    private String activityIdForNewAO;
    private String noOfActivityOfferings;

    //TODO: why do we need wrapper objects here. why can't be COInfo.id?
    /**
     * This is used to hold the Course Offering for mange the CO page
     */
    private CourseOfferingWrapper currentCourseOfferingWrapper;
    private CourseOfferingWrapper previousCourseOfferingWrapper;
    private CourseOfferingWrapper nextCourseOfferingWrapper;

    //TODO: do we need them?
    private String selectedOfferingAction;
    private String coViewLinkWrapper = "View"; // temp var to hold/store the View Details Link

    /**
     * To display the soc state
     */
    private String socState;

    /**
     * This is being used in authz to decide the toolbar permissions.
     */
    private String socStateKey;

    /**
     * FIXME: Dont think we're using this property at view xml
     */
    private String socSchedulingStateKey;

    /**
     * FIXME: Cant we have a flag at the ActivityOfferingWrapper to handle whether the AO is selected to delete or not?
     * I dont think we need a seperate list to handle that - courseOfferingCopyWrapper
     */
    private List<ActivityOfferingWrapper> selectedToDeleteList;
    private CourseOfferingCopyWrapper courseOfferingCopyWrapper;

    private boolean isCrossListedCO;
    private int numOfCrossListedCosToDelete=0;
    private boolean isColocatedCO;
    private boolean isColocatedCoOnly;
    private Integer numOfColocatedCosToDelete = 0;
    private Integer numOfColocatedAosToDelete = 0;

    //TODO: do we need this one?
    private boolean readOnly;
    //TODO: do we need this one?
    private List<CourseOfferingListSectionWrapper> selectedCoToDeleteList;
    private int totalAOsToBeDeleted = 0;

    private String toBeScheduledCourseOfferingsUI;
    private int toBeScheduledCourseOfferingsCount;
    private boolean selectedIllegalAOInDeletion = false;

    private boolean withinPortal = true;

    private boolean editAuthz;

    private boolean enableAddButton = false;

    private Date termClassStartDate;

    //for manage AOs, Clusters, and RGs under a CO
    private String privateClusterNamePopover;
    private String publishedClusterNamePopover;
    private String formatOfferingIdForViewRG;
    private String formatOfferingName;
    private List<String> formatOfferingIds;
    private String clusterIdForAOMove;
    private int aoCount=0;

//  Begin Cluster Related Properties:
    private List<ActivityOfferingClusterWrapper> filteredAOClusterWrapperList = new ArrayList<ActivityOfferingClusterWrapper>();
//    private List<ActivityOfferingWrapper> filteredUnassignedAOsForSelectedFO;
    private boolean hasAOCluster;

    public boolean isHasAOCluster() {
        return hasAOCluster;
    }

    public void setHasAOCluster(boolean hasAOCluster) {
        this.hasAOCluster = hasAOCluster;
    }
//    public List<ActivityOfferingWrapper> getFilteredUnassignedAOsForSelectedFO() {
//        return filteredUnassignedAOsForSelectedFO;
//    }
//
//    public void setFilteredUnassignedAOsForSelectedFO(List<ActivityOfferingWrapper> filteredUnassignedAOsForSelectedFO) {
//        this.filteredUnassignedAOsForSelectedFO = filteredUnassignedAOsForSelectedFO;
//    }

    public List<ActivityOfferingClusterWrapper> getFilteredAOClusterWrapperList() {
        return filteredAOClusterWrapperList;
    }

    public void setFilteredAOClusterWrapperList(List<ActivityOfferingClusterWrapper> filteredAOClusterWrapperList) {
        this.filteredAOClusterWrapperList = filteredAOClusterWrapperList;
    }


//  End Cluster Related Properties:

    public ARGCourseOfferingManagementForm (){
        activityWrapperList = new ArrayList<ActivityOfferingWrapper>();
        selectedToDeleteList = new ArrayList<ActivityOfferingWrapper>();
        courseOfferingResultList = new ArrayList<CourseOfferingListSectionWrapper>();
        selectedCoToDeleteList = new ArrayList<CourseOfferingListSectionWrapper>();
        clusterResultList = new ArrayList<ActivityOfferingClusterWrapper>();
        rgResultList = new ArrayList<RegistrationGroupWrapper>();

        setCourseOfferingCopyWrapper(null);
    }

    public boolean isHasAO() {
        return !activityWrapperList.isEmpty();
    }

    public String getTermCode(){
        return termCode;
    }

    public void setTermCode(String termCode){
        this.termCode = termCode;
    }

    public TermInfo getTermInfo(){
        return termInfo;
    }

    public void setTermInfo(TermInfo termInfo){
        this.termInfo = termInfo;
    }

    /**
     * Returns the Subject (Org) code.
     *
     * @see #setSubjectCode(String)
     * @return
     */
    public String getSubjectCode(){
        return subjectCode;
    }

    /**
     * Sets the subject code (Org Code). Based on this code, subject are description will be displayed
     * for CO search,
     *
     * @see #setSubjectCodeDescription(String)
     * @param subjectCode
     */
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getInputCode(){
        return inputCode;
    }

    public void setInputCode(String inputCode){
        this.inputCode = inputCode;
    }

    public String getSelectedOfferingAction() {
        return selectedOfferingAction;
    }

    public void setSelectedOfferingAction(String selectedOfferingAction) {
        this.selectedOfferingAction = selectedOfferingAction;
    }

    public String getNoOfActivityOfferings() {
        return noOfActivityOfferings;
    }

    public void setNoOfActivityOfferings(String noOfActivityOfferings) {
        this.noOfActivityOfferings = noOfActivityOfferings;
    }

    public List<ActivityOfferingWrapper> getActivityWrapperList() {
        return activityWrapperList;
    }

    public void setActivityWrapperList(List<ActivityOfferingWrapper> activityWrapperList) {
        this.activityWrapperList = activityWrapperList;
    }

    public List<ActivityOfferingWrapper> getSelectedToDeleteList() {
        return selectedToDeleteList;
    }

    public void setSelectedToDeleteList(List<ActivityOfferingWrapper> selectedToDeleteList) {
        this.selectedToDeleteList = selectedToDeleteList;
    }

    public String getFormatIdForNewAO() {
        return formatIdForNewAO;
    }

    public void setFormatIdForNewAO(String formatIdForNewAO) {
        this.formatIdForNewAO = formatIdForNewAO;
    }

    public String getActivityIdForNewAO() {
        return activityIdForNewAO;
    }

    public void setActivityIdForNewAO(String activityIdForNewAO) {
        this.activityIdForNewAO = activityIdForNewAO;
    }

    public String getCoViewLinkWrapper() {
        return coViewLinkWrapper;
    }

    public void setCoViewLinkWrapper(String coViewLinkWrapper) {
        this.coViewLinkWrapper = coViewLinkWrapper;
    }

    public CourseOfferingCopyWrapper getCourseOfferingCopyWrapper() {
        return courseOfferingCopyWrapper;
    }

    public void setCourseOfferingCopyWrapper(CourseOfferingCopyWrapper courseOfferingCopyWrapper) {
        this.courseOfferingCopyWrapper = courseOfferingCopyWrapper;
    }

    /**
     * This has its ref at view xml.
     *
     * @see #setSubjectCodeDescription(String)
     * @return
     */
    @SuppressWarnings("unused")
    public String getSubjectCodeDescription() {
        return subjectCodeDescription;
    }

    /**
     * Sets the subject code. This will be displayed when the result set is only Course Offerings
     *
     * @param subjectCodeDescription
     */
    public void setSubjectCodeDescription(String subjectCodeDescription) {
        this.subjectCodeDescription = subjectCodeDescription;
    }

    public String getToBeScheduledCourseOfferingsUI() {
        return toBeScheduledCourseOfferingsUI;
    }

    public void setToBeScheduledCourseOfferingsUI(String toBeScheduledCourseOfferingsUI) {
        this.toBeScheduledCourseOfferingsUI = toBeScheduledCourseOfferingsUI;
    }

    public int getToBeScheduledCourseOfferingsCount() {
        return toBeScheduledCourseOfferingsCount;
    }

    public void setToBeScheduledCourseOfferingsCount(int toBeScheduledCourseOfferingsCount) {
        this.toBeScheduledCourseOfferingsCount = toBeScheduledCourseOfferingsCount;
    }

    public boolean isSelectedIllegalAOInDeletion() {
        return selectedIllegalAOInDeletion;
    }

    public void setSelectedIllegalAOInDeletion(boolean selectedIllegalAOInDeletion) {
        this.selectedIllegalAOInDeletion = selectedIllegalAOInDeletion;
    }

    public boolean isWithinPortal() {
        return withinPortal;
    }

    public void setWithinPortal(boolean withinPortal) {
        this.withinPortal = withinPortal;
    }

    public String getAdminOrg() {
        return adminOrg;
    }

    public void setAdminOrg(String adminOrg) {
        this.adminOrg = adminOrg;
    }

    public List<CourseOfferingListSectionWrapper> getCourseOfferingResultList() {
        return courseOfferingResultList;
    }

    public void setCourseOfferingResultList(List<CourseOfferingListSectionWrapper> courseOfferingResultList) {
        this.courseOfferingResultList = courseOfferingResultList;
    }

    public List<ActivityOfferingClusterWrapper> getClusterResultList() {
        return clusterResultList;
    }

    public void setClusterResultList(List<ActivityOfferingClusterWrapper> clusterResultList) {
        this.clusterResultList = clusterResultList;
    }

    public List<RegistrationGroupWrapper> getRgResultList() {
        return rgResultList;
    }

    public void setRgResultList(List<RegistrationGroupWrapper> rgResultList) {
        this.rgResultList = rgResultList;
    }

    public boolean getEditAuthz(){
        return editAuthz;
    }
    public void setEditAuthz(boolean editAuthz){
        this.editAuthz=editAuthz;
    }

    public String getSocState() {
        return socState;
    }

    public void setSocState(String socState) {
        this.socState = socState;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isEnableAddButton() {
        return enableAddButton;
    }

    public void setEnableAddButton(boolean enableAddButton) {
        this.enableAddButton = enableAddButton;
    }

    public String getSocStateKey() {
        return socStateKey;
    }

    public void setSocStateKey(String socStateKey) {
        this.socStateKey = socStateKey;
    }

    public String getSocSchedulingStateKey() {
        return socSchedulingStateKey;
    }

    public void setSocSchedulingStateKey(String socSchedulingStateKey) {
        this.socSchedulingStateKey = socSchedulingStateKey;
    }

    public CourseOfferingWrapper getPreviousCourseOfferingWrapper() {
        return previousCourseOfferingWrapper;
    }

    public void setPreviousCourseOfferingWrapper(CourseOfferingWrapper previousCourseOfferingWrapper) {
        this.previousCourseOfferingWrapper = previousCourseOfferingWrapper;
    }

    public CourseOfferingWrapper getNextCourseOfferingWrapper() {
        return nextCourseOfferingWrapper;
    }

    public void setNextCourseOfferingWrapper(CourseOfferingWrapper nextCourseOfferingWrapper) {
        this.nextCourseOfferingWrapper = nextCourseOfferingWrapper;
    }

    public CourseOfferingWrapper getCurrentCourseOfferingWrapper() {
        return currentCourseOfferingWrapper;
    }

    public void setCurrentCourseOfferingWrapper(CourseOfferingWrapper currentCourseOfferingWrapper) {
        this.currentCourseOfferingWrapper = currentCourseOfferingWrapper;
    }

    public List<CourseOfferingListSectionWrapper> getSelectedCoToDeleteList() {
        return selectedCoToDeleteList;
    }

    public void setSelectedCoToDeleteList(List<CourseOfferingListSectionWrapper> selectedCoToDeleteList) {
        this.selectedCoToDeleteList = selectedCoToDeleteList;
    }

    public int getTotalAOsToBeDeleted() {
        return totalAOsToBeDeleted;
    }

    public void setTotalAOsToBeDeleted(int totalAOsToBeDeleted) {
        this.totalAOsToBeDeleted = totalAOsToBeDeleted;
    }

    public boolean isCrossListedCO() {
        return isCrossListedCO;
    }

    public void setCrossListedCO(boolean crossListedCO) {
        this.isCrossListedCO = crossListedCO;
    }

    public Date getTermClassStartDate() {
        return termClassStartDate;
    }

    public void setTermClassStartDate(Date termClassStartDate) {
        this.termClassStartDate = termClassStartDate;
    }

    public String getPrivateClusterNamePopover() {
        return privateClusterNamePopover;
    }

    public void setPrivateClusterNamePopover(String privateClusterNamePopover) {
        this.privateClusterNamePopover = privateClusterNamePopover;
    }

    public String getPublishedClusterNamePopover() {
        return publishedClusterNamePopover;
    }

    public void setPublishedClusterNamePopover(String publishedClusterNamePopover) {
        this.publishedClusterNamePopover = publishedClusterNamePopover;
    }

    public String getFormatOfferingIdForViewRG() {
        return formatOfferingIdForViewRG;
    }

    public void setFormatOfferingIdForViewRG(String formatOfferingIdForViewRG) {
        this.formatOfferingIdForViewRG = formatOfferingIdForViewRG;
    }

    public String getFormatOfferingName() {
        return formatOfferingName;
    }

    public void setFormatOfferingName(String formatOfferingName) {
        this.formatOfferingName = formatOfferingName;
    }

    public List<String> getFormatOfferingIds() {
        return formatOfferingIds;
    }

    public void setFormatOfferingIds(List<String> formatOfferingIds) {
        this.formatOfferingIds = formatOfferingIds;
    }
    
    public String getClusterIdForAOMove() {
        return clusterIdForAOMove;
    }

    public void setClusterIdForAOMove(String clusterIdForAOMove) {
        this.clusterIdForAOMove = clusterIdForAOMove;
    }

    public boolean isHasMoreThanOneFormat() {
        return hasMoreThanOneFormat;
    }

    public void setHasMoreThanOneFormat(boolean hasMoreThanOneFormat) {
        this.hasMoreThanOneFormat = hasMoreThanOneFormat;
    }

    public boolean isHasMoreThanOneCluster() {
        return hasMoreThanOneCluster;
    }

    public void setHasMoreThanOneCluster(boolean hasMoreThanOneCluster) {
        this.hasMoreThanOneCluster = hasMoreThanOneCluster;
    }

    public int getNumOfCrossListedCosToDelete() {
        return numOfCrossListedCosToDelete;
    }

    public void setNumOfCrossListedCosToDelete(int numOfCrossListedCosToDelete) {
        this.numOfCrossListedCosToDelete = numOfCrossListedCosToDelete;
    }

    public boolean isColocatedCO() {
        return isColocatedCO;
    }

    public void setColocatedCO(boolean colocatedCO) {
        isColocatedCO = colocatedCO;
    }

    public boolean isColocatedCoOnly() {
        return isColocatedCoOnly;
    }

    public void setColocatedCoOnly(boolean colocatedCoOnly) {
        isColocatedCoOnly = colocatedCoOnly;
    }

    public Integer getNumOfColocatedCosToDelete() {
        return numOfColocatedCosToDelete;
    }

    public void setNumOfColocatedCosToDelete(Integer numOfColocatedCosToDelete) {
        this.numOfColocatedCosToDelete = numOfColocatedCosToDelete;
    }

    public Integer getNumOfColocatedAosToDelete() {
        return numOfColocatedAosToDelete;
    }

    public void setNumOfColocatedAosToDelete(Integer numOfColocatedAosToDelete) {
        this.numOfColocatedAosToDelete = numOfColocatedAosToDelete;
    }

    public int getAoCount() {
        return aoCount;
    }

    public void setAoCount(int aoCount) {
        this.aoCount = aoCount;
    }
}
