-- KSENROLL-7192
-- Missed subterm relation data
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.group.kuali.atp.type.group.term.kuali.atp.type.HalfFall1', null, 'kuali.type.type.relation.type.group', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.group.term', 'kuali.atp.type.HalfFall1', 1, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.allowed.kuali.atp.type.HalfFall1.kuali.milestone.type.group.instructional', null, 'kuali.type.type.relation.type.allowed', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.HalfFall1', 'kuali.milestone.type.group.instructional', 1, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.allowed.kuali.atp.type.HalfFall1.kuali.milestone.type.group.registration', null, 'kuali.type.type.relation.type.allowed', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.HalfFall1', 'kuali.milestone.type.group.registration', 0, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.group.kuali.atp.type.group.term.kuali.atp.type.HalfFall2', null, 'kuali.type.type.relation.type.group', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.group.term', 'kuali.atp.type.HalfFall2', 1, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.allowed.kuali.atp.type.HalfFall2.kuali.milestone.type.group.instructional', null, 'kuali.type.type.relation.type.allowed', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.HalfFall2', 'kuali.milestone.type.group.instructional', 1, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (ID, OBJ_ID, TYPETYPE_RELTN_TYPE, TYPETYPE_RELTN_STATE, EFF_DT, EXPIR_DT, OWNER_TYPE_ID, RELATED_TYPE_ID, RANK, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('kuali.type.type.relation.type.allowed.kuali.atp.type.HalfFall2.kuali.milestone.type.group.registration', null, 'kuali.type.type.relation.type.allowed', 'kuali.type.type.relation.state.active', null, null, 'kuali.atp.type.HalfFall2', 'kuali.milestone.type.group.registration', 0, 0, TIMESTAMP '2012-05-29 19:00:00', 'SYSTEMLOADER', null, null)
/
INSERT INTO KSEN_TYPETYPE_RELTN (CREATEID,CREATETIME,ID,OWNER_TYPE_ID,RANK,RELATED_TYPE_ID,TYPETYPE_RELTN_STATE,TYPETYPE_RELTN_TYPE,VER_NBR)
  VALUES ('SYSTEMLOADER',TO_DATE( '20120301000000', 'YYYYMMDDHH24MISS' ),'kuali.type.type.relation.type.allowed.kuali.atp.type.AcademicCalendar.kuali.atp.type.HalfFall1','kuali.atp.type.AcademicCalendar',0,'kuali.atp.type.HalfFall1','kuali.type.type.relation.state.active','kuali.type.type.relation.type.allowed',0)
/
INSERT INTO KSEN_TYPETYPE_RELTN (CREATEID,CREATETIME,ID,OWNER_TYPE_ID,RANK,RELATED_TYPE_ID,TYPETYPE_RELTN_STATE,TYPETYPE_RELTN_TYPE,VER_NBR)
  VALUES ('SYSTEMLOADER',TO_DATE( '20120301000000', 'YYYYMMDDHH24MISS' ),'kuali.type.type.relation.type.allowed.kuali.atp.type.AcademicCalendar.kuali.atp.type.HalfFall2','kuali.atp.type.AcademicCalendar',0,'kuali.atp.type.HalfFall2','kuali.type.type.relation.state.active','kuali.type.type.relation.type.allowed',0)
/

-- SUTERM ATTR TO BUILD CODE
INSERT INTO KSEN_TYPE_ATTR (ID, OBJ_ID, ATTR_KEY, ATTR_VALUE, OWNER_ID)
  VALUES ('CD310601C8CE4223958463DB2A194307', 'AEAFD4FCFE734FD6B3D6F3B3EC6C4423', 'kuali.attribute.type.atp.term.type.code', '09','kuali.atp.type.HalfFall1')
/
INSERT INTO KSEN_TYPE_ATTR (ID, OBJ_ID, ATTR_KEY, ATTR_VALUE, OWNER_ID)
  VALUES ('66C88E6B32A64BAC874E1BD6E0370BDB', '9901332A758D49558DF0920FAEF0BBE7', 'kuali.attribute.type.atp.term.type.code', '10','kuali.atp.type.HalfFall2')
/

-- ATP (specific for year/term)
INSERT INTO KSEN_ATP (ATP_CD,ATP_STATE,ATP_TYPE,CREATEID,CREATETIME,DESCR_FORMATTED,DESCR_PLAIN,END_DT,ID,NAME,OBJ_ID,START_DT,UPDATEID,UPDATETIME,VER_NBR)
  VALUES ('201209','kuali.atp.state.Official','kuali.atp.type.HalfFall1','batchjob',TO_DATE( '20130102101250', 'YYYYMMDDHH24MISS' ),'Fall 2012 Sub Term 1','Fall 2012 Sub Term 1',TO_DATE( '20121021000000', 'YYYYMMDDHH24MISS' ),'kuali.atp.2012HalfFall1','Fall 2012 Sub Term 1','5C72D18FC3EA4087B844E264824507D7',TO_DATE( '20120829000000', 'YYYYMMDDHH24MISS' ),'batchjob',TO_DATE( '20130102101250', 'YYYYMMDDHH24MISS' ),0)
/
INSERT INTO KSEN_ATP (ATP_CD,ATP_STATE,ATP_TYPE,CREATEID,CREATETIME,DESCR_FORMATTED,DESCR_PLAIN,END_DT,ID,NAME,OBJ_ID,START_DT,UPDATEID,UPDATETIME,VER_NBR)
  VALUES ('201210','kuali.atp.state.Official','kuali.atp.type.HalfFall2','batchjob',TO_DATE( '20130102101250', 'YYYYMMDDHH24MISS' ),'Fall 2012 Sub Term 2','Fall 2012 Sub Term 2',TO_DATE( '20121211000000', 'YYYYMMDDHH24MISS' ),'kuali.atp.2012HalfFall2','Fall 2012 Sub Term 2','B8629E37663D41DAAB728BE56534B204',TO_DATE( '20121022000000', 'YYYYMMDDHH24MISS' ),'batchjob',TO_DATE( '20130102101250', 'YYYYMMDDHH24MISS' ),0)
/

-- Tie Subterms to Terms
INSERT INTO KSEN_ATPATP_RELTN (ID, OBJ_ID, ATP_TYPE, ATP_STATE, ATP_ID, RELATED_ATP_ID, EFF_DT, EXPIR_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('B7A61D93097F4A2C9BE30E222ECC26A8', 'DB288CE441E848CBABCE6879B9483E80', 'kuali.atp.atp.relation.includes', 'kuali.atp.atp.relation.state.active', 'kuali.atp.2012Fall', 'kuali.atp.2012HalfFall1', TIMESTAMP '2013-02-02 05:13:18', null, 0, TIMESTAMP '2013-05-02 05:12:50', 'batchjob', TIMESTAMP '2013-05-02 05:12:50', 'batchjob')
/
INSERT INTO KSEN_ATPATP_RELTN (ID, OBJ_ID, ATP_TYPE, ATP_STATE, ATP_ID, RELATED_ATP_ID, EFF_DT, EXPIR_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('6F273E88AD7E446099C27BC60EF8AEB1', '5D41F71CEC094698B85012218EFD6F10', 'kuali.atp.atp.relation.includes', 'kuali.atp.atp.relation.state.active', 'kuali.atp.2012Fall', 'kuali.atp.2012HalfFall2', TIMESTAMP '2013-02-02 05:13:18', null, 0, TIMESTAMP '2013-05-02 05:12:50', 'batchjob', TIMESTAMP '2013-05-02 05:12:50', 'batchjob')
/

-- Insert new Milestone dates
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('63295B954B434D168C3957F4F9A4D5FD', '95D5419CA32C4660B9DDF29216D4521C', 'kuali.atp.milestone.AdvancedRegistrationPeriod', 'kuali.atp.state.Official', 'Registration Period', 'Registration Period', 'Registration Period', 0, 0, 0, null, 1, TIMESTAMP '2012-03-15 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('AA22A483607B469C9D99A34C4084687E', '0EFEF0A92F664886BD47D6619B7F30F3', 'kuali.atp.milestone.DropDate', 'kuali.atp.state.Official', 'Last Day to Drop', 'Last Day to Drop', 'Last Day to Drop', 0, 0, 0, null, 0, TIMESTAMP '2012-10-04 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('D1DB43A491B945CEB2E812B4A1B45999', 'EE26C93340F343CB889CD61C059BCFB7', 'kuali.atp.milestone.GradesDue', 'kuali.atp.state.Official', 'Grades Due', 'Grades Due', 'Grades Due', 0, 0, 0, null, 0, TIMESTAMP '2012-10-23 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('785A6B4BFD42482388857C29E0DD8E92', '249065FE4EB64CDBBD5EC1389F42541D', 'kuali.atp.milestone.InstructionalPeriod', 'kuali.atp.state.Official', 'Instructional period', 'Instructional period', 'Instructional period', 0, 0, 0, null, 1, TIMESTAMP '2012-08-29 00:00:00', TIMESTAMP '2012-10-21 00:00:00', 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('CF06F3885A37474DB70330F14A7EF166', '2B6A8D0EA5B740F5A0780728A7B53E23', 'kuali.atp.milestone.lastdayofclasses', 'kuali.atp.state.Official', 'Last Day of Classes', 'Last Day of Classes', 'Last Day of Classes', 0, 0, 0, null, 0, TIMESTAMP '2012-10-21 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('B0868028A5314BE4B2AE9D2DF8B2A801', '8BFEA49FF4594ADC885A9EBE11537CEB', 'kuali.atp.milestone.CourseSelectionPeriodEnd', 'kuali.atp.state.Official', 'Schedule Adjustment Deadline', 'Schedule Adjustment Deadline', 'Schedule Adjustment Deadline', 0, 0, 0, null, 0, TIMESTAMP '2012-10-26 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('62216D73E43E40CCA3610337CA8F9513', 'C07CC344F95745228DC25A58347841EE', 'kuali.atp.milestone.DropDate', 'kuali.atp.state.Official', 'Last Day to Drop', 'Last Day to Drop', 'Last Day to Drop', 0, 0, 0, null, 0, TIMESTAMP '2012-11-27 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('789CF9E03E2544A4B117EE92B919113F', 'B7223F1ADE694CDEB920BDB2E5B24247', 'kuali.atp.milestone.GradesDue', 'kuali.atp.state.Official', 'Grades Due', 'Grades Due', 'Grades Due', 0, 0, 0, null, 0, TIMESTAMP '2012-12-13 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('92D59A864A30461F9143AA3D23179089', '44F9277EDF9F41B98EA6F727CD7BFB51', 'kuali.atp.milestone.InstructionalPeriod', 'kuali.atp.state.Official', 'Instructional period', 'Instructional period', 'Instructional period', 0, 0, 0, null, 1, TIMESTAMP '2012-10-22 00:00:00', TIMESTAMP '2012-12-11 00:00:00', 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('F020007ED15249F98272416A11592003', '870404FA2AD34F6BA780D5A49C37B20C', 'kuali.atp.milestone.firstdayofclasses', 'kuali.atp.state.Official', 'First Day of Classes', 'First Day of Classes', 'First Day of Classes', 0, 0, 0, null, 0, TIMESTAMP '2012-10-22 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/
INSERT INTO KSEN_MSTONE (ID, OBJ_ID, MSTONE_TYPE, MSTONE_STATE, NAME, DESCR_PLAIN, DESCR_FORMATTED, IS_ALL_DAY, IS_INSTRCT_DAY, IS_RELATIVE, RELATIVE_ANCHOR_MSTONE_ID, IS_DATE_RANGE, START_DT, END_DT, VER_NBR, CREATETIME, CREATEID, UPDATETIME, UPDATEID)
  VALUES ('58E3FA52E66340FF919D6EB15CAF6B57', '697397A6AF9D4513913EDCC64D32849B', 'kuali.atp.milestone.lastdayofclasses', 'kuali.atp.state.Official', 'Last Day of Classes', 'Last Day of Classes', 'Last Day of Classes', 0, 0, 0, null, 0, TIMESTAMP '2012-12-11 00:00:00', null, 0, TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob')
/

-- Milestone
--  2012HalfFall1
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('F3246527091F4496B180D32C55A5B937', 'A310A9C18BC54A96BC8BA18457687D1B', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', '63295B954B434D168C3957F4F9A4D5FD')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('8432FCC6535C4F82ADA7965B7472241C', '1077B5C028604BAABF441172563B5B4C', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', '083b0863-0922-4168-92c2-cfb19ca02550')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('4BAA6CCC95674974A80B37130C2D3AB9', 'D3CA5DC451514E7791DF5C6C6FCD4CEE', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', 'e49ac0a4-7b7f-4da9-8db3-26606d11791b')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('A18E5D51503A425BB93DFAE3F2868A46', 'F382BA1CFBB94549BA2F48C1E34EE63A', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', '8bb4c8de-e0a6-46de-8e66-60b4add61fed')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('90F04A321494462BBF00214255331C58', '90D34819291943F791768677519DC3E4', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', 'd0db6be2-670c-4dec-885c-2ac9937a3c5d')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('A52114F72A494F49B0405FB1C9FE9864', 'E44C57FDA614458083A79D9322665CC3', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-6-11 05:13:33', 'kuali.atp.2012HalfFall1', '8b25483a-d9ad-4f57-b2a2-537563f9f701')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('239BA1F735FB430095DB5EFA2E9A5AF9', 'FD213E92ED2145B1B984D5559D6EDF2B', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', 'AA22A483607B469C9D99A34C4084687E')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('EC4D0561C88249AD98A76CE90640D946', 'A55A92E158E94436A226B2DFA6D8654F', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', 'D1DB43A491B945CEB2E812B4A1B45999')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('2C0DB0A7787F4D8DA5E2FD6CBE45584F', 'E40FB1A7F154464B8593CD661DD6F14F', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', '785A6B4BFD42482388857C29E0DD8E92')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('1B79BDC84BC64AAF8F739B25017DB360', 'E4F482B6E8CB4B83A2CFF994FFFA8CA6', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', '2543891a-2d13-40ad-9f91-a45ac0d842b8')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('B99776A5CC644A7CA9C422C4F15B58BC', 'E98AE558009C4F6783C450173675C4D6', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall1', 'CF06F3885A37474DB70330F14A7EF166')
/
--  2012HalfFall2
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('5FF729E6D3C5426AB41174A6F4F0A0E5', 'F12A232505FE408395EC8AB6FD090C6E', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '63295B954B434D168C3957F4F9A4D5FD')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('7E17E2DCD3D24D5682E16D2ED8B28737', '6F67D409A7A246DD90000520CF0FBE9D', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '083b0863-0922-4168-92c2-cfb19ca02550')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('9E79202CF2AD46718FC447CCE0B429EE', '4FED84A6770C4A9EBEA53961F99B9AD7', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', 'e49ac0a4-7b7f-4da9-8db3-26606d11791b')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('A2DE58FCC6584D8EA24256F2DAD00800', '3648D1DE0281442F8A5AD6DD09F75E55', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '8bb4c8de-e0a6-46de-8e66-60b4add61fed')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('664B259821754304AFE1EBB0A09473DB', '08D6F09113454AC8A0FA11C19D75BDC4', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', 'd0db6be2-670c-4dec-885c-2ac9937a3c5d')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('36CF4F6DD5424A5AB518F6ABF21C240A', '0550FC16C59045A3A4160772E3E11C61', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', 'B0868028A5314BE4B2AE9D2DF8B2A801')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('E796CE697B054C618C07909ABE8511EE', '4855E3E115F948B2A62F9C23C76608C3', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '62216D73E43E40CCA3610337CA8F9513')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('D68EF14818D042C0A4EC211068ADB373', 'A609054A7FC7442D81709F584DDAA288', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '789CF9E03E2544A4B117EE92B919113F')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('1ADE60C7E2784A8282092C26D09E1CB2', '7794F2A26D4D4AD4B335AC1A9BDCE566', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '92D59A864A30461F9143AA3D23179089')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('202F398F70684204BEC20327B459F805', '74B99962B26A487E9439CE2FD7D0A5AD', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', 'F020007ED15249F98272416A11592003')
/
INSERT INTO KSEN_ATPMSTONE_RELTN (ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, ATP_ID, MSTONE_ID)
  VALUES ('DC150D7173EE4E49A4EF0221875B212D', '04B97E4BA2604CC198E5AD1F46401D95', 0, 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'batchjob', TIMESTAMP '2013-06-11 05:13:33', 'kuali.atp.2012HalfFall2', '58E3FA52E66340FF919D6EB15CAF6B57')
/