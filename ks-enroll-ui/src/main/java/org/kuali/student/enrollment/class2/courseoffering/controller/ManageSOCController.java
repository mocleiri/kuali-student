/**
 * Copyright 2012 The Kuali Foundation Licensed under the
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
 */

package org.kuali.student.enrollment.class2.courseoffering.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.common.util.security.ContextUtils;
import org.kuali.student.enrollment.batch.BatchScheduler;
import org.kuali.student.enrollment.batch.dto.BatchParameter;
import org.kuali.student.enrollment.batch.util.BatchSchedulerConstants;
import org.kuali.student.enrollment.class2.courseoffering.form.ManageSOCForm;
import org.kuali.student.enrollment.class2.courseoffering.service.ManageSOCViewHelperService;
import org.kuali.student.enrollment.class2.courseoffering.util.ManageSocConstants;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.common.util.date.DateFormatters;
import org.kuali.student.r2.common.util.date.KSDateTimeFormatter;
import org.kuali.student.r2.core.acal.dto.TermInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class handles all the request for Managing SOC. This handles requests from ManageSOCView for different SOC state
 * changes and scheduling/publishing events. This controller is mapped to the view defined in <code>ManageSOCView.xml</code>
 *
 * @author Kuali Student Team
 *
 */
@Controller
@RequestMapping(value = "/manageSOC")
public class ManageSOCController extends UifControllerBase {

    private final static Logger LOG = LoggerFactory.getLogger(ManageSOCController.class);
    private BatchScheduler batchScheduler;

    @Override
    protected UifFormBase createInitialForm(@SuppressWarnings("unused") HttpServletRequest request) {
        return new ManageSOCForm();
    }

    @RequestMapping(params = "methodToCall=lockSOC")
    public ModelAndView lockSOC(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        LOG.debug("Locking SOC");

        String dialogId = ManageSocConstants.ConfirmDialogs.LOCK;
        // returns null if no response OR if response was negative
        if (socForm.getDialogResponse(dialogId) == null) {
            return showDialog(dialogId, true, socForm);
        }

        if (socForm.getSocInfo() == null) {
            throw new RuntimeException("SocInfo not exists in the form. Please enter the term code and click on GO button");
        }

        if (!StringUtils.equals(CourseOfferingSetServiceConstants.OPEN_SOC_STATE_KEY, socForm.getSocInfo().getStateKey())) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_INVALID_STATUS_FOR_LOCK);
            return getModelAndView(socForm);
        }

        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);
        viewHelper.lockSOC(socForm);

        return buildModel(socForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=sendApprovedActivitiesToScheduler")
    public ModelAndView sendApprovedActivitiesToScheduler(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                                          @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        if (!StringUtils.equals(CourseOfferingSetServiceConstants.LOCKED_SOC_STATE_KEY, socForm.getSocInfo().getStateKey())) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_INVALID_STATUS_FOR_SCHEDULE);
            return getModelAndView(socForm);
        }

        String dialogId = ManageSocConstants.ConfirmDialogs.MASS_SCHEDULING;
        // returns null if no response OR if response was negative
        if (socForm.getDialogResponse(dialogId) == null) {
            return showDialog(dialogId, true, socForm);
        }

        // start send approved activities to scheduler
        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);
        viewHelper.startMassScheduling(socForm);
        return buildModel(socForm, result, request, response);
    }

    /**
     * This is called when the user enters the term code and hit the Go button.
     */
    @RequestMapping(params = "methodToCall=buildModel")
    public ModelAndView buildModel(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                   @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) {

        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);


        TermInfo term = viewHelper.getTermByCode(socForm.getTermCode());
        if(term!=null){
            socForm.clear();
            socForm.setTermInfo(term);
            viewHelper.buildModel(socForm);
        } else {
            socForm.clear();
        }
        return getModelAndView(socForm);


    }

    @RequestMapping(params = "methodToCall=allowFinalEdits")
    public ModelAndView allowFinalEdits(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                        @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        if (socForm.getSocInfo() == null) {
            throw new RuntimeException("SocInfo not exists in the form. Please enter the term code and click on GO button");
        }

        if (!StringUtils.equals(CourseOfferingSetServiceConstants.SOC_SCHEDULING_STATE_COMPLETED, socForm.getSocInfo().getSchedulingStateKey())) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_INVALID_STATUS_FOR_FINALEDIT);
            return getModelAndView(socForm);
        }

        String dialogId = ManageSocConstants.ConfirmDialogs.FINAL_EDITS;
        // returns null if no response OR if response was negative
        if (socForm.getDialogResponse(dialogId) == null) {
            return showDialog(dialogId, true, socForm);
        }

        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);
        viewHelper.allowSOCFinalEdit(socForm);

        return buildModel(socForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=publishSOC")
    public ModelAndView publishSOC(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                   @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        if (socForm.getSocInfo() == null) {
            throw new RuntimeException("SocInfo not exists in the form. Please enter the term code and click on GO button");
        }

        if (!StringUtils.equals(CourseOfferingSetServiceConstants.FINALEDITS_SOC_STATE_KEY, socForm.getSocInfo().getStateKey())) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_INVALID_STATUS_FOR_PUBLISH);
            return getModelAndView(socForm);
        }

        String dialogId = ManageSocConstants.ConfirmDialogs.MASS_PUBLISHLING;
        // returns null if no response OR if response was negative
        if (socForm.getDialogResponse(dialogId) == null) {
            return showDialog(dialogId, true, socForm);
        }
        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);
        viewHelper.publishSOC(socForm);
        return buildModel(socForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=closeSOC")
    public ModelAndView closeSOC(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                 @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {
        if (socForm.getSocInfo() == null) {
            throw new RuntimeException("SocInfo not exists in the form. Please enter the term code and click on GO button");
        }

        if (!StringUtils.equals(CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY, socForm.getSocInfo().getStateKey())) {
            GlobalVariables.getMessageMap().putError(KRADConstants.GLOBAL_ERRORS, ManageSocConstants.MessageKeys.ERROR_INVALID_STATUS_FOR_CLOSE);
            return getModelAndView(socForm);
        }

        String dialogId = ManageSocConstants.ConfirmDialogs.CLOSE_SET;
        // returns null if no response OR if response was negative
        if (socForm.getDialogResponse(dialogId) == null) {
            return showDialog(dialogId, true, socForm);
        }
        ManageSOCViewHelperService viewHelper = (ManageSOCViewHelperService) KSControllerHelper.getViewHelperService(socForm);
        viewHelper.closeSOC(socForm);

        return buildModel(socForm, result, request, response);
    }

    @RequestMapping(params = "methodToCall=createEOBulkScheduler")
    public ModelAndView createEOBulkScheduler(@ModelAttribute("KualiForm") ManageSOCForm socForm, @SuppressWarnings("unused") BindingResult result,
                                                          @SuppressWarnings("unused") HttpServletRequest request, @SuppressWarnings("unused") HttpServletResponse response) throws Exception {

        KSDateTimeFormatter dateFormatter = DateFormatters.MONTH_DAY_YEAR_DATE_FORMATTER;
        String date = dateFormatter.format(new Date());

        //03/26/2014 02:14 PM
        KSDateTimeFormatter dateTimeFormatter = DateFormatters.MONTH_DAY_YEAR_TIME_DATE_FORMATTER;
        Date dateAndTime = dateTimeFormatter.parse(date + " " + "02:52"  + " " + "AM");

        List<BatchParameter> parameters = new ArrayList<BatchParameter>();
        parameters.add(new BatchParameter("kuali.batch.socId", socForm.getSocInfo().getId()));
        this.getBatchScheduler().schedule("kuali.batch.job.examOffering.slotting", parameters, dateAndTime, ContextUtils.createDefaultContextInfo());
        return super.navigate(socForm, result, request, response);
    }

    private BatchScheduler getBatchScheduler() {
        if (batchScheduler == null) {
            batchScheduler = GlobalResourceLoader.getService(new QName(BatchSchedulerConstants.NAMESPACE, BatchSchedulerConstants.SERVICE_NAME_LOCAL_PART));
        }
        return batchScheduler;
    }


}
