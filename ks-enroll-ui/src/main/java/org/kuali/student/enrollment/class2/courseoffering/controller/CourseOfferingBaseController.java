package org.kuali.student.enrollment.class2.courseoffering.controller;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.maintenance.MaintenanceDocumentController;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.common.uif.form.KSUifMaintenanceDocumentForm;
import org.kuali.student.common.uif.util.KSControllerHelper;
import org.kuali.student.enrollment.class2.courseoffering.dto.CourseOfferingEditWrapper;
import org.kuali.student.enrollment.class2.courseoffering.dto.FormatOfferingWrapper;
import org.kuali.student.enrollment.class2.courseoffering.service.impl.CourseOfferingEditMaintainableImpl;
import org.kuali.student.enrollment.class2.courseoffering.util.CourseOfferingConstants;
import org.kuali.student.r2.core.class1.search.CourseOfferingManagementSearchImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CourseOfferingBaseController extends MaintenanceDocumentController {

    // TODO: KSENROLL-13348 remove this method once KULRICE-12907 is resolved
    protected void checkViewAuthorization(UifFormBase form, String methodToCall) {
    }

    @Override
    protected MaintenanceDocumentForm createInitialForm() {
        return new KSUifMaintenanceDocumentForm();
    }

    /* Returns a ModelAndView for the route()-method to return to original view if there were errors.
     * Should only be called if there are indeed errors.
     */
    protected ModelAndView handleRouteForErrors(DocumentFormBase form) {

        assert ( GlobalVariables.getMessageMap().hasErrors() );

        if (((MaintenanceDocumentForm)form).getDocument().getNewMaintainableObject().getDataObject() instanceof CourseOfferingEditWrapper){
            CourseOfferingEditMaintainableImpl viewHelper = (CourseOfferingEditMaintainableImpl)KSControllerHelper.getViewHelperService(form);
            //Make the format type drop down readonly.. otherwise, we run into display issue when the server returns back error
            CourseOfferingEditWrapper dataObject = (CourseOfferingEditWrapper)((MaintenanceDocumentForm)form).getDocument().getNewMaintainableObject().getDataObject();
            viewHelper.populateFormatNames(dataObject);
        }

        return getModelAndView(form);    // because there were errors, return a MAV to re-nav back to
    }

    @RequestMapping(params = "methodToCall=cancel")
    @Override
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form) {

        DocumentFormBase documentForm = (DocumentFormBase) form;
        super.cancel(documentForm);

        String urlToRedirectTo;
        Properties urlParameters = new Properties();

        // determine which url to redirect to
        String returnLocationFromForm = form.getReturnLocation();
        if( StringUtils.contains( returnLocationFromForm,"viewId=courseOfferingManagementView" )
                || StringUtils.contains( returnLocationFromForm,"pageId=manageTheCourseOfferingPage" ) )
        {
            // wrap with HashMap since viewRequestParameters is set with Collections.unmodifiableMap()
            // in org.kuali.rice.krad.uif.view.View.setViewRequestParameters()
            Map<String, String> additionalParameters = new HashMap<String, String>(form.getViewRequestParameters());
            if ( !returnLocationFromForm.contains("methodToCall=") ) {  // This happens when we display a list of COs and then user click on Manage action
                additionalParameters.put(CourseOfferingManagementSearchImpl.SearchParameters.IS_EXACT_MATCH_CO_CODE_SEARCH, Boolean.TRUE.toString());
            }
            else {
                additionalParameters.put(CourseOfferingManagementSearchImpl.SearchParameters.IS_EXACT_MATCH_CO_CODE_SEARCH, Boolean.FALSE.toString());
            }
            form.setViewRequestParameters(additionalParameters);
            urlToRedirectTo = returnLocationFromForm.replaceFirst("methodToCall=[a-zA-Z0-9]+","methodToCall=show");
        }
        else {
            urlToRedirectTo = returnLocationFromForm;
        }

        String loadNewCO = form.getActionParameters().get( "coId" );
        if( StringUtils.isNotBlank( loadNewCO ) ) {

            urlParameters.put( KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.Maintenance.METHOD_TO_CALL_EDIT );
            urlParameters.put( "courseOfferingInfo.id", loadNewCO );
            urlParameters.put( KRADConstants.DATA_OBJECT_CLASS_ATTRIBUTE, CourseOfferingEditWrapper.class.getName() );
            urlParameters.put( "returnLocation", urlToRedirectTo );

            urlToRedirectTo = CourseOfferingConstants.CONTROLLER_PATH_COURSEOFFERING_EDIT_MAINTENANCE;
            GlobalVariables.getUifFormManager().removeSessionForm(form);
            return performRedirect(form, urlToRedirectTo, urlParameters);
        }

        String newUrl = urlToRedirectTo.replaceAll("growl[^&]*&", "");
        form.setReturnLocation(newUrl);
        return back(form);
    }


    /**
     * This override is specifically for making sure users selected the format types before adding new lines (there is no
     * util method available at view helper).
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addBlankLine")
    @Override
    public ModelAndView addBlankLine(@ModelAttribute("KualiForm") final UifFormBase form) {

        boolean validAction = true;
        if (((MaintenanceDocumentForm)form).getDocument().getNewMaintainableObject().getDataObject() instanceof CourseOfferingEditWrapper){
            CourseOfferingEditWrapper dataObject = (CourseOfferingEditWrapper)((MaintenanceDocumentForm)form).getDocument().getNewMaintainableObject().getDataObject();
            String selectedCollectionPath = form.getActionParamaterValue(UifParameters.SELECTED_COLLECTION_PATH);
            if (StringUtils.endsWith(selectedCollectionPath, "formatOfferingList")) {
                for (FormatOfferingWrapper foWrapper : dataObject.getFormatOfferingList()){
                    if (StringUtils.isBlank(foWrapper.getFormatOfferingInfo().getFormatId())){
                        GlobalVariables.getMessageMap().putError("KS-CourseOfferingEdit-DeliveryFormats",CourseOfferingConstants.COURSEOFFERING_FORMAT_REQUIRED);
                        validAction = false;
                        break;
                    }
                }
            }
        }

        if (validAction){
            return super.addBlankLine(form);
        } else {
            return getModelAndView(form);
        }
    }
}
