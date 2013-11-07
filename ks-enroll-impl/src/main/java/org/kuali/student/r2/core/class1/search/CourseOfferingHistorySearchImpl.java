package org.kuali.student.r2.core.class1.search;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.student.r2.common.class1.search.SearchServiceAbstractHardwiredImpl;
import org.kuali.student.r2.common.dao.GenericEntityDao;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.RichTextHelper;
import org.kuali.student.r2.common.util.constants.LuiServiceConstants;
import org.kuali.student.r2.core.class1.type.dto.TypeInfo;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.dto.SearchResultInfo;
import org.kuali.student.r2.core.search.dto.SearchResultRowInfo;
import org.kuali.student.r2.core.search.util.SearchRequestHelper;

import javax.persistence.TemporalType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author venkat
 */
public class CourseOfferingHistorySearchImpl extends SearchServiceAbstractHardwiredImpl {

    private String noOfYears;

    private GenericEntityDao genericEntityDao;

    private static final String OWNER_UI_SUFFIX = " (Owner)";

    public static final String COURSE_ID = "cluId";

    public static final TypeInfo PAST_CO_SEARCH;

    public static final String TARGET_YEAR_PARAM = "targetYear";

    public static final class SearchParameters {
        public static final String CROSS_LIST_SEARCH_ENABLED = "crossListSearchEnabled";
    }

    public static final class SearchResultColumns {
        public static final String CROSS_LISTED_COURSES = "crossListedCodes";
        public static final String IS_CROSS_LISTED = "isCrossListedCode";
        public static final String OWNER_CODE = "ownerCode";
        public static final String OWNER_ALIASES = "ownerAliases";
        public static final String CO_ID = "courseOfferingId";
        public static final String CODE = "courseOfferingCode";
    }



    static {
        TypeInfo info = new TypeInfo();
        info.setKey("kuali.search.type.lui.pastCourseOfferings");
        info.setName("Past COs");
        info.setDescr(new RichTextHelper().fromPlain("Get all the past 5 years Course Offerings"));
        DateFormat mmddyyyy = new SimpleDateFormat("MM/dd/yyyy");
        try {
            info.setEffectiveDate(mmddyyyy.parse("01/01/2012"));
        } catch (ParseException ex) {
            throw new RuntimeException("bad code");
        }
        PAST_CO_SEARCH = info;
    }

    @Override
    public TypeInfo getSearchType() {
        return PAST_CO_SEARCH;
    }

    @Override
    public SearchResultInfo search(SearchRequestInfo searchRequestInfo, ContextInfo contextInfo)
            throws MissingParameterException, OperationFailedException, PermissionDeniedException {

        if (!StringUtils.equals(searchRequestInfo.getSearchKey(),PAST_CO_SEARCH.getKey())) {
            throw new OperationFailedException("Unsupported search type: " + searchRequestInfo.getSearchKey());
        }

        SearchRequestHelper requestHelper = new SearchRequestHelper(searchRequestInfo);

        String courseId = requestHelper.getParamAsString(COURSE_ID);
        boolean enableCrossListSearch = BooleanUtils.toBoolean(requestHelper.getParamAsString(SearchParameters.CROSS_LIST_SEARCH_ENABLED));

        if (StringUtils.isEmpty(courseId)){
            throw new RuntimeException("Course id is required");
        }

        String targetYear = requestHelper.getParamAsString(TARGET_YEAR_PARAM);

        if (StringUtils.isEmpty(targetYear)) {
            throw new RuntimeException("Target year parameter is required");
        }

        if (StringUtils.isEmpty(noOfYears)){
            throw new RuntimeException("No of years should be configured");
        }

        int year = Integer.parseInt(targetYear) - Integer.parseInt(noOfYears);

        Date startDate;
        try {
            startDate = new SimpleDateFormat("MM/dd/yyyy").parse("01/01/" + year);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


        // Return any COs in terms within the calculated year range that are in an Offered or Cancelled state
        List<Object[]> luiIds = genericEntityDao.getEm().createQuery("select lui.id,ident.type,ident.code from LuiIdentifierEntity ident,LuiEntity lui,AtpEntity atp " +
                "where lui.id = ident.lui.id and lui.atpId=atp.id and lui.cluId = :cluId and " +
                "lui.luiType = '" + LuiServiceConstants.COURSE_OFFERING_TYPE_KEY + "' and (" +
                    "lui.luiState = '" + LuiServiceConstants.LUI_CO_STATE_OFFERED_KEY + "' or  " +
                    "lui.luiState = '" + LuiServiceConstants.LUI_CO_STATE_CANCELED_KEY + "' )  " +
                "and atp.startDate >= :startDate").setParameter("startDate", startDate, TemporalType.DATE).setParameter("cluId", courseId).getResultList();

        SearchResultInfo resultInfo = new SearchResultInfo();
        resultInfo.setTotalResults(luiIds.size());
        resultInfo.setStartAt(0);

        Map<String,String> luiIds2AlternateCodes = new HashMap<String, String>();

        for (Object[] result : luiIds) {
            SearchResultRowInfo row = new SearchResultRowInfo();
            resultInfo.getRows().add(row);
            String courseOfferingId = (String)result[0];
            String luiIdentifierType = (String)result[1];
            String coCode = (String)result[2];
            boolean isCrossListed = false;
            if (StringUtils.equals(luiIdentifierType,LuiServiceConstants.LUI_IDENTIFIER_CROSSLISTED_TYPE_KEY)){
                isCrossListed = true;
            }
            row.addCell(SearchResultColumns.CO_ID,courseOfferingId);
            row.addCell(SearchResultColumns.IS_CROSS_LISTED,"" + isCrossListed);
            row.addCell(SearchResultColumns.CODE,coCode);
            if (enableCrossListSearch){
                String alternateCodes = luiIds2AlternateCodes.get(courseOfferingId);
                String currentCode = getAlternateCodeUI(coCode,luiIdentifierType);
                if (!StringUtils.contains(alternateCodes,currentCode)){
                    String buildAlternateCodes = StringUtils.defaultString(alternateCodes) + currentCode;
                    luiIds2AlternateCodes.put(courseOfferingId,buildAlternateCodes + ",");
                }
            }
        }

        if (enableCrossListSearch){
            buildCrossListSearchResult(resultInfo,luiIds2AlternateCodes);
        }
        resultInfo.setStartAt(0);
        luiIds2AlternateCodes.clear();

        return resultInfo;
    }

    private void buildCrossListSearchResult(SearchResultInfo resultInfo,Map<String,String> luiIds2AlternateCodes){

        /**
         * If the result needs all the alternate code, iterate all the result set rows and look for
         * the matching Lui. If found, get all the alternate code and add it to the existing result
         * set.
         */
        for (SearchResultRowInfo row : resultInfo.getRows()){

            String courseOfferingCode = row.getCells().get(2).getValue();
            String courseOfferingId = row.getCells().get(0).getValue();
            boolean isCrossListed = BooleanUtils.toBoolean(row.getCells().get(1).getValue());

            String alternateCodes = luiIds2AlternateCodes.get(courseOfferingId);
            String ownerCode;
            String ownerAliases;
            if (!isCrossListed){
                alternateCodes = StringUtils.remove(alternateCodes,courseOfferingCode + OWNER_UI_SUFFIX);
                ownerCode = courseOfferingCode;
            } else {
                alternateCodes = StringUtils.remove(alternateCodes,courseOfferingCode);
                String partOfCodes = alternateCodes.substring(0, alternateCodes.indexOf(OWNER_UI_SUFFIX));
                int idx = alternateCodes.substring(0, alternateCodes.indexOf(OWNER_UI_SUFFIX)).lastIndexOf(",");
                if(idx >= 0){
                    ownerCode = partOfCodes.substring(idx + 1);
                }else{
                    ownerCode = partOfCodes.substring(0);
                }
            }

            ownerAliases = StringUtils.remove(luiIds2AlternateCodes.get(courseOfferingId),ownerCode + OWNER_UI_SUFFIX);
            row.addCell(SearchResultColumns.OWNER_CODE,ownerCode);
            row.addCell(SearchResultColumns.OWNER_ALIASES,ownerAliases);
            row.addCell(SearchResultColumns.CROSS_LISTED_COURSES,alternateCodes);

        }
    }

    /**
     * Append <code>"Owner"</code> if a lui identifier is of official type. This is used for display purpose.
     *
     * @param luiAlternateCode
     * @param luiIdentifierType
     * @return
     */
    protected String getAlternateCodeUI(String luiAlternateCode,String luiIdentifierType){
        if (StringUtils.equals(luiIdentifierType,LuiServiceConstants.LUI_IDENTIFIER_OFFICIAL_TYPE_KEY)){
            return luiAlternateCode + OWNER_UI_SUFFIX;
        } else {
            return luiAlternateCode;
        }
    }

    public void setGenericEntityDao(GenericEntityDao genericEntityDao) {
        this.genericEntityDao = genericEntityDao;
    }

    public void setNoOfYears(String noOfYears) {
        this.noOfYears = noOfYears;
    }
}
