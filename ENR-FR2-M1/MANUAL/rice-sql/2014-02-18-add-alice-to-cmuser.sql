-- Adding alice to 'Kuali Student CM Curriculum Specialist' role (KSCM-1535).
INSERT INTO KRIM_ENTITY_T (ENTITY_ID,OBJ_ID,VER_NBR,ACTV_IND,LAST_UPDT_DT) VALUES ('KS-1535',SYS_GUID(),0,'Y',sysdate)
/
INSERT INTO KRIM_PRNCPL_T (PRNCPL_ID,ENTITY_ID,OBJ_ID,VER_NBR,PRNCPL_NM,PRNCPL_PSWD,ACTV_IND,LAST_UPDT_DT)
VALUES ('alice','KS-1535',SYS_GUID(),0,'alice','alice','Y',sysdate)
/
INSERT INTO KRIM_ENTITY_ENT_TYP_T (actv_ind, ent_typ_cd, entity_id, obj_id, ver_nbr)
VALUES('Y','PERSON','KS-1535', SYS_GUID(), '1' )
/
