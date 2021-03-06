INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Kuali Student Exception Routing', 'Resolve Exception', 'KS-SYS', '5ADFE1V2441D6320E04AAAA189D85169', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Resolve Exception' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Department Review', 'DepartmentReview', 'KS-CM', 'DepartmentReview0000000000000RSP', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for College Review', 'CollegeReview', 'KS-CM', 'CollegeReview0000000000000000RSP', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Division Review', 'DivisionReview', 'KS-CM', 'DivisionReview000000000000000RSP', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Senate Review', 'SenateReview', 'KS-CM', 'SenateReview00000000000000000RSP', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Publication Review', 'PublicationReview', 'KS-CM', 'PublicationReview000000000000RSP', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Publication Decision Review', 'PublicationDecisionReview', 'KS-CM', '87BF0F95-368E-8863-F65E-6AE4FDF96373', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Document Organization Review', 'DocOrgReview', 'KS-CM', '294E015D-D736-802E-2B98-0B8D62B66442', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Review' AND NMSPC_CD='KR_WKFLW'), 1)
/
INSERT INTO KRIM_RSP_T (ACTV_IND, DESC_TXT, NM, NMSPC_CD, OBJ_ID, RSP_ID, RSP_TMPL_ID, VER_NBR)
  VALUES ('Y', 'Responsibility for Kuali Rice Exception Routing', 'Resolve Exception', 'KR-SYS', '5D8B0E3E613996A3E0404F8189D8468D', CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), (SELECT RSP_TMPL_ID FROM KRIM_RSP_TMPL_T WHERE NM='Resolve Exception' AND NMSPC_CD='KR_WKFLW'), 1)
/
