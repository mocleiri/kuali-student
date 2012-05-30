--------------------------------------------------------
--  File created - Wednesday-May-30-2012   
--------------------------------------------------------

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