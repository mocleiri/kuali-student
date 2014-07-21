package org.kuali.student.ap.coursesearch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.kuali.student.ap.framework.config.KsapFrameworkServiceLocator;
import org.kuali.student.ap.framework.util.KsapHelperUtil;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.core.search.dto.SearchRequestInfo;
import org.kuali.student.r2.core.search.infc.SearchResult;
import org.kuali.student.r2.core.search.infc.SearchResultRow;
import org.kuali.student.r2.lum.clu.service.CluService;
@Deprecated
public class CoursePreReqSearch {

    private CluService cluService;

    protected CluService getCluService() {
        if (this.cluService == null) {
            this.cluService = KsapFrameworkServiceLocator.getCluService();
        }
        return this.cluService;
    }

    public void setCluService(CluService luService) {
        this.cluService = luService;
    }


    /**
     *
     * @param subject eg "A A", "CHEM", aka division
     *
     * @return
     */
    public List<String> getCoursePreReqBySubject( String subject, ContextInfo context ) {
        try {

            ArrayList<String> courseList = new ArrayList<String>();
            SearchRequestInfo req = new SearchRequestInfo( "ksap.course.prereqsearch.subject" );
            req.addParam( "subject", subject );
            SearchResult result = getCluService().search( req, context );
            for (SearchResultRow row : result.getRows()) {
                String cluid = KsapHelperUtil.getCellValue(row, "lu.resultColumn.cluId");
                courseList.add( cluid );
            }
            return courseList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCoursePreReqBySubjectAndRange( String subject, String range, ContextInfo context ) {
        try {
            if( range == null )
            {
                throw new NullPointerException( "range" );
            }
            if( range.length() != 3 )
            {
                throw new IllegalArgumentException( "range must be 3 chars" );
            }
            range = range.toUpperCase().replace( "X", "_" );

            ArrayList<String> courseList = new ArrayList<String>();
            SearchRequestInfo req = new SearchRequestInfo( "ksap.course.prereqsearch.range" );
            req.addParam( "subject", subject );
            req.addParam( "range", range );
            SearchResult result = getCluService().search( req, context );
            for (SearchResultRow row : result.getRows()) {
                String cluid = KsapHelperUtil.getCellValue( row, "lu.resultColumn.cluId");
                courseList.add( cluid );
            }
            return courseList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCoursePreReqWithExclusions( String subject, String range, Set<String> excludeList, ContextInfo context ) {
        try {
            if( range == null )
            {
                throw new NullPointerException( "range" );
            }
            if( range.length() != 3 )
            {
                throw new IllegalArgumentException( "range must be 3 chars" );
            }
            range = range.toUpperCase().replace( "X", "_" );

            ArrayList<String> courseList = new ArrayList<String>();
            SearchRequestInfo req = new SearchRequestInfo( "ksap.course.prereqsearch.exclusions" );
            req.addParam( "subject", subject );
            req.addParam( "range", range );
            SearchResult result = getCluService().search( req, context );
            for (SearchResultRow row : result.getRows()) {
                String cluid = KsapHelperUtil.getCellValue( row, "lu.resultColumn.cluId");
                String code = KsapHelperUtil.getCellValue( row, "lu.resultColumn.luOptionalCode");
                if( !excludeList.contains( code ))
                {
                    courseList.add( cluid );
                }
            }
            return courseList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
