INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.hold', 'Hold Check', 'A Check to the Hold Service.', 'A729A232-F862-27D8-AC5B-8FC0751F2F8C', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.milestone.startdate', 'Start Date Check', 'A Check to the ATP Service for the start of a Milestone.', 'A729A233-F822-3E86-6632-8E58B8BE3AA9', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.milestone.deadline', 'Deadline Check', 'A Check to the ATP Service for the end of a Milestone.', 'A729A234-B242-3994-23EF-447C3AEA4084', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.milestone.period', 'Time Period Check', 'A Check to the ATP Service for the date range of a Milestone.', 'A729A235-C648-4C87-2568-A08CC9F1FEE2', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.process', 'Process Check', 'A Check that depends on another Process in the Process Service.', 'A729A236-A5B8-9514-D6E0-D39908947833', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.rule.direct', 'Direct Rule Check', 'A Check based on a known or nameable rule (Agenda).', 'A729A237-C8ED-FB9E-6EE2-13DA41ABB8AD', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.rule.indirect', 'Indirect Rule Check', 'A Check to some logic (rule) to determine another rule to eveluate.', 'A729A238-D213-B9D5-FC64-E93D58ADEC6C', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.value.equals', 'Equal Value Check', 'A Check to determine if a value is equal to the value specified in the Check.', 'A729A239-A2EC-157C-5410-A867B59A669D', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.value.max', 'Maximum Value Check', 'A Check to determine if a value is equal to or less than the value specified in the Check.', 'A729A23A-C0C5-198B-4325-791151118824', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_CHECK_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.check.type.value.min', 'Minimum Value Check', 'A Check to determine if a value is equal to or greater than the value specified in the Check.', 'A729A23B-BD47-9A26-BCE5-656CB259FC9A', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_INSTR_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.instruction.type', 'Instruction', 'An Instruction.', 'A729A23F-0BAF-80C0-24A3-C49AD174E08A', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)
INSERT INTO KSEN_PROCESS_TYPE ( TYPE_KEY, NAME, TYPE_DESC, OBJ_ID, VER_NBR, EFF_DT, EXPIR_DT, REF_OBJECT_URI) VALUES ( 'kuali.process.process.type', 'Process', 'A Process.', 'A4956D5C-DF3D-BC23-2691-EE8C7CC36DDE', 0, {ts '2010-01-01 00:00:00.0'}, NULL, NULL)

INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.check.state.enabled', 'Enabled', 'kuali.process.check.lifecycle', 'Indicates that this Check is active and should be checked across all Processes.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.check.state.disabled', 'Disabled', 'kuali.process.check.lifecycle', 'Indicates that this Check is disabled across all Processes and should be skipped with a success.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.check.state.inactive', 'Inactive', 'kuali.process.check.lifecycle', 'Indicates that this Check is inactive (out to pasture) across all Processes and should fail.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.instruction.state.enabled', 'Enabled', 'kuali.process.instruction.lifecycle', 'Indicates that this Instruction is active and enabled.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.instruction.state.disabled', 'Disabled', 'kuali.process.instruction.lifecycle', 'Indicates that this Instruction is disabled and should be skipped.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.process.state.enabled', 'Enabled', 'kuali.process.process.lifecycle', 'Indicates that this Process is enabled.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.process.state.disabled', 'Disabled', 'kuali.process.process.lifecycle', 'Indicates that this Process is disabled and should be skipped resulting in success.', 0)
INSERT INTO KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) VALUES ('kuali.process.process.state.inactive', 'Inactive', 'kuali.process.process.lifecycle', 'Indicates that this Process is inactive because it was put out to pasture. Any checks for this process should fail.', 0)

INSERT INTO KSEN_CHECK_RICH_TEXT (ID, FORMATTED, PLAIN,VER_NBR) VALUES ('StudentPaidTuitonCheckDesc', '<p>Student Paid Tuiton</p>', 'Student Paid Tuiton', 0)
INSERT INTO KSEN_CHECK ( ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, NAME, RT_DESCR_ID, STATE_ID, TYPE_ID, ISSUE_ID, MILESTONE_TYPE_ID, AGENDA_ID, PROCESS_ID) VALUES ( 'StudentPaidTuitonCheck', 'A63166D6-B6EB-A489-773E-6B815D2AAC9E', 0, 'admin', {ts '2010-01-01 00:00:00.0'}, 'admin', {ts '2010-01-01 00:00:00.0'}, 'Check Student Paid Tuiton', 'StudentPaidTuitonCheckDesc', 'kuali.process.check.state.enabled', 'kuali.process.check.type.hold', 'Hold-Issue-1', 'kuali.atp.milestone.RegistrationPeriod', 'agendaId-1', NULL)

INSERT INTO KSEN_PROCESS_RICH_TEXT (ID, FORMATTED, PLAIN,VER_NBR) VALUES ('StudentEligibleForRegistrationThisTermProcessDesc', '<p>Student Eligible For Registration This Term</p>', 'Student Eligible For Registration This Term', 0)
INSERT INTO KSEN_PROCESS( ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, NAME, RT_DESCR_ID, STATE_ID, TYPE_ID, OWNER_ORG_ID) VALUES ( 'StudentEligibleForRegistrationThisTermProcess', 'A4956D5D-C087-82D0-460D-EF2948C0DFF1', 0, 'admin', {ts '2010-01-01 00:00:00.0'}, 'admin', {ts '2010-01-01 00:00:00.0'}, 'Student Eligible For Registration This Term', 'StudentEligibleForRegistrationThisTermProcessDesc', 'kuali.process.process.state.enabled', 'kuali.process.process.type', NULL)

INSERT INTO KSEN_INSTR_MESSAGE ( ID, OBJ_ID, PLAIN, FORMATTED, VER_NBR) VALUES ('InstructionMessage', '6DAA013E-FC02-3C0D-8334-B40EDAE47741', 'Instruction Message', '<p>Instruction Message</p>', 0);
INSERT INTO KSEN_INSTR ( ID, OBJ_ID, VER_NBR, CREATEID, CREATETIME, UPDATEID, UPDATETIME, STATE_ID, TYPE_ID, EFF_DT, EXPIR_DT, PROCESS_ID, CHECK_ID, MESSAGE, POSITION, IS_WARNING, CONTINUE_ON_FAIL, IS_EXEMPTABLE) VALUES ( 'StudentEligibleForRegistrationThisTermProcessStudentPaidTuitonCheckInstruction', 'A63166D7-B74B-F176-CF32-E877901373E1', 0, 'admin', {ts '2010-01-01 00:00:00.0'}, 'admin', {ts '2010-01-01 00:00:00.0'}, 'kuali.process.instruction.state.enabled', 'kuali.process.instruction.type', {ts '2010-01-01 00:00:00.0'}, NULL, 'StudentEligibleForRegistrationThisTermProcess', 'StudentPaidTuitonCheck', 'InstructionMessage', 1, 0, 1, 1)
