--Check Rule and proposition types.
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','kuali.krms.type.check.name','KS-SYS','ruleTypeService','kuali.krms.type.check',0)
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','kuali.krms.proposition.type.custom.name','KS-SYS','customPropositionTypeService','kuali.krms.type.custom',0)
/
INSERT INTO KRMS_TYP_T (ACTV,NM,NMSPC_CD,SRVC_NM,TYP_ID,VER_NBR)
  VALUES ('Y','kuali.krms.proposition.type.requisites.name','KS-SYS','customPropositionTypeService','kuali.krms.type.requisites',0)
/
