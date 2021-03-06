INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'RiceDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type (Responsibility)' AND NMSPC_CD = 'KR-KEW'), '5D8B0E3E634E96A3E0404F8189D8468D', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'Resolve Exception' AND NMSPC_CD = 'KR-SYS'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'KualiStudentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type (Responsibility)' AND NMSPC_CD = 'KR-KEW'), '5G4F09744G28EF33E0404F8189AAAF24', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'Resolve Exception' AND NMSPC_CD = 'KS-SYS'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '92C8871C-2E74-E6B2-4807-F770473B6976', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationDecisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Publication Decision Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '8716D7C0-D8D2-28DC-6A02-46D60E18771B', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationDecisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'kuali.proposal.type.course.modify', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '0F0F29DF-51DD-DF75-B9CE-46D6C31A8CFC', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationDecisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '9CD7E041-AC7D-D68D-EB5F-4A42C73ACB30', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationDecisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '2AEF5F8F-E09D-90CE-6A95-CB6B1FD09684', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DocOrgReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Document Organization Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '06EC65DC-4E21-C243-378D-7DC160050EC1', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DocOrgReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'kuali.proposal.type.course.modify', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '344A005C-613C-79F3-FCE6-FB89586EA07B', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DocOrgReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), '7B6F5601-DCB1-8EA9-8656-7999962F505C', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DocOrgReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'CluCreditCourseParentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DepartmentReview0000000RSPATTR01', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DepartmentReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Department Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DepartmentReview0000000RSPATTR02', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DepartmentReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DepartmentReview0000000RSPATTR03', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DepartmentReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DepartmentReview0000000RSPATTR04', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DepartmentReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'CluCreditCourseParentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'CollegeReview0000000000RSPATTR01', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'CollegeReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'College Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'CollegeReview0000000000RSPATTR02', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'CollegeReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'CollegeReview0000000000RSPATTR03', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'CollegeReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'CollegeReview0000000000RSPATTR04', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'CollegeReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'CluCreditCourseParentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DivisionReview000000000RSPATTR01', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DivisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Division Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DivisionReview000000000RSPATTR02', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DivisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DivisionReview000000000RSPATTR03', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DivisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'DivisionReview000000000RSPATTR04', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'DivisionReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'CluCreditCourseParentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'SenateReview00000000000RSPATTR01', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'SenateReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Senate Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'SenateReview00000000000RSPATTR02', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'SenateReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'SenateReview00000000000RSPATTR03', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'SenateReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'SenateReview00000000000RSPATTR04', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'SenateReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'CluCreditCourseParentDocument', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'documentTypeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'PublicationReview000000RSPATTR01', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'Publication Review', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'routeNodeName' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'PublicationReview000000RSPATTR02', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'actionDetailsAtRoleMemberLevel' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'PublicationReview000000RSPATTR03', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationReview' AND NMSPC_CD = 'KS-CM'), 1)
/
INSERT INTO KRIM_RSP_ATTR_DATA_T (ATTR_DATA_ID, ATTR_VAL, KIM_ATTR_DEFN_ID, KIM_TYP_ID, OBJ_ID, RSP_ID, VER_NBR)
  VALUES (CONCAT('KS-', KS_RICE_ID_S.NEXTVAL), 'false', (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM = 'required' AND NMSPC_CD = 'KR-WKFLW'), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Document Type, Routing Node & Action Information' AND NMSPC_CD = 'KR-WKFLW'), 'PublicationReview000000RSPATTR04', (SELECT RSP_ID FROM KRIM_RSP_T WHERE NM = 'PublicationReview' AND NMSPC_CD = 'KS-CM'), 1)
/
