package org.kuali.student.ap.coursesearch.controller;

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal;
import static org.kuali.rice.core.api.criteria.PredicateFactory.or;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.student.ap.academicplan.dto.LearningPlanInfo;
import org.kuali.student.ap.academicplan.dto.PlanItemInfo;
import org.kuali.student.ap.academicplan.infc.LearningPlan;
import org.kuali.student.ap.academicplan.infc.PlanItem;
import org.kuali.student.ap.academicplan.service.AcademicPlanService;
import org.kuali.student.ap.academicplan.service.AcademicPlanServiceConstants;
import org.kuali.student.ap.coursesearch.dataobject.CourseSearchItemImpl;
import org.kuali.student.ap.coursesearch.dataobject.FacetItem;
import org.kuali.student.ap.coursesearch.form.CourseSearchFormImpl;
import org.kuali.student.ap.coursesearch.util.CourseLevelFacet;
import org.kuali.student.ap.coursesearch.util.CreditsFacet;
import org.kuali.student.ap.coursesearch.util.CurriculumFacet;
import org.kuali.student.ap.coursesearch.util.GenEduReqFacet;
import org.kuali.student.ap.coursesearch.util.TermsFacet;
import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.context.CourseSearchConstants;
import org.kuali.student.ap.framework.course.CourseSearchForm;
import org.kuali.student.ap.framework.course.CourseSearchItem;
import org.kuali.student.ap.framework.course.CourseSearchStrategy;
import org.kuali.student.ap.framework.course.Credit;
import org.kuali.student.common.collection.KSCollectionUtils;
import org.kuali.student.enrollment.courseoffering.dto.CourseOfferingInfo;
import org.kuali.student.enrollment.courseoffering.service.CourseOfferingService;
import org.kuali.student.enrollment.courseofferingset.dto.SocInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.CourseOfferingSetServiceConstants;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;
import org.kuali.student.r2.core.acal.infc.Term;
import org.kuali.student.r2.core.atp.dto.AtpInfo;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.enumerationmanagement.dto.EnumeratedValueInfo;
import org.kuali.student.r2.core.search.dto.SearchParamInfo;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.infc.SearchResult;
import org.kuali.student.r2.core.search.infc.SearchResultCell;
import org.kuali.student.r2.core.search.infc.SearchResultRow;
import org.kuali.student.r2.lum.clu.service.CluService;
import org.kuali.student.r2.lum.lrc.dto.ResultValuesGroupInfo;

public class CourseSearchStrategyImpl implements CourseSearchStrategy {

	private static final Logger LOG = Logger
			.getLogger(CourseSearchStrategyImpl.class);

	private static final Map<String, Comparator<String>> FACET_SORT;
	private static final int MAX_HITS = 1000;
	private static WeakReference<Map<String, Credit>> creditMapRef;

	public static final String NO_CAMPUS = "-1";

	static {
		// Related to CourseSearchUI.xml definitions
		Map<String, Comparator<String>> l = new java.util.LinkedHashMap<String, Comparator<String>>(
				6);
		l.put("facet_quarter", TERMS);
		l.put("facet_genedureq", ALPHA);
		l.put("facet_credits", NUMERIC);
		l.put("facet_level", NUMERIC);
		l.put("facet_curriculum", ALPHA);
		l.put("facet_keywords", ALPHA);
		FACET_SORT = Collections
				.unmodifiableMap(Collections.synchronizedMap(l));
	}

	@Override
	public CourseSearchForm createSearchForm() {
		CourseSearchFormImpl rv = new CourseSearchFormImpl();
		Set<String> o = getCampusLocations();
		rv.setCampusSelect(new java.util.ArrayList<String>(o));
		return rv;
	}

	public String getCellValue(SearchResultRow row, String key) {
		String value = null;
		for (SearchResultCell cell : row.getCells()) {
			if (key.equals(cell.getKey())) {
				// return cell.getValue();
				value = cell.getValue();
				break;
			}
		}
		assert value == null : "cell result '" + key + "' not found";
		return value;
		// throw new RuntimeException("cell result '" + key + "' not found");
	}

	public static class Hit {
		public String courseID;
		public int count = 0;

		public Hit(String courseID) {
			this.courseID = courseID;
			count = 1;
		}

		@Override
		public boolean equals(Object other) {
			return courseID.equals(((Hit) other).courseID);
		}

		@Override
		public int hashCode() {
			return courseID.hashCode();
		}
	}

	public static class HitComparator implements Comparator<Hit> {
		@Override
		public int compare(Hit x, Hit y) {
			if (x == null)
				return -1;
			if (y == null)
				return 1;
			return y.count - x.count;
		}
	}

	public List<Hit> processSearchRequests(List<SearchRequestInfo> requests) {
		LOG.info("Start of processSearchRequests of CourseSearchController:"
				+ System.currentTimeMillis());
		List<Hit> hits = new java.util.LinkedList<Hit>();
		Set<String> seen = new java.util.HashSet<String>();
		String id;
		for (SearchRequestInfo request : requests)
			try {
				for (SearchResultRow row : KsapFrameworkServiceLocator
						.getCluService()
						.search(request,
								KsapFrameworkServiceLocator.getContext()
										.getContextInfo()).getRows())
					if (seen.add(id = getCellValue(row, "lu.resultColumn.cluId")))
						hits.add(new Hit(id));
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException(
						"Invalid course ID or CLU lookup error", e);
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException(
						"Invalid course ID or CLU lookup error", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("CLU lookup error", e);
			} catch (PermissionDeniedException e) {
				throw new IllegalArgumentException("CLU lookup error", e);
			}
		LOG.info("End of processSearchRequests of CourseSearchController:"
				+ System.currentTimeMillis());
		return hits;
	}

	public static class CreditImpl implements Credit {
		private String id;
		private String display;
		private float min;
		private float max;
		private CourseSearchItem.CreditType type;

		public String getId() {
			return id;
		}

		public String getDisplay() {
			return display;
		}

		public float getMin() {
			return min;
		}

		public float getMax() {
			return max;
		}

		public CourseSearchItem.CreditType getType() {
			return type;
		}

	}

	public Map<String, Credit> getCreditMap() {
		Map<String, Credit> rv = creditMapRef == null ? null : creditMapRef
				.get();
		if (rv == null) {
			Map<String, Credit> creditMap = new java.util.LinkedHashMap<String, Credit>();
			// SearchRequestInfo searchRequest = new SearchRequestInfo(
			// "ksap.course.info.credits.details");
			// searchRequest.setParams(Collections.<SearchParamInfo>
			// emptyList());
			String resultScaleKey = "kuali.result.scale.credit.degree";
			ContextInfo contextInfo = KsapFrameworkServiceLocator.getContext()
					.getContextInfo();
			List<ResultValuesGroupInfo> resultValuesGroupInfos = null;

			try {
				resultValuesGroupInfos = KsapFrameworkServiceLocator
						.getLrcService().getResultValuesGroupsByResultScale(
								resultScaleKey, contextInfo);
			} catch (DoesNotExistException e) {
				throw new IllegalArgumentException("LRC lookup error", e);
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException("LRC lookup error", e);
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException("LRC lookup error", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("LRC lookup error", e);
			} catch (PermissionDeniedException e) {
				throw new IllegalStateException("LRC lookup error", e);
			}

			if ((resultValuesGroupInfos != null)
					&& (resultValuesGroupInfos.size() > 0)) {
				for (ResultValuesGroupInfo resultValuesGroupInfo : resultValuesGroupInfos) {
					String id = resultValuesGroupInfo.getKey();
					String type = resultValuesGroupInfo.getTypeKey()
							.toLowerCase();
					String display = resultValuesGroupInfo.getName();
					String min = null;
					String max = null;

					String[] arraySplitType = type.split("\\.");
					if ((arraySplitType != null) && (arraySplitType.length > 0)) {
						type = arraySplitType[arraySplitType.length - 1];
					}
					if (resultValuesGroupInfo.getResultValueRange() != null) {
						min = resultValuesGroupInfo.getResultValueRange()
								.getMinValue();
						max = resultValuesGroupInfo.getResultValueRange()
								.getMaxValue();
					} else { // if ("multiple".equals(type))
						String[] tempArray = null;
						if (id.toLowerCase().contains(
								CourseSearchConstants.DEGREE_CREDIT_ID_SUFFIX
										.toLowerCase())) {
							tempArray = id
									.toLowerCase()
									.split(CourseSearchConstants.DEGREE_CREDIT_ID_SUFFIX
											.toLowerCase());
							if (tempArray != null) {
								tempArray = tempArray[tempArray.length - 1]
										.split(",");
							}
							if (tempArray != null) {
								min = tempArray[0];
								max = tempArray[tempArray.length - 1];
								display = min + ", " + max + " Credits";
							}
						}
					}
					// Currently, the types listed in the database are: fixed,
					// range, multiple
					if ((!"fixed".equals(type)) && (!"range".equals(type))
							&& (!"multiple".equals(type))) {
						type = "unknown";
					}
					CreditImpl credit = new CreditImpl();
					credit.id = id;
					credit.display = display;
					credit.type = CourseSearchItem.CreditType.valueOf(type);

					Float tempValueHolder = 0F;
					try {
						credit.min = Float.parseFloat(min);
						credit.max = Float.parseFloat(max);
					} catch (Exception e) {
						credit.min = tempValueHolder;
						credit.max = tempValueHolder;
					}
					creditMap.put(id, credit);
				} // End for loop
			}
			creditMapRef = new WeakReference<Map<String, Credit>>(
					rv = Collections.unmodifiableMap(Collections
							.synchronizedMap(creditMap)));
		}
		return rv;
	}

	public Credit getCreditByID(String id) {
		Map<String, Credit> creditMap = getCreditMap();
		Credit credit = creditMap.get(id);
		return credit == null ? getCreditMap().get("u") : credit;
	}

	private List<CourseSearchItemImpl> getCoursesInfo(List<String> courseIDs) {
		LOG.info("Start of method getCourseInfo of CourseSearchController:"
				+ System.currentTimeMillis());
		List<CourseSearchItemImpl> listOfCourses = new ArrayList<CourseSearchItemImpl>();
		SearchRequestInfo request = new SearchRequestInfo("ksap.course.info");
		request.addParam("courseIDs", courseIDs);
		SearchResult result;
		try {
			result = KsapFrameworkServiceLocator.getCluService().search(
					request,
					KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CLU lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalArgumentException("CLU lookup error", e);
		}
		if ((result != null) && (!result.getRows().isEmpty())) {
			for (SearchResultRow row : result.getRows()) {
				CourseSearchItemImpl course = new CourseSearchItemImpl();
				course.setCourseId(getCellValue(row, "course.id"));
				course.setSubject(getCellValue(row, "course.subject"));
				course.setNumber(getCellValue(row, "course.number"));
				course.setLevel(getCellValue(row, "course.level"));
				course.setCourseName(getCellValue(row, "course.name"));
				course.setCode(getCellValue(row, "course.code"));

				String cellValue = getCellValue(row, "course.credits");
				Credit credit = getCreditByID(cellValue);
				if (credit != null) {
					course.setCreditMin(credit.getMin());
					course.setCreditMax(credit.getMax());
					course.setCreditType(credit.getType());
					course.setCredit(credit.getDisplay());
				}
				listOfCourses.add(course);
			}
		}

		LOG.info("End of method getCourseInfo of CourseSearchController:"
				+ System.currentTimeMillis());
		return listOfCourses;
	}

	public boolean isCourseOffered(CourseSearchForm form,
			CourseSearchItem course) {
		// Unused in Default Implementation
        return true;
	}

    /**
     * Filters the result course ids from the search based on the term filter
     *
     * @param courseIds - Full list of course ids found by the search
     * @param termFilter - Term to filter by
     * @return A list of course ids with offerings matching the selected filter
     */
    private List<String> termfilterCourseIds(List<String> courseIds, String termFilter){
        LOG.info("Start of method termfilterCourseIds of CourseSearchController:"
                + System.currentTimeMillis());

        // If any term option is select return list as is, no filtering needed.
        if(termFilter.equals(CourseSearchForm.SEARCH_TERM_ANY_ITEM)){
            return courseIds;
        }

        // Build list of valid terms based on the filter
        List<Term> terms = new ArrayList<Term>();
        if(termFilter.equals(CourseSearchForm.SEARCH_TERM_SCHEDULED)){
            // Any Scheduled term selected
            List<Term> currentScheduled = KsapFrameworkServiceLocator.getTermHelper().getCurrentTermsWithPublishedSOC();
            List<Term> futureScheduled = KsapFrameworkServiceLocator.getTermHelper().getFutureTermsWithPublishedSOC();
            if(currentScheduled!=null) terms.addAll(currentScheduled);
            if(futureScheduled!=null) terms.addAll(futureScheduled);
        }else{
            // Single Term selected
            terms.add(KsapFrameworkServiceLocator.getTermHelper().getTerm(termFilter));
        }
        List<String> filteredIds = new ArrayList<String>();
        try {

            // Search for all course offerings of search results in terms
            Predicate termPredicates[] = getTermPredicates(terms);
            Predicate coursePredicates[] = getCourseIdPredicates(courseIds);
            QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(or(coursePredicates),
                    or(termPredicates), equal("luiType", LuiServiceConstants.COURSE_OFFERING_TYPE_KEY));
            List<CourseOfferingInfo> offerings = KsapFrameworkServiceLocator.getCourseOfferingService()
                    .searchForCourseOfferings(query,KsapFrameworkServiceLocator.getContext().getContextInfo());

            // Fill filtered id list
            for(CourseOfferingInfo offering : offerings){
                for(int i=0;i<courseIds.size();i++){
                    String courseId = courseIds.get(i);
                    if(courseId.equals(offering.getCourseId())){
                        if(!filteredIds.contains(courseId)){
                            filteredIds.add(courseId);
                            courseIds.remove(i);
                            break;
                        }
                    }

                }
            }

        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        }
        LOG.info("End of method termfilterCourseIds of CourseSearchController:"
                + System.currentTimeMillis());
        return filteredIds;

    }

    private List<String> getTermsToFilterOn(String termFilter){
        List<String> termsToFilterOn = new ArrayList<String>();
        if(termFilter.equals(CourseSearchForm.SEARCH_TERM_ANY_ITEM)){
            termsToFilterOn.add("ANY");
        }

        // Build list of valid terms based on the filter

        else if(termFilter.equals(CourseSearchForm.SEARCH_TERM_SCHEDULED)){
            // Any Scheduled term selected
            List<Term> terms = new ArrayList<Term>();
            List<Term> currentScheduled = KsapFrameworkServiceLocator.getTermHelper().getCurrentTermsWithPublishedSOC();
            List<Term> futureScheduled = KsapFrameworkServiceLocator.getTermHelper().getFutureTermsWithPublishedSOC();
            if(currentScheduled!=null) terms.addAll(currentScheduled);
            if(futureScheduled!=null) terms.addAll(futureScheduled);
            for(int i=0;i<terms.size();i++){
               termsToFilterOn.add(terms.get(i).getId());
            }
        }else{
            // Single Term selected
            termsToFilterOn.add(termFilter);
        }
        return termsToFilterOn;
    }


    /**
     * Load scheduling information for courses based on their course offerings
     *
     * @param courses - List of courses to load information for.
     */
	private void loadScheduledTerms(List<CourseSearchItemImpl> courses) {
		LOG.info("Start of method loadScheduledTerms of CourseSearchController:"
				+ System.currentTimeMillis());

        // Load list of terms to find offerings in
		List<Term> terms = new ArrayList<Term>();
        List<Term> currentScheduled = KsapFrameworkServiceLocator.getTermHelper().getCurrentTermsWithPublishedSOC();
        List<Term> futureScheduled = KsapFrameworkServiceLocator.getTermHelper().getFutureTermsWithPublishedSOC();
        if(currentScheduled!=null) terms.addAll(currentScheduled);
        if(futureScheduled!=null) terms.addAll(futureScheduled);

        try {

            // Search for all course offerings of search results in terms
            Predicate termPredicates[] = getTermPredicates(terms);
            Predicate coursePredicates[] = getCoursePredicates(courses);
            QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(or(coursePredicates),
                    or(termPredicates), equal("luiType", LuiServiceConstants.COURSE_OFFERING_TYPE_KEY));
            List<CourseOfferingInfo> offerings = KsapFrameworkServiceLocator.getCourseOfferingService()
                    .searchForCourseOfferings(query,KsapFrameworkServiceLocator.getContext().getContextInfo());

            // Load scheduling data into course results from there course offerings
            for(CourseOfferingInfo offering : offerings){
                for(CourseSearchItemImpl course : courses){
                    if(course.getCourseId().equals(offering.getCourseId())){
                        // Avoid Duplicates
                        if(!course.getScheduledTermsList().contains(offering.getTermId()))
                            course.addScheduledTerm(offering.getTermId());
                    }
                }
            }
        } catch (InvalidParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (MissingParameterException e) {
            throw new IllegalArgumentException("ATP lookup failed", e);
        } catch (OperationFailedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        } catch (PermissionDeniedException e) {
            throw new IllegalStateException("ATP lookup failed", e);
        }

		LOG.info("End of method loadScheduledTerms of CourseSearchController:"
				+ System.currentTimeMillis());
	}

    /**
     * Build an array of equal predicates for a list of terms.
     * Filters terms by the SOC state of published
     *
     * @param terms - List of terms to be in predicate
     * @return An array of equal predicates of ("atpId',termId)
     */
    private Predicate[] getTermPredicates(List<Term> terms) {
        // Build predicate based on term id
        Predicate termPredicates[] = new Predicate[terms.size()];
        for(int i=0;i<terms.size();i++){
            termPredicates[i]=equal("termId", terms.get(i).getId());
        }
        try {
            // Get Published Soc states based on term predicates
            QueryByCriteria query = QueryByCriteria.Builder.fromPredicates(
                    or(termPredicates), equal("socState", CourseOfferingSetServiceConstants.PUBLISHED_SOC_STATE_KEY));
            List<SocInfo> socs = KsapFrameworkServiceLocator.getCourseOfferingSetService().searchForSocs(query,
                            KsapFrameworkServiceLocator.getContext().getContextInfo());

            // Create term predicates based on published soc states
            Predicate predicates[] = new Predicate[socs.size()];
            for(int j=0;j<socs.size();j++){
                predicates[j]=equal("atpId", socs.get(j).getTermId());
            }
            return predicates;
        } catch (Exception e) {
            LOG.warn("Unable to build term list for scheduling", e);
        }

        // If predicate list can not be created return empty
        return new Predicate[0];
    }

    /**
     * Build an array of equal predicates for a list of courses
     *
     * @param courses - List of courses to be in the predicate
     * @return An array of equal predicates of ("cluId",courseId)
     */
    private Predicate[] getCoursePredicates(List<CourseSearchItemImpl> courses) {
        Predicate predicates[] = new Predicate[courses.size()];
        for(int i=0;i<courses.size();i++){
            predicates[i]=equal("cluId", courses.get(i).getCourseId());
        }
        return predicates;
    }

    /**
     * Build an array of equal predicates for a list of course ids
     *
     * @param courses - List of course ids to be in the predicate
     * @return An array of equal predicates of ("cluId",courseId)
     */
    private Predicate[] getCourseIdPredicates(List<String> courses) {
        Predicate predicates[] = new Predicate[courses.size()];
        for(int i=0;i<courses.size();i++){
            predicates[i]=equal("cluId", courses.get(i));
        }
        return predicates;
    }

    // This needs rewrote.  Looks like an incomplete translation of a single course entry into a list of courses
    /**
     * Loads projected term information for the courses.
     * This information is found in the KSLU_CLU_ATP_TYPE_KEY table
     *
     * @param courses - The list of course information for the courses
     * @param courseIDs - The list of course ids for the courses.
     */
	private void loadTermsOffered(List<CourseSearchItemImpl> courses,
			final List<String> courseIDs) {
		LOG.info("Start of method loadTermsOffered of CourseSearchController:"
				+ System.currentTimeMillis());

        // Search for projected offered terms for the courses
		SearchRequestInfo request = new SearchRequestInfo(
				"ksap.course.info.atp");
		request.addParam("courseIDs", courseIDs);
		SearchResult result;
		try {
			result = KsapFrameworkServiceLocator.getCluService().search(
					request,
					KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CLU lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalArgumentException("CLU lookup error", e);
		}

        // Process the found term information
		if (result == null) {
			return;
		}

        Map<String, List<String>> offeredMap = new HashMap<String,List<String>>();

        for (SearchResultRow row : result.getRows()) {
            String courseId = getCellValue(row, "course.key");
            String type = getCellValue(row, "atp.id");
            if(offeredMap.containsKey(courseId)){
                offeredMap.get(courseId).add(type);
            }else{
                offeredMap.put(courseId,new ArrayList<String>());
                offeredMap.get(courseId).add(type);
            }
        }

        for(CourseSearchItemImpl course : courses){
            if(offeredMap.containsKey(course.getCourseId())){
                List<String> termsOffered = offeredMap.get(course.getCourseId());
                Collections.sort(termsOffered);
                course.setTermInfoList(termsOffered);
            }
        }

		LOG.info("End of method loadTermsOffered of CourseSearchController:"
				+ System.currentTimeMillis());
	}

    /**
     * Format a list of gen ed requirements into a comma seperated string
     *
     * @param genEduRequirements - The list of gen ed requirements
     * @return A comma seperated string
     */
	private String formatGenEduReq(List<String> genEduRequirements) {
		// Make the order predictable.
		Collections.sort(genEduRequirements);
		StringBuilder genEdsOut = new StringBuilder();
		for (String req : genEduRequirements) {
			if (genEdsOut.length() != 0) {
				genEdsOut.append(", ");
			}
			req = KsapFrameworkServiceLocator.getEnumerationHelper()
					.getEnumAbbrValForCode(req);
			/* Doing this to fix a bug in IE8 which is trimming off the I&S as I */
			if (req.contains("&")) {
				req = req.replace("&", "&amp;");
			}
			genEdsOut.append(req);
		}
		return genEdsOut.toString();
	}

    // This needs rewrote.  Looks like an incomplete translation of a single course entry into a list of courses
    /**
     * Loads the gen ed information for the courses.
     * Gen ed information is stored as attributes of the course's clu entry.
     *
     * @param courses - The list of course inforamtion for the courses
     * @param courseIDs - The list of course ids for the courses
     */
	private void loadGenEduReqs(List<CourseSearchItemImpl> courses,
			final List<String> courseIDs) {
		LOG.info("Start of method loadGenEduReqs of CourseSearchController:"
				+ System.currentTimeMillis());

        // Search for gen ed requirements
		SearchRequestInfo request = new SearchRequestInfo(
				"ksap.course.info.gened");
		request.addParam("courseIDs", courseIDs);
		List<String> reqs = new ArrayList<String>();
		SearchResult result;
		try {
			result = KsapFrameworkServiceLocator.getCluService().search(
					request,
					KsapFrameworkServiceLocator.getContext().getContextInfo());
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException(
					"Invalid course ID or CLU lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("CLU lookup error", e);
		} catch (PermissionDeniedException e) {
			throw new IllegalArgumentException("CLU lookup error", e);
		}
		if (result == null) {
			return;
		}
		for (SearchResultRow row : result.getRows()) {
			String genEd = getCellValue(row, "gened.name");
			reqs.add(genEd);
		}
		String courseId = null;
		for (SearchResultRow row : result.getRows()) {
			courseId = getCellValue(row, "course.owner");
			for (CourseSearchItemImpl course : courses) {
				if (courseId.equals(course.getCourseId())) {
					String formatted = formatGenEduReq(reqs);
					course.setGenEduReq(formatted);
					break;
				}
			}
		}
		LOG.info("End of method loadGenEduReqs of CourseSearchController:"
				+ System.currentTimeMillis());
	}

    /**
     * Creates a map relating the course to its status in the plan.
     * @param studentID - Id of the user running the search
     * @return A map of the plan state for a course.
     */
	private Map<String, CourseSearchItem.PlanState> getCourseStatusMap(
			String studentID) {
		LOG.info("Start of method getCourseStatusMap of CourseSearchController:"
				+ System.currentTimeMillis());
		AcademicPlanService academicPlanService = KsapFrameworkServiceLocator
				.getAcademicPlanService();

		ContextInfo context = new ContextInfo();

		String planTypeKey = AcademicPlanServiceConstants.LEARNING_PLAN_TYPE_PLAN;

		Map<String, CourseSearchItem.PlanState> savedCourseSet = new HashMap<String, CourseSearchItem.PlanState>();

		/*
		 * For each plan item in each plan set the state based on the type.
		 */
        // Find list of learning plans
		List<LearningPlanInfo> learningPlanList;
		try {
			learningPlanList = academicPlanService
					.getLearningPlansForStudentByType(studentID, planTypeKey,
							context);
		} catch (DoesNotExistException e) {
			throw new IllegalArgumentException("Learning plan does not exist",
					e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("LP lookup error", e);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("LP lookup error", e);
		} catch (OperationFailedException e) {
			throw new IllegalStateException("LP lookup error", e);
		}

        // Process list of learning plan's entries
		for (LearningPlan learningPlan : learningPlanList) {
			String learningPlanID = learningPlan.getId();
			List<PlanItemInfo> planItemList;
			try {
				planItemList = academicPlanService.getPlanItemsInPlan(
						learningPlanID, context);
			} catch (DoesNotExistException e) {
				throw new IllegalArgumentException(
						"Learning plan items do not exist", e);
			} catch (InvalidParameterException e) {
				throw new IllegalArgumentException("LP lookup error", e);
			} catch (MissingParameterException e) {
				throw new IllegalArgumentException("LP lookup error", e);
			} catch (OperationFailedException e) {
				throw new IllegalStateException("LP lookup error", e);
			}

            // Process plan items in learning plan
			for (PlanItem planItem : planItemList) {
				String courseID = planItem.getRefObjectId();
				CourseSearchItem.PlanState state;
				if (planItem.getCategory().equals(
						AcademicPlanServiceConstants.ItemCategory.WISHLIST)) {
					state = CourseSearchItem.PlanState.SAVED;
				} else if (planItem.getCategory().equals(
						AcademicPlanServiceConstants.ItemCategory.PLANNED)
						|| planItem.getCategory().equals(
								AcademicPlanServiceConstants.ItemCategory.BACKUP)
                        || planItem.getCategory().equals(
                                AcademicPlanServiceConstants.ItemCategory.CART)) {
					state = CourseSearchItem.PlanState.IN_PLAN;
				} else {
					throw new RuntimeException("Unknown plan item type.");
				}

                // Add entry to map
				savedCourseSet.put(courseID, state);
			}
		}
		LOG.info("End of method getCourseStatusMap of CourseSearchController:"
				+ System.currentTimeMillis());
		return savedCourseSet;
	}

	public void populateFacets(CourseSearchForm form,
			List<CourseSearchItem> courses) {
		LOG.info("Start of method populateFacets of CourseSearchController:"
				+ System.currentTimeMillis());
		// Initialize facets.
		CurriculumFacet curriculumFacet = new CurriculumFacet();
		CreditsFacet creditsFacet = new CreditsFacet();
		CourseLevelFacet courseLevelFacet = new CourseLevelFacet();
		GenEduReqFacet genEduReqFacet = new GenEduReqFacet();
		TermsFacet termsFacet = new TermsFacet();

		// Update facet info and code the item.
		for (CourseSearchItem course : courses) {
			curriculumFacet.process(course);
			courseLevelFacet.process(course);
			genEduReqFacet.process(course);
			creditsFacet.process(course);
			termsFacet.process(course);
		}
		/* Removing Duplicate entries from genEduReqFacet */
		List<FacetItem> genEduReqFacetItems = new ArrayList<FacetItem>();
		for (FacetItem facetItem : genEduReqFacet.getFacetItems()) {
			boolean itemExists = false;
			for (FacetItem facetItem1 : genEduReqFacetItems) {
				if (facetItem1.getKey().equalsIgnoreCase(facetItem.getKey())) {
					itemExists = true;
				}
			}
			if (!itemExists) {
				genEduReqFacetItems.add(facetItem);
			}
		}
	}

	public List<CourseSearchItem> courseSearch(CourseSearchForm form,
			String studentId) {
		String maxCountProp = ConfigContext.getCurrentContextConfig()
				.getProperty("ksap.search.results.max");
		int maxCount = maxCountProp != null && !"".equals(maxCountProp.trim()) ? Integer
				.valueOf(maxCountProp) : MAX_HITS;

		List<SearchRequestInfo> requests = queryToRequests(form);
		List<Hit> hits = processSearchRequests(requests);
		List<CourseSearchItem> courseList = new ArrayList<CourseSearchItem>();
		Map<String, CourseSearchItem.PlanState> courseStatusMap = getCourseStatusMap(studentId);
		List<String> courseIDs = new ArrayList<String>();
		for (Hit hit : hits) {
			courseIDs.add(hit.courseID);
		}
        List<CourseSearchItemImpl> courses = new ArrayList<CourseSearchItemImpl>();
        if(!courseIDs.isEmpty()){
            courseIDs = termfilterCourseIds(courseIDs,form.getSearchTerm());
            if (!courseIDs.isEmpty()) {
                courses = getCoursesInfo(courseIDs);
                loadScheduledTerms(courses);
                loadTermsOffered(courses, courseIDs);
                loadGenEduReqs(courses, courseIDs);
            }
        }
		for (CourseSearchItemImpl course : courses) {
            String courseId = course.getCourseId();
            if (courseStatusMap.containsKey(courseId)) {
                course.setStatus(courseStatusMap.get(courseId));
            }
            courseList.add(course);
            if (courseList.size() >= maxCount) {
                break;
            }
		}
		populateFacets(form, courseList);

		return courseList;
	}

	public void hitCourseID(Map<String, Hit> courseMap, String id) {
		Hit hit = null;
		if (courseMap.containsKey(id)) {
			hit = courseMap.get(id);
			hit.count++;
		} else {
			hit = new Hit(id);
			courseMap.put(id, hit);
		}
	}

	/*
	 * Remove the HashMap after enumeration service is in the ehcache and remove
	 * the hashmap occurance in this
	 */
	private Map<String, Set<String>> orgTypeCache;
	private Map<String, Map<String, String>> hashMap;

	public Map<String, Set<String>> getOrgTypeCache() {
		if (this.orgTypeCache == null) {
			this.orgTypeCache = new java.util.HashMap<String, Set<String>>();
		}
		return this.orgTypeCache;
	}

	public void setOrgTypeCache(Map<String, Set<String>> orgTypeCache) {
		this.orgTypeCache = orgTypeCache;
	}

	public Map<String, Map<String, String>> getHashMap() {
		if (this.hashMap == null) {
			this.hashMap = new java.util.HashMap<String, Map<String, String>>();
		}
		return this.hashMap;
	}

	public void setHashMap(HashMap<String, Map<String, String>> hashMap) {
		this.hashMap = hashMap;
	}

	private Set<String> getCampusLocations() {
		Set<String> campusLocations = getOrgTypeCache().get(
				CourseSearchConstants.CAMPUS_LOCATION);
		if (campusLocations == null) {
			// ContextInfo context = KsapFrameworkServiceLocator.getContext()
			// .getContextInfo();
			// List<Org> all = new java.util.ArrayList<Org>(
			// KsapFrameworkServiceLocator
			// .getOrgHelper()
			// .getOrgInfo(
			// CourseSearchConstants.CAMPUS_LOCATION,
			// CourseSearchConstants.ORG_QUERY_SEARCH_BY_TYPE_REQUEST,
			// CourseSearchConstants.ORG_TYPE_PARAM,
			// context));

			List<EnumeratedValueInfo> enumeratedValueInfoList = KsapFrameworkServiceLocator
					.getEnumerationHelper().getEnumerationValueInfoList(
							"kuali.lu.campusLocation");
			Set<String> alc = new java.util.LinkedHashSet<String>();
			// for (Org o : all)
			for (EnumeratedValueInfo o : enumeratedValueInfoList)
				// alc.add(o.getId());
				alc.add(o.getCode());
			this.getOrgTypeCache().put(CourseSearchConstants.CAMPUS_LOCATION,
					campusLocations = alc);
		}
		assert campusLocations != null : "Failed to build campus location cache";
		return campusLocations;
	}

	private List<String> getDivisionCodes() {
		ContextInfo context = KsapFrameworkServiceLocator.getContext()
				.getContextInfo();
		CluService cluService = KsapFrameworkServiceLocator.getCluService();
		SearchRequestInfo request = new SearchRequestInfo(
				"ksap.distinct.clu.divisions");
		SearchResult result;
		try {
			result = cluService.search(request, context);
		} catch (MissingParameterException e) {
			throw new IllegalArgumentException("Error in CLU division search",
					e);
		} catch (InvalidParameterException e) {
			throw new IllegalArgumentException("Error in CLU division search",
					e);
		} catch (OperationFailedException e) {
			throw new IllegalArgumentException("Error in CLU division search",
					e);
		} catch (PermissionDeniedException e) {
			throw new IllegalArgumentException("Error in CLU division search",
					e);
		}
		List<? extends SearchResultRow> rr = result.getRows();
		List<String> rv = new java.util.ArrayList<String>(rr.size());
		for (SearchResultRow row : rr)
			for (SearchResultCell cell : row.getCells())
				rv.add(cell.getValue());
		return rv;
	}

	@Override
	public Map<String, String> fetchCourseDivisions() {
		Map<String, String> map = new java.util.LinkedHashMap<String, String>();
		for (String div : getDivisionCodes())
			// Store both trimmed and original, because source data
			// is sometimes space padded.
			map.put(div.trim().replaceAll("\\s+", ""), div);
		return map;
	}

	public void addCampusParams(List<SearchRequestInfo> requests,
			CourseSearchForm form) {
		List<String> sel = form.getCampusSelect();
		if (sel == null)
			sel = new java.util.ArrayList<String>(1);
		Iterator<String> seli = sel.iterator();
		Set<String> campusLocations = getCampusLocations();
		while (seli.hasNext())
			if (!campusLocations.contains(seli.next()))
				seli.remove();
		if (sel.isEmpty())
			sel.add(NO_CAMPUS);
		for (SearchRequestInfo request : requests)
			request.getParams().add(new SearchParamInfo("campuses", sel));
	}

	public void addCampusParam(SearchRequestInfo request, CourseSearchForm form) {
		addCampusParams(Collections.singletonList(request), form);
	}

	/**
	 * @param divisionMap
	 *            for reference
	 * @param query
	 *            initial query
	 * @param divisions
	 *            matches found
	 * @return query string, minus matches found
	 */
	public String extractDivisions(HashMap<String, String> divisionMap,
			String query, List<String> divisions) {
		boolean match = true;
		while (match) {
			match = false;
			// Retokenize after each division found is removed
			// Remove extra spaces to normalize input
			/*
			 * Replacing all the special characters in query with empty space
			 * except for Ampersand(&)character cause it might be in course
			 * codes
			 */
			query = query.trim().replaceAll(
					"[\\s\\\\/:?\\\"<>|`~!@#$%^*()_+-={}\\]\\[;',.]", " ");
			List<QueryTokenizer.Token> tokens = QueryTokenizer.tokenize(query);
			List<String> list = QueryTokenizer.toStringList(tokens);
			List<String> pairs = TokenPairs.toPairs(list);
			TokenPairs.sortedLongestFirst(pairs);

			Iterator<String> i = pairs.iterator();
			while (match == false && i.hasNext()) {
				String pair = i.next();

				String key = pair.replace(" ", "");
				if (divisionMap.containsKey(key)) {
					String division = divisionMap.get(key);
					divisions.add(division);
					query = query.replace(pair, "");
					match = true;
				}
			}
		}
		return query;
	}

    /**
     * Add the division type searches to the search requests
     *
     * @param divisions - The list of divisions found in the query string
     * @param codes - The list of course codes found in the query string
     * @param levels - The list of course levels found in the query string
     * @param incompleteCodes - The list of possible incomplete course codes found in the query string
     * @param requests - The list of search requests to be ran
     */
	public void addDivisionSearches(List<String> divisions, List<String> codes,
			List<String> levels, List<String> incompleteCodes, List<SearchRequestInfo> requests) {

        // for each division found determine the search types needed
		for (String division : divisions) {
			boolean needDivisionQuery = true;

            // Create code searches for that division
			for (String code : codes) {
				needDivisionQuery = false;
				SearchRequestInfo request = new SearchRequestInfo(
						"ksap.lu.search.divisionAndCode");
				request.addParam("division", division);
				request.addParam("code", code);
				requests.add(request);
			}

            // Create level searchs for that division
			for (String level : levels) {
				needDivisionQuery = false;

				// Converts "1XX" to "100"
				level = level.substring(0, 1) + "00";

				SearchRequestInfo request = new SearchRequestInfo(
						"ksap.lu.search.divisionAndLevel");
				request.addParam("division", division);
				request.addParam("level", level);
				requests.add(request);
			}

            // Create course code only search
            for (String incompleteCode : incompleteCodes) {
                needDivisionQuery = false;

                SearchRequestInfo request = new SearchRequestInfo(
                        "ksap.lu.search.courseCode");
                request.addParam("code", incompleteCode);
                requests.add(request);
            }

            // If no other searches are created create a general division search.
			if (needDivisionQuery) {
				SearchRequestInfo request = new SearchRequestInfo(
						"ksap.lu.search.division");
				request.addParam("division", division);
				requests.add(request);
			}
		}
	}

    /**
     * Add the full text searches to the search requests
     *
     * @param query - The query string
     * @param requests - The list of search requests to be ran
     */
	public void addFullTextSearches(String query,
			List<SearchRequestInfo> requests) {
        //find all tokens in the query string
		List<QueryTokenizer.Token> tokens = QueryTokenizer.tokenize(query);

        // Create a search for each token found
		for (QueryTokenizer.Token token : tokens) {
            // Convert token to its correct text
			String queryText = null;
			switch (token.rule) {
			case WORD:
				queryText = token.value;
				break;
			case QUOTED:
				queryText = token.value;
				queryText = queryText.substring(1, queryText.length() - 1);
				break;
			default:
				break;
			}

            // Add requests for full text
			SearchRequestInfo request = new SearchRequestInfo(
					"ksap.lu.search.fulltext");
			request.addParam("queryText", queryText);
			requests.add(request);
		}
	}

    /**
     * Creates a list of search requests from the search information supplied by the user
     *
     * @param form - Page form with search information
     * @return The list of search requests to be ran.
     */
	public List<SearchRequestInfo> queryToRequests(CourseSearchForm form) {
		LOG.info("Start Of Method queryToRequests in CourseSearchStrategy:"
				+ System.currentTimeMillis());

        // To keep search from being case specific all text is uppercased
		String query = form.getSearchQuery().toUpperCase();

        // Unchanging query for full text search
        String pureQuery = query;

        // Extract possible levels from the query text
		List<String> levels = QueryTokenizer.extractCourseLevels(query);
		for (String level : levels) {
			query = query.replace(level, "");
		}

        // Extract Possible course codes from the query text
		List<String> codes = QueryTokenizer.extractCourseCodes(query);
		for (String code : codes) {
			query = query.replace(code, "");
		}

        // Extract possible division from the query text
		Map<String, String> divisionMap = fetchCourseDivisions();
		List<String> divisions = new ArrayList<String>();
		extractDivisions(divisionMap, query, divisions, true);

        // Extract possible partial course codes from the query text
        List<String> incompleteCodes = QueryTokenizer.extractIncompleteCourseCodes(query,divisions);

        // Create list of search requests
		ArrayList<SearchRequestInfo> requests = new ArrayList<SearchRequestInfo>();

		LOG.info("Start of method addDivisionSearches of CourseSearchStrategy:"
				+ System.currentTimeMillis());
		// Order is important, more exact search results appear at top of list
		addDivisionSearches(divisions, codes, levels, incompleteCodes, requests);
		LOG.info("End of method addDivisionSearches of CourseSearchStrategy:"
				+ System.currentTimeMillis());

		LOG.info("Start of method addFullTextSearches of CourseSearchStrategy:"
				+ System.currentTimeMillis());
		addFullTextSearches(pureQuery, requests);
		LOG.info("End of method addFullTextSearches of CourseSearchStrategy:"
				+ System.currentTimeMillis());

        // Add additional params to the search requests
		LOG.info("Start of method addCampusParams of CourseSearchStrategy:"
				+ System.currentTimeMillis());
		//addCampusParams(requests, form);
		LOG.info("Start of method addCampusParams of CourseSearchStrategy:"
				+ System.currentTimeMillis());

        // Process Current list of Requests into direct search queries
		LOG.info("Count of No of Query Tokens:" + requests.size());
		processRequests(requests, form);
		LOG.info("No of Requests after processRequest method:"
				+ requests.size());

		LOG.info("End Of Method queryToRequests in CourseSearchStrategy:"
				+ System.currentTimeMillis());
/*
		addVersionDateParam(requests);
*/

		return requests;
	}

	/**
	 * Process the Request with search key as division or full Text
	 * 
	 * @param requests - The list of requests.
	 * @param form - The search form.
	 */
	public void processRequests(List<SearchRequestInfo> requests,
			CourseSearchForm form) {
		LOG.info("Start of method processRequests in CourseSearchStrategy:"
				+ System.currentTimeMillis());
		Map<String, String> subjects = null;
		int size = requests.size();
        // Process search requests
		for (int i = 0; i < size; i++) {
			if (requests.get(i).getSearchKey() != null) {

                // Assumes a controlled build order of the query Division parameters should always be added first.
				if (requests.get(i).getSearchKey()
						.equalsIgnoreCase("ksap.lu.search.division")) {
                    // Add redundant checks to insure no empty lists,
                    String queryText = "";
                    String key = "";
                    try{
                        queryText = (String) KSCollectionUtils.getRequiredZeroElement(KSCollectionUtils
                                .getRequiredZeroElement(requests.get(i).getParams()).getValues());
                        key = (String) KSCollectionUtils.getRequiredZeroElement(KSCollectionUtils
                                .getRequiredZeroElement(requests.get(i).getParams()).getValues());
                    }catch(OperationFailedException e){
                        LOG.warn("Incorrectly Formed ksap.lu.search.division",e);
                    }
					if (form.getSearchQuery().length() <= 2) {
						break;
					} else {
						SearchRequestInfo request0 = new SearchRequestInfo(
								"ksap.lu.search.title");
						request0.addParam("queryText", queryText.trim());
						//addCampusParam(request0, form);
						requests.add(request0);
                        //Add course offering title search
                        SearchRequestInfo requestOffering = new SearchRequestInfo(
                                "ksap.lu.search.offering.title");
                        requestOffering.addParam("queryText", queryText.trim());
                        requestOffering.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                        //addCampusParam(request0, form);
                        requests.add(requestOffering);
						if (!this.getHashMap().containsKey(
								CourseSearchConstants.SUBJECT_AREA)) {
							subjects = KsapFrameworkServiceLocator
									.getOrgHelper().getSubjectAreas();
							getHashMap().put(
									CourseSearchConstants.SUBJECT_AREA,
									subjects);

						} else {
							subjects = getHashMap().get(
									CourseSearchConstants.SUBJECT_AREA);
						}
						StringBuilder additionalDivisions = new StringBuilder();
						if (subjects != null && subjects.size() > 0) {
							// Add the individual term items.
							for (Map.Entry<String, String> entry : subjects
									.entrySet()) {
								if (entry.getKey().trim().contains(key.trim())) {
									if (!entry.getKey().equalsIgnoreCase(
											queryText)) {
										additionalDivisions.append(entry
												.getKey() + ",");
									}
								}

							}
						}
						if (additionalDivisions.length() > 0) {
							String div = additionalDivisions.substring(0,
									additionalDivisions.length() - 1);
							SearchRequestInfo request1 = new SearchRequestInfo(
									"ksap.lu.search.additionalDivision");
							request1.addParam("divisions", div.trim());
							//addCampusParam(request1, form);
							requests.add(request1);
						}
						SearchRequestInfo request2 = new SearchRequestInfo(
								"ksap.lu.search.description");
						request2.addParam("queryText", queryText.trim());
						//addCampusParam(request2, form);
						requests.add(request2);
                        SearchRequestInfo requestOfferingDescr = new SearchRequestInfo(
                                "ksap.lu.search.offering.description");
                        requestOfferingDescr.addParam("queryText", queryText.trim());
                        requestOfferingDescr.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                        //addCampusParam(request2, form);
                        requests.add(requestOfferingDescr);
					}

				}
				if (requests.get(i).getSearchKey()
						.equalsIgnoreCase("ksap.lu.search.fulltext")) {
                    String key;
                    try{
                        key = (String) KSCollectionUtils.getRequiredZeroElement(KSCollectionUtils
                                .getRequiredZeroElement(requests.get(i).getParams()).getValues());
                    }catch (OperationFailedException e){
                        throw new RuntimeException("Search Failure", e);
                    }
					String division = null;
					if (key.length() <= 2) {
                        try{
                            KSCollectionUtils.getRequiredZeroElement(requests.get(i).getParams())
								.setValues(Arrays.asList("null"));
                        } catch (OperationFailedException e){

                        }
						break;
					} else {
						if (key.length() > 2) {

							if (!this.getHashMap().containsKey(
									CourseSearchConstants.SUBJECT_AREA)) {
								subjects = KsapFrameworkServiceLocator
										.getOrgHelper().getSubjectAreas();
								getHashMap().put(
										CourseSearchConstants.SUBJECT_AREA,
										subjects);

							} else {
								subjects = getHashMap().get(
										CourseSearchConstants.SUBJECT_AREA);
							}

							if (subjects != null && subjects.size() > 0) {
								// Add the individual term items.
								for (Map.Entry<String, String> entry : subjects
										.entrySet()) {
									if (entry.getValue().trim()
											.equalsIgnoreCase(key.trim())) {
										division = entry.getKey();

									}

								}
							}
							if (division != null) {
								requests.get(i).setSearchKey(
										"ksap.lu.search.division");
                                try{
                                    KSCollectionUtils.getRequiredZeroElement(requests.get(i).getParams())
										.setKey("division");
                                    KSCollectionUtils.getRequiredZeroElement(requests.get(i).getParams())
										.setValues(Arrays.asList(division));
                                }catch (OperationFailedException e){
                                    LOG.warn("Couldn't set params for division");
                                }

								SearchRequestInfo request1 = new SearchRequestInfo(
										"ksap.lu.search.title");
								request1.addParam("queryText", key.trim());
								//addCampusParam(request1, form);
								requests.add(request1);
                                //Add course offering title search
                                SearchRequestInfo requestOffering = new SearchRequestInfo(
                                        "ksap.lu.search.offering.title");
                                requestOffering.addParam("queryText", key.trim());
                                requestOffering.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                                //addCampusParam(request0, form);
                                requests.add(requestOffering);
								SearchRequestInfo request2 = new SearchRequestInfo(
										"ksap.lu.search.description");
								request2.addParam("queryText", key.trim());
								//addCampusParam(request2, form);
								requests.add(request2);
                                SearchRequestInfo requestOfferingDescr = new SearchRequestInfo(
                                        "ksap.lu.search.offering.description");
                                requestOfferingDescr.addParam("queryText", key.trim());
                                requestOfferingDescr.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                                //addCampusParam(request2, form);
                                requests.add(requestOfferingDescr);

							} else {
								requests.get(i).setSearchKey(
										"ksap.lu.search.title");
								SearchRequestInfo request2 = new SearchRequestInfo(
										"ksap.lu.search.description");
								request2.addParam("queryText", key.trim());
								//addCampusParam(request2, form);
								requests.add(request2);
                                //Add course offering title search
                                SearchRequestInfo requestOffering = new SearchRequestInfo(
                                        "ksap.lu.search.offering.title");
                                requestOffering.addParam("queryText", key.trim());
                                requestOffering.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                                //addCampusParam(request0, form);
                                requests.add(requestOffering);
                                SearchRequestInfo requestOfferingDescr = new SearchRequestInfo(
                                        "ksap.lu.search.offering.description");
                                requestOfferingDescr.addParam("queryText", key.trim());
                                requestOfferingDescr.addParam("termList",getTermsToFilterOn(form.getSearchTerm()));
                                //addCampusParam(request2, form);
                                requests.add(requestOfferingDescr);
							}
						}

					}
				}
			}
		}

		LOG.info("End of processRequests method in CourseSearchStrategy:"
				+ System.currentTimeMillis());
	}

	private void addVersionDateParam(List<SearchRequestInfo> searchRequests) {
		// String currentTerm =
		// KsapFrameworkServiceLocator.getAtpHelper().getCurrentAtpId();
		String lastScheduledTerm = KsapFrameworkServiceLocator.getTermHelper()
				.getLastScheduledTerm().getId();
		for (SearchRequestInfo searchRequest : searchRequests) {
			// searchRequest.addParam("currentTerm", currentTerm);
			searchRequest.addParam("lastScheduledTerm", lastScheduledTerm);
		}
	}

    /**
     * Extracts the possible divisions in the query string
     *
     * @param divisionMap - Map of possible divisions
     * @param query - The query string
     * @param divisions - List of extracted divisions
     * @param isSpaceAllowed - If spaces are allowed in the divisions
     * @return query string, minus matches found
     */
	@Override
	public String extractDivisions(Map<String, String> divisionMap,
			String query, List<String> divisions, boolean isSpaceAllowed) {
		boolean match = true;
		while (match) {
			match = false;
			// Retokenize after each division found is removed
			// Remove extra spaces to normalize input
			if (!isSpaceAllowed) {
				query = query.trim().replaceAll(
						"[\\s\\\\/:?\\\"<>|`~!@#$%^*()_+-={}\\]\\[;',.]", " ");
			} else {
				query = query.replaceAll(
						"[\\\\/:?\\\"<>|`~!@#$%^*()_+-={}\\]\\[;',.]", " ");
			}
			List<QueryTokenizer.Token> tokens = QueryTokenizer.tokenize(query);
			List<String> list = QueryTokenizer.toStringList(tokens);
			List<String> pairs = TokenPairs.toPairs(list);
			TokenPairs.sortedLongestFirst(pairs);

			Iterator<String> i = pairs.iterator();
			while (match == false && i.hasNext()) {
				String pair = i.next();

				String key = pair.replace(" ", "");
				if (divisionMap.containsKey(key)) {
					String division = divisionMap.get(key);
					divisions.add(division);
					query = query.replace(pair, "");
					match = true;
				}
			}
		}
		return query;
	}

	@Override
	public Map<String, Comparator<String>> getFacetSort() {
		return FACET_SORT;
	}

}
