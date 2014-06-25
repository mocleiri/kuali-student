package org.kuali.student.ap.framework.context.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.ap.academicplan.dto.LearningPlanInfo;
import org.kuali.student.ap.academicplan.dto.PlanItemInfo;
import org.kuali.student.ap.academicplan.infc.DegreeMapRequirement;
import org.kuali.student.ap.academicplan.infc.Placeholder;
import org.kuali.student.ap.academicplan.infc.PlaceholderInstance;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.academicplan.infc.TypedObjectReference;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.PlaceholderResolver;
import org.kuali.student.ap.framework.context.PlanConstants;
import org.kuali.student.ap.framework.context.PlanHelper;
import org.kuali.student.common.collection.KSCollectionUtils;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.lum.course.infc.Course;

/**
 * Default implementation of the PlanHelper
 */
public class DefaultPlanHelper implements PlanHelper {

	/**
	 * Retrieves the first plan item of type
	 * PlanConstants.Learning_Plan_Type_Plan for the student as the default
	 * plan.
	 * 
	 * @see org.kuali.student.ap.framework.context.PlanHelper
	 * 
	 * @return A single learning plan.
	 */
	@Override
	public LearningPlanInfo getDefaultLearningPlan() {
		LearningPlanInfo defaultPlan = null;
		String studentId = KsapFrameworkServiceLocator.getUserSessionHelper().getStudentId();

		List<LearningPlanInfo> learningPlans = null;
		try {
			learningPlans = KsapFrameworkServiceLocator.getAcademicPlanService().getLearningPlansForStudentByType(
					studentId, PlanConstants.LEARNING_PLAN_TYPE_PLAN,
					KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (Exception e) {
			throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
		}
        if(learningPlans==null || learningPlans.isEmpty()){
            LearningPlanInfo newPlan = new LearningPlanInfo();
            newPlan.setStudentId(studentId);
            newPlan.setStateKey(AcademicPlanServiceConstants.LEARNING_PLAN_ACTIVE_STATE_KEY);
            newPlan.setTypeKey(AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN);
            newPlan.setShared(true);
            RichTextInfo descr = new RichTextInfo("Default Plan For "+KsapFrameworkServiceLocator.getUserSessionHelper().getStudentName(),"Default Plan For "+KsapFrameworkServiceLocator.getUserSessionHelper().getStudentName());
            newPlan.setDescr(descr);
            try{
                defaultPlan = KsapFrameworkServiceLocator.getAcademicPlanService().createLearningPlan(newPlan,KsapFrameworkServiceLocator.getContext().getContextInfo());
            } catch (InvalidParameterException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            } catch (PermissionDeniedException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            } catch (OperationFailedException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            } catch (AlreadyExistsException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            } catch (MissingParameterException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            } catch (DataValidationErrorException e) {
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            }
        }else{
            try {
                defaultPlan =  KSCollectionUtils.getRequiredZeroElement(learningPlans);
            }catch (OperationFailedException e){
                throw new RuntimeException(String.format("Could not fetch plan for user [%s].", studentId), e);
            }
        }

		return defaultPlan;
	}

    /**
     * Gets the id of the term that the planner should display first.
     *
     * @return Term Id
     */
    @Override
    public String getPlannerFirstTermId() {
        return KsapFrameworkServiceLocator.getTermHelper().getFirstTermIdOfCurrentAcademicYear();
    }

    /**
     * Gets the list of Terms to use in the Planner Calendar using a Start Term.
     *
     * @param startTerm - Term that the calendar starts around
     * @return A full List of terms to display in the calendar.
     */
    @Override
    public List<Term> getPlannerCalendarTerms(Term startTerm) {
        Calendar c = Calendar.getInstance();
        Date startDate = startTerm.getStartDate();

        // Check that start term is before the current date, in not use current date as start term
        if(c.getTime().before(startTerm.getStartDate())){
            startDate=c.getTime();
        }

        int futureYears = Integer.parseInt(ConfigContext.getCurrentContextConfig().getProperty( "ks.ap.planner.future.years"));
        c.add(Calendar.YEAR, futureYears);
        List<Term> calendarTerms = KsapFrameworkServiceLocator.getTermHelper().getTermsByDateRange(startDate,c.getTime());
        calendarTerms = KsapFrameworkServiceLocator.getTermHelper().sortTermsByStartDate(calendarTerms,true);
        if(calendarTerms.isEmpty()){
            throw new RuntimeException("No Valid Terms Found for Calendar "+startDate.toString() +" to " + c.getTime().toString());
        }
        Term start = calendarTerms.get(0);
        Term end = calendarTerms.get(calendarTerms.size()-1);
        List<Term> startYear = KsapFrameworkServiceLocator.getTermHelper().getTermsInAcademicYear(new DefaultYearTerm(start.getId(),start.getTypeKey(),start.getStartDate().getYear()));
        List<Term> endYear= KsapFrameworkServiceLocator.getTermHelper().getTermsInAcademicYear(new DefaultYearTerm(end.getId(),end.getTypeKey(),end.getStartDate().getYear()));

        // Sorted in reverse order so terms are added in order.
        startYear = KsapFrameworkServiceLocator.getTermHelper().sortTermsByStartDate(startYear,false);

        endYear = KsapFrameworkServiceLocator.getTermHelper().sortTermsByStartDate(endYear,false);
        Collections.sort(endYear, new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        for(Term t : startYear){
            if(t.getStartDate().compareTo(start.getStartDate())<0){
                calendarTerms.add(0,t);
            }
        }
        for(Term t : endYear){
            if(t.getStartDate().compareTo(end.getStartDate())>0){
                calendarTerms.add(t);
            }
        }
        return calendarTerms;
    }

    @Override
    public List<PlanItem> loadStudentsPlanItemsForCourse(Course course) {
        String studentId = KsapFrameworkServiceLocator.getUserSessionHelper().getStudentId();
        if (studentId == null)
            return new ArrayList<PlanItem>();

        try {
            // Retrieve plan items for the student's default plan
            LearningPlanInfo learningPlan = KsapFrameworkServiceLocator.getPlanHelper().getDefaultLearningPlan();
            List<PlanItemInfo> planItems = KsapFrameworkServiceLocator.getAcademicPlanService().getPlanItemsInPlan(
                    learningPlan.getId(), KsapFrameworkServiceLocator.getContext().getContextInfo());
            List<PlanItem> planItemsForCourse = new ArrayList<PlanItem>();

            // Filter plan items by the course
            for(PlanItem item : planItems){
                if(item.getRefObjectId().equals(course.getId())){
                    planItemsForCourse.add(new PlanItemInfo(item));
                }
            }

            return planItemsForCourse;
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("LP lookup failure ", e);
        } catch (MissingParameterException e) {
            throw new IllegalStateException("LP lookup failure ", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("LP lookup failure ", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("LP lookup permission failure ", e);
        }
    }
    
	@Override
	public Placeholder getPlaceHolder(TypedObjectReference ref) {

		if (ref.getRefObjectType().equals(PlanConstants.REF_TYPE_PLACEHOLDER)) {

			String placeholderId = ref.getRefObjectId();
			Placeholder placeholder;
			try {
				placeholder = KsapFrameworkServiceLocator.getDegreeMapService()
						.getPlaceholder(
								placeholderId,
								KsapFrameworkServiceLocator.getContext()
										.getContextInfo());
				if (PlanConstants.PLACEHOLDER_ERROR.equals(placeholder.getTypeKey()))
						throw new IllegalArgumentException("Placeholder type is error.");
				return placeholder;
			} catch (DoesNotExistException e) {
				return null;
			} catch (InvalidParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder with id [%s].",
						placeholderId), e);
			} catch (MissingParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder with id [%s].",
						placeholderId), e);
			} catch (OperationFailedException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder with id [%s].",
						placeholderId), e);
			}

		}

		return null;
	}

	@Override
	public PlaceholderInstance getPlaceHolderInstance(TypedObjectReference ref) {
		if (ref.getRefObjectType().equals(
				PlanConstants.REF_TYPE_PLACEHOLDER_INSTANCE)) {

			String pid = ref.getRefObjectId();
			PlaceholderInstance pi;
			
			try {
				pi = KsapFrameworkServiceLocator.getDegreeMapService()
						.getPlaceholderInstance(
								pid,
								KsapFrameworkServiceLocator.getContext()
										.getContextInfo());
				return pi;
			} catch (DoesNotExistException e) {
				return null;
			} catch (InvalidParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder instance with id [%s].",
						pid), e);
			} catch (MissingParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder instance with id [%s].",
						pid), e);
			} catch (OperationFailedException e) {
				throw new RuntimeException(String.format(
						"Could not fetch placeholder instance with id [%s].",
						pid), e);
			}

		}

		return null;
	}

	@Override
	public DegreeMapRequirement getRequirement(TypedObjectReference ref) {
		if (ref.getRefObjectType().equals(
				PlanConstants.REF_TYPE_DEGREE_MAP_REQUIREMENT)) {

			String rid = ref.getRefObjectId();
			DegreeMapRequirement dmr;
			try {
				dmr = KsapFrameworkServiceLocator.getDegreeMapService()
						.getRequirement(
								rid,
								KsapFrameworkServiceLocator.getContext()
										.getContextInfo());
				return dmr;
			} catch (DoesNotExistException e) {
				return null;
			} catch (InvalidParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch degree map requirement with id [%s].",
						rid), e);
			} catch (MissingParameterException e) {
				throw new RuntimeException(String.format(
						"Could not fetch degree map requirement with id [%s].",
						rid), e);
			} catch (OperationFailedException e) {
				throw new RuntimeException(String.format(
						"Could not fetch degree map requirement with id [%s].",
						rid), e);
			}

		}

		return null;
	}

	@Override
	public Set<String> getCourseIdsForPlaceHolder(Placeholder ph) {
		// TODO Change to bean-switched version
		PlaceholderResolver phr;

		if (PlanConstants.PLACEHOLDER_COURSE.equals(ph.getTypeKey())) {
			phr = new CoursePlaceholderResolver();
		} else if (PlanConstants.PLACEHOLDER_SEARCH.equals(ph.getTypeKey())) {
			phr = new SearchPlaceholderResolver();
		} else {
			throw new IllegalArgumentException("Placeholder type "
					+ ph.getTypeKey() + " not recognized.");
		}

		try {
			return phr.resolve(ph);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException(
					"Missing requirement parameter for placeholder type "
							+ ph.getTypeKey());
		}
	}
	
}
