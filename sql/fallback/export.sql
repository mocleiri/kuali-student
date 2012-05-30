--------------------------------------------------------
--  File created - Wednesday-May-30-2012   
--------------------------------------------------------

--------------------------------------------------------
--  DDL for Table KSSA_TAG
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_TAG" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	"ACCESS_LEVEL" NUMBER(10,0), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_CREDIT_PERMISSION
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_CREDIT_PERMISSION" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"ALLOWABLE_DEBIT_TYPE_MASK" VARCHAR2(20 CHAR), 
	"PRIORITY" NUMBER(10,0), 
	"TRANSACTION_TYPE_ID_FK" VARCHAR2(20 CHAR), 
	"TRANSACTION_TYPE_SUB_CODE_FK" NUMBER(10,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FK1F74048CBC57B259" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ACNT_STATUS_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ACNT_STATUS_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_TRANSACTION
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_TRANSACTION" 
   (	"TYPE" VARCHAR2(31 CHAR) NOT NULL ENABLE, 
	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"ALLOCATED" NUMBER(19,2), 
	"AMNT" NUMBER(19,2), 
	"EFFECTIVE_DATE" TIMESTAMP (6), 
	"EXTN_ID" VARCHAR2(45 CHAR), 
	"GL_ENTRY_GENERATED" CHAR(1 CHAR), 
	"IS_INTERNAL_TRN" CHAR(1 CHAR), 
	"LEDGER_DATE" TIMESTAMP (6), 
	"LOCKED_ALLOCATED" NUMBER(19,2), 
	"NATIVE_AMNT" NUMBER(19,2), 
	"ORIG_DATE" TIMESTAMP (6), 
	"REFUND_RULE" VARCHAR2(2000 CHAR), 
	"RESP_ENTITY" VARCHAR2(45 CHAR), 
	"STATEMENT_TXT" VARCHAR2(100 CHAR), 
	"IS_REFUNDABLE" CHAR(1 CHAR), 
	"DEFER_ID" NUMBER(19,0), 
	"EXPIRATION_DATE" TIMESTAMP (6), 
	"CLEAR_DATE" TIMESTAMP (6), 
	"IS_DEFERRED" CHAR(1 CHAR), 
	"IS_GL_OVERRIDDEN" CHAR(1 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	"CURRENCY_ID_FK" NUMBER(19,0), 
	"DOCUMENT_ID_FK" NUMBER(19,0), 
	"ROLLUP_ID_FK" NUMBER(19,0), 
	"TRANSACTION_TYPE_ID_FK" VARCHAR2(20 CHAR), 
	"TRANSACTION_TYPE_SUB_CODE_FK" NUMBER(10,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKDCED3DB598518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE, 
	 CONSTRAINT "FKDCED3DB5FB9EC59" FOREIGN KEY ("CURRENCY_ID_FK")
	  REFERENCES "KSA"."KSSA_CURRENCY" ("ID") ENABLE, 
	 CONSTRAINT "FKDCED3DB590ED3EED" FOREIGN KEY ("DOCUMENT_ID_FK")
	  REFERENCES "KSA"."KSSA_DOCUMENT" ("ID") ENABLE, 
	 CONSTRAINT "FKDCED3DB5CE008744" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE, 
	 CONSTRAINT "FKDCED3DB5F7D721E7" FOREIGN KEY ("ROLLUP_ID_FK")
	  REFERENCES "KSA"."KSSA_ROLLUP" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_TAX_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_TAX_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_POSTAL_ADDRESS
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_POSTAL_ADDRESS" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"COUNTRY_CODE" VARCHAR2(10 CHAR), 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"IS_DEFAULT" CHAR(1 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"KIM_ADDRESS_TYPE" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"POSTAL_CODE" VARCHAR2(12 CHAR), 
	"STATE_CODE" VARCHAR2(5 CHAR), 
	"CITY" VARCHAR2(100 CHAR), 
	"LINE1" VARCHAR2(100 CHAR), 
	"LINE2" VARCHAR2(100 CHAR), 
	"LINE3" VARCHAR2(100 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKC90A760998518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_BANK_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_BANK_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_PERSON_NAME
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_PERSON_NAME" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"IS_DEFAULT" CHAR(1 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"FIRST_NAME" VARCHAR2(100 CHAR), 
	"KIM_NAME_TYPE" VARCHAR2(45 CHAR), 
	"LAST_NAME" VARCHAR2(100 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"MIDDLE_NAME" VARCHAR2(100 CHAR), 
	"SUFFIX" VARCHAR2(10 CHAR), 
	"TITLE" VARCHAR2(10 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FK45AEC02C98518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_INFORMATION
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_INFORMATION" 
   (	"TYPE" VARCHAR2(31 CHAR) NOT NULL ENABLE, 
	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"ACCESS_LEVEL" NUMBER(10,0), 
	"CREATION_DATE" TIMESTAMP (6), 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"EFFECTIVE_DATE" TIMESTAMP (6), 
	"EXPIRATION_DATE" TIMESTAMP (6), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"RESP_ENTITY" VARCHAR2(45 CHAR), 
	"SEVERITY" NUMBER(10,0), 
	"TEXT" VARCHAR2(4000 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	"TRN_ID_FK" NUMBER(19,0), 
	"FLAG_TYPE_ID_FK" NUMBER(19,0), 
	"NEXT_ID" NUMBER(19,0), 
	"PREV_ID" NUMBER(19,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKD2A2E003DBB2002A" FOREIGN KEY ("FLAG_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_FLAG_TYPE" ("ID") ENABLE, 
	 CONSTRAINT "FKD2A2E00316BC312E" FOREIGN KEY ("NEXT_ID")
	  REFERENCES "KSA"."KSSA_INFORMATION" ("ID") ENABLE, 
	 CONSTRAINT "FKD2A2E00395ACD1EE" FOREIGN KEY ("PREV_ID")
	  REFERENCES "KSA"."KSSA_INFORMATION" ("ID") ENABLE, 
	 CONSTRAINT "FKD2A2E00398518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE, 
	 CONSTRAINT "FKD2A2E003FE6E074B" FOREIGN KEY ("TRN_ID_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ACTIVITY
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ACTIVITY" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"ALTERED_ACNT_ID" VARCHAR2(255 CHAR), 
	"ALTERED_ENTITY_ID" VARCHAR2(45 CHAR), 
	"IP" VARCHAR2(32 CHAR), 
	"LOG_DETAIL" VARCHAR2(200 CHAR), 
	"MAC" VARCHAR2(12 CHAR), 
	"NEW_ATTRIBUTE" VARCHAR2(4000 CHAR), 
	"OLD_ATTRIBUTE" VARCHAR2(4000 CHAR), 
	"CREATION_DATE" TIMESTAMP (6), 
	"ACTIVITY_TYPE_ID_FK" NUMBER(19,0), 
	"CREATOR_ID" VARCHAR2(45), 
	"ALTERED_ENTITY" VARCHAR2(100), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ACNT
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ACNT" 
   (	"TYPE" VARCHAR2(31 CHAR) NOT NULL ENABLE, 
	"ID" VARCHAR2(45 CHAR) NOT NULL ENABLE, 
	"CAN_AUTHENTICATE" CHAR(1 CHAR), 
	"CREATION_DATE" TIMESTAMP (6), 
	"CREDIT_LIMIT" NUMBER(19,2), 
	"ENTITY_ID" VARCHAR2(45 CHAR), 
	"IS_KIM_ACNT" CHAR(1 CHAR), 
	"LAST_KIM_UPDATE" TIMESTAMP (6), 
	"LATE1" NUMBER(19,2), 
	"LATE2" NUMBER(19,2), 
	"LATE3" NUMBER(19,2), 
	"DUE" NUMBER(19,2), 
	"LATE_LAST_UPDATE" TIMESTAMP (6), 
	"OUTSTANDING" NUMBER(19,2), 
	"LATE_PERIOD_ID_FK" NUMBER(19,0), 
	"ACNT_STATUS_TYPE_ID_FK" NUMBER(19,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKB8F79251AE7C54D4" FOREIGN KEY ("ACNT_STATUS_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT_STATUS_TYPE" ("ID") ENABLE, 
	 CONSTRAINT "FKB8F7925156D383B8" FOREIGN KEY ("LATE_PERIOD_ID_FK")
	  REFERENCES "KSA"."KSSA_LATE_PERIOD" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ACTIVITY_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ACTIVITY_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ALLOCATION
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ALLOCATION" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"AMNT" NUMBER(19,2), 
	"IS_LOCKED" CHAR(1 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	"TRN_ID_1_FK" NUMBER(19,0), 
	"TRN_ID_2_FK" NUMBER(19,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKC2912B0998518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE, 
	 CONSTRAINT "FKC2912B09ED7E538" FOREIGN KEY ("TRN_ID_2_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE, 
	 CONSTRAINT "FKC2912B09ED770D9" FOREIGN KEY ("TRN_ID_1_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_GL_BREAKDOWN
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_GL_BREAKDOWN" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"GL_ACCOUNT" VARCHAR2(45 CHAR), 
	"BREAKDOWN" NUMBER(19,2), 
	"TRANSACTION_TYPE_ID_FK" VARCHAR2(20 CHAR), 
	"TRANSACTION_TYPE_SUB_CODE_FK" NUMBER(10,0), 
	"GL_TYPE_ID_FK" NUMBER(19,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKF48BE710AE27AC92" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE, 
	 CONSTRAINT "FKF48BE710FA70171C" FOREIGN KEY ("GL_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_GL_TYPE" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_TRANSACTION_TYPE_TAG
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" 
   (	"TRANSACTION_TYPE_ID_FK" VARCHAR2(20 CHAR) NOT NULL ENABLE, 
	"TRANSACTION_TYPE_SUB_CODE_FK" NUMBER(10,0) NOT NULL ENABLE, 
	"TAG_ID_FK" NUMBER(19,0) NOT NULL ENABLE, 
	 CONSTRAINT "FKA1635C3F47AB5D71" FOREIGN KEY ("TAG_ID_FK")
	  REFERENCES "KSA"."KSSA_TAG" ("ID") ENABLE, 
	 CONSTRAINT "FKA1635C3FCE008744" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_SEQUENCE_TABLE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_SEQUENCE_TABLE" 
   (	"SEQ_NAME" VARCHAR2(255 CHAR) NOT NULL ENABLE, 
	"SEQ_VALUE" NUMBER(10,0) NOT NULL ENABLE, 
	 PRIMARY KEY ("SEQ_NAME") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ACNT_PROTECTED_INFO
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ACNT_PROTECTED_INFO" 
   (	"ID" VARCHAR2(45 CHAR) NOT NULL ENABLE, 
	"BANK_DETAILS" VARCHAR2(100 CHAR), 
	"TAX_REFERENCE" VARCHAR2(45 CHAR), 
	"BANK_TYPE_ID_FK" NUMBER(19,0), 
	"TAX_TYPE_ID_FK" NUMBER(19,0), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FKEF75726D2C28B62A" FOREIGN KEY ("BANK_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_BANK_TYPE" ("ID") ENABLE, 
	 CONSTRAINT "FKEF75726DACFC7690" FOREIGN KEY ("TAX_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_TAX_TYPE" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_GL_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_GL_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_CURRENCY
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_CURRENCY" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	"ISO" VARCHAR2(10 CHAR) NOT NULL ENABLE, 
	 PRIMARY KEY ("ID") ENABLE, 
	 UNIQUE ("ISO") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_TRANSACTION_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_TRANSACTION_TYPE" 
   (	"TYPE" VARCHAR2(31 CHAR) NOT NULL ENABLE, 
	"ID" VARCHAR2(20 CHAR) NOT NULL ENABLE, 
	"SUB_CODE" NUMBER(10,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DEF_TRN_TXT" VARCHAR2(100 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"END_DATE" TIMESTAMP (6), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"START_DATE" TIMESTAMP (6), 
	"PRIORITY" NUMBER(10,0), 
	"AUTH_TXT" VARCHAR2(1000 CHAR), 
	"CLEAR_PERIOD" NUMBER(10,0), 
	"REFUND_RULE" VARCHAR2(2000 CHAR), 
	"DEF_ROLLUP_ID_FK" NUMBER(19,0), 
	 PRIMARY KEY ("ID", "SUB_CODE") ENABLE, 
	 CONSTRAINT "FK81104B8496077E1" FOREIGN KEY ("DEF_ROLLUP_ID_FK")
	  REFERENCES "KSA"."KSSA_ROLLUP" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ROLLUP
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ROLLUP" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_ELECTRONIC_CONTACT
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_ELECTRONIC_CONTACT" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"IS_DEFAULT" CHAR(1 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"EMAIL_ADDRESS" VARCHAR2(255 CHAR), 
	"KIM_EMAIL_ADDRESS_TYPE" VARCHAR2(45 CHAR), 
	"KIM_PHONE_TYPE" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"PHONE_COUNTRY" VARCHAR2(5 CHAR), 
	"PHONE_EXTN" VARCHAR2(10 CHAR), 
	"PHONE_NUMBER" VARCHAR2(20 CHAR), 
	"ACNT_ID_FK" VARCHAR2(45 CHAR), 
	 PRIMARY KEY ("ID") ENABLE, 
	 CONSTRAINT "FK77758E6698518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_LATE_PERIOD
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_LATE_PERIOD" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	"DAYS_LATE1" NUMBER(10,0), 
	"DAYS_LATE2" NUMBER(10,0), 
	"DAYS_LATE3" NUMBER(10,0), 
	"IS_DEFAULT" CHAR(1 CHAR), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_FLAG_TYPE
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_FLAG_TYPE" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"DESCRIPTION" VARCHAR2(2000 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	"NAME" VARCHAR2(100 CHAR), 
	"ACCESS_LEVEL" NUMBER(10,0), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  DDL for Table KSSA_DOCUMENT
--------------------------------------------------------

  CREATE TABLE "KSA"."KSSA_DOCUMENT" 
   (	"ID" NUMBER(19,0) NOT NULL ENABLE, 
	"DOCUMENT" VARCHAR2(255 CHAR), 
	"CREATION_DATE" TIMESTAMP (6), 
	"CREATOR_ID" VARCHAR2(45 CHAR), 
	"EDIT_REASON" VARCHAR2(512 CHAR), 
	"EDITOR_ID" VARCHAR2(45 CHAR), 
	"LAST_UPDATE" TIMESTAMP (6), 
	 PRIMARY KEY ("ID") ENABLE
   ) ;
--------------------------------------------------------
--  Constraints for Table KSSA_TAG
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TAG" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TAG" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_CREDIT_PERMISSION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_CREDIT_PERMISSION" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_CREDIT_PERMISSION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ACNT_STATUS_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACNT_STATUS_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACNT_STATUS_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_TRANSACTION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_TRANSACTION" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_TAX_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TAX_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TAX_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_POSTAL_ADDRESS
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_POSTAL_ADDRESS" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_POSTAL_ADDRESS" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_BANK_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_BANK_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_BANK_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_PERSON_NAME
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_PERSON_NAME" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_PERSON_NAME" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_INFORMATION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_INFORMATION" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_INFORMATION" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ACTIVITY
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACTIVITY" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACTIVITY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ACNT
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACNT" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACNT" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_ACNT" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ACTIVITY_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACTIVITY_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACTIVITY_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ALLOCATION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ALLOCATION" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ALLOCATION" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_GL_BREAKDOWN
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_GL_BREAKDOWN" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_GL_BREAKDOWN" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_TRANSACTION_TYPE_TAG
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" MODIFY ("TAG_ID_FK" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" MODIFY ("TRANSACTION_TYPE_SUB_CODE_FK" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" MODIFY ("TRANSACTION_TYPE_ID_FK" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_SEQUENCE_TABLE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_SEQUENCE_TABLE" ADD PRIMARY KEY ("SEQ_NAME") ENABLE;
  ALTER TABLE "KSA"."KSSA_SEQUENCE_TABLE" MODIFY ("SEQ_VALUE" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_SEQUENCE_TABLE" MODIFY ("SEQ_NAME" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ACNT_PROTECTED_INFO
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACNT_PROTECTED_INFO" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACNT_PROTECTED_INFO" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_GL_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_GL_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_GL_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_CURRENCY
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_CURRENCY" ADD UNIQUE ("ISO") ENABLE;
  ALTER TABLE "KSA"."KSSA_CURRENCY" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_CURRENCY" MODIFY ("ISO" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_CURRENCY" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_TRANSACTION_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE" ADD PRIMARY KEY ("ID", "SUB_CODE") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE" MODIFY ("SUB_CODE" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE" MODIFY ("TYPE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ROLLUP
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ROLLUP" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ROLLUP" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_ELECTRONIC_CONTACT
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ELECTRONIC_CONTACT" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ELECTRONIC_CONTACT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_LATE_PERIOD
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_LATE_PERIOD" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_LATE_PERIOD" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_FLAG_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_FLAG_TYPE" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_FLAG_TYPE" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table KSSA_DOCUMENT
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_DOCUMENT" ADD PRIMARY KEY ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_DOCUMENT" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Index SYS_C008263
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008263" ON "KSA"."KSSA_SEQUENCE_TABLE" ("SEQ_NAME") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008266
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008266" ON "KSA"."KSSA_ACNT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008270
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008270" ON "KSA"."KSSA_ACNT_STATUS_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008274
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008274" ON "KSA"."KSSA_ACTIVITY_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008276
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008276" ON "KSA"."KSSA_ALLOCATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008278
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008278" ON "KSA"."KSSA_BANK_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008280
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008280" ON "KSA"."KSSA_CREDIT_PERMISSION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008283
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008283" ON "KSA"."KSSA_CURRENCY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008284
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008284" ON "KSA"."KSSA_CURRENCY" ("ISO") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008286
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008286" ON "KSA"."KSSA_DOCUMENT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008290
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008290" ON "KSA"."KSSA_FLAG_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008292
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008292" ON "KSA"."KSSA_GL_BREAKDOWN" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008294
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008294" ON "KSA"."KSSA_GL_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008297
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008297" ON "KSA"."KSSA_INFORMATION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008299
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008299" ON "KSA"."KSSA_LATE_PERIOD" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008305
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008305" ON "KSA"."KSSA_ROLLUP" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008307
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008307" ON "KSA"."KSSA_TAG" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008309
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008309" ON "KSA"."KSSA_TAX_TYPE" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008312
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008312" ON "KSA"."KSSA_TRANSACTION" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008316
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008316" ON "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C009810
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C009810" ON "KSA"."KSSA_ACTIVITY" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008268
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008268" ON "KSA"."KSSA_ACNT_PROTECTED_INFO" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008288
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008288" ON "KSA"."KSSA_ELECTRONIC_CONTACT" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008301
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008301" ON "KSA"."KSSA_PERSON_NAME" ("ID") 
  ;
--------------------------------------------------------
--  DDL for Index SYS_C008303
--------------------------------------------------------

  CREATE UNIQUE INDEX "KSA"."SYS_C008303" ON "KSA"."KSSA_POSTAL_ADDRESS" ("ID") 
  ;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_CREDIT_PERMISSION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_CREDIT_PERMISSION" ADD CONSTRAINT "FK1F74048CBC57B259" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_TRANSACTION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD CONSTRAINT "FKDCED3DB590ED3EED" FOREIGN KEY ("DOCUMENT_ID_FK")
	  REFERENCES "KSA"."KSSA_DOCUMENT" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD CONSTRAINT "FKDCED3DB598518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD CONSTRAINT "FKDCED3DB5CE008744" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD CONSTRAINT "FKDCED3DB5F7D721E7" FOREIGN KEY ("ROLLUP_ID_FK")
	  REFERENCES "KSA"."KSSA_ROLLUP" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION" ADD CONSTRAINT "FKDCED3DB5FB9EC59" FOREIGN KEY ("CURRENCY_ID_FK")
	  REFERENCES "KSA"."KSSA_CURRENCY" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_POSTAL_ADDRESS
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_POSTAL_ADDRESS" ADD CONSTRAINT "FKC90A760998518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_PERSON_NAME
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_PERSON_NAME" ADD CONSTRAINT "FK45AEC02C98518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_INFORMATION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD CONSTRAINT "FKD2A2E00316BC312E" FOREIGN KEY ("NEXT_ID")
	  REFERENCES "KSA"."KSSA_INFORMATION" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD CONSTRAINT "FKD2A2E00395ACD1EE" FOREIGN KEY ("PREV_ID")
	  REFERENCES "KSA"."KSSA_INFORMATION" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD CONSTRAINT "FKD2A2E00398518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD CONSTRAINT "FKD2A2E003DBB2002A" FOREIGN KEY ("FLAG_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_FLAG_TYPE" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_INFORMATION" ADD CONSTRAINT "FKD2A2E003FE6E074B" FOREIGN KEY ("TRN_ID_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_ACNT
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACNT" ADD CONSTRAINT "FKB8F7925156D383B8" FOREIGN KEY ("LATE_PERIOD_ID_FK")
	  REFERENCES "KSA"."KSSA_LATE_PERIOD" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACNT" ADD CONSTRAINT "FKB8F79251AE7C54D4" FOREIGN KEY ("ACNT_STATUS_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT_STATUS_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_ALLOCATION
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ALLOCATION" ADD CONSTRAINT "FKC2912B0998518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ALLOCATION" ADD CONSTRAINT "FKC2912B09ED770D9" FOREIGN KEY ("TRN_ID_1_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ALLOCATION" ADD CONSTRAINT "FKC2912B09ED7E538" FOREIGN KEY ("TRN_ID_2_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_GL_BREAKDOWN
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_GL_BREAKDOWN" ADD CONSTRAINT "FKF48BE710AE27AC92" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE;
  ALTER TABLE "KSA"."KSSA_GL_BREAKDOWN" ADD CONSTRAINT "FKF48BE710FA70171C" FOREIGN KEY ("GL_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_GL_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_TRANSACTION_TYPE_TAG
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" ADD CONSTRAINT "FKA1635C3F47AB5D71" FOREIGN KEY ("TAG_ID_FK")
	  REFERENCES "KSA"."KSSA_TAG" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE_TAG" ADD CONSTRAINT "FKA1635C3FCE008744" FOREIGN KEY ("TRANSACTION_TYPE_ID_FK", "TRANSACTION_TYPE_SUB_CODE_FK")
	  REFERENCES "KSA"."KSSA_TRANSACTION_TYPE" ("ID", "SUB_CODE") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_ACNT_PROTECTED_INFO
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ACNT_PROTECTED_INFO" ADD CONSTRAINT "FKEF75726D2C28B62A" FOREIGN KEY ("BANK_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_BANK_TYPE" ("ID") ENABLE;
  ALTER TABLE "KSA"."KSSA_ACNT_PROTECTED_INFO" ADD CONSTRAINT "FKEF75726DACFC7690" FOREIGN KEY ("TAX_TYPE_ID_FK")
	  REFERENCES "KSA"."KSSA_TAX_TYPE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_TRANSACTION_TYPE
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_TRANSACTION_TYPE" ADD CONSTRAINT "FK81104B8496077E1" FOREIGN KEY ("DEF_ROLLUP_ID_FK")
	  REFERENCES "KSA"."KSSA_ROLLUP" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table KSSA_ELECTRONIC_CONTACT
--------------------------------------------------------

  ALTER TABLE "KSA"."KSSA_ELECTRONIC_CONTACT" ADD CONSTRAINT "FK77758E6698518DD2" FOREIGN KEY ("ACNT_ID_FK")
	  REFERENCES "KSA"."KSSA_ACNT" ("ID") ENABLE;
---------------------------------------------------
--   DATA FOR TABLE KSSA_TAG
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_TAG
Insert into KSSA_TAG (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (1,'pheald','This item will be reported on IRS form 1098(T).',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1098T',3);
Insert into KSSA_TAG (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (2,'pheald','This item is considered cash for the combined-cash limit tracking requirement.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Cash or Equivalent',5);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_TAG
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_CREDIT_PERMISSION
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_CREDIT_PERMISSION
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (1,'*',0,'cash',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (2,'*',0,'pp',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (3,'*',0,'ach',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (5,'*',0,'chma',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (6,'*',0,'chip',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (7,'*',0,'ccol',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (4,'*',0,'ccma',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (8,'1*',0,'finaid',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (9,'11*',0,'finaid2',1);
Insert into KSSA_CREDIT_PERMISSION (ID,ALLOWABLE_DEBIT_TYPE_MASK,PRIORITY,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values (10,'12*',0,'finaid2',1);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_CREDIT_PERMISSION
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ACNT_STATUS_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ACNT_STATUS_TYPE
Insert into KSSA_ACNT_STATUS_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (2,'pheald','Account is in good standing but has a history of delinquency',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Currently In Good Standing');
Insert into KSSA_ACNT_STATUS_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (3,'pheald','Account is in collections.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'In Collections');
Insert into KSSA_ACNT_STATUS_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (4,'pheald','Account is in process of being closed.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Closing');
Insert into KSSA_ACNT_STATUS_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (5,'pheald','Account is closed.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Closed');
Insert into KSSA_ACNT_STATUS_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (1,'pheald','Account is in good standing',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'In Good Standing');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ACNT_STATUS_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_TRANSACTION
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_TRANSACTION
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',5,null,50,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,50,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Cash Payment','Y',null,null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,5,'cash',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',6,null,1000,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'PROSAM-100291',null,null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,1000,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Financial Aid Payment','Y',null,null,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,2,'finaid',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',7,null,1500,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'ACH-20019201092',null,null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,1500,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','ACH Payment','Y',null,null,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,6,'ach',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',8,null,2500,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'521521',null,null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,2500,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Credit Card (In Person Payment)','Y',null,null,to_timestamp('03-APR-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,5,'ccip',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',9,null,50,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'N','N',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,37,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Study Abroad Charge',null,null,null,null,null,null,'pgriffin',2,null,null,'1356',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',10,null,132,to_timestamp('21-MAR-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'N','N',to_timestamp('21-MAR-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,132,to_timestamp('21-MAR-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Technology Fee',null,null,null,null,null,null,'pgriffin',1,null,null,'1356',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',11,null,35.52,to_timestamp('20-FEB-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'N','N',to_timestamp('20-FEB-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,35.62,to_timestamp('20-FEB-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Performing Arts',null,null,null,null,null,null,'pgriffin',1,null,null,'1356',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',12,null,17.6,to_timestamp('21-JAN-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'N','N',to_timestamp('21-JAN-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,17.6,to_timestamp('21-JAN-12 05.10.37.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Health Center',null,null,null,null,null,null,'pgriffin',1,null,null,'1356',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',1,null,10000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'FA-10200291','N','N',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,10000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Tuition',null,null,null,null,null,null,'pgriffin',1,null,3,'1020',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',2,null,10000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'FA-10029919','N','N',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,10000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Tuition',null,null,null,null,null,null,'lgriffin',1,null,3,'1020',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',4,null,2000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'FA-01299101','N','N',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,2000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Mandatory Fee',null,null,null,null,null,null,'mgriffin',1,null,3,'1001',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',3,null,15000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'FA-10200222','N','N',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,15000,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Tuition',null,null,null,null,null,null,'mgriffin',1,null,3,'1020',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TDC',13,null,10000,to_timestamp('03-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,null,to_timestamp('03-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,10000,to_timestamp('03-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'pheald','Tuition',null,null,null,null,null,null,'rtimmons',1,null,3,'1020',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',50050,0,2000,to_timestamp('24-MAY-12 01.55.51.937000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'rhafenbr','N','N',null,0,2000,null,null,'admin','Cash Payment','N',null,null,to_timestamp('24-MAY-12 01.55.51.937000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,null,'cash',1);
Insert into KSSA_TRANSACTION (TYPE,ID,ALLOCATED,AMNT,EFFECTIVE_DATE,EXTN_ID,GL_ENTRY_GENERATED,IS_INTERNAL_TRN,LEDGER_DATE,LOCKED_ALLOCATED,NATIVE_AMNT,ORIG_DATE,REFUND_RULE,RESP_ENTITY,STATEMENT_TXT,IS_REFUNDABLE,DEFER_ID,EXPIRATION_DATE,CLEAR_DATE,IS_DEFERRED,IS_GL_OVERRIDDEN,ACNT_ID_FK,CURRENCY_ID_FK,DOCUMENT_ID_FK,ROLLUP_ID_FK,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK) values ('TCP',50051,0,2000,to_timestamp('24-MAY-12 01.56.51.558000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'rhafenbr','N','N',null,0,2000,null,null,'admin','Cash Payment','N',null,null,to_timestamp('24-MAY-12 01.56.51.558000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'pgriffin',1,null,null,'cash',1);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_TRANSACTION
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_TAX_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_TAX_TYPE
Insert into KSSA_TAX_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (1,'pheald','US Social Security Nuber',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'US Social SecurityNumber');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_TAX_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_POSTAL_ADDRESS
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_POSTAL_ADDRESS
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (1,'USA','pheald','Y',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'00093','RI','Qahog','31 Spooner Street',null,null,'pgriffin');
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (2,'USA','pheald','Y',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'00093','RI','Qahog','31 Spooner Street',null,null,'lgriffin');
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (3,'USA','pheald','Y',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'00093','RI','Qahog','31 Spooner Street',null,null,'mgriffin');
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (4,'USA','pheald','N',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'00093','RI','Qahog','28 Spooner Street','C/O Mr. Glenn Quagmire',null,'mgriffin');
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (50050,null,'system','Y',null,null,to_timestamp('22-APR-12 07.32.03.506000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,null,null,null,null,'admin');
Insert into KSSA_POSTAL_ADDRESS (ID,COUNTRY_CODE,CREATOR_ID,IS_DEFAULT,EDITOR_ID,KIM_ADDRESS_TYPE,LAST_UPDATE,POSTAL_CODE,STATE_CODE,CITY,LINE1,LINE2,LINE3,ACNT_ID_FK) values (5,'USA','pheald','Y',null,null,to_timestamp('03-MAY-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),'19107','PA','Philadelphia','1901 Chestnut St',null,null,'rtimmons');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_POSTAL_ADDRESS
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_BANK_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_BANK_TYPE
Insert into KSSA_BANK_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (1,'pheald','This type of field is used to record automated clearing house transactions for payments to accounts within the United States of America. The required information for this type of transaction is the routing number, the account number, and the account type.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'US ACH.');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_BANK_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_PERSON_NAME
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_PERSON_NAME
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (1,'pheald','Y',null,'Peter',null,'Griffin',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Mr','pgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (4,'pheald','N',null,'Lois',null,'Petwerschmiddt',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Miss','lgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (6,'pheald','Y',null,'Meg',null,'Griffin',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Miss','mgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (5,'pheald','N',null,'Megan',null,'Griffin',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Miss','mgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (3,'pheald','Y',null,'Lois',null,'Griffin',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Mrs','lgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (2,'pheald','N',null,'Pete',null,'Griffin',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Mr','pgriffin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (50050,'system','Y',null,'admin','PERSON','admin',to_timestamp('22-APR-12 07.32.03.506000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,null,'admin');
Insert into KSSA_PERSON_NAME (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,FIRST_NAME,KIM_NAME_TYPE,LAST_NAME,LAST_UPDATE,MIDDLE_NAME,SUFFIX,TITLE,ACNT_ID_FK) values (7,'pheald','Y',null,'Raymond',null,'Timmons',to_timestamp('05-MAY-20 12.00.00.000000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Mr','rtimmons');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_PERSON_NAME
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_INFORMATION
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_INFORMATION
Insert into KSSA_INFORMATION (TYPE,ID,ACCESS_LEVEL,CREATION_DATE,CREATOR_ID,EDITOR_ID,EFFECTIVE_DATE,EXPIRATION_DATE,LAST_UPDATE,RESP_ENTITY,SEVERITY,TEXT,ACNT_ID_FK,TRN_ID_FK,FLAG_TYPE_ID_FK,NEXT_ID,PREV_ID) values ('M',50050,null,to_timestamp('24-MAY-12 02.02.57.303000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,to_timestamp('24-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,to_timestamp('24-MAY-12 02.02.57.303000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'Testing the memo add for Peter Griffin.  He stopped by today and wrote a check.','pgriffin',null,null,null,null);
Insert into KSSA_INFORMATION (TYPE,ID,ACCESS_LEVEL,CREATION_DATE,CREATOR_ID,EDITOR_ID,EFFECTIVE_DATE,EXPIRATION_DATE,LAST_UPDATE,RESP_ENTITY,SEVERITY,TEXT,ACNT_ID_FK,TRN_ID_FK,FLAG_TYPE_ID_FK,NEXT_ID,PREV_ID) values ('M',50051,null,to_timestamp('24-MAY-12 02.06.00.585000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,to_timestamp('25-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,to_timestamp('24-MAY-12 02.06.00.585000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,'another test memo','pgriffin',null,null,null,null);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_INFORMATION
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ACTIVITY
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ACTIVITY
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (1,null,'20','000.000.000.000','Batch XML Processing',null,null,'USD',to_timestamp('27-APR-12 09.23.31.654000000 AM','DD-MON-RR HH.MI.SS.FF AM'),7,'rtimmons','Currency');
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (2,null,'20','000.000.000.000','Batch XML Processing',null,null,'CZK',to_timestamp('03-MAY-12 02.10.10.123000000 PM','DD-MON-RR HH.MI.SS.FF AM'),7,'admin','Currency');
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (3,'user1','65','010.012.025.148','KSA Login',null,null,'admin2390',to_timestamp('03-MAY-12 03.34.42.253000000 PM','DD-MON-RR HH.MI.SS.FF AM'),7,'user1',null);
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (4,null,'20','000.000.000.000','Upload XML File',null,null,'c:\users\file.xml',to_timestamp('03-MAY-12 02.27.35.154000000 PM','DD-MON-RR HH.MI.SS.FF AM'),7,'user1',null);
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (5,'user1','65','000.000.000.000','Age Accounts',null,null,'0',to_timestamp('03-MAY-12 07.48.54.814000000 AM','DD-MON-RR HH.MI.SS.FF AM'),7,'jmeyer',null);
Insert into KSSA_ACTIVITY (ID,ALTERED_ACNT_ID,ALTERED_ENTITY_ID,IP,LOG_DETAIL,MAC,NEW_ATTRIBUTE,OLD_ATTRIBUTE,CREATION_DATE,ACTIVITY_TYPE_ID_FK,CREATOR_ID,ALTERED_ENTITY) values (6,'user1','65','000.000.000.000','Add Charge',null,null,'10,000',to_timestamp('03-MAY-12 07.56.14.684000000 AM','DD-MON-RR HH.MI.SS.FF AM'),7,'jtilton',null);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ACTIVITY
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ACNT
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ACNT
Insert into KSSA_ACNT (TYPE,ID,CAN_AUTHENTICATE,CREATION_DATE,CREDIT_LIMIT,ENTITY_ID,IS_KIM_ACNT,LAST_KIM_UPDATE,LATE1,LATE2,LATE3,DUE,LATE_LAST_UPDATE,OUTSTANDING,LATE_PERIOD_ID_FK,ACNT_STATUS_TYPE_ID_FK) values ('ACD','pgriffin','N',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),0,'pgriffin','N',null,10182,35.52,17.6,null,to_timestamp('03-MAY-12 03.13.15.352000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,1,null);
Insert into KSSA_ACNT (TYPE,ID,CAN_AUTHENTICATE,CREATION_DATE,CREDIT_LIMIT,ENTITY_ID,IS_KIM_ACNT,LAST_KIM_UPDATE,LATE1,LATE2,LATE3,DUE,LATE_LAST_UPDATE,OUTSTANDING,LATE_PERIOD_ID_FK,ACNT_STATUS_TYPE_ID_FK) values ('ACD','lgriffin','N',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),0,'lgriffin','N',null,10000,0,0,null,to_timestamp('03-MAY-12 11.09.28.056000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,1,null);
Insert into KSSA_ACNT (TYPE,ID,CAN_AUTHENTICATE,CREATION_DATE,CREDIT_LIMIT,ENTITY_ID,IS_KIM_ACNT,LAST_KIM_UPDATE,LATE1,LATE2,LATE3,DUE,LATE_LAST_UPDATE,OUTSTANDING,LATE_PERIOD_ID_FK,ACNT_STATUS_TYPE_ID_FK) values ('ACD','mgriffin','N',to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),0,'mgriffin','N',null,null,null,null,null,null,null,1,null);
Insert into KSSA_ACNT (TYPE,ID,CAN_AUTHENTICATE,CREATION_DATE,CREDIT_LIMIT,ENTITY_ID,IS_KIM_ACNT,LAST_KIM_UPDATE,LATE1,LATE2,LATE3,DUE,LATE_LAST_UPDATE,OUTSTANDING,LATE_PERIOD_ID_FK,ACNT_STATUS_TYPE_ID_FK) values ('ACD','admin','Y',to_timestamp('22-APR-12 07.32.03.506000000 PM','DD-MON-RR HH.MI.SS.FF AM'),0,'1100','Y',null,0,0,0,null,to_timestamp('03-MAY-12 10.58.58.316000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,1,1);
Insert into KSSA_ACNT (TYPE,ID,CAN_AUTHENTICATE,CREATION_DATE,CREDIT_LIMIT,ENTITY_ID,IS_KIM_ACNT,LAST_KIM_UPDATE,LATE1,LATE2,LATE3,DUE,LATE_LAST_UPDATE,OUTSTANDING,LATE_PERIOD_ID_FK,ACNT_STATUS_TYPE_ID_FK) values ('ACD','rtimmons','N',to_timestamp('05-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),0,'rtimmons','N',null,null,null,null,null,null,null,1,null);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ACNT
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ACTIVITY_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ACTIVITY_TYPE
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (1,'pheald','Emergencies - system is unusable.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'emerg');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (2,'pheald','Action must be taken immediately.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'alert');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (3,'pheald','Critical Conditions.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'crit');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (4,'pheald','Error conditions.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'error');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (5,'pheald','Warning conditions.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'warn');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (6,'pheald','Normal but significant condition.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'notice');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (7,'pheald','Informational.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'info');
Insert into KSSA_ACTIVITY_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (8,'pheald','Debug-level messages.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'debug');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ACTIVITY_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ALLOCATION
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ALLOCATION

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ALLOCATION
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_GL_BREAKDOWN
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_GL_BREAKDOWN
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (164,'01-1-13125 0148',0,'1511',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (165,'01-8-88888 8888',0,'1512',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (166,'01-1-12900 0177',0,'1513',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (167,'01-1-12900 0138',0,'1514',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (168,'01-0-14508 2601',0,'1515',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (169,'01-2-94508 0170',0,'1516',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (170,'01-2-94508 0170',0,'1517',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (171,'01-1-11337 0183',0,'1518',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (172,'01-2-94306 0570',0,'1519',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (173,'01-8-88888 8888',0,'1520',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (174,'01-1-15503 0171',0,'1521',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (175,'01-9-99999 0000',0,'1522',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (176,'01-1-15503 0122',0,'1523',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (177,'01-1-15503 0120',0,'1524',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (178,'01-4-30196 3924',0,'1525',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (179,'01-2-94553 0615',0,'1526',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (180,'01-2-94546 3928',0,'1527',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (181,'01-4-30196 3924',0,'1528',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (182,'01-1-10200 0183',0,'1529',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (183,'01-2-94561 0189',0,'1530',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (184,'01-2-94561 0189',0,'1531',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (185,'01-1-11178 0139',0,'1532',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (186,'01-1-11178 0139',0,'1533',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (187,'01-1-16101 3924',0,'1534',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (188,'01-1-11494 0115',0,'1535',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (189,'01-1-16101 3924',0,'1536',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (190,'01-2-93506 3924',0,'1537',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (191,'01-2-95419 3924',0,'1538',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (192,'01-1-16101 3924',0,'1539',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (193,'01-1-11700 0599',0,'1540',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (194,'01-1-11700 0599',0,'1541',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (195,'01-1-11649 0599',0,'1542',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (196,'01-1-11700 0187',0,'1543',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (197,'01-9-99009 0189',0,'1544',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (198,'01-1-11700 0599',0,'1545',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (199,'01-9-99014 0189',0,'1546',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (200,'01-1-11700 0599',0,'1547',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (201,'01-1-11700 0189',0,'1548',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (202,'01-1-11700 0599',0,'1549',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (203,'01-2-93205 0570',0,'1550',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (204,'01-1-11934 0570',0,'1551',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (205,'01-1-11850 0570',0,'1552',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (206,'01-1-11904 0570',0,'1553',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (207,'01-2-93212 0570',0,'1554',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (208,'01-1-13201 3240',0,'1555',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (209,'01-2-94965 0189',0,'1556',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (210,'01-2-94702 0190',0,'1557',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (211,'01-2-93506 0189',0,'1558',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (212,'01-2-94508 0170',0,'1559',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (213,'01-2-94627 0146',0,'1560',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (214,'01-1-10250 0570',0,'1561',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (215,'01-2-95166 0170',0,'1562',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (216,'01-1-11878 0118',0,'1563',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (217,'01-2-94306 0570',0,'1564',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (218,'01-2-94306 0570',0,'1565',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (219,'01-2-94306 0570',0,'1566',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (220,'01-2-94306 0570',0,'1567',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (221,'01-1-14445 0139',0,'1568',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (222,'01-1-01203 0141',0,'1600',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (223,'01-1-01204 0147',0,'1602',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (224,'00-0-00000 0000',0,'1604',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (225,'01-1-01201 0150',0,'1606',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (226,'01-1-01203 0140',0,'1608',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (227,'01-1-14237 0139',0,'1609',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (228,'01-1-14236 0139',0,'1610',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (229,'01-1-14236 0139',0,'1611',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (230,'01-1-14222 0140',0,'1612',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (231,'01-1-14222 0140',0,'1613',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (232,'01-1-14498 0141',0,'1614',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (233,'00-0-00000 0000',0,'1700',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (234,'00-0-00000 0000',0,'1701',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (235,'01-2-94101 0139',0,'1702',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (1,'01-8-88888 8888',0,'1001',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (2,'01-1-01000 0102',0,'1020',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (3,'01-8-88888 8888',0,'1021',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (4,'01-1-01000 0103',0,'1040',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (5,'01-8-88888 8888',0,'1041',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (6,'01-0-50032 1430',0,'1045',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (7,'01-1-01000 0104',0,'1050',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (8,'00-0-00000 0000',0,'1051',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (9,'01-8-88888 8888',0,'1060',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (10,'01-1-01000 0101',0,'1070',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (11,'01-2-81305 0187',0,'1071',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (12,'01-2-81305 0187',0,'1072',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (13,'01-2-81305 0187',0,'1073',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (14,'01-1-01000 0102',0,'1080',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (15,'01-1-01000 0110',0,'1100',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (16,'00-0-00000 0000',0,'1101',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (17,'01-1-01000 0110',0,'1120',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (18,'01-8-88888 8888',0,'1121',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (19,'01-1-01000 0111',0,'1140',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (20,'00-0-00000 0000',0,'1141',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (21,'01-1-01000 0111',0,'1160',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (22,'01-8-88888 8888',0,'1161',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (23,'01-1-12750 3360',0,'1162',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (24,'01-1-01000 0110',0,'1165',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (25,'01-1-12902 0149',0,'1166',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (26,'01-1-01000 0111',0,'1167',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (27,'01-1-12902 0149',0,'1168',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (28,'01-1-11700 0138',0,'1170',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (29,'01-1-01000 0110',0,'1171',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (30,'01-1-11700 0138',0,'1172',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (31,'01-1-01000 0111',0,'1173',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (32,'01-1-11700 0138',0,'1174',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (33,'01-2-80810 0135',0,'1175',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (34,'01-1-11704 0136',0,'1176',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (35,'01-1-11700 0179',0,'1177',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (36,'01-1-12912 0118',0,'1178',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (37,'01-1-11684 0129',0,'1179',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (38,'01-1-12261 0118',0,'1180',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (39,'01-1-12211 0139',0,'1181',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (40,'01-2-94125 0139',0,'1182',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (41,'01-2-27600 0111',0,'1183',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (42,'01-2-27600 0110',0,'1184',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (43,'01-1-12900 0137',0,'1186',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (44,'01-1-12900 0139',0,'1187',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (45,'01-1-12613 0110',0,'1188',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (46,'01-1-12613 0111',0,'1189',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (47,'01-1-12613 0151',0,'1190',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (48,'01-1-11685 0129',0,'1191',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (49,'01-1-12614 0110',0,'1192',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (50,'01-1-12614 0111',0,'1193',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (51,'01-1-12614 0151',0,'1194',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (52,'01-1-13038 3752',0,'1195',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (53,'01-1-01202 0140',0,'1196',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (54,'01-1-01202 0146',0,'1198',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (55,'01-1-01202 0140',0,'1202',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (56,'01-8-88888 8888',0,'1204',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (57,'01-8-88888 8888',0,'1206',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (58,'01-8-88888 8888',0,'1208',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (59,'01-8-88888 8888',0,'1210',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (60,'01-8-88888 8888',0,'1212',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (61,'01-8-88888 8888',0,'1213',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (62,'01-8-88888 8888',0,'1214',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (63,'01-8-88888 8888',0,'1215',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (64,'00-0-00000 0000',0,'1216',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (65,'00-0-00000 0000',0,'1218',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (66,'01-2-80400 0172',0,'1240',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (67,'01-2-80400 0172',0,'1242',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (68,'01-2-80400 0172',0,'1244',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (69,'01-2-80400 0172',0,'1245',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (70,'01-2-80400 0172',0,'1246',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (71,'01-2-80400 0172',0,'1247',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (72,'01-2-80400 0172',0,'1248',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (73,'01-2-94678 0132',0,'1256',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (74,'01-2-94565 0132',0,'1257',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (75,'01-2-95472 0132',0,'1258',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (76,'01-2-95472 0132',0,'1259',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (77,'01-2-81000 0132',0,'1260',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (78,'01-2-81000 0139',0,'1261',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (79,'01-2-94565 0139',0,'1262',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (80,'01-2-94669 0139',0,'1263',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (81,'01-2-94669 0132',0,'1264',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (82,'01-2-95464 0139',0,'1265',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (83,'01-2-95464 0132',0,'1266',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (84,'01-2-95117 0139',0,'1267',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (85,'01-2-95117 0132',0,'1268',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (86,'01-2-94678 0139',0,'1269',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (87,'01-2-94678 0132',0,'1270',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (88,'01-1-13300 3752',0,'1271',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (89,'01-2-94679 0132',0,'1272',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (90,'01-2-94679 0139',0,'1273',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (91,'01-2-94679 0139',0,'1274',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (92,'01-2-94565 0139',0,'1275',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (93,'01-2-94565 0132',0,'1276',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (94,'01-2-94679 0132',0,'1277',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (95,'01-2-94678 0132',0,'1278',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (96,'01-2-94678 0139',0,'1279',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (97,'01-0-13112 1326',0,'1280',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (98,'01-0-13112 1326',0,'1282',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (99,'01-2-95118 0132',0,'1283',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (100,'01-2-95118 0139',0,'1284',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (101,'01-2-94678 0132',0,'1287',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (102,'01-2-94678 0139',0,'1288',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (103,'01-2-95472 0132',0,'1289',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (104,'01-2-95472 0139',0,'1290',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (105,'01-2-95112 0132',0,'1291',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (106,'01-2-95112 0139',0,'1292',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (107,'01-2-95113 0132',0,'1293',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (108,'01-2-95113 0139',0,'1294',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (109,'01-1-12913 0118',0,'1295',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (110,'01-2-95114 0132',0,'1296',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (111,'01-2-95114 0139',0,'1297',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (112,'01-2-95115 0139',0,'1298',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (113,'01-2-95115 0139',0,'1299',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (114,'01-1-12331 0118',0,'1301',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (115,'01-1-12331 0118',0,'1302',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (116,'01-1-14444 0120',0,'1303',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (117,'01-1-14444 0122',0,'1304',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (118,'01-1-14444 0125',0,'1305',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (119,'01-1-14444 0126',0,'1306',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (120,'01-2-95116 0132',0,'1307',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (121,'01-2-95116 0139',0,'1308',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (122,'01-2-95119 0132',0,'1309',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (123,'01-2-95119 0139',0,'1310',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (124,'01-2-95120 0132',0,'1311',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (125,'01-2-95120 0139',0,'1312',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (126,'01-2-95121 0132',0,'1313',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (127,'01-2-95121 0139',0,'1314',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (128,'01-2-95122 0132',0,'1315',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (129,'01-2-95122 0139',0,'1316',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (130,'01-2-95125 0132',0,'1317',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (131,'01-2-95125 0139',0,'1318',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (132,'01-1-01000 0139',0,'1350',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (133,'01-1-12750 3360',0,'1351',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (134,'01-2-95162 4600',0,'1352',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (135,'01-2-95162 4600',0,'1353',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (136,'01-2-95163 4600',0,'1354',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (137,'01-2-95163 4600',0,'1355',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (138,'01-2-95164 4600',0,'1356',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (139,'01-2-95164 4600',0,'1357',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (140,'01-2-95165 4600',0,'1358',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (141,'01-2-95165 4600',0,'1359',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (142,'01-8-88888 8888',0,'1400',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (143,'01-8-88888 8888',0,'1401',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (144,'01-8-88888 8888',0,'1402',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (145,'01-8-88888 8888',0,'1403',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (146,'01-8-88888 8888',0,'1404',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (147,'01-1-12401 0189',0,'1420',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (148,'01-1-11703 0176',0,'1495',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (149,'01-1-11012 0118',0,'1496',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (150,'01-1-11873 0118',0,'1497',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (151,'01-1-11014 0118',0,'1498',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (152,'01-1-11013 0189',0,'1499',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (153,'01-1-11700 0176',0,'1500',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (154,'01-1-11013 0182',0,'1501',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (155,'01-2-80720 0171',0,'1502',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (156,'01-1-12721 0173',0,'1503',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (157,'01-1-12040 0183',0,'1504',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (158,'01-1-11331 0181',0,'1505',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (159,'00-0-00000 0000',0,'1506',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (160,'01-1-11464 0174',0,'1507',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (161,'01-1-13200 0131',0,'1508',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (162,'01-1-13125 0148',0,'1509',1,0);
Insert into KSSA_GL_BREAKDOWN (ID,GL_ACCOUNT,BREAKDOWN,TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,GL_TYPE_ID_FK) values (163,'01-1-12207 0130',0,'1510',1,0);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_GL_BREAKDOWN
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_TRANSACTION_TYPE_TAG
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_TRANSACTION_TYPE_TAG
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1292',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1293',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1294',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1295',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1296',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1297',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1298',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1299',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1301',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1302',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1303',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1304',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1305',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1306',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1307',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1308',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1309',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1310',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1311',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1312',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1313',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1314',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1315',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1316',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1317',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1318',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1350',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1351',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1352',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1353',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1354',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1355',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1356',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1357',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1358',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1359',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1400',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1401',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1402',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1403',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1404',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1420',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1495',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1496',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1497',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1498',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1499',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1500',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1501',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1502',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1503',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1504',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1505',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1506',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1507',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1508',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1509',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1510',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1511',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1512',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1513',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1514',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1515',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1516',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1517',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1518',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1519',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1520',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1521',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1522',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1523',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1524',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1525',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1526',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1527',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1528',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1529',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1530',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1531',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1532',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1533',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1534',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1535',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1536',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1537',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1538',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1539',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1540',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1541',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1542',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1543',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1544',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1545',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1546',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1547',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1548',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1549',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1550',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1551',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1552',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1553',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1554',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1555',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1556',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1557',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1558',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1559',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1560',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1561',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1562',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1563',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1564',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1565',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1566',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1567',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1568',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1600',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1602',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1604',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1606',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1608',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1609',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1610',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1611',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1612',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1613',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1614',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1700',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1701',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1702',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1050',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1051',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1060',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1070',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1071',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1072',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1073',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1080',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1100',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1101',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1120',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1121',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1140',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1141',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1160',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1161',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1162',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1165',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1166',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1167',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1168',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1170',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1171',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1172',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1173',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1174',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1175',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1176',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1177',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1178',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1179',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1180',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1181',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1182',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1183',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1184',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1186',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1187',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1188',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1189',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1190',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1191',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1192',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1193',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1194',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1195',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1196',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1198',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1202',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1204',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1206',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1208',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1210',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1212',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1213',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1214',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1215',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1216',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1218',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1240',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1242',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1244',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1245',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1246',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1247',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1248',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1256',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1257',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1258',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1259',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1260',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1261',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1262',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1263',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1264',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1265',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1266',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1267',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1268',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1269',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1270',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1271',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1272',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1273',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1274',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1275',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1276',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1277',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1278',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1279',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1280',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1282',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1283',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1284',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1287',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1288',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1289',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1290',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1291',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1001',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1020',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1021',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1040',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1041',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('1045',1,1);
Insert into KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK,TRANSACTION_TYPE_SUB_CODE_FK,TAG_ID_FK) values ('cash',1,2);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_TRANSACTION_TYPE_TAG
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_SEQUENCE_TABLE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_SEQUENCE_TABLE
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ACCOUNT_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ACTIVITY_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ACTIVITY_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ALLOCATION_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('BANK_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('CREDIT_PERMISSION_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('CURRENCY_SEQ',1002);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('DOCUMENT_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ELECTRONIC_CONTACT_SEQ',1002);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('FLAG_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('GL_BREAKDOWN_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('GL_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('INFORMATION_SEQ',1002);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('LATE_PERIOD_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('PERSON_NAME_SEQ',1002);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ADDRESS_NAME_SEQ',1002);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('ROLLUP_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('TAG_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('TAX_TYPE_SEQ',1001);
Insert into KSSA_SEQUENCE_TABLE (SEQ_NAME,SEQ_VALUE) values ('TRANSACTION_SEQ',1002);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_SEQUENCE_TABLE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ACNT_PROTECTED_INFO
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ACNT_PROTECTED_INFO
Insert into KSSA_ACNT_PROTECTED_INFO (ID,BANK_DETAILS,TAX_REFERENCE,BANK_TYPE_ID_FK,TAX_TYPE_ID_FK) values ('lgriffin','00000548:382389883:C','412-55-5658',1,1);
Insert into KSSA_ACNT_PROTECTED_INFO (ID,BANK_DETAILS,TAX_REFERENCE,BANK_TYPE_ID_FK,TAX_TYPE_ID_FK) values ('mgriffin','000000121:00000010201:S','666-85-7754',1,1);
Insert into KSSA_ACNT_PROTECTED_INFO (ID,BANK_DETAILS,TAX_REFERENCE,BANK_TYPE_ID_FK,TAX_TYPE_ID_FK) values ('pgriffin','0019921:20100020101:C','192-54-6666',1,1);
Insert into KSSA_ACNT_PROTECTED_INFO (ID,BANK_DETAILS,TAX_REFERENCE,BANK_TYPE_ID_FK,TAX_TYPE_ID_FK) values ('rtimmons','1121212544:8481454:C','999-09-0000',1,1);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ACNT_PROTECTED_INFO
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_GL_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_GL_TYPE
Insert into KSSA_GL_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (0,'pheald','General GL Type',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'General');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_GL_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_CURRENCY
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_CURRENCY
Insert into KSSA_CURRENCY (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ISO) values (4,'pheald','Euro',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Euro','EUR');
Insert into KSSA_CURRENCY (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ISO) values (1,'pheald','United States Dollar as used in the USA.','admin',to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Dollar','USD');
Insert into KSSA_CURRENCY (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ISO) values (2,'pheald','British Pound as used in the United Kingdom. Also referred to as sterling.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Sterling','GBP');
Insert into KSSA_CURRENCY (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ISO) values (3,'pheald','Australian Dollar.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Dollar','AUD');
Insert into KSSA_CURRENCY (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ISO) values (5,'pheald','South African Rand',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Rand','ZAR');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_CURRENCY
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_TRANSACTION_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_TRANSACTION_TYPE
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1553',1,'pheald','PHYSICS LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1554',1,'pheald','AOSC LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1555',1,'pheald','CLICKER FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1556',1,'pheald','JOUR PROF FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1557',1,'pheald','MCERT INTERNSHIP LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1558',1,'pheald','EDCP 692',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1559',1,'pheald','GRAD FEE STUDIO',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1560',1,'pheald','PERFORMANCE ASSESSMENT SYS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1561',1,'pheald','ANSC LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1562',1,'pheald','GRAD FEE STUDIO',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1563',1,'pheald','AOSC MPAO TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1564',1,'pheald','PLSC 433 TECH FRUIT + VEG PRO',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1565',1,'pheald','PLSC 244 HERBACEOUS PLANTS',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1566',1,'pheald','PLSC452 LANDSCAPE EST + MAINT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1567',1,'pheald','PLSC 361 COMM PRINCIPLES OF LM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1568',1,'pheald','MPS LEAD TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1600',1,'pheald','UNDERGRADUATE APPLICATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1602',1,'pheald','GRAD SCHOOL APPLICATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1604',1,'pheald','GRAD SCHOOL CONTINUOUS REG',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1606',1,'pheald','LATE REGISTRATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1608',1,'pheald','SHADY GROVE UG APPLICATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1609',1,'pheald','MULTIMEDIA JOUR',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1610',1,'pheald','LAW PREP WORKSHOP',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1611',1,'pheald','LSAT PREP WRKSHP',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1612',1,'pheald','PROF PROG UG REG FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1613',1,'pheald','PROF PROG GR REG FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1614',1,'pheald','STUDENT SVC FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1700',1,'pheald','TUITION AND FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1701',1,'pheald','TUITION AND FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1702',1,'pheald','EARLY COLLEGE PROGRAM OUTREACH',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1050',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1051',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1060',1,'pheald','GOLDEN ID FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1070',1,'pheald','SHADY GROVE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1071',1,'pheald','SHADY GROVE AUXILIARY FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1072',1,'pheald','SHADY GROVE FT FACILITIES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1073',1,'pheald','SHADY GROVE PT FACILITIES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1080',1,'pheald','SHADY GROVE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1100',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1101',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1120',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1121',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1140',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1141',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1160',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1161',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1162',1,'pheald','BELIZE TRAVEL-STUDY',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1165',1,'pheald','PUBLIC POLICY TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1166',1,'pheald','PUB POLICY TUIT DIFFERENTIAL',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1167',1,'pheald','PUBLIC POLICY TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1168',1,'pheald','PUB POLICY TUIT DIFFERENTIAL',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1170',1,'pheald','MBA TUITION DIFFERENTIAL RES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1171',1,'pheald','MBA TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1172',1,'pheald','MBA TUITION DIFFERENTIAL NRES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1173',1,'pheald','MBA TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1174',1,'pheald','MBA TUITION DIFFERENTIAL',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1175',1,'pheald','MBA SHADY GROVE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1176',1,'pheald','BALTIMORE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1177',1,'pheald','BALTIMORE ADMINISTRATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1178',1,'pheald','EXEC MASTERS PUBLIC POLICY',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1179',1,'pheald','EXECUTIVE MBA TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1180',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1181',1,'pheald','COLLABORATIVE ENGR TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1182',1,'pheald','PUAF EXEC CERTIFICATE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1183',1,'pheald','JPSM GRAD NONRESIDENT TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1184',1,'pheald','JPSM GRAD RESIDENT TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1186',1,'pheald','PUBLIC POLICY-DC',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1187',1,'pheald','PUAF DC ADMINISTRATIVE FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1188',1,'pheald','MLS SG GRAD RESIDENT TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1189',1,'pheald','MLS SG GRAD NONRES TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1190',1,'pheald','MLS SG SPECIAL PROGRAM FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1191',1,'pheald','EXECUTIVE MBA TUITION INTL',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1192',1,'pheald','MLS ONLINE RESIDENT TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1193',1,'pheald','MLS ONLINE NON-RES TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1194',1,'pheald','MLS ONLINE PROGRAM FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1195',1,'pheald','GOLDEN ID FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1196',1,'pheald','ACADEMIC SERVICES FEE - UG',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1198',1,'pheald','ACADEMIC SERVICES FEE - GRAD',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1202',1,'pheald','ACADEMIC SERVICES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1204',1,'pheald','ATHLETIC FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1206',1,'pheald','HEALTH FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1208',1,'pheald','SHUTTLE BUS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1210',1,'pheald','S.U. AND RECREATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1212',1,'pheald','STUDENT ACTIVITY FEE - UG',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1213',1,'pheald','STUDENT ACTIVITY FEE - GRAD',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1214',1,'pheald','AUXILIARY FACILITIES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1215',1,'pheald','BUILDING SERVICES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1216',1,'pheald','INSTRUCTIONAL MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1218',1,'pheald','NON RESIDENT FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1240',1,'pheald','MD ENGL INST INTENSIVE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1242',1,'pheald','MD ENGL INST SEMI-INTENSIVE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1244',1,'pheald','MD ENGL INST FLUENCY PROGRAM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1245',1,'pheald','MEI ADVANCED WRITING ONLINE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1246',1,'pheald','MD ENGL INST ADVANCED WRITING',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1247',1,'pheald','MD ENGL INST ORAL COMM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1248',1,'pheald','ACTIVITY/LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1256',1,'pheald','S/Y EXCH PGM SAO FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1257',1,'pheald','AFFIL ST PGM SAO FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1258',1,'pheald','NON-UM AFFIL ST PGM SAO FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1259',1,'pheald','S/Y NON-UM AFFIL PGM SAO FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1260',1,'pheald','MARYLAND IN LONDON',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1261',1,'pheald','MARYLAND IN LONDON PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1262',1,'pheald','UM SHORT TERM STUDY ABROAD FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1263',1,'pheald','MARYLAND IN NICE PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1264',1,'pheald','MARYLAND IN NICE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1265',1,'pheald','MARYLAND IN BERLIN PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1266',1,'pheald','MARYLAND IN BERLIN',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1267',1,'pheald','MARYLAND IN ROME PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1268',1,'pheald','MARYLAND IN ROME',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1269',1,'pheald','STUDY ABROAD PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1270',1,'pheald','STUDY ABROAD EXCHANGE PROGRAM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1271',1,'pheald','I E S STUDENT SERVICES FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1272',1,'pheald','STUDY IN ISRAEL PROGRAM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1273',1,'pheald','STUDY ABROAD PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1274',1,'pheald','MARYLAND IN HAIFA PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1275',1,'pheald','STUDY ABROAD PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1276',1,'pheald','STUDY IN GENOA',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1277',1,'pheald','MARYLAND IN HAIFA',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1278',1,'pheald','STUDY IN KIPLIN HALL',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1279',1,'pheald','STUDY IN KIPLIN HALL PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1280',1,'pheald','NATL TECH UNIV PROGRAM FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1282',1,'pheald','NATL TECH UNIV PROGRAM REM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1283',1,'pheald','MARYLAND IN CHINA',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1284',1,'pheald','MARYLAND IN SHANGHAI PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1287',1,'pheald','THE EDUCATION ABROAD NETWORK',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1288',1,'pheald','EDUCATION ABROAD NETWORK PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1289',1,'pheald','NON AFFILIATED EDUC ABR FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1290',1,'pheald','NON AFFIL''D EA FEES PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1291',1,'pheald','MACQUARIE UNIVERSITY',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1001',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1020',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1021',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1040',1,'pheald','TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1041',1,'pheald','MANDATORY FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1045',1,'pheald','ARCHITECTURE KEA LOAN',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','finaid2',1,'pheald','Tuition Only Scholarship',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,0,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','finaid',1,'pheald','General Financial Aid Transaction',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,null,0,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','pp',1,'pheald','Paypal Transfer',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the paypal authorization number.',0,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','ach',1,'pheald','Electronic Funds Transfer (ACH)',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the ACH reference number',0,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','ccma',1,'pheald','Credit Card payment made by mail',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the autorization code from the credit card terminal.',10,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','chma',1,'pheald','Check payment made by mail',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the check number',10,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','chip',1,'pheald','Check payment made in person',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the check number',10,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','ccip',1,'pheald','Credit Card Payment made In Person',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Please enter the authorization code for the transaction from the credit card terminal.',10,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','ccol',1,'pheald','Credit Card Payment made Online',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'Authorization code will be pre-filled by the system with the credit card authorization number.',10,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('C','cash',1,'pheald','Cash Payment',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),null,'No code requried for cash',0,null,null);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1292',1,'pheald','MACQUARIE UNIVERSITY PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1293',1,'pheald','ACC INT''L ACAD COLLABORATION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1294',1,'pheald','ACC INT''L ACAD COLLABOR PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1295',1,'pheald','MEPP JOINT PRGM TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1296',1,'pheald','DANISH INST FOR STUDY ABROAD',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1297',1,'pheald','DANISH INST STUDY ABROAD PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1298',1,'pheald','MARYLAND IN BARCELONA',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1299',1,'pheald','MARYLAND IN BARCELONA PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1301',1,'pheald','ENPM TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1302',1,'pheald','GCEN TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1303',1,'pheald','LIFE SCIENCE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1304',1,'pheald','LIFE SCIENCE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1305',1,'pheald','LIFE SCIENCE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1306',1,'pheald','LIFE SCIENCE TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1307',1,'pheald','MARYLAND IN LEIDEN',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1308',1,'pheald','MARYLAND IN LEIDEN PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1309',1,'pheald','MD EDUCATION ABROAD SCHOLAR',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1310',1,'pheald','MD EDUC ABR SCHOLARSHIP PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1311',1,'pheald','UNIV STUDIES ABROAD CONSORTIUM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1312',1,'pheald','UNIV STY ABROAD CONSORTIUM PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1313',1,'pheald','EDUCATION ABROAD EVENTS',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1314',1,'pheald','EDUCATION ABROAD EVENTS PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1315',1,'pheald','EDUCATION ABROAD ADMINISTRTION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1316',1,'pheald','EDUCATION ABROAD ADMIN PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1317',1,'pheald','EMERGENCY FUNDS',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1318',1,'pheald','EMERGENCY FUNDS PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1350',1,'pheald','BIOSPHERE 2 TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1351',1,'pheald','AUSTRALIA TRAVEL-STUDY 2010',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1352',1,'pheald','WINTER TERM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1353',1,'pheald','WINTER TERM PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1354',1,'pheald','SPRING SHORT-TERM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1355',1,'pheald','SPRING SHORT-TERM PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1356',1,'pheald','SUMMER TERM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1357',1,'pheald','SUMMER TERM PAYMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1358',1,'pheald','FRESHMAN ABR DESTN LONDON',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1359',1,'pheald','FRESHMAN ABR DESTN LONDON PMT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1400',1,'pheald','TITLE IV TUITION ADJUSTMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1401',1,'pheald','TITLE IV TUITION ADJUSTMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1402',1,'pheald','TITLE IV TUITION ADJUSTMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1403',1,'pheald','TITLE IV TUITION ADJUSTMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1404',1,'pheald','TITLE IV TUITION ADJUSTMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1420',1,'pheald','SPHL GLOBALHEALTHCERTIFICATE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1495',1,'pheald','RHS GRAD FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1496',1,'pheald','MASTERS REAL ESTATE DEV TUIT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1497',1,'pheald','MAIT TUITION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1498',1,'pheald','MILITARY REAL ESTATE DEV TUIT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1499',1,'pheald','SCHOOL OF ARCHITECTURE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1500',1,'pheald','RHS GRAD FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1501',1,'pheald','ARCHITECTURE STUDIO FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1502',1,'pheald','SPECIAL MATH FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1503',1,'pheald','LABORATORY MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1504',1,'pheald','STUDENT TEACHING LAB FEES',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1505',1,'pheald','APPLIED MUSIC FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1506',1,'pheald','SPECIAL CHEMISTRY FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1507',1,'pheald','ANTHROPOLOGY LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1508',1,'pheald','CO-OP EDUCATION PROGRAM',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1509',1,'pheald','TECHNOLOGY FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1510',1,'pheald','ENGINEERING CO-OP EDUCATION',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1511',1,'pheald','TECHNOLOGY FEE PART TIME',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1512',1,'pheald','MID CAREER WORKSHOP',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1513',1,'pheald','PUAF ENROLLMENT FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1514',1,'pheald','PUAF MD FELLOWS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1515',1,'pheald','LANDSCAPE ARCHITECTURE KEY',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1516',1,'pheald','LANDSCAPE ARCHITECTURE STUDIO',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1517',1,'pheald','LANDSCAPE ARCHITECTURE STUDIO',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1518',1,'pheald','MUED STUDENT TEACHING FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1519',1,'pheald','PLANT SCIENCES LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1520',1,'pheald','CLINICAL PRACTICUM FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1521',1,'pheald','FRESHMEN CONNECTION APP FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1522',1,'pheald','FRESHMEN CONNECTION ENROLLMENT',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1523',1,'pheald','FRESHMEN CONNECTION UGRAD NR',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1524',1,'pheald','FRESHMEN CONNECTION UGRAD R',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1525',1,'pheald','MFRI STUDENT FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1526',1,'pheald','MFRI STUDENT FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1527',1,'pheald','MFRI BOOKSTORE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1528',1,'pheald','MFRI BOOK FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1529',1,'pheald','AREC 306 FIELD TRIP FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1530',1,'pheald','GEOGRAPHY LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1531',1,'pheald','GEOGRAPHY 448 LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1532',1,'pheald','ART STUDIO GRAD FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1533',1,'pheald','ART STUDIO UG FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1534',1,'pheald','EDCP 315 MATERIAL FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1535',1,'pheald','CCJS 320 LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1536',1,'pheald','EDCP 417 MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1537',1,'pheald','EDCP 632 MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1538',1,'pheald','EDCP 108D MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1539',1,'pheald','EDCP 217 COURSE MATERIALS',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1540',1,'pheald','BMGT 367 COURSE MATERIALS',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1541',1,'pheald','BMGT 608A COURSE BOOKIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1542',1,'pheald','BMGT MATERIALS FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1543',1,'pheald','MBA TECHNOLOGY FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1544',1,'pheald','MBA ASSOCIATION FEE FULL-TIME',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1545',1,'pheald','MBS MATH CAMP FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1546',1,'pheald','MBA ASSOCIATION FEE PART-TIME',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1547',1,'pheald','MBS ORIENTATION FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1548',1,'pheald','MBA COLLEGE PARK SITE FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1549',1,'pheald','MBS LOCKER FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1550',1,'pheald','PHYSICS CONTROLLER',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1551',1,'pheald','ASTRONOMY LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);
Insert into KSSA_TRANSACTION_TYPE (TYPE,ID,SUB_CODE,CREATOR_ID,DEF_TRN_TXT,EDITOR_ID,END_DATE,LAST_UPDATE,START_DATE,PRIORITY,AUTH_TXT,CLEAR_PERIOD,REFUND_RULE,DEF_ROLLUP_ID_FK) values ('D','1552',1,'pheald','GEOLOGY LAB FEE',null,null,to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),to_timestamp('29-MAR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),1,null,null,null,3);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_TRANSACTION_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ROLLUP
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ROLLUP
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (7,'pheald','Any payment made by mail to the student finance office.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Payments Made by Mail');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (1,'pheald','All of the charges that were accrued at the bookstore.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Bookstore Charges');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (2,'pheald','All forms of financial aid.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Financial Aid Payments');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (3,'pheald','Tuition Charges for the undergraduate programs.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Undergraduate Tuition Charges');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (4,'pheald','Tuition charges for the graduate programs.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Graduate Tuition Charges');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (5,'pheald','Payments made in person with a cashier.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Payments Made in Person');
Insert into KSSA_ROLLUP (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME) values (6,'pheald','Payments Made online through the web portal.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Payments Made Online');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ROLLUP
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_ELECTRONIC_CONTACT
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_ELECTRONIC_CONTACT
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (1,'pheald','Y',null,'pgriffin@sigmauniversity.edu',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1',null,'401-555-0121','pgriffin');
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (2,'pheald','Y',null,'lgriffin@sigmaunivesrity.edu',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1',null,'401-555-0121','lgriffin');
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (3,'pheald','Y',null,'mgriffin@sigmauniversity.edu',null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1',null,'401-555-0121','mgriffin');
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (4,'pheald','N',null,null,null,null,to_timestamp('02-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1',null,'401-555-0114','mgriffin');
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (50050,'system','Y',null,'test@email.edu',null,null,to_timestamp('22-APR-12 07.32.03.506000000 PM','DD-MON-RR HH.MI.SS.FF AM'),null,null,null,'admin');
Insert into KSSA_ELECTRONIC_CONTACT (ID,CREATOR_ID,IS_DEFAULT,EDITOR_ID,EMAIL_ADDRESS,KIM_EMAIL_ADDRESS_TYPE,KIM_PHONE_TYPE,LAST_UPDATE,PHONE_COUNTRY,PHONE_EXTN,PHONE_NUMBER,ACNT_ID_FK) values (5,'pheald','Y',null,'rtimmons@sigmauniversity.edu',null,null,to_timestamp('05-MAY-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'1',null,'202-999-9999','rtimmons');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_ELECTRONIC_CONTACT
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_LATE_PERIOD
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_LATE_PERIOD
Insert into KSSA_LATE_PERIOD (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,DAYS_LATE1,DAYS_LATE2,DAYS_LATE3,IS_DEFAULT) values (1,'pheald','Standard 30/60/90 Late-period definition.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Standard',30,60,90,'Y');
Insert into KSSA_LATE_PERIOD (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,DAYS_LATE1,DAYS_LATE2,DAYS_LATE3,IS_DEFAULT) values (2,'pheald','Extended late period defintion used for businesses who pay by the middle of the semester.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Extended',90,120,150,'N');

---------------------------------------------------
--   END DATA FOR TABLE KSSA_LATE_PERIOD
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_FLAG_TYPE
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_FLAG_TYPE
Insert into KSSA_FLAG_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (1,'pheald','Account has been taken over allowable credit limit.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Over Limit',1);
Insert into KSSA_FLAG_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (2,'pheald','The address on the account has been flagged as invalid. Please verify.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Bad Address',0);
Insert into KSSA_FLAG_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (3,'pheald','The user has passed a bad check to the office.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Bad Check',1);
Insert into KSSA_FLAG_TYPE (ID,CREATOR_ID,DESCRIPTION,EDITOR_ID,LAST_UPDATE,NAME,ACCESS_LEVEL) values (4,'pheald','This account is past due. Prompt payment is required.',null,to_timestamp('03-APR-12 12.00.00.000000000 AM','DD-MON-RR HH.MI.SS.FF AM'),'Account Late',0);

---------------------------------------------------
--   END DATA FOR TABLE KSSA_FLAG_TYPE
---------------------------------------------------

---------------------------------------------------
--   DATA FOR TABLE KSSA_DOCUMENT
--   FILTER = none used
---------------------------------------------------
REM INSERTING into KSSA_DOCUMENT

---------------------------------------------------
--   END DATA FOR TABLE KSSA_DOCUMENT
---------------------------------------------------
