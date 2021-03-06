-- DO NOT COPY AND PASTE THIS COMMENT.  VIOLATORS WILL LOSE COMMIT ACCESS.
-- KEY1:MjAxNC0wNi0wNS1LU0VOX0xQUl9UUkFOX0lURU1fVlItbWVyZ2Uuc3Fs
-- KEY2:U1RSVUNUVVJF
-- TYPE:STRUCTURE

-- merge in new table for validation results on LPR transaction items
-- (KSENROLL-12479 api changes, KSENROLL-13032 impl, merge to trunk)
CREATE TABLE KSEN_LPR_TRANS_ITEM_VR
(	ID                   VARCHAR2(255) NOT NULL ,
	LPR_TRANS_ITEM_ID    VARCHAR2(255) NOT NULL ,
	ELEMENT              VARCHAR2(255) NULL ,
	ERROR_LEVEL_CD       VARCHAR2(255) NOT NULL ,
	VR_MESSAGE           VARCHAR2(4000) NULL
)
/

ALTER TABLE KSEN_LPR_TRANS_ITEM_VR
	ADD CONSTRAINT  KSEN_LPR_TRANS_ITEM_VR PRIMARY KEY (ID)
/

ALTER TABLE KSEN_LPR_TRANS_ITEM_VR
	ADD (CONSTRAINT KSEN_LPR_TRANS_ITEM_VR_FK1 FOREIGN KEY (LPR_TRANS_ITEM_ID) REFERENCES KSEN_LPR_TRANS_ITEM (ID))
/

CREATE  INDEX KSEN_LPR_TRANS_ITEM_VR_IF1 ON KSEN_LPR_TRANS_ITEM_VR
(LPR_TRANS_ITEM_ID   ASC)
/

ALTER TABLE KSEN_LPR_TRANS_ITEM DROP COLUMN LTI_RESULT_MESSAGE
/

ALTER TABLE KSEN_LPR_TRANS_ITEM DROP COLUMN LTI_RESULT_STATUS
/
