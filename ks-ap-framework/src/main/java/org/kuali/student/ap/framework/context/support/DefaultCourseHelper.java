package org.kuali.student.ap.framework.context.support;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateFactory;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.student.ap.academicplan.constants.AcademicPlanServiceConstants;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.coursesearch.CourseSearchItem;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.CourseHelper;
import org.kuali.student.ap.framework.util.KsapHelperUtil;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingDisplayInfo;
import org.kuali.student.enrollment.courseoffering.dto.ActivityOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.enrollment.courseoffering.dto.FormatOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.core.versionmanagement.dto.VersionDisplayInfo;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.course.infc.Course;
import org.kuali.student.r2.lum.course.service.CourseService;
import org.kuali.student.r2.lum.util.constants.CluServiceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.or;

//
public class DefaultCourseHelper implements CourseHelper, Serializable {

	private static final long serialVersionUID = 8000868050066661992L;

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCourseHelper.class);
	private static final CourseMarkerKey COURSE_MARKER_KEY = new CourseMarkerKey();

	private static class CourseMarkerKey {
		@Override
		public int hashCode() {
			return CourseMarkerKey.class.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof CourseMarkerKey;
		}
	}

	private static class CourseTermKey {
		private final String courseId;
		private final String termId;

		private CourseTermKey(String courseId, String termId) {
			super();
			this.courseId = courseId;
			this.termId = termId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
			result = prime * result + ((termId == null) ? 0 : termId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CourseTermKey other = (CourseTermKey) obj;
			if (courseId == null) {
				if (other.courseId != null)
					return false;
			} else if (!courseId.equals(other.courseId))
				return false;
			if (termId == null) {
				if (other.termId != null)
					return false;
			} else if (!termId.equals(other.termId))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "CourseTermKey [courseId=" + courseId + ", termId=" + termId + "]";
		}
	}

	private static class CourseMarker {
		private Map<String, CourseInfo> coursesById = new java.util.HashMap<String, CourseInfo>();
		private Map<CourseTermKey, List<ActivityOfferingDisplayInfo>> activityOfferingDisplaysByCourseAndTerm = new java.util.HashMap<CourseTermKey, List<ActivityOfferingDisplayInfo>>();

		private void cache(CourseInfo courseInfo) {
			if (courseInfo != null) {
				coursesById.put(courseInfo.getId(), courseInfo);
			}
		}

		private void cache(List<CourseInfo> courseInfos) {
			if (courseInfos != null)
				for (CourseInfo courseInfo : courseInfos)
					cache(courseInfo);
		}
	}

	private static CourseMarker getCourseMarker() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			CourseMarker rv = (CourseMarker) TransactionSynchronizationManager.getResource(COURSE_MARKER_KEY);
			if (rv == null) {
				TransactionSynchronizationManager.bindResource(COURSE_MARKER_KEY, rv = new CourseMarker());
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCompletion(int status) {
						TransactionSynchronizationManager.unbindResourceIfPossible(COURSE_MARKER_KEY);
					}
				});
			}
			return rv;
		} else {
			return new CourseMarker();
		}
	}

	@Override
	public void frontLoad(List<String> courseIds, String... termId) {
		StringBuilder sb = null;
		if (LOG.isDebugEnabled())
			sb = new StringBuilder("Front load courses " + courseIds);
		try {

			CourseService courseService = KsapFrameworkServiceLocator.getCourseService();
			ContextInfo context = KsapFrameworkServiceLocator.getContext().getContextInfo();
			CourseMarker cm = getCourseMarker();
			List<CourseInfo> courses = new ArrayList<CourseInfo>();
            for(String courseId : courseIds){
                courses.add(courseService.getCourse(courseId,context));
            }
			cm.cache(courses);

			if (sb != null) {
				for (CourseInfo course : courses) {
					sb.append("\n    ");
					sb.append(course.getCode());
					sb.append(" ");
					sb.append(course.getCourseTitle());
				}
			}

			if (termId == null || termId.length == 0) {
				if (sb != null) {
					sb.append("\nNo terms... ");
					sb.append(termId);
					LOG.debug(sb.toString());
				}
				return;
			}

			CourseOfferingService courseOfferingService = KsapFrameworkServiceLocator.getCourseOfferingService();
			QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(PredicateFactory.and(
					PredicateFactory.in("cluId", courseIds.toArray()), PredicateFactory.in("atpId", termId)));
			List<CourseOfferingInfo> cos = courseOfferingService.searchForCourseOfferings(query, context);
			if (cos == null)
				return;
			if (sb != null)
				sb.append("\nCourse Offerings:");

			Map<String, CourseTermKey> coid2key = new java.util.HashMap<String, CourseTermKey>();
			List<String> additionalCourseIds = new java.util.LinkedList<String>();
			for (CourseOfferingInfo co : cos) {
				Course c = cm.coursesById.get(co.getCourseId());
				if (c == null)
					additionalCourseIds.add(co.getCourseId());
				if (sb != null) {
					sb.append("\n    ");
					sb.append(co.getId());
					if (c == null)
						sb.append(" course queued");
					else {
						sb.append(" -> ");
						sb.append(c.getCode());
					}
				}
				CourseTermKey k = new CourseTermKey(co.getCourseId(), co.getTermId());
				coid2key.put(co.getId(), k);
				List<ActivityOfferingDisplayInfo> orm = cm.activityOfferingDisplaysByCourseAndTerm.remove(k);
				LOG.warn("Cleared stale AO display key {} {}", k, orm);
			}

			if (!additionalCourseIds.isEmpty()){
                List<CourseInfo> additionalCourses = new ArrayList<CourseInfo>();
                for(String courseId : additionalCourseIds){
                    courses.add(courseService.getCourse(courseId,context));
                }
				cm.cache(additionalCourses);
				if (sb != null) {
					sb.append("\nAdditional Courses: ");
					for (Course ac : additionalCourses) {
						sb.append("\n    ");
						sb.append(ac.getCode());
					}
				}
			}

			List<FormatOfferingInfo> fos = courseOfferingService.searchForFormatOfferings(query, context);
			Map<String, String> foid2coid = new java.util.HashMap<String, String>();
			if (sb != null)
				sb.append("\nFormat Offerings:");
			for (FormatOfferingInfo fo : fos) {
				CourseTermKey k = coid2key.get(fo.getCourseOfferingId());
				if (sb != null) {
					sb.append("\n    ");
					sb.append(fo.getId());
					sb.append(" -> ");
					sb.append(fo.getCourseOfferingId());
					sb.append(" -> ");
					sb.append(k.courseId);
				}
				foid2coid.put(fo.getId(), fo.getCourseOfferingId());
			}

			List<String> aoids = courseOfferingService.searchForActivityOfferingIds(query, context);
			List<ActivityOfferingDisplayInfo> aods = courseOfferingService.getActivityOfferingDisplaysByIds(aoids,
					context);
			if (sb != null)
				sb.append("\nActivity Offerings:");
			for (ActivityOfferingDisplayInfo aodi : aods) {
				String coid = foid2coid.get(aodi.getFormatOfferingId());
				assert coid != null : aodi.getId() + " " + aodi.getFormatOfferingId();
				CourseTermKey k = coid2key.get(coid);
				assert k != null : aodi.getId() + " " + aodi.getFormatOfferingId() + " " + coid;
				List<ActivityOfferingDisplayInfo> aodByCo = cm.activityOfferingDisplaysByCourseAndTerm.get(k);
				if (aodByCo == null)
					cm.activityOfferingDisplaysByCourseAndTerm.put(k,
							aodByCo = new java.util.LinkedList<ActivityOfferingDisplayInfo>());
				aodByCo.add(aodi);
				if (LOG.isDebugEnabled()) {
					sb.append("\n    ");
					sb.append(aodi.getId());
					sb.append(" -> ");
					sb.append(aodi.getFormatOfferingId());
					sb.append(" -> ");
					sb.append(coid);
					sb.append(" -> ");
					sb.append(k.courseId);
				}
			}

		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup error", e);
		} finally {
			if (sb != null)
				LOG.debug(sb.toString());
		}
	}

    @Override
	public CourseInfo getCourseInfo(String courseId) {
		CourseMarker cm = getCourseMarker();
		// call through service locator to facilitate proxying delegate override of getCurrentVersionIdOfCourse
		String verifiedCourseId = KsapFrameworkServiceLocator.getCourseHelper().getCurrentVersionIdOfCourse(courseId);
		CourseInfo rv = cm.coursesById.get(verifiedCourseId);
		if (rv == null)
			try {
				cm.coursesById.put(
						courseId,
						rv = KsapFrameworkServiceLocator.getCourseService().getCourse(verifiedCourseId,
								KsapFrameworkServiceLocator.getContext().getContextInfo()));
			} catch (DoesNotExistException e) {
				return null;
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException("CLU lookup error", e);
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException("CLU lookup error", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("CLU lookup error", e);
			} catch (PermissionDeniedException e) {
				throw new IllegalStateException("CLU lookup error", e);
			}
		return rv;
	}

	@Override
	public List<ActivityOfferingInfo> getActivityOfferings(String courseId) {
		// TODO: Implement for KS. Current supported via IU override for iGPS 2.0.
		throw new UnsupportedOperationException();
	}

	@Override
	public List<ActivityOfferingDisplayInfo> getActivityOfferingDisplaysByCourseAndTerm(String courseId, String termId) {
		CourseMarker cm = getCourseMarker();
		CourseTermKey k = new CourseTermKey(courseId, termId);
		List<ActivityOfferingDisplayInfo> rv = cm.activityOfferingDisplaysByCourseAndTerm.get(k);
		if (rv == null)
			try {
				CourseOfferingService courseOfferingService = KsapFrameworkServiceLocator.getCourseOfferingService();
				ContextInfo context = KsapFrameworkServiceLocator.getContext().getContextInfo();
				rv = new java.util.LinkedList<ActivityOfferingDisplayInfo>();
				StringBuilder msg = null;
				if (LOG.isDebugEnabled())
					msg = new StringBuilder("CO cache miss ").append(courseId).append(" ").append(termId);
				for (CourseOfferingInfo co : courseOfferingService.getCourseOfferingsByCourseAndTerm(courseId, termId,
						context)) {
					if (msg != null)
						msg.append("\n  CO ").append(co.getId());
					List<ActivityOfferingDisplayInfo> aol = KsapFrameworkServiceLocator.getCourseOfferingService()
							.getActivityOfferingDisplaysForCourseOffering(co.getId(),
									KsapFrameworkServiceLocator.getContext().getContextInfo());
					for (ActivityOfferingDisplayInfo aodi : aol) {
						if (msg != null)
							msg.append("\n    AO ").append(aodi.getId());
						rv.add(aodi);
					}
				}
				if (msg != null)
					LOG.debug(msg.toString());
				cm.activityOfferingDisplaysByCourseAndTerm.put(k, rv);
			} catch (DoesNotExistException e) {
				throw new IllegalArgumentException("CO lookup failure");
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException("CO lookup failure");
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException("CO lookup failure");
			} catch (OperationFailedException e) {
				throw new IllegalStateException("CO lookup failure");
			} catch (PermissionDeniedException e) {
				throw new IllegalStateException("CO lookup failure");
			}
		return rv;
	}

	@Override
	public List<String> getScheduledTermsForCourse(Course course) {
		ContextInfo ctx = KsapFrameworkServiceLocator.getContext().getContextInfo();
        List<String> scheduledTerms = new java.util.LinkedList<String>();
		try {
            List<CourseOfferingInfo> offerings = KsapFrameworkServiceLocator.getCourseOfferingService().getCourseOfferingsByCourse(course.getId(),ctx);

            List<Term> terms = new ArrayList<Term>();
            List<Term> currentScheduled = KsapFrameworkServiceLocator.getTermHelper().getCurrentTermsWithPublishedSOC();
            List<Term> futureScheduled = KsapFrameworkServiceLocator.getTermHelper().getFutureTermsWithPublishedSOC();
            if(currentScheduled!=null) terms.addAll(currentScheduled);
            if(futureScheduled!=null) terms.addAll(futureScheduled);

            for(CourseOfferingInfo offering : offerings){
                for(Term t : terms) {
                    if(offering.getTermId().equals(t.getId())){
                        if(!scheduledTerms.contains(t.getId())){
                            scheduledTerms.add(t.getId());
                        }
                    }
                }
            }
			return scheduledTerms;
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup failure", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup failure", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup failure", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup failure", e);
		} catch (DoesNotExistException e){
            throw new IllegalStateException("CO lookup failure", e);
        }
	}

    @Override
    public List<CourseOfferingInfo> getCourseOfferingsForCourses(List<CourseSearchItem> courses){
        List<CourseOfferingInfo> offerings = new ArrayList<CourseOfferingInfo>();
        try {
            // Load list of terms to find offerings in
            List<Term> terms = new ArrayList<Term>();
            List<Term> currentScheduled = KsapFrameworkServiceLocator.getTermHelper().getCurrentTermsWithPublishedSOC();
            List<Term> futureScheduled = KsapFrameworkServiceLocator.getTermHelper().getFutureTermsWithPublishedSOC();
            if(currentScheduled!=null) terms.addAll(currentScheduled);
            if(futureScheduled!=null) terms.addAll(futureScheduled);
            // Search for all course offerings of search results in terms
            Predicate termPredicates[] = KsapHelperUtil.getTermPredicates(terms);
            List<CourseSearchItem> tempCourses = new ArrayList<CourseSearchItem>(courses);
            Predicate coursePredicates[] = KsapHelperUtil.getCoursePredicates(tempCourses);
            QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(or(coursePredicates),
                    or(termPredicates), equal("luiType", LuiServiceConstants.COURSE_OFFERING_TYPE_KEY));
            offerings = KsapFrameworkServiceLocator.getCourseOfferingService()
                    .searchForCourseOfferings(query,KsapFrameworkServiceLocator.getContext().getContextInfo());
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        }

        return offerings;
    }

    @Override
    public List<CourseOfferingInfo> getCourseOfferingsForCoursesAndTerms(List<String> courseIds, List<Term> terms){
        List<CourseOfferingInfo> offerings = new ArrayList<CourseOfferingInfo>();
        try {
            // Load list of terms to find offerings in
            // Search for all course offerings of search results in terms
            Predicate termPredicates[] = KsapHelperUtil.getTermPredicates(terms);
            List<String> tempCourseIds = new ArrayList<String>(courseIds);
            Predicate coursePredicates[] = KsapHelperUtil.getCourseIdPredicates(tempCourseIds);
            QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(or(coursePredicates),
                    or(termPredicates), equal("luiType", LuiServiceConstants.COURSE_OFFERING_TYPE_KEY));
            offerings = KsapFrameworkServiceLocator.getCourseOfferingService()
                    .searchForCourseOfferings(query,KsapFrameworkServiceLocator.getContext().getContextInfo());
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        }

        return offerings;
    }



    /*
     *  - Only display Last Offered if it has been offered within the last 10 years.
     *  - If the course has not been offered within the last 10 years, return null
     */
	@Override
	public Term getLastOfferedTermForCourse(Course course) {
		ContextInfo ctx = KsapFrameworkServiceLocator.getContext().getContextInfo();
        Date currentDate = KsapHelperUtil.getCurrentDate();
        Term lastOfferedTerm = null;
        Term termInfo;

		try {
            List<CourseOfferingInfo> offerings = KsapFrameworkServiceLocator.getCourseOfferingService().getCourseOfferingsByCourse(course.getId(),ctx);
            for (CourseOfferingInfo co: offerings){                
                //check if the CO state is offered
                if (co.getStateKey().equalsIgnoreCase(LuiServiceConstants.LUI_CO_STATE_OFFERED_KEY)){            
                    termInfo = KsapFrameworkServiceLocator.getAcademicCalendarService().getTerm(co.getTermId(), ctx);
                    //check if the term is NOT a future or a current term and within the last 10 years
                    Calendar currentCal = Calendar.getInstance();
                    currentCal.setTime(currentDate);
                    Calendar oldCal = Calendar.getInstance();
                    oldCal.set(currentCal.get(Calendar.YEAR)-10, currentCal.get(Calendar.MONTH), currentCal.get(Calendar.DAY_OF_MONTH));
                    Date oldDate = oldCal.getTime();

                    if (termInfo.getEndDate().before(currentDate) && termInfo.getEndDate().after(oldDate) )   {
                        if (lastOfferedTerm == null){
                            lastOfferedTerm = termInfo;
                        }
                        else if(lastOfferedTerm.getStartDate().before(termInfo.getStartDate()) && lastOfferedTerm.getEndDate().after(termInfo.getEndDate())) {
                                lastOfferedTerm = termInfo;
                        }
                    }
                }
            }
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup failure", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup failure", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup failure", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup failure", e);
		} catch (DoesNotExistException e){
            throw new IllegalStateException("CO lookup failure", e);
        }

        return lastOfferedTerm;

	}


	@Override
	public List<Course> getCoursesByCode(String courseCd) {
		try {
            String cleanedCourseCd = StringUtils.remove(courseCd," ").toUpperCase();

			return new ArrayList<Course>(KsapFrameworkServiceLocator.getCourseService()
					.searchForCourses(
							QueryByCriteria.Builder.fromPredicates(PredicateFactory.equal("officialIdentifier.code",
                                    cleanedCourseCd)), KsapFrameworkServiceLocator.getContext().getContextInfo()));
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("Course lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("Course lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("Course lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("Course lookup error", e);
		}
	}

	@Override
	public String getCurrentVersionIdOfCourse(String courseId) {
		Course course = getCurrentVersionOfCourse(courseId);
        if (course != null)
            return course.getId();
        return null;
	}

    @Override
    public Course getCurrentVersionOfCourse(String courseId) {
        // Retrieve course information using the course code entered by the user
        Course course;
        try {
            ContextInfo contextInfo = KsapFrameworkServiceLocator.getContext().getContextInfo();
            course = KsapFrameworkServiceLocator.getCourseService().getCourse(courseId, contextInfo);
            VersionDisplayInfo currentVersion = KsapFrameworkServiceLocator.getCluService().getCurrentVersion(CluServiceConstants.CLU_NAMESPACE_URI, course.getVersion().getVersionIndId(), contextInfo);
            course = KsapFrameworkServiceLocator.getCourseService().getCourse(currentVersion.getId(), contextInfo);
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
        return course;
    }

    @Override
	public String getSLN(String year, String term, String subject, String number, String activityCd) {
		String activityId = StringUtils.join(new Object[]{year, term, subject, number, activityCd}, ":");
		ActivityOfferingDisplayInfo activityOfferingInfo = null;
		try {
			activityOfferingInfo = KsapFrameworkServiceLocator.getCourseOfferingService().getActivityOfferingDisplay(
					activityId, KsapFrameworkServiceLocator.getContext().getContextInfo());
			if (activityOfferingInfo != null)
				for (AttributeInfo attributeInfo : activityOfferingInfo.getAttributes())
					if (attributeInfo.getKey().equalsIgnoreCase("SLN"))
						return attributeInfo.getValue();
			return null;
		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup error", e);
		}
	}

    @Override
	public String getCourseCdFromActivityId(String activityId) {
		ActivityOfferingDisplayInfo activityDisplayInfo;
		try {
			activityDisplayInfo = KsapFrameworkServiceLocator.getCourseOfferingService().getActivityOfferingDisplay(
					activityId, KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup error", e);
		}
		assert activityDisplayInfo != null : "activity id " + activityId + " returned null";

		String courseOfferingId = null;
		for (AttributeInfo attributeInfo : activityDisplayInfo.getAttributes())
			if ("PrimaryActivityOfferingId".equalsIgnoreCase(attributeInfo.getKey()))
				courseOfferingId = attributeInfo.getValue();
		assert courseOfferingId != null : "activity id " + activityId + " missing PrimaryActiveOfferingId";

		try {
			CourseOfferingInfo courseOfferingInfo = KsapFrameworkServiceLocator.getCourseOfferingService()
					.getCourseOffering(courseOfferingId, KsapFrameworkServiceLocator.getContext().getContextInfo());
			return courseOfferingInfo != null ? courseOfferingInfo.getCourseCode() : null;
		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("CO lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CO lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalStateException("CO lookup error", e);
		}
	}

    @Override
    public boolean isCourseOffered(Term term, Course course) {
        try {
            List<CourseOfferingInfo> cos = KsapFrameworkServiceLocator.getCourseOfferingService()
                    .getCourseOfferingsByCourseAndTerm(course.getId(), term.getId(),
                            KsapFrameworkServiceLocator.getContext().getContextInfo());
            return cos != null && !cos.isEmpty();
        } catch (DoesNotExistException e) {
            return false;
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("CO lookup failure", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("CO lookup failure", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("CO lookup failure", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("CO lookup failure", e);
        }
    }

    @Override
    public boolean isCourseBookmarked(Course course, List<PlanItem> planItems){
        for(PlanItem item : planItems){
            if(item.getCategory().equals(AcademicPlanServiceConstants.ItemCategory.WISHLIST)){
                return true;
            }
        }
        return false;
    }
}
