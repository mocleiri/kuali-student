package org.kuali.student.ap.planner.controller;

import org.kuali.rice.krad.uif.view.ViewAuthorizerBase;
import org.kuali.rice.krad.web.controller.MethodAccessible;
import org.kuali.rice.krad.web.controller.extension.KsapControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.ap.academicplan.dto.PlanItemInfo;
import org.kuali.student.ap.academicplan.dto.TypedObjectReferenceInfo;
import org.kuali.student.ap.academicplan.infc.LearningPlan;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.academicplan.infc.TypedObjectReference;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.PlanConstants;
import org.kuali.student.ap.framework.util.KsapHelperUtil;
import org.kuali.student.ap.framework.util.KsapStringUtil;
import org.kuali.student.ap.planner.PlannerForm;
import org.kuali.student.ap.planner.form.PlannerFormImpl;
import org.kuali.student.ap.planner.support.PlanItemControllerHelper;
import org.kuali.student.ap.planner.util.PlanEventUtils;
import org.kuali.student.common.collection.KSCollectionUtils;
import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.exceptions.VersionMismatchException;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.core.comment.dto.CommentInfo;
import org.kuali.student.r2.core.comment.service.CommentService;
import org.kuali.student.r2.lum.course.infc.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Fix under KSAP-265
 * UifControllerBase replaced by KsapControllerBase
 *
 * Controller for the Planner screen and its associated dialogs.
 */
@Controller
@RequestMapping(value = "/planner/**")
public class PlannerController extends KsapControllerBase {

	private static final Logger LOG = LoggerFactory.getLogger(PlannerController.class);

	private static final String PLANNER_FORM = "Planner-FormView";
	private static final String DIALOG_FORM = "PlannerDialog-FormView";

	private static final String QUICKADD_COURSE_PAGE = "planner_add_course_page";
	private static final String EDIT_TERM_NOTE_PAGE = "planner_edit_term_note_page";
	private static final String COURSE_SUMMARY_PAGE = "planner_course_summary_page";
	private static final String COPY_COURSE_PAGE = "planner_copy_course_page";
	private static final String EDIT_PLAN_ITEM_PAGE = "planner_edit_plan_item_page";
	private static final String COPY_PLAN_ITEM_PAGE = "planner_copy_plan_item_page";
	private static final String MOVE_PLAN_ITEM_PAGE = "planner_move_plan_item_page";
	private static final String DELETE_PLAN_ITEM_PAGE = "planner_delete_plan_item_page";
    private static final String ADD_BOOKMARK_PAGE = "bookmark_add_course_page";
    private static final String DELETE_BOOKMARK_PAGE = "bookmark_delete_course_page";
    private static final String ADD_COURSE_PAGE = "course_add_course_page";

	@Override
	protected UifFormBase createInitialForm(HttpServletRequest request) {
		return new PlannerFormImpl();
	}

    /**
     * Entry point for the screen that handles setting up the form for the first display of the screen.
     *
     * Does not appear to be hit at any time.
     */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView startPlanner(@ModelAttribute("KualiForm") PlannerForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response) == null)
			return null;

		UifFormBase uifForm = (UifFormBase) form;
		super.start(uifForm, request, response);

		uifForm.setViewId(PLANNER_FORM);
		uifForm.setView(super.getViewService().getViewById(PLANNER_FORM));

		return getUIFModelAndView(uifForm);
	}

    /**
     * Loads the Term information for the planner.
     * Planner is loaded in two stages the first is the loading of the whole page with the calendar information blank.
     * The second stage refreshes the calendar with js retreiveComponent(componentId, methodToCall) which hits here
     * to load the calendar term data.
     */
    @MethodAccessible
    @RequestMapping(params = "methodToCall=load")
	public ModelAndView loadPlanner(@ModelAttribute("KualiForm") PlannerForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response) == null)
			return null;
        // Set form to load terms
        PlannerFormImpl newForm = (PlannerFormImpl) form;
        newForm.setLoadCalendar(true);

		// Force loading of terms prior to rendering.
        newForm.getTerms();

		UifFormBase uifForm = (UifFormBase) newForm;
        uifForm.getView().setAuthorizer(new ViewAuthorizerBase());
		super.start(uifForm, request, response);

		uifForm.setViewId(PLANNER_FORM);
		uifForm.setView(super.getViewService().getViewById(PLANNER_FORM));

		return getUIFModelAndView(uifForm);
	}

    /**
     * Loads the initial information for any dialog screen opened in the planner.
     */
    @MethodAccessible
    @RequestMapping(params = "methodToCall=startDialog")
	public ModelAndView startDialog(@ModelAttribute("KualiForm") PlannerForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
		if (plan == null)
			return null;

		UifFormBase uifForm = (UifFormBase) form;
		super.start(uifForm, request, response);

		String pageId = uifForm.getPageId();

        // If screen is add course or edit term note valid term information is needed
		boolean termRequired = QUICKADD_COURSE_PAGE.equals(pageId) || EDIT_TERM_NOTE_PAGE.equals(pageId);
		if (termRequired) {
			Term term = form.getTerm();
			if (term == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid term (ID=" + form.getTermId()+")");
				return null;
			}
		}

        // If screen is course summary or copy course valid course information is needed
		boolean courseRequired = COURSE_SUMMARY_PAGE.equals(pageId) || COPY_COURSE_PAGE.equals(pageId) || ADD_COURSE_PAGE.equals(pageId);

        // Retrieve plan item information if an id is returned
		boolean hasPlanItem = form.getPlanItemId() != null;
		if (hasPlanItem) {
			PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
			if (planItem == null)
				return null;

			form.populateFromPlanItem();

		} else if (!termRequired && !courseRequired) {
            // If term or course information are not required then a plan item should be found.
			LOG.warn("Missing plan item for loading page {}", pageId);
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing plan item for loading page " + pageId);
			return null;
		}

        // Retrieve course information if possible
		Course course = form.getCourse();
		if (course == null) {
            if (courseRequired) {
                LOG.warn("Missing course for summary {}", pageId);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing course for summary " + pageId);
                return null;
            }
		} else {
            //Load course related planItems to identify if the course has been bookmarked
            List<PlanItem> planItems = KsapFrameworkServiceLocator.getPlanHelper().loadStudentsPlanItemsForCourse(course);
            form.setBookmarked(KsapFrameworkServiceLocator.getCourseHelper().isCourseBookmarked(course, planItems));
            if (ADD_BOOKMARK_PAGE.equals(pageId)) {
                form.setPlannedMessage(
                        KsapFrameworkServiceLocator.getPlanHelper().createPlanningStatusMessages(planItems));
            }
            //Find terms that already contain this planned course
            List<String> plannedTermIds = KsapFrameworkServiceLocator.getPlanHelper().getTermIdsForPlanItems(planItems);
            form.setPlannedTermIds(plannedTermIds);
        }

		uifForm.setViewId(DIALOG_FORM);
		uifForm.setView(super.getViewService().getViewById(DIALOG_FORM));

		return getUIFModelAndView(uifForm);
	}

    /**
     * Handles submissions from the term note dialog.
     * Formats and updates the term's note in the database.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + EDIT_TERM_NOTE_PAGE)
	public ModelAndView editTermNote(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
		if (plan == null)
			return null;

		String uniqueId = form.getUniqueId();
		if (uniqueId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unique ID note posted");
			return null;
		}

        // Format the text submitted by the user.
		String termId = form.getTermId();
		String termNote = form.getTermNote();
		if (termNote != null)
			termNote = KsapStringUtil.replaceSmartCharacters(termNote);

        // Retrieve the list of term notes for this plan.
		CommentService commentService = KsapFrameworkServiceLocator.getCommentService();
		List<CommentInfo> commentInfos;
		try {
			commentInfos = commentService.getCommentsByRefObject(plan.getId(),
                    PlanConstants.TERM_NOTE_COMMENT_TYPE, KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("Comment lookup failure", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("Comment lookup failure", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("Comment lookup failure", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("Comment lookup failure", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("Comment lookup failure", e);
		}


        // Create replacement rich text with new term note
		RichTextInfo newNote = new RichTextInfo();
		newNote.setFormatted(termNote);
		newNote.setPlain(termNote);

        // Search for existing term note for that term.
        boolean found = false;
		for (CommentInfo comment : commentInfos) {
			String commentAtpId = comment.getAttributeValue(PlanConstants.TERM_NOTE_COMMENT_ATTRIBUTE_ATPID);
			if (termId.equals(commentAtpId)) {
				found = true;
				comment.setCommentText(newNote);
				try {
                    if(StringUtils.isEmpty(termNote)){
                        commentService.deleteComment(comment.getId(),KsapFrameworkServiceLocator.getContext().getContextInfo());
                    }else{
                        // If existing note is found replace the rich text and update it in the database.
                        commentService.updateComment(comment.getId(), comment, KsapFrameworkServiceLocator.getContext()
                                .getContextInfo());
                    }
				} catch (DataValidationErrorException e) {
					throw new IllegalArgumentException("Comment lookup failure", e);
				} catch (DoesNotExistException e) {
					throw new IllegalArgumentException("Comment lookup failure", e);
				} catch (InvalidParameterException e) {
					throw new IllegalArgumentException("Comment lookup failure", e);
				} catch (MissingParameterException e) {
					throw new IllegalArgumentException("Comment lookup failure", e);
				} catch (OperationFailedException e) {
					throw new IllegalStateException("Comment lookup failure", e);
				} catch (PermissionDeniedException e) {
					throw new IllegalStateException("Comment lookup failure", e);
				} catch (ReadOnlyException e) {
					throw new IllegalStateException("Comment lookup failure", e);
				} catch (VersionMismatchException e) {
					throw new IllegalStateException("Comment lookup failure", e);
				}
				break;
			}
		}

        // If no existing note is found create new term note and save it to the database
		if (!found && !StringUtils.isEmpty(termNote)) {
			CommentInfo newComment = new CommentInfo();
			newComment.setCommentText(newNote);
			newComment.setEffectiveDate(KsapHelperUtil.getCurrentDate());
			newComment.setRefObjectId(plan.getId());
			newComment.setRefObjectUri(PlanConstants.TERM_NOTE_COMMENT_TYPE);
			newComment.setTypeKey(PlanConstants.TERM_NOTE_COMMENT_TYPE);
			newComment.setStateKey("ACTIVE");
			AttributeInfo atpIdAttr = new AttributeInfo();
			atpIdAttr.setKey(PlanConstants.TERM_NOTE_COMMENT_ATTRIBUTE_ATPID);
			atpIdAttr.setValue(termId);
			newComment.getAttributes().add(atpIdAttr);
			try {
				commentService.createComment(newComment.getRefObjectId(), newComment.getRefObjectUri(),
						PlanConstants.TERM_NOTE_COMMENT_TYPE, newComment, KsapFrameworkServiceLocator.getContext()
								.getContextInfo());
			} catch (DataValidationErrorException e) {
				throw new IllegalArgumentException("Comment lookup failure", e);
			} catch (DoesNotExistException e) {
				throw new IllegalArgumentException("Comment lookup failure", e);
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException("Comment lookup failure", e);
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException("Comment lookup failure", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("Comment lookup failure", e);
			} catch (PermissionDeniedException e) {
				throw new IllegalStateException("Comment lookup failure", e);
			} catch (ReadOnlyException e) {
				throw new IllegalStateException("Comment lookup failure", e);
			}
		}

        // Create Json strings for displaying action's response and updating the planner screen.
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.updateTermNoteEvent(uniqueId, termNote, eventList);
		PlanEventUtils.sendJsonEvents(true, null, response, eventList);
		return null;
	}

    /**
     * Handles submissions from the quick add course dialog.
     * Validates the course and addes it to the students plan.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + QUICKADD_COURSE_PAGE)
	public ModelAndView addPlanItem(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve student's plan
		LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
        JsonObjectBuilder eventList = Json.createObjectBuilder();
		if (plan == null)
			return null;

		String termId = form.getTermId();

		String courseCd = form.getCourseCd();
		if (!StringUtils.hasText(courseCd)) {
			PlanEventUtils.sendJsonEvents(false, "Course code required", response, eventList);
			return null;
		}

        // Retrieve course information using the course code entered by the user
		Course course=null;
		try {
			List<Course> courses = KsapFrameworkServiceLocator.getCourseHelper().getCoursesByCode(courseCd);
			if (courses == null || courses.isEmpty()) {
				PlanEventUtils.sendJsonEvents(false, "Course " + courseCd + " not found", response, eventList);
				return null;
			}
            for(Course courseTemp : courses){
                if(courseTemp.getStateKey().equals("Active")){
                    course=courseTemp;
                    break;
                }
            }
            if (course == null) {
                PlanEventUtils.sendJsonEvents(false, "Course " + courseCd + " not active", response, eventList);
                return null;
            }
		} catch (IllegalArgumentException e) {
			LOG.error(String.format("Invalid course code %s", courseCd), e);
			PlanEventUtils.sendJsonEvents(false, "Course " + courseCd + " not found", response, eventList);
			return null;
		}

        // Add the course to the plan
		finishAddCourse(plan, form, course, termId, response);
		return null;
	}

    /**
     * Handles the submissions from the copy course dialog (when copying from a completed plan item).
     * Copies the course for a plan item into a new item under a new term.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + COPY_COURSE_PAGE)
	public ModelAndView copyCourse(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve the student's plan
		LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
		if (plan == null)
			return null;

		String termId = form.getTargetTermId();

        // Retrieve Coure information
		Course course = form.getCourse();
		if (course == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course " + form.getCourseId() + " not found");
			return null;
		}

        // Add course to plan
		finishAddCourse(plan, form, course, termId, response);
		return null;
	}

    /**
     * Handles the submissions from the edit plan item dialog.
     * Changes the information for a single plan item.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + EDIT_PLAN_ITEM_PAGE)
	public ModelAndView editPlanItem(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String expectedTermId = form.getTermId();
		boolean creditEdited = false;
		boolean notesEdited = false;
		boolean newNoteFlag = false;

		if (expectedTermId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing term ID");
			return null;
		}

		PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
		if (planItem == null)
			return null;

		PlanItemInfo planItemInfo = new PlanItemInfo(planItem);
		RichTextInfo previousDescr = planItemInfo.getDescr();
		if (StringUtils.hasText(form.getCourseNote())) {

            // Copy new course note into the plan item
			RichTextInfo descr = new RichTextInfo();
			descr.setPlain(form.getCourseNote());
			descr.setFormatted(form.getCourseNote());
			planItemInfo.setDescr(descr);

            // Determine if course note has been edited
            String oldFormatted;
            if(previousDescr == null)oldFormatted=null;
			else oldFormatted = previousDescr.getFormatted();
			String newFormatted = descr.getFormatted();
			if(!newFormatted.equals(oldFormatted)){
				if(previousDescr==null || previousDescr.getFormatted() == null){
					newNoteFlag = true;
				}
				notesEdited = true;
			}
		} else
			planItemInfo.setDescr(null);
		
		BigDecimal oldCredit = planItemInfo.getCredits();
		
		LOG.debug("In PlannerController: oldCredit is {}", oldCredit);
		LOG.debug("form.getCreditsForPlanItem() is {}", form.getCreditsForPlanItem());
		
		planItemInfo.setCredit(form.getCreditsForPlanItem());
		BigDecimal newCredit = planItemInfo.getCredits();
		
		LOG.debug("In PlannerController: newCredit is " + newCredit);

        // Determine if Credit has be changed
		if (oldCredit == null) {
			if (newCredit != null)
				creditEdited = true;
		} else {
			if (newCredit == null || oldCredit.compareTo(newCredit) != 0) {
				creditEdited = true;
			}
		}

        // Update the plan item in the database
	    planItemInfo = (PlanItemInfo)KsapFrameworkServiceLocator.getPlanHelper().updatePlanItem(planItemInfo);

        // Construct json events for updating the planner screen
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.updatePlanItemEvent(form.getUniqueId(), planItemInfo, eventList);
        try{
            eventList = PlanEventUtils.updateTotalCreditsEvent(true, KSCollectionUtils.getRequiredZeroElement(planItemInfo.getPlanTermIds()), eventList);
        }catch(OperationFailedException e){
            LOG.warn("Unable to update total credits", e);
        }

        // Create json strings for displaying action's response and send those updating the planner screen.
		if(notesEdited && creditEdited){
			PlanEventUtils.sendJsonEvents(true, "Changes to the notes and credits for " + form.getTerm().getName() +" "+ form.getCourse().getCode() +" is saved", response, eventList);
		}else if (notesEdited){
			if(newNoteFlag){
				PlanEventUtils.sendJsonEvents(true, "Note added to " + form.getTerm().getName() +" "+ form.getCourse().getCode(), response, eventList);
			}else{
				PlanEventUtils.sendJsonEvents(true, "Note edited for " + form.getTerm().getName() +" "+ form.getCourse().getCode() , response, eventList);
			}
		}else if(creditEdited){
			PlanEventUtils.sendJsonEvents(true, "Changes to the credits for " + form.getTerm().getName() +" "+ form.getCourse().getCode() +" is saved", response, eventList);
		}else{
			PlanEventUtils.sendJsonEvents(true, null, response, eventList);
		}
		return null;
	}

    /**
     * Handles the submissions from the copy plan item dialog.
     * Copies the course for a plan item into a new item under a new term.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + COPY_PLAN_ITEM_PAGE)
	public ModelAndView copyPlanItem(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve student's plan.
		LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
		if (plan == null)
			return null;

        // Retrieve plan item to be copied
		PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
		if (planItem == null)
			return null;

		String termId = form.getTargetTermId();

        // Retrieve course information
		Course course = form.getCourse();
		if (course == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Course " + form.getCourseId() + " not found");
			return null;
		}
		assert course.getId().equals(planItem.getRefObjectId());

        // Populate addition information need to add a plan item from the one to copy.
		form.populateFromPlanItem();

        // Add the course to the plan
		finishAddCourse(plan, form, course, termId, response);
		return null;
	}

    /**
     * Handles the submissions from the move plan item dialog.
     * Changes the term that an existing plan item is in to another one.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + MOVE_PLAN_ITEM_PAGE)
	public ModelAndView movePlanItem(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String expectedTermId = form.getTermId();
		if (expectedTermId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing term ID");
			return null;
		}

		String termId = form.getTargetTermId();

        // Retrieve term information
		Term term = KsapFrameworkServiceLocator.getTermHelper().getTerm(termId);

        // Retrieve plan item information
		PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
		if (planItem == null)
			return null;

        // Replaces the existing term information with the new term
		PlanItemInfo planItemInfo = new PlanItemInfo(planItem);
		List<String> planTermIds = new ArrayList<String>(1);
		planTermIds.add(termId);
		planItemInfo.setPlanTermIds(planTermIds);

        // Save updated plan item
		planItemInfo = (PlanItemInfo) KsapFrameworkServiceLocator.getPlanHelper().updatePlanItem(planItemInfo);

        // Create json strings for displaying action's response and updating the planner screen.
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.makeRemoveEvent(form.getUniqueId(), planItem, eventList);
        eventList = PlanEventUtils.makeAddEvent(planItemInfo, eventList);
        eventList = PlanEventUtils.updateTotalCreditsEvent(false, expectedTermId, eventList);
        eventList = PlanEventUtils.updateTotalCreditsEvent(true, termId, eventList);
        PlanEventUtils.sendJsonEvents(true, "Course " + form.getCourse().getCode() + " moved to " + term.getName(),
				response, eventList);
		return null;
	}

    /**
     * Handles the submissions from the delete plan dialog.
     * Removes a plan item from a students plan and deletes it from the database.
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + DELETE_PLAN_ITEM_PAGE)
	public ModelAndView deletePlanItem(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String expectedTermId = form.getTermId();
		if (expectedTermId == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing term ID");
			return null;
		}
		Term term = KsapFrameworkServiceLocator.getTermHelper().getTerm(expectedTermId);

        // Retrieve valid plan item
		PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
		if (planItem == null)
			return null;

        // Delete plan item from the database
		KsapFrameworkServiceLocator.getPlanHelper().removePlanItem(planItem.getId());

        // Create json strings for displaying action's response and updating the planner screen.
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.makeRemoveEvent(form.getUniqueId(), planItem, eventList);
        eventList = PlanEventUtils.updateTotalCreditsEvent(true, term.getId(), eventList);
		PlanEventUtils.sendJsonEvents(true, "Course " + form.getCourse().getCode() + " removed from " + term.getName(),
				response, eventList);
		return null;
	}

    /**
     * Handles changing a plan item's type.
     * Changes a plan item from its current type (planned, backup) to a new type (planned, backup)
     */
    @MethodAccessible
	@RequestMapping(method = RequestMethod.POST, params = "methodToCall=updatePlanItemCategory")
	public ModelAndView updatePlanItemType(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve a valid plan item
		PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
		if (planItem == null)
			return null;

		PlanItemInfo planItemInfo = new PlanItemInfo(planItem);


        // Set the new category for the item
        planItemInfo.setCategory(form.isBackup() ? AcademicPlanServiceConstants.ItemCategory.BACKUP
                : AcademicPlanServiceConstants.ItemCategory.PLANNED);

        // Update the plan item in the database.
		planItemInfo = (PlanItemInfo)KsapFrameworkServiceLocator.getPlanHelper().updatePlanItem(planItemInfo);

        // Create json strings for displaying action's response and updating the planner screen.
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.makeRemoveEvent(form.getUniqueId(), planItem, eventList);
        eventList = PlanEventUtils.makeAddEvent(planItemInfo, eventList);
        eventList = PlanEventUtils.updateTotalCreditsEvent(true, form.getTermId(), eventList);
		PlanEventUtils.sendJsonEvents(true, "Course " + form.getCourse().getCode() + " updated", response, eventList);
		return null;
	}

    /**
     * Helps with adding courses to the student's plan.
     * Creates a new or retrieves an existing learning plan item and fills in the proper information before
     * saving it to the database.
     *
     * @param plan - The student's learning plan
     * @param form - Form containing all information entered for the new plan item
     * @param course - Course plan item is being created for
     * @param termId - Id of the term course is being added to
     * @param response - Service response object
     * @throws IOException -
     * @throws ServletException
     */
    private void finishAddCourse(LearningPlan plan, PlannerForm form, Course course, String termId,
                                 HttpServletResponse response) throws IOException, ServletException {
        AcademicPlanServiceConstants.ItemCategory category = form.isBackup() ? AcademicPlanServiceConstants.ItemCategory.BACKUP
                : AcademicPlanServiceConstants.ItemCategory.PLANNED;
        Term term = KsapFrameworkServiceLocator.getTermHelper().getTerm(termId);
        JsonObjectBuilder eventList = Json.createObjectBuilder();


        PlanItem planItemInfo;
        List<String> planTermIds = new ArrayList<String>(1);
        planTermIds.add(termId);
        TypedObjectReference planItemRef = new TypedObjectReferenceInfo(PlanConstants.COURSE_TYPE, course.getVersion().getVersionIndId());
        List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
        try {
            planItemInfo = KsapFrameworkServiceLocator.getPlanHelper().addPlanItem(plan.getId(), category,
                    form.getCourseNote(),form.getCreditsForPlanItem(course),planTermIds,planItemRef,attributes);
        } catch (AlreadyExistsException e) {
            LOG.warn(String.format("Course %s is already planned for %s", course.getCode(), term.getName()), e);
            PlanEventUtils.sendJsonEvents(false,
                    "Course " + course.getCode() + " is already planned for " + term.getName(), response, eventList);
            return;
        }

        // Create json strings for displaying action's response and updating the planner screen.
        eventList = PlanEventUtils.makeAddEvent(planItemInfo, eventList);
        eventList = PlanEventUtils.updateTotalCreditsEvent(true, termId, eventList);
        eventList = PlanEventUtils.makeUpdateBookmarkTotalEvent(planItemInfo, eventList);

        List<PlanItem> planItems = KsapFrameworkServiceLocator.getPlanHelper().loadStudentsPlanItemsForCourse(course);
        eventList = PlanEventUtils.makeUpdatePlanItemStatusMessage(planItems, eventList);
        PlanEventUtils.sendJsonEvents(true, course.getCode() + " was successfully added to your plan.",
                response, eventList);
    }

    /**
     * Handles submissions from the bookmark add dialog.
     * Validates the course and adds it to the students plan.
     */
    @MethodAccessible
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + ADD_BOOKMARK_PAGE)
    public ModelAndView addBookmarkedCourse(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve student's plan
        LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        if (plan == null)
            return null;

        String termId = form.getTermId();

        String courseId = form.getCourseId();
        if (!StringUtils.hasText(courseId)) {
            PlanEventUtils.sendJsonEvents(false, "Course id required", response, eventList);
            return null;
        }

        // Retrieve course information using the course code entered by the user
        Course course;
        try {
            course = KsapFrameworkServiceLocator.getCourseService().getCourse(courseId, KsapFrameworkServiceLocator.getContext().getContextInfo());
        } catch (PermissionDeniedException e) {
            throw new IllegalArgumentException("Course service failure", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("Course service failure", e);
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("Course service failure", e);
        } catch (OperationFailedException e) {
            throw new IllegalArgumentException("Course service failure", e);
        } catch (DoesNotExistException e) {
            throw new IllegalArgumentException("Course service failure", e);
        }

        // Add the course to the plan
        finishAddCourse(plan, form, course, termId, response);
        return null;
    }

    /**
     * Handles the submissions from the bookmark delete dialog.
     * Removes a plan item from a students bookmarks and deletes it from the database.
     */
    @MethodAccessible
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + DELETE_BOOKMARK_PAGE)
    public ModelAndView deleteBookmark(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
                                       HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve valid plan item
        PlanItem planItem = PlanItemControllerHelper.getValidatedPlanItem(form, request, response);
        if (planItem == null)
            return null;

        // Delete plan item from the database
        KsapFrameworkServiceLocator.getPlanHelper().removePlanItem(planItem.getId());

        // Create json strings for displaying action's response and updating the planner screen.
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        eventList = PlanEventUtils.makeRemoveEvent(form.getUniqueId(), planItem, eventList);
        eventList = PlanEventUtils.makeUpdateBookmarkTotalEvent(planItem, eventList);
        PlanEventUtils.sendJsonEvents(true, "Course " + form.getCourse().getCode() + " removed from Bookmarks",
                response, eventList);
        return null;
    }

    /**
     * Handles submissions from the course add dialog.
     * Validates the course and adds it to the students plan.
     */
    @MethodAccessible
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=" + ADD_COURSE_PAGE)
    public ModelAndView addCourse(@ModelAttribute("KualiForm") PlannerForm form, BindingResult result,
                                            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Retrieve student's plan
        LearningPlan plan = PlanItemControllerHelper.getAuthorizedLearningPlan(form, request, response);
        JsonObjectBuilder eventList = Json.createObjectBuilder();
        if (plan == null)
            return null;

        String termId = form.getTermId();

        String courseId = form.getCourseId();
        if (!StringUtils.hasText(courseId)) {
            PlanEventUtils.sendJsonEvents(false, "Course id required", response, eventList);
            return null;
        }

        // Retrieve course information using the course code entered by the user
        Course course = KsapFrameworkServiceLocator.getCourseHelper().getCurrentVersionOfCourse(courseId);
        String responseMessage =  KsapFrameworkServiceLocator.getCourseHelper().validateCourseForAdd(course);
        if(responseMessage !=null){
            PlanEventUtils.sendJsonEvents(false, responseMessage, response, eventList);
            return null;
        }

        // Add the course to the plan
        finishAddCourse(plan, form, course, termId, response);
        return null;
    }
}
