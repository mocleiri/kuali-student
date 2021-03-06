
CREATE  INDEX KSEN_COMMENT_I1 ON KSEN_COMMENT
(COMMENT_TYPE   ASC)
/


CREATE  INDEX KSEN_COMMENT_I2 ON KSEN_COMMENT
(REF_OBJECT_ID   ASC,REF_OBJECT_TYPE   ASC)
/


CREATE  INDEX KSEN_COMMENT_ATTR_IF1 ON KSEN_COMMENT_ATTR
(OWNER_ID   ASC)
/


ALTER TABLE KSEN_COMMENT_ATTR
	ADD (CONSTRAINT KSEN_COMMENT_ATTR_FK1 FOREIGN KEY (OWNER_ID) REFERENCES KSEN_COMMENT (ID))
/
