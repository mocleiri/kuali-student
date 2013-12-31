package org.kuali.student.ap.planner.support;

import org.apache.log4j.Logger;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.kuali.student.ap.academicplan.service.AcademicPlanServiceConstants;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.PlanConstants;
import org.kuali.student.ap.planner.PlanItemForm;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.ap.academicplan.infc.LearningPlan;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.lum.course.infc.Course;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

public abstract class AbstractPlanItemForm extends UifFormBase implements PlanItemForm {

	private static final long serialVersionUID = -6749034321198329558L;

	private static final Logger LOG = Logger.getLogger(DefaultPlannerForm.class);

	private String uniqueId;

	private String learningPlanId;
	private String termId;
	private String planItemId;
	private String courseId;
	private AcademicPlanServiceConstants.ItemCategory expectedPlanItemCategory;

	private transient LearningPlan learningPlan;
	private transient Term term;
	private transient PlanItem planItem;

	private transient Course course;

	private transient List<PlanItem> existingPlanItems;

	@Override
	public String getLearningPlanId() {
		return learningPlanId;
	}

	public void setLearningPlanId(String learningPlanId) {
		this.learningPlanId = StringUtils.hasText(learningPlanId) ? learningPlanId : null;
		this.learningPlan = null;
		this.planItem = null;
		this.course = null;
		this.existingPlanItems = null;
	}

	@Override
	public LearningPlan getLearningPlan() {
		if (learningPlan == null) {

			if (!StringUtils.hasText(learningPlanId)) {
				learningPlan = KsapFrameworkServiceLocator.getPlanHelper().getDefaultLearningPlan();
			} else {
				try {
					learningPlan = KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlan(learningPlanId,
							KsapFrameworkServiceLocator.getContext().getContextInfo());
				} catch (DoesNotExistException e) {
					LOG.warn("Learning plan " + learningPlanId + " does not exist", e);
				} catch (InvalidParameterException e) {
					LOG.warn("Invalid learning plan ID " + learningPlanId, e);
				} catch (MissingParameterException e) {
					throw new IllegalStateException("LP lookup failure", e);
				} catch (OperationFailedException e) {
					throw new IllegalStateException("LP lookup failure", e);
				}
			}

		}

		return learningPlan;
	}

	@Override
	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = StringUtils.hasText(termId) ? termId : null;
		this.term = null;
	}

	@Override
	public boolean isPlanning() {
		return termId != null && KsapFrameworkServiceLocator.getTermHelper().isPlanning(termId);
	}

	@Override
	public boolean isOfficial() {
		return termId != null && KsapFrameworkServiceLocator.getTermHelper().isOfficial(termId);
	}

	@Override
	public Term getTerm() {
		return term == null && termId != null ? term = KsapFrameworkServiceLocator.getTermHelper().getTerm(termId)
				: term;
	}

	@Override
	public String getPlanItemId() {
		return planItemId;
	}

	public void setPlanItemId(String planItemId) {
		this.planItemId = StringUtils.hasText(planItemId) ? planItemId : null;
		this.planItem = null;
		this.course = null;
		this.existingPlanItems = null;
	}

	@Override
	public PlanItem getPlanItem() {
		if (planItem == null && planItemId != null) {
			try {
				planItem = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItem(planItemId,
						KsapFrameworkServiceLocator.getContext().getContextInfo());
			} catch (DoesNotExistException e) {
				LOG.warn("Plan item " + planItemId + " does not exist", e);
			} catch (InvalidParameterException e) {
				LOG.warn("Invalid plan item ID " + planItemId, e);
			} catch (MissingParameterException e) {
				throw new IllegalStateException("LP lookup failure", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("LP lookup failure", e);
			}
		}
		return planItem;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = StringUtils.hasText(courseId) ? courseId : null;
		this.course = null;
		this.existingPlanItems = null;
	}

	@Override
	public Course getCourse() {
		if (course == null) {
			PlanItem planItem = getPlanItem();
			if (planItem != null) {
				course = KsapFrameworkServiceLocator.getCourseHelper().getCourseInfo(planItem.getRefObjectId());
			} else if (courseId != null) {
				course = KsapFrameworkServiceLocator.getCourseHelper().getCourseInfo(courseId);
			}
		}
		return course;
	}

	@Override
	public List<PlanItem> getExistingPlanItems() {
		if (existingPlanItems == null) {
			LearningPlan plan = getLearningPlan();
			PlanItem planItem = getPlanItem();
			if (planItem != null || courseId != null)
				try {
					existingPlanItems = Collections.<PlanItem> unmodifiableList(KsapFrameworkServiceLocator
							.getAcademicPlanService().getPlanItemsInPlanByRefObjectIdByRefObjectType(plan.getId(),
									planItem == null ? courseId : planItem.getRefObjectId(), PlanConstants.COURSE_TYPE,
									KsapFrameworkServiceLocator.getContext().getContextInfo()));
				} catch (DoesNotExistException e) {
					throw new IllegalArgumentException("LP lookup error", e);
				} catch (InvalidParameterException e) {
					throw new IllegalArgumentException("LP lookup error", e);
				} catch (MissingParameterException e) {
					throw new IllegalArgumentException("LP lookup error", e);
				} catch (OperationFailedException e) {
					throw new IllegalStateException("LP lookup error", e);
				}
		}
		return existingPlanItems;
	}

	@Override
	public AcademicPlanServiceConstants.ItemCategory getExpectedPlanItemCategory() {
		return expectedPlanItemCategory;
	}

	@Override
	public void setExpectedPlanItemCategory(AcademicPlanServiceConstants.ItemCategory category) {
		this.expectedPlanItemCategory = category;
	}

}
