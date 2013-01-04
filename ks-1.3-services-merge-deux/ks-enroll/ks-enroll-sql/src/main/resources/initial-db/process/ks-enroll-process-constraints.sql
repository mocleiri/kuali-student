
CREATE  INDEX KSEN_PROC_I1 ON KSEN_PROCESS
(PROCESS_TYPE   ASC)
/


CREATE  INDEX KSEN_PROC_ATTR_IF1 ON KSEN_PROCESS_ATTR
(OWNER_ID   ASC)
/


CREATE  INDEX KSEN_PROC_CAT_I1 ON KSEN_PROCESS_CATEGORY
(PROCESS_CATEGORY_TYPE   ASC)
/


CREATE  INDEX KSEN_PROC_CAT_ATTR_IF1 ON KSEN_PROCESS_CATEGORY_ATTR
(OWNER_ID   ASC)
/


CREATE  INDEX KSEN_PROC_CAT_RELTN_IF1 ON KSEN_PROCESS_CATEGORY_RELTN
(PROCESS_ID   ASC)
/


CREATE  INDEX KSEN_PROC_CAT_RELTN_IF2 ON KSEN_PROCESS_CATEGORY_RELTN
(PROCESS_CATEGORY_ID   ASC)
/


CREATE  INDEX KSEN_PROCESS_CHECK_IF1 ON KSEN_PROCESS_CHECK
(CHILD_PROCESS_ID   ASC)
/


CREATE  INDEX KSEN_PROCE_CHECK_I1 ON KSEN_PROCESS_CHECK
(PROCESS_CHECK_TYPE   ASC)
/


CREATE  INDEX KSEN_PROCESS_CHECK_ATTR_IF1 ON KSEN_PROCESS_CHECK_ATTR
(OWNER_ID   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_I1 ON KSEN_PROCESS_INSTRN
(PROCESS_INSTRN_TYPE   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_I2 ON KSEN_PROCESS_INSTRN
(PROCESS_ID   ASC,CHECK_ID   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_IF1 ON KSEN_PROCESS_INSTRN
(CHECK_ID   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_IF2 ON KSEN_PROCESS_INSTRN
(PROCESS_ID   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_AAT_IF1 ON KSEN_PROCESS_INSTRN_AAT
(PROCESS_INSTRN_ID   ASC)
/


CREATE  INDEX KSEN_PROC_INSTRN_ATTR_IF1 ON KSEN_PROCESS_INSTRN_ATTR
(OWNER_ID   ASC)
/


ALTER TABLE KSEN_PROCESS_ATTR
	ADD (CONSTRAINT KSEN_PROC_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_PROCESS (ID))
/


ALTER TABLE KSEN_PROCESS_CATEGORY_ATTR
	ADD (CONSTRAINT KSEN_PROC_CAT_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_PROCESS_CATEGORY (ID))
/


ALTER TABLE KSEN_PROCESS_CATEGORY_RELTN
	ADD (CONSTRAINT KSEN_PROC_CAT_RELTN_FK1 FOREIGN KEY (PROCESS_ID) REFERENCES KSEN_PROCESS (ID))
/


ALTER TABLE KSEN_PROCESS_CATEGORY_RELTN
	ADD (CONSTRAINT KSEN_PROC_CAT_RELTN_FK2 FOREIGN KEY (PROCESS_CATEGORY_ID) REFERENCES KSEN_PROCESS_CATEGORY (ID))
/


ALTER TABLE KSEN_PROCESS_CHECK
	ADD (CONSTRAINT KSEN_PROC_CHECK_FK1 FOREIGN KEY (CHILD_PROCESS_ID) REFERENCES KSEN_PROCESS (ID))
/


ALTER TABLE KSEN_PROCESS_CHECK_ATTR
	ADD (CONSTRAINT KSEN_PROC_CHECK_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_PROCESS_CHECK (ID))
/


ALTER TABLE KSEN_PROCESS_INSTRN
	ADD (CONSTRAINT KSEN_PROC_INSTN_FK1 FOREIGN KEY (CHECK_ID) REFERENCES KSEN_PROCESS_CHECK (ID))
/


ALTER TABLE KSEN_PROCESS_INSTRN
	ADD (CONSTRAINT KSEN_PROC_INSTRN_FK2 FOREIGN KEY (PROCESS_ID) REFERENCES KSEN_PROCESS (ID))
/


ALTER TABLE KSEN_PROCESS_INSTRN_AAT
	ADD (CONSTRAINT KSEN_PROC_INSTRN_FK1 FOREIGN KEY (PROCESS_INSTRN_ID) REFERENCES KSEN_PROCESS_INSTRN (ID))
/


ALTER TABLE KSEN_PROCESS_INSTRN_ATTR
	ADD (CONSTRAINT KSEN_PROC_INSTRN_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_PROCESS_INSTRN (ID))
/
