package org.kuali.student.ap.framework.course;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.student.ap.framework.course.CreditsFormatter;
import org.kuali.student.r2.common.dto.AttributeInfo;
import org.kuali.student.r2.lum.course.dto.CourseInfo;
import org.kuali.student.r2.lum.lrc.dto.ResultValuesGroupInfo;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testKsapFrameworkInit-context.xml" })
public class CreditsFormatterTest {
    @Test
    public void formatCreditsNullEmptyAndFixed() {
        CourseInfo courseInfo = new CourseInfo();

        //  Null credit options list.
        assertEquals("", CreditsFormatter.formatCredits(courseInfo));

        //  Empty credit options list.
        List<ResultValuesGroupInfo> creditOptions = new ArrayList<ResultValuesGroupInfo>();
        courseInfo.setCreditOptions(creditOptions);

        assertEquals("", CreditsFormatter.formatCredits(courseInfo));

        //  Credit options: fixed.
        String creditsText = "3";
        ResultValuesGroupInfo rci = new ResultValuesGroupInfo();
        rci.setTypeKey("kuali.result.values.group.type.fixed");
        List<AttributeInfo> courseOptionAttributes = new java.util.LinkedList<AttributeInfo>();
        courseOptionAttributes.add(new AttributeInfo("fixedCreditValue", creditsText));
        rci.setAttributes(courseOptionAttributes);
        creditOptions.add(rci);

        assertEquals(creditsText, CreditsFormatter.formatCredits(courseInfo));
    }

    @Test
    public void formatCreditsMultiple() {

        CourseInfo courseInfo = new CourseInfo();
        List<ResultValuesGroupInfo> creditOptions = new ArrayList<ResultValuesGroupInfo>();

        //  Credit options: list.
        String creditsText = "1, 2, 5, 20";
        ResultValuesGroupInfo rci = new ResultValuesGroupInfo();
        rci.setTypeKey("kuali.result.values.group.type.multiple");

        List<String> resultValues = new ArrayList<String>();
        resultValues.add("kuali.result.value.credit.degree.1.0");
        resultValues.add("kuali.result.value.credit.degree.2.0");
        resultValues.add("kuali.result.value.credit.degree.5.0");
        resultValues.add("kuali.result.value.credit.degree.20.0");

        rci.setResultValueKeys(resultValues);

        creditOptions.add(rci);

        courseInfo.setCreditOptions(creditOptions);

        assertEquals(creditsText, CreditsFormatter.formatCredits(courseInfo));
    }

    @Test
    public void formatCreditsRange() {
        CourseInfo courseInfo = new CourseInfo();
        List<ResultValuesGroupInfo> creditOptions = new ArrayList<ResultValuesGroupInfo>();

        //  Credit options: list.
        String creditsText = "1.5 - 25";
        ResultValuesGroupInfo rci = new ResultValuesGroupInfo();
        rci.setTypeKey("kuali.result.values.group.type.range");

        List<AttributeInfo> courseOptionAttributes = new java.util.LinkedList<AttributeInfo>();
        courseOptionAttributes.add(new AttributeInfo("minCreditValue", "1.5"));
        courseOptionAttributes.add(new AttributeInfo("maxCreditValue", "25"));

        rci.setAttributes(courseOptionAttributes);

        creditOptions.add(rci);

        courseInfo.setCreditOptions(creditOptions);

        assertEquals(creditsText, CreditsFormatter.formatCredits(courseInfo));
    }

    @Test
    public void unknownCreditType() {
        CourseInfo courseInfo = new CourseInfo();

        List<ResultValuesGroupInfo> creditOptions = new ArrayList<ResultValuesGroupInfo>();
        courseInfo.setCreditOptions(creditOptions);

        //  Credit options: fixed.
        String creditsText = "0";

        ResultValuesGroupInfo rci = new ResultValuesGroupInfo();
        rci.setTypeKey("Unknown");

        List<AttributeInfo> courseOptionAttributes = new java.util.LinkedList<AttributeInfo>();
        courseOptionAttributes.add(new AttributeInfo("fixedCreditValue", "25"));
        rci.setAttributes(courseOptionAttributes);
        creditOptions.add(rci);

        assertEquals(creditsText, CreditsFormatter.formatCredits(courseInfo));
    }

    @Test
    public void trim() {
        assertEquals("1", CreditsFormatter.trimCredits("1"));
        assertEquals("1", CreditsFormatter.trimCredits("1.0"));
        assertEquals("1.5", CreditsFormatter.trimCredits("1.5"));
        assertEquals("1.5", CreditsFormatter.trimCredits("1.5 "));
        assertEquals("1.5", CreditsFormatter.trimCredits(" 1.5 "));
        assertEquals("", CreditsFormatter.trimCredits(null));
        assertEquals("", CreditsFormatter.trimCredits(" "));
    }
}
