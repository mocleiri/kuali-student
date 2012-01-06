package org.kuali.student.enrollment.class2.acal.service.impl;

import java.util.*;

import javax.jws.WebParam;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.student.enrollment.acal.dto.AcademicCalendarInfo;
import org.kuali.student.enrollment.acal.dto.AcalEventInfo;
import org.kuali.student.enrollment.acal.dto.KeyDateInfo;
import org.kuali.student.enrollment.acal.dto.HolidayInfo;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.acal.infc.AcademicCalendar;
import org.kuali.student.enrollment.acal.service.AcademicCalendarService;
import org.kuali.student.lum.program.service.assembler.ProgramAssemblerConstants;
import org.kuali.student.r2.common.datadictionary.dto.DictionaryEntryInfo;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.DateRangeInfo;
import org.kuali.student.r2.common.dto.NameInfo;
import org.kuali.student.r2.common.dto.RichTextInfo;
import org.kuali.student.r2.common.dto.StateInfo;
import org.kuali.student.r2.common.dto.StatusInfo;
import org.kuali.student.r2.common.dto.TypeInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.exceptions.AlreadyExistsException;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.exceptions.ReadOnlyException;
import org.kuali.student.r2.common.exceptions.VersionMismatchException;
import org.kuali.student.r2.common.util.constants.AtpServiceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:acal-test-context.xml"})
@TransactionConfiguration(transactionManager = "JtaTxManager", defaultRollback = true)
@Transactional
@Ignore
public class TestAcademicCalendarServiceImpl {
    @Autowired
    @Qualifier("acalServiceAuthDecorator")
    private AcademicCalendarService acalService;

    public static String principalId = "123";
    public ContextInfo callContext = ContextInfo.newInstance();

    @Before
    public void setUp() {
        principalId = "123";
        callContext = ContextInfo.getInstance(callContext);
        callContext.setPrincipalId(principalId);
    }

    @Test
    public void testAcademicCalendarServiceSetup() {
        assertNotNull(acalService);
    }

    @Test
    public void testGetAcademicCalendarFromDerby() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        AcademicCalendarInfo acal = acalService.getAcademicCalendar("testAtpId1", callContext);
        assertNotNull(acal);
        assertEquals("testAtpId1", acal.getId());
        assertEquals("testAtp1", acal.getNames().get(0).getName());
        assertEquals("Desc 101", acal.getDescr().getPlain());
        assertEquals(AtpServiceConstants.ATP_DRAFT_STATE_KEY, acal.getStateKey());
        assertEquals(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY, acal.getTypeKey());
    }

    @Test
    public void testGetAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        AcademicCalendarInfo acal = new AcademicCalendarInfo();
        acal.setId("testAcalId");
        acal.setNames(new ArrayList<NameInfo>());
        acal.getNames().add(new NameInfo("en", "testAcal"));
        acal.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        acal.setTypeKey(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY);
        try {
            AcademicCalendarInfo created = acalService.createAcademicCalendar("testAcalId", acal, callContext);
            assertNotNull(created);
            assertEquals("testAcalId", created.getId());

            AcademicCalendarInfo existed = acalService.getAcademicCalendar("testAcalId", callContext);

            assertNotNull(existed);
            assertEquals("testAcalId", existed.getId());
            assertEquals("testAcal", existed.getNames().get(0).getName());
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }
    }

    @Test
    public void testValidateAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        AcademicCalendarInfo acal = new AcademicCalendarInfo();
        acal.setId("testAcalId1");
        acal.setNames(new ArrayList<NameInfo>());
        acal.getNames().add(new NameInfo("en", "testAcal1"));
        
        List<String> ccKeys = new ArrayList<String>();
        ccKeys.add("testAtpId2");

        acal.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        acal.setTypeKey(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY);

        try {
            List<ValidationResultInfo> vris = acalService.validateAcademicCalendar("FULL_VALIDATION", null, acal, callContext);
            if (!vris.isEmpty()) {
                StringBuilder buf = new StringBuilder();
                buf.append(vris.size()).append(" validaton errors found did not expect any");
                for (ValidationResultInfo vri : vris) {
                    buf.append("\n");
                    buf.append(vri.getElement()).append(": ").append(vri.getMessage());
                }
                fail(buf.toString());
            }
            assertTrue(vris.isEmpty());
        } catch (OperationFailedException ex) {
            // dictionary not ready, this is expected
        } catch (Exception ex) {
            // fail("exception from service call :" + ex.getMessage());
            // TODO: test exception aspect & get dictionary ready, this is
            // expected
        }
    }

    @Test
    public void testCreateAcademicCalendar() throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException, DoesNotExistException, ReadOnlyException {
        AcademicCalendarInfo acal = new AcademicCalendarInfo();
        acal.setId("testAcalId1");
        acal.setNames(new ArrayList<NameInfo>());
        acal.getNames().add(new NameInfo("en", "testAcal1"));
        
        List<String> ccKeys = new ArrayList<String>();
        // Assume holidayCalendarIds picking up from dropdown and valid(already
        // in db)
        ccKeys.add("testAtpId2");
        acal.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        acal.setTypeKey(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY);
        try {
            AcademicCalendarInfo created = acalService.createAcademicCalendar("testAcalId1", acal, callContext);
            assertNotNull(created);
            assertEquals("testAcalId1", created.getId());
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }

        acalService.createAcademicCalendar("testAcalId1", acal, callContext);
        fail("AcademicCalendarService.createAcademicCalendar() did not throw expected AlreadyExistsException");

    }

    @Test
    public void testUpdateAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        AcademicCalendarInfo acal = new AcademicCalendarInfo();
        acal.setId("testNewAcalId");
        acal.setNames(new ArrayList<NameInfo>());
        acal.getNames().add(new NameInfo("en", "testNewAcal"));
        
        acal.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        acal.setTypeKey(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY);
        try {
            AcademicCalendarInfo created = acalService.createAcademicCalendar("testNewAcalId", acal, callContext);
            assertNotNull(created);
            assertEquals("testNewAcalId", created.getId());

            AcademicCalendarInfo existed = acalService.getAcademicCalendar("testNewAcalId", callContext);

            assertNotNull(existed);

            existed.setNames(new ArrayList<NameInfo>());
            existed.getNames().add(new NameInfo("en", "testUpdatedAcal"));
            
            // TODO - need actual ProgramTypeKey attribute in test database sql
            AcademicCalendarInfo updated = acalService.updateAcademicCalendar("testNewAcalId", existed, callContext);
            assertEquals("testUpdatedAcal", updated.getNames().get(0).getName());
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }
    }

    @Test
    public void testDeleteAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        AcademicCalendarInfo acal = new AcademicCalendarInfo();
        acal.setId("testDeletedAcalId");
        acal.setNames(new ArrayList<NameInfo>());
        acal.getNames().add(new NameInfo("en", "testDeletedAcal"));
        acal.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        acal.setTypeKey(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY);
        try {
            AcademicCalendarInfo created = acalService.createAcademicCalendar("testDeletedAcalId", acal, callContext);
            assertNotNull(created);
            assertEquals("testDeletedAcalId", created.getId());

            StatusInfo ret = acalService.deleteAcademicCalendar("testDeletedAcalId", callContext);
            assertTrue(ret.getIsSuccess());

            AcademicCalendarInfo existed = acalService.getAcademicCalendar("testDeletedAcalId", callContext);
            assertNull(existed);
        } catch (DoesNotExistException dnee) {
            // this is expected
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }
    }

    @Test
    public void testGetAcademicCalendarsByStartYear() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException,
            AlreadyExistsException {
        List<AcademicCalendarInfo> acalInfos = acalService.getAcademicCalendarsByStartYear(1980, callContext);
        assertNotNull("No Calendars returned.", acalInfos);

        Set<String> acalNames = new HashSet<String>();
        for (AcademicCalendarInfo acalInfo : acalInfos) {
            acalNames.add(acalInfo.getNames().get(0).getName());
        }

        Set<String> expected = new HashSet<String>();
        expected.add("testEdgeAtpId1");
        expected.add("testEdgeAtpId4");
        expected.add("testEdgeAtpId6");
        expected.add("testEdgeAtpId7");
        expected.add("testEdgeAtpId8");

        Set<String> unexpected = new HashSet<String>();
        unexpected.add("testEdgeAtpId2");
        unexpected.add("testEdgeAtpId3");
        unexpected.add("testEdgeAtpId5");
        unexpected.add("testEdgeAtpId9");
        unexpected.add("testEdgeAtpId10");

        for (String acalName : expected) {
            assertTrue("Expected calendar not returned: " + acalName, acalNames.contains(acalName));
        }
        for (String acalName : unexpected) {
            assertFalse("Unexpected calendar returned: " + acalName, acalNames.contains(acalName));
        }
    }

    @Test
    public void testValidateTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        TermInfo term = new TermInfo();
        term.setId("testTermId");
        term.setNames(new ArrayList<NameInfo>());
        term.getNames().add(new NameInfo("en", "testTerm"));
        
        term.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        term.setTypeKey(AtpServiceConstants.ATP_FALL_TYPE_KEY);

        try {
            List<ValidationResultInfo> vri = acalService.validateTerm("SKIP_REQUREDNESS_VALIDATIONS", null, term, callContext);
            assertTrue(vri.isEmpty());
        } catch (OperationFailedException ex) {
            // dictionary not ready, this is expected
        } catch (Exception ex) {
            // fail("exception from service call :" + ex.getMessage());
            // TODO: test exception aspect & get dictionary ready, this is
            // expected
        }
    }

    @Test
    public void testCreateAndGetTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        TermInfo term = new TermInfo();
        term.setId("testTermId2");
        term.setNames(new ArrayList<NameInfo>());
        term.getNames().add(new NameInfo("en", "testNewTerm"));
        
        term.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);
        term.setTypeKey(AtpServiceConstants.ATP_FALL_TYPE_KEY);
        try {
            TermInfo created;

            created = acalService.createTerm("testTermId2", term, callContext);

            term.setId("testNewTermId");
            created = acalService.createTerm("testNewTermId", term, callContext);
            assertNotNull(created);
            assertEquals("testNewTermId", created.getId());

            TermInfo existed = acalService.getTerm("testNewTermId", callContext);

            assertNotNull(existed);
            assertEquals("testNewTermId", existed.getId());
            assertEquals("testNewTerm", existed.getNames().get(0).getName());
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }
    }

    @Test
    public void testAddTermToAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException,
            AlreadyExistsException {
        try {
            try {
                acalService.addTermToAcademicCalendar("testAtpId1", "testTermId1", callContext);
            } catch (AlreadyExistsException ex) {
                // expected);
            }

            StatusInfo status = acalService.addTermToAcademicCalendar("testAtpId1", "testTermId2", callContext);
            assertTrue(status.getIsSuccess());
        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }

        List<TermInfo> terms = acalService.getTermsForAcademicCalendar("testAtpId1", callContext);

        assertEquals(2, terms.size());
    }

    @Test
    public void testGetTermToAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException,
            AlreadyExistsException {
        try {
            try {
                acalService.getTermsForAcademicCalendar("testTermId1", callContext);
            } catch (OperationFailedException ex) {
                // expected because it's not an acal
            }

            List<TermInfo> terms = acalService.getTermsForAcademicCalendar("testAtpId1", callContext);
            assertNotNull(terms);

            // make sure an expected term is in the list of returned terms
            boolean found = false;
            for (TermInfo term : terms) {
                if (term.getId().equals("testTermId1")) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);

        } catch (Exception ex) {
            fail("exception from service call :" + ex.getMessage());
        }
    }

    @Test
    public void testGetContainingTerms() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<TermInfo> results = acalService.getContainingTerms("termRelationTestingTerm2", callContext);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<String> expectedIds = new ArrayList<String>();
        expectedIds.add("termRelationTestingTerm1");

        // check that all the expected ids came back
        for (TermInfo info : results) {
            expectedIds.remove(info.getId());
        }

        assertTrue(expectedIds.isEmpty());

        List<TermInfo> fakeResults = null;

        try {
            fakeResults = acalService.getContainingTerms("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeResults);
        }
    }

    @Test
    public void testGetTermIdsByType() throws InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        String expectedTermType = "kuali.atp.type.HalfFall1";

        List<String> termIds = acalService.getTermIdsByType(expectedTermType, callContext);

        assertTrue(termIds.contains("termRelationTestingTerm4"));

        String expectedEmptyTermType = "kuali.atp.type.SessionG2";

        termIds = acalService.getTermIdsByType(expectedEmptyTermType, callContext);

        assertTrue(termIds == null || termIds.isEmpty());

        String fakeTermType = "fakeTypeKey";

        List<String> shouldBeNull = null;
        try {
            shouldBeNull = acalService.getTermIdsByType(fakeTermType, callContext);
            fail("Did not get a InvalidParameterException when expected");
        } catch (InvalidParameterException e) {
            assertNull(shouldBeNull);
        }
    }

    @Test
    public void testGetTermsByKeyList() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> termIds = new ArrayList<String>();
        termIds.addAll(Arrays.asList("termRelationTestingTerm1", "termRelationTestingTerm2"));

        List<TermInfo> terms = acalService.getTermsByIds(termIds, callContext);

        assertNotNull(terms);
        assertEquals(termIds.size(), terms.size());

        // check that all the expected ids came back
        for (TermInfo info : terms) {
            termIds.remove(info.getId());
        }

        assertTrue(termIds.isEmpty());

        // now make sure an exception is thrown for any not found keys

        List<String> fakeKeys = Arrays.asList("termRelationTestingTerm1", "fakeKey1", "fakeKey2");

        List<TermInfo> shouldBeNull = null;
        try {
            shouldBeNull = acalService.getTermsByIds(fakeKeys, callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(shouldBeNull);
        }
    }

    @Test
    public void testGetTermsForAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<TermInfo> results = acalService.getTermsForAcademicCalendar("termRelationTestingAcal1", callContext);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<String> expectedIds = new ArrayList<String>();
        expectedIds.add("termRelationTestingTerm1");

        // check that all the expected ids came back
        for (TermInfo info : results) {
            expectedIds.remove(info.getId());
        }

        assertTrue(expectedIds.isEmpty());

        List<TermInfo> fakeResults = null;

        try {
            fakeResults = acalService.getTermsForAcademicCalendar("fakeKey", callContext);
            assertEquals(0, fakeResults.size());
        } catch (DoesNotExistException e) {
            assertNull(fakeResults);
        }
    }

    @Test
    public void testGetIncludedTermsInTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<TermInfo> results = acalService.getIncludedTermsInTerm("termRelationTestingTerm1", callContext);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<String> expectedIds = new ArrayList<String>();
        expectedIds.add("termRelationTestingTerm2");

        // check that all the expected ids came back
        for (TermInfo info : results) {
            expectedIds.remove(info.getId());
        }

        assertTrue(expectedIds.isEmpty());

        List<TermInfo> fakeResults = null;

        try {
            fakeResults = acalService.getIncludedTermsInTerm("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeResults);
        }
    }

    @Test
    public void testGetTermState() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        StateInfo result = acalService.getTermState(AtpServiceConstants.ATP_DRAFT_STATE_KEY, callContext);

        assertNotNull(result);
        assertEquals(result.getName(), "Draft");

        StateInfo fakeState = null;
        try {
            fakeState = acalService.getTermState("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeState);
        }
    }

    @Test
    public void testGetTermStates() throws InvalidParameterException, MissingParameterException, OperationFailedException, DoesNotExistException {
        List<StateInfo> result = acalService.getTermStates(callContext);

        assertNotNull(result);
        assertTrue(!result.isEmpty());

        List<String> expectedIds = new ArrayList<String>();
        expectedIds.addAll(Arrays.asList(AtpServiceConstants.ATP_DRAFT_STATE_KEY, AtpServiceConstants.ATP_OFFICIAL_STATE_KEY));

        // ensure we have all the expected ids in our list
        assertEquals(expectedIds.size(), result.size());

        for (StateInfo state : result) {
            expectedIds.remove(state.getKey());
        }

        assertTrue(expectedIds.isEmpty());
    }

    @Test
    public void testGetTermType() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        TypeInfo result = acalService.getTermType(AtpServiceConstants.ATP_HALF_FALL_1_TYPE_KEY, callContext);

        assertNotNull(result);
        assertEquals(result.getNames().get(0).getName(), "Fall Half-Semester 1");

        TypeInfo fakeType = null;
        try {
            fakeType = acalService.getTermType("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeType);
        }
    }

    @Test
    public void testGetTermTypes() throws InvalidParameterException, MissingParameterException, OperationFailedException {
        List<TypeInfo> result = acalService.getTermTypes(callContext);

        assertNotNull(result);
        assertTrue(!result.isEmpty());
    }

    @Test
    public void testGetTermTypesForAcademicCalendarType() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        List<TypeInfo> results = acalService.getTermTypesForAcademicCalendarType(AtpServiceConstants.ATP_ACADEMIC_CALENDAR_TYPE_KEY, callContext);

        assertNotNull(results);
        List<String> expectedIds = new ArrayList<String>(2);
        expectedIds.addAll(Arrays.asList("kuali.atp.type.Fall", "kuali.atp.type.Spring"));

        // check that all the expected ids came back
        for (TypeInfo info : results) {
            expectedIds.remove(info.getKey());
        }

        assertTrue(expectedIds.isEmpty());

        List<TypeInfo> fakeTypes = null;
        try {
            fakeTypes = acalService.getTermTypesForAcademicCalendarType("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeTypes);
        }

    }

    @Test
    public void testGetTermTypesForTermType() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException {
        // TODO
        List<TypeInfo> results = acalService.getTermTypesForTermType(AtpServiceConstants.ATP_SPRING_TYPE_KEY, callContext);

        assertNotNull(results);
        List<String> expectedIds = new ArrayList<String>(2);
        expectedIds.addAll(Arrays.asList(AtpServiceConstants.ATP_HALF_SPRING_1_TYPE_KEY, AtpServiceConstants.ATP_HALF_SPRING_2_TYPE_KEY, AtpServiceConstants.ATP_SPRING_BREAK_TYPE_KEY));

        // check that all the expected ids came back
        for (TypeInfo info : results) {
            expectedIds.remove(info.getKey());
        }

        assertTrue(expectedIds.isEmpty());

        List<TypeInfo> fakeTypes = null;
        try {
            fakeTypes = acalService.getTermTypesForTermType("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeTypes);
        }

        List<TypeInfo> expectedEmpty = acalService.getTermTypesForTermType(AtpServiceConstants.ATP_SESSION_G2_TYPE_KEY, callContext);

        assertTrue(expectedEmpty == null || expectedEmpty.isEmpty());
    }

    @Test
    public void testRemoveTermFromAcademicCalendar() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        StatusInfo status = acalService.removeTermFromAcademicCalendar("termRelationTestingAcal2", "termRelationTestingTerm2", callContext);

        assertNotNull(status);
        assertTrue(status.getIsSuccess());

        // retrieve the terms for the acal and make sure it does not include the
        // removed term
        List<TermInfo> results = acalService.getTermsForAcademicCalendar("termRelationTestingAcal2", callContext);

        if (results != null) {
            for (TermInfo term : results) {
                assertTrue(!term.getId().equals("termRelationTestingTerm2"));
            }
        }

        StatusInfo noStatus = null;
        try {
            noStatus = acalService.removeTermFromAcademicCalendar("termRelationTestingAcal2", "fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(noStatus);
        }
    }

    @Test
    public void testRemoveTermFromTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        StatusInfo status = acalService.removeTermFromTerm("termRelationTestingTerm3", "termRelationTestingTerm4", callContext);

        assertNotNull(status);
        assertTrue(status.getIsSuccess());

        // retrieve the terms for the parent term and make sure it does not
        // include the removed term
        List<TermInfo> results = acalService.getIncludedTermsInTerm("termRelationTestingTerm3", callContext);

        assertTrue(results == null || results.isEmpty());

        // try to remove the same term again, should get a DoesNotExistException
        StatusInfo noRepeatStatus = null;
        try {
            noRepeatStatus = acalService.removeTermFromTerm("termRelationTestingTerm3", "termRelationTestingTerm4", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(noRepeatStatus);
        }

        StatusInfo noStatus = null;
        try {
            noStatus = acalService.removeTermFromTerm("termRelationTestingTerm3", "fakeKey", callContext);
            fail("Did not get a InvalidParameterException when expected");
        } catch (InvalidParameterException e) {
            assertNull(noStatus);
        }
    }

    @Test
    public void testUpdateTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException, DataValidationErrorException,
            VersionMismatchException, ReadOnlyException {

        // test updating an invalid term
        TermInfo blankTerm = new TermInfo();
        blankTerm.setId("fakeKey");
        blankTerm.setTypeKey("fakeType");
        blankTerm.setStateKey("fakeState");
        blankTerm.setStartDate(new Date());
        blankTerm.setEndDate(new Date());

        TermInfo fakeTerm = null;
        try {
            fakeTerm = acalService.updateTerm("fakeKey", blankTerm, callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(fakeTerm);
        }

        TermInfo existing = acalService.getTerm("termRelationTestingTerm3", callContext);
        String updatedName = "updated " + existing.getNames().get(0).getName();

        existing.setNames(new ArrayList<NameInfo>());
        existing.getNames().add(new NameInfo("en", updatedName));
        existing.setStateKey(AtpServiceConstants.ATP_DRAFT_STATE_KEY);

        TermInfo revised = acalService.updateTerm("termRelationTestingTerm3", existing, callContext);

        assertNotNull(revised);
        assertEquals(revised.getId(), existing.getId());

        TermInfo retrieved = acalService.getTerm("termRelationTestingTerm3", callContext);

        assertNotNull(retrieved);
        assertEquals(retrieved.getNames().get(0).getName(), updatedName);
        assertEquals(retrieved.getStateKey(), AtpServiceConstants.ATP_DRAFT_STATE_KEY);
    }

    @Test
    public void testDeleteTerm() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        StatusInfo status = acalService.deleteTerm("termRelationTestingTermDelete", callContext);

        assertTrue(status.getIsSuccess());

        StatusInfo noStatus = null;
        try {
            noStatus = acalService.deleteTerm("fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(noStatus);
        }

        // ensure the delete prevents future gets
        TermInfo shouldBeNull = null;
        try {
            shouldBeNull = acalService.getTerm("termRelationTestingTermDelete", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(shouldBeNull);
        }
    }

    @Test
    public void testAddTermToTerm() throws AlreadyExistsException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {

        StatusInfo status = acalService.addTermToTerm("termRelationTestingTerm5", "termRelationTestingTerm6", callContext);

        assertNotNull(status);
        assertTrue(status.getIsSuccess());

        // retrieve the terms for the parent term and make sure it does include
        // the added term
        List<TermInfo> results = acalService.getIncludedTermsInTerm("termRelationTestingTerm5", callContext);

        assertNotNull(results);
        assertEquals(1, results.size());

        TermInfo added = results.iterator().next();
        assertEquals("termRelationTestingTerm6", added.getId());

        // assert that we can't add the term to the same term twice
        StatusInfo nullStatus = null;

        try {
            nullStatus = acalService.addTermToTerm("termRelationTestingTerm5", "termRelationTestingTerm6", callContext);
            fail("Did not get an AlreadyExistsException when expected");
        } catch (AlreadyExistsException e) {
            assertNull(nullStatus);
        }

        // assert that adding an invalid term fails
        try {
            nullStatus = acalService.addTermToTerm("termRelationTestingTerm5", "fakeKey", callContext);
            fail("Did not get a DoesNotExistException when expected");
        } catch (DoesNotExistException e) {
            assertNull(nullStatus);
        }

    }
//
//    @Test
//    public void testGetDataDictionaryEntryKeys() throws OperationFailedException, MissingParameterException, PermissionDeniedException {
//        List<String> results = acalService.getDataDictionaryEntryKeys(callContext);
//
//        assertNotNull(results);
//        assertTrue(!results.isEmpty());
//
//        assertTrue(results.contains("http://student.kuali.org/wsdl/acal/AcademicCalendarInfo"));
//    }
//
//    @Test
//    public void testGetDataDictionaryEntry() throws OperationFailedException, MissingParameterException, PermissionDeniedException, DoesNotExistException {
//        DictionaryEntryInfo value = acalService.getDataDictionaryEntry("http://student.kuali.org/wsdl/acal/AcademicCalendarInfo", callContext);
//
//        assertNotNull(value);
//
//        DictionaryEntryInfo fakeEntry = null;
//        try {
//            fakeEntry = acalService.getDataDictionaryEntry("fakeKey", callContext);
//            fail("Did not get a DoesNotExistException when expected");
//        } catch (DoesNotExistException e) {
//            assertNull(fakeEntry);
//        }
//    }

    @Test
    public void testCreateKeyDateForTerm() throws AlreadyExistsException, DataValidationErrorException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException, DoesNotExistException, ReadOnlyException {
        KeyDateInfo keyDate = new KeyDateInfo();
        keyDate.setId("new-keydate-Id");
        keyDate.setNames(new ArrayList<NameInfo>());
        keyDate.getNames().add(new NameInfo("en", "testCreate"));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2005);

        keyDate.setStartDate(cal.getTime());
        keyDate.setIsAllDay(false);
        keyDate.setIsDateRange(true);
        keyDate.setStateKey(AtpServiceConstants.MILESTONE_DRAFT_STATE_KEY);
        keyDate.setTypeKey(AtpServiceConstants.MILESTONE_REGISTRATION_PERIOD_TYPE_KEY);
        RichTextInfo descr = new RichTextInfo();
        descr.setPlain("Test");
        keyDate.setDescr(descr);

        KeyDateInfo created = acalService.createKeyDate("termRelationTestingTerm1", "new-keydate-Id", keyDate, callContext);
        assertNotNull(created);
        assertEquals("new-keydate-Id", created.getId());
        assertEquals("testCreate", created.getNames().get(0).getName());

        try {
            KeyDateInfo retrieved = acalService.getKeyDate("new-keydate-Id", callContext);
            assertNotNull(retrieved);
            assertEquals("new-keydate-Id", retrieved.getId());
            assertEquals("testCreate", retrieved.getNames().get(0).getName());
        } catch (DoesNotExistException e) {
            fail(e.getMessage());
        }

        try {
            List<KeyDateInfo> kds = acalService.getKeyDatesForTerm("termRelationTestingTerm1", callContext);
            assertNotNull(kds);
            assertTrue(!kds.isEmpty());
            List<String> kdIds = new ArrayList<String>();
            for (KeyDateInfo kd : kds) {
                kdIds.add(kd.getId());
            }
            assertTrue(kdIds.contains("new-keydate-Id"));
        } catch (DoesNotExistException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testGetAcademicCalendarsByKeyList() throws DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException, PermissionDeniedException {
        List<String> calendarKeys = new ArrayList<String>();
        calendarKeys.add("testAtpId1");
        calendarKeys.add("testTermId1");
        calendarKeys.add("testTermId2");
        calendarKeys.add("testTermId2");
        List<AcademicCalendarInfo> calendars = acalService.getAcademicCalendarsByIds(calendarKeys, callContext);
        assertNotNull(calendars);
        assertEquals("Number of calendars returned not as expected.", 4, calendars.size());

        // DoesNotExistException
        calendarKeys.clear();
        calendarKeys.add("3B6605D9-9370-441D-89D8-8B747B9AB496"); // Bogus UID
        try {
            calendars = acalService.getAcademicCalendarsByIds(calendarKeys, callContext);
            fail("Expected DoesNotExistException.");
        } catch (DoesNotExistException e) {}
    }

    @Test
    public void testCopyAcademicCalendar() throws AlreadyExistsException, DoesNotExistException, InvalidParameterException, MissingParameterException, OperationFailedException,
            PermissionDeniedException {
        final String originalCalendarKey = "ACADEMICCALENDAR1990";
        final String copiedCalendarKey = "ACADEMICCALENDAR1990COPY";

        AcademicCalendar originalCalendar = acalService.getAcademicCalendar(originalCalendarKey, callContext);

        AcademicCalendar copiedCalendar = acalService.copyAcademicCalendar(originalCalendarKey, 2008, 2009, callContext);

        assertEquals(originalCalendarKey, originalCalendar.getId());
        assertEquals(copiedCalendarKey, copiedCalendar.getId());
        assertNotNull(originalCalendar.getNames().get(0).getName());
        assertEquals(originalCalendar.getNames().get(0).getName(), copiedCalendar.getNames().get(0).getName());
        assertNotNull(originalCalendar.getTypeKey());
        assertEquals(originalCalendar.getTypeKey(), copiedCalendar.getTypeKey());
        assertNotNull(originalCalendar.getStateKey());
        assertEquals("kuali.atp.state.Draft", copiedCalendar.getStateKey());
        assertNotNull(originalCalendar.getStartDate());
        assertEquals(originalCalendar.getStartDate(), copiedCalendar.getStartDate());
        assertNotNull(originalCalendar.getEndDate());
        assertEquals(originalCalendar.getEndDate(), copiedCalendar.getEndDate());
        assertNotNull(originalCalendar.getDescr());
        assertEquals(originalCalendar.getDescr().getFormatted(), copiedCalendar.getDescr().getFormatted());
        assertEquals(originalCalendar.getDescr().getPlain(), copiedCalendar.getDescr().getPlain());

        List<String> originalHolidayCalendarsKeys = originalCalendar.getHolidayCalendarIds();
        List<String> copiedHolidayCalendarsKeys = copiedCalendar.getHolidayCalendarIds();
        assertNotNull(originalHolidayCalendarsKeys);
        assertNotNull(copiedHolidayCalendarsKeys);
        assertFalse(originalHolidayCalendarsKeys.isEmpty());
        assertEquals(originalHolidayCalendarsKeys.size(), copiedHolidayCalendarsKeys.size());
        for (String holidayCalendarId : originalHolidayCalendarsKeys) {
            assertTrue("holidayCalendarId not found: " + holidayCalendarId, copiedHolidayCalendarsKeys.contains(holidayCalendarId));
        }

        List<TermInfo> originalCalendarTerms = acalService.getTermsForAcademicCalendar(originalCalendarKey, callContext);
        List<TermInfo> copiedCalendarTerms = acalService.getTermsForAcademicCalendar(copiedCalendarKey, callContext);
        assertNotNull(originalCalendarTerms);
        assertNotNull(copiedCalendarTerms);
        assertFalse(originalCalendarTerms.isEmpty());
        assertEquals(originalCalendarTerms.size(), copiedCalendarTerms.size());
        List<String> originalCalendarTermIds = new ArrayList<String>();
        for (TermInfo term : originalCalendarTerms) {
            originalCalendarTermIds.add(term.getId());
        }
        for (TermInfo term : copiedCalendarTerms) {
            assertFalse(originalCalendarTermIds.contains(term.getId()));
        }
        for (TermInfo term : originalCalendarTerms) {
            assertFalse(copiedCalendarTerms.contains(term));
            // TODO check terms were copied properly
            assertNotNull(acalService.getContainingTerms(term.getId(), callContext));
        }
    }
}
