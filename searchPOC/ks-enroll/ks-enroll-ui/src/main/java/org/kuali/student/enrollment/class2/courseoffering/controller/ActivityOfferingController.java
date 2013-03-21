package org.kuali.student.enrollment.class2.courseoffering.controller;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.student.enrollment.class2.courseoffering.dto.ActivityOfferingWrapper;
import org.kuali.student.enrollment.class2.courseoffering.dto.ScheduleWrapper;
import org.kuali.student.enrollment.class2.courseoffering.form.ActivityOfferingForm;
import org.kuali.student.enrollment.class2.courseoffering.service.ActivityOfferingMaintainable;
import org.kuali.student.enrollment.class2.courseoffering.util.ActivityOfferingConstants;
import org.kuali.student.enrollment.uif.form.KSUifMaintenanceDocumentForm;
import org.kuali.student.enrollment.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.uif.util.KSUifUtils;
import org.kuali.student.r2.common.util.date.KSDateTimeFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/activityOffering")
public class ActivityOfferingController extends MaintenanceDocumentController {

    @Override
    protected MaintenanceDocumentForm createInitialForm(HttpServletRequest request) {
        return new ActivityOfferingForm();
    }

    /**
     * Setups a new <code>MaintenanceDocumentView</code> with the edit maintenance
     * action
     */
    @RequestMapping(params = "methodToCall=" + KRADConstants.Maintenance.METHOD_TO_CALL_EDIT)
    public ModelAndView maintenanceEdit(@ModelAttribute("KualiForm") MaintenanceDocumentForm form, BindingResult result,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        setupMaintenance(form, request, KRADConstants.MAINTENANCE_EDIT_ACTION);

        // check view authorization
        // TODO: this needs to be invoked for each request
        if (form.getView() != null) {
            String methodToCall = request.getParameter(KRADConstants.DISPATCH_REQUEST_PARAMETER);
            checkViewAuthorization(form, methodToCall);
//            form.setEditAuthz(checkEditViewAuthz(form));
        }

        //populate the previousFormsMap of the form. The map contains info about the previous view to generate customized breadcrumb
        KSUifUtils.populationPreviousFormsMap(request, (KSUifMaintenanceDocumentForm)form);

        return getUIFModelAndView(form);
    }

    /*@RequestMapping(params = "methodToCall=reviseSchedule")
    public ModelAndView reviseSchedule(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
        ActivityOfferingMaintainable viewHelper = (ActivityOfferingMaintainable) KSControllerHelper.getViewHelperService(form);
        viewHelper.prepareForScheduleRevise(activityOfferingWrapper);
        form.setDeliveryLogisiticsAddButtonText("Add");

        return getUIFModelAndView(form,ActivityOfferingForm.SCHEDULE_PAGE);
    }*/

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=editScheduleComponent")
    public ModelAndView editScheduleComponent(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
        ScheduleWrapper scheduleWrapper = (ScheduleWrapper)getSelectedObject(form);

        ScheduleWrapper newSchedule = new ScheduleWrapper();
        newSchedule.copyForEditing(scheduleWrapper);
        activityOfferingWrapper.setNewScheduleRequest(newSchedule);
        activityOfferingWrapper.getRequestedScheduleComponents().remove(scheduleWrapper);
        form.setScheduleEditInProgress(true);

        return getUIFModelAndView(form);
    }

    @RequestMapping(params = "methodToCall=addScheduleComponent")
    public ModelAndView addScheduleComponent(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();

        ScheduleWrapper scheduleWrapper = activityOfferingWrapper.getNewScheduleRequest();
        if (validateTime(scheduleWrapper.getStartTime(), scheduleWrapper.getStartTimeAMPM(), scheduleWrapper.getEndTime(), scheduleWrapper.getEndTimeAMPM())) {
            GlobalVariables.getMessageMap().putError("requestedDeliveryLogistic", ActivityOfferingConstants.MSG_ERROR_INVALID_START_TIME);
            return getUIFModelAndView(form);
        }

        ActivityOfferingMaintainable viewHelper = (ActivityOfferingMaintainable) KSControllerHelper.getViewHelperService(form);
        boolean success = viewHelper.addScheduleRequestComponent(form);

        if (success){
//            form.setDeliveryLogisiticsAddButtonText("Add");
            form.setScheduleEditInProgress(false);
            activityOfferingWrapper.setSchedulesModified(true);
        }
        
        return getUIFModelAndView(form);
    }

    /*@RequestMapping(params = "methodToCall=saveRevisedSchedules")
    public ModelAndView saveRevisedSchedules(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();

        if (form.isScheduleEditInProgress()){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_CUSTOM, "Editing a schedule request in progress. Please update it first before processing");
            return getUIFModelAndView(form,ActivityOfferingForm.SCHEDULE_PAGE);
        }

        activityOfferingWrapper.getRequestedScheduleComponents().clear();

        for (ScheduleWrapper scheduleWrapper : activityOfferingWrapper.getRevisedScheduleRequestComponents()){
            activityOfferingWrapper.getRequestedScheduleComponents().add(scheduleWrapper);
        }

        activityOfferingWrapper.setNewScheduleRequest(new ScheduleWrapper());
        activityOfferingWrapper.getRevisedScheduleRequestComponents().clear();

        return getUIFModelAndView(form,ActivityOfferingForm.MAIN_PAGE);
    }

    @RequestMapping(params = "methodToCall=processRevisedSchedules")
    public ModelAndView processRevisedSchedules(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();

        if (form.isScheduleEditInProgress()){
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_CUSTOM, "Editing a schedule request in progress. Please update it first before processing");
            return getUIFModelAndView(form,ActivityOfferingForm.SCHEDULE_PAGE);
        }

        activityOfferingWrapper.getRequestedScheduleComponents().clear();

        for (ScheduleWrapper scheduleWrapper : activityOfferingWrapper.getRevisedScheduleRequestComponents()){
            activityOfferingWrapper.getRequestedScheduleComponents().add(scheduleWrapper);
        }

        ActivityOfferingMaintainable viewHelper = (ActivityOfferingMaintainable) KSControllerHelper.getViewHelperService(form);
        viewHelper.processRevisedSchedules(activityOfferingWrapper);

        activityOfferingWrapper.setNewScheduleRequest(new ScheduleWrapper());
        activityOfferingWrapper.getRevisedScheduleRequestComponents().clear();
        activityOfferingWrapper.setSchedulesModified(false);

        GlobalVariables.getMessageMap().putInfo(KRADConstants.GLOBAL_INFO, RiceKeyConstants.ERROR_CUSTOM, "Schedule has been successfully processed");

        return getUIFModelAndView(form,ActivityOfferingForm.MAIN_PAGE);
    }

    @RequestMapping(params = "methodToCall=cancelScheduleRevise")
    public ModelAndView cancelScheduleRevise(@ModelAttribute("KualiForm") ActivityOfferingForm form) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();

        activityOfferingWrapper.getRevisedScheduleRequestComponents().clear();
        activityOfferingWrapper.setSchedulesModified(false);

        return getUIFModelAndView(form,ActivityOfferingForm.MAIN_PAGE);
    }*/


    @Override
    @RequestMapping(params = "methodToCall=route")
    public ModelAndView route(@ModelAttribute("KualiForm") DocumentFormBase form, BindingResult result,
                              HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = super.route(form, result, request, response);

        if( form.getFormHistory().getHistoryEntries().isEmpty()){
            return modelAndView;
        }

        //Redirect to last page (some hackery here that I'd rather not do
        //The url has lots of dulpicate params which prevents history from working correctly
        String url = form.getFormHistory().getHistoryEntries().get(form.getFormHistory().getHistoryEntries().size() - 1).getUrl();
        url = url.replaceAll("&methodToCall=[a-zA-Z]+","");
        url = url.replaceAll("&pageId=[a-zA-Z]+","");
        url = url.replaceAll("\\?methodToCall=[a-zA-Z]+&","?");
        url = url.replaceAll("\\?pageId=[a-zA-Z]+&","?");
        form.getFormHistory().getHistoryEntries().get(form.getFormHistory().getHistoryEntries().size() - 1).setUrl(url);
        modelAndView = returnToHistory(form, false);
        modelAndView.setViewName(modelAndView.getViewName().replaceFirst("methodToCall="+ UifConstants.MethodToCallNames.REFRESH,"methodToCall=show"));
        return modelAndView;
    }

    /*@RequestMapping(method = RequestMethod.POST, params = "methodToCall=deleteLine")
    public ModelAndView deleteLine(@ModelAttribute("KualiForm") ActivityOfferingForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActivityOfferingWrapper activityOfferingWrapper = (ActivityOfferingWrapper)form.getDocument().getNewMaintainableObject().getDataObject();
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);

        if (StringUtils.endsWith(selectedCollectionPath,"revisedScheduleRequestComponents")){
            if (activityOfferingWrapper.getRequestedScheduleComponents().size() == 1){
                String dialogName = "deleteScheduleConfirmDialog";

                if (!hasDialogBeenAnswered(dialogName, form)) {
                    return showDialog(dialogName, form, request, response);
                }

                boolean dialogAnswer = getBooleanDialogResponse(dialogName, form, request, response);
                form.getDialogManager().resetDialogStatus(dialogName);

                if (!dialogAnswer) {
                    return getUIFModelAndView(form);
                }
            }
        }

        return super.deleteLine(form,result,request,response);

    }*/

    private Object getSelectedObject(ActivityOfferingForm form){
        String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELLECTED_COLLECTION_PATH);
        if (StringUtils.isBlank(selectedCollectionPath)) {
            throw new RuntimeException("Selected collection was not set");
        }

        int selectedLineIndex = KSControllerHelper.getSelectedCollectionLineIndex(form);

        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(form, selectedCollectionPath);
        return ((List<Object>) collection).get(selectedLineIndex);
    }
    
    public boolean validateTime (String startTime, String startAmPm, String endTime, String endAmPm) {
        //Set Date objects
        KSDateTimeFormatter timeFormatter = new KSDateTimeFormatter("hh:mm aa");
        DateTime startingTime = timeFormatter.getFormatter().parseDateTime(startTime + " " + startAmPm);
        DateTime endingTime = timeFormatter.getFormatter().parseDateTime(endTime + " " + endAmPm);
        //Compare and throw exception if start time is after end time
        if (DateTimeComparator.getInstance().compare(startingTime, endingTime) > 0 ) {
            return true;
        } else {
            return false;
        }
    }
}
