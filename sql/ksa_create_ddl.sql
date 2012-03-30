
-- This Oracle script creates database objects in KSA schema.
-- It needs to be run by the schema owner
-- Creating sequence table

create table KSA.KSSA_SEQUENCE_TABLE ( SEQ_NAME varchar2(255 char),  SEQ_VALUE number(10,0) ) ;

-- Creating base tables

create table KSA.KSSA_ACNT (TYPE varchar2(31 char) not null, ID varchar2(45 char) not null, CAN_AUTHENTICATE char(1 char), CREATION_DATE timestamp, CREDIT_LIMIT number(19,2), ENTITY_ID varchar2(45 char), IS_KIM_ACNT number(1,0), LAST_KIM_UPDATE timestamp, LATE_PERIOD_ID_FK number(19,0), primary key (ID));
create table KSA.KSSA_ACNT_STATUS_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_ACTIVITY (ID number(19,0) not null, ATTRIBUTE varchar2(200 char), ENTITY_ID varchar2(45 char), IP varchar2(32 char), LOG_DETAIL varchar2(200 char), MAC varchar2(12 char), CREATION_DATE timestamp, ACNT_ID_FK varchar2(45 char), ACTIVITY_TYPE_ID_FK number(19,0), primary key (ID));
create table KSA.KSSA_ACTIVITY_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_ALLOCATION (ID number(19,0) not null, AMNT number(19,2), ACNT_ID_FK varchar2(45 char), TRN_ID_1_FK number(19,0), TRN_ID_2_FK number(19,0), primary key (ID));
create table KSA.KSSA_BANK_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_CREDIT_PERMISSION (ID number(19,0) not null, ALLOWABLE_DEBIT_TYPE_MASK varchar2(20 char), PRIORITY number(10,0), TRANSACTION_TYPE_ID_FK varchar2(20 char), TRANSACTION_TYPE_SUB_CODE_FK number(10,0), primary key (ID));
create table KSA.KSSA_CURRENCY (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_DOCUMENT (ID number(19,0) not null, DOCUMENT varchar2(255 char), CREATION_DATE timestamp, CREATOR_ID varchar2(45 char), EDIT_REASON varchar2(512 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, primary key (ID));
create table KSA.KSSA_ELECTRONIC_CONTACT (ID number(19,0) not null, CREATOR_ID varchar2(45 char), IS_DEFAULT char(1 char), EDITOR_ID varchar2(45 char), EMAIL_ADDRESS varchar2(255 char), KIM_EMAIL_ADDRESS_TYPE varchar2(45 char), KIM_PHONE_TYPE varchar2(45 char), LAST_UPDATE timestamp, PHONE_COUNTRY varchar2(5 char), PHONE_EXTN varchar2(10 char), PHONE_NUMBER varchar2(20 char), ACNT_ID_FK varchar2(45 char), primary key (ID));
create table KSA.KSSA_FLAG_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), ACCESS_LEVEL number(10,0), primary key (ID));
create table KSA.KSSA_GL_BREAKDOWN (ID number(19,0) not null, GL_ACCOUNT varchar2(45 char), BREAKDOWN number(19,2), TRANSACTION_TYPE_ID_FK varchar2(20 char), TRANSACTION_TYPE_SUB_CODE_FK number(10,0), GL_TYPE_ID_FK number(19,0), primary key (ID));
create table KSA.KSSA_GL_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_INFORMATION (TYPE varchar2(31 char) not null, ID number(19,0) not null, ACCESS_LEVEL number(10,0), CREATION_DATE timestamp, CREATOR_ID varchar2(45 char), EDITOR_ID varchar2(45 char), EFFECTIVE_DATE timestamp, EXPIRATION_DATE timestamp, LAST_UPDATE timestamp, RESP_ENTITY varchar2(45 char), TEXT varchar2(4000 char), SEVERITY number(10,0), ACNT_ID_FK varchar2(45 char), TRN_ID_FK number(19,0), NEXT_ID number(19,0), PREV_ID number(19,0), FLAG_TYPE_ID_FK number(19,0), primary key (ID));
create table KSA.KSSA_LATE_PERIOD (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), DAYS_LATE1 number(10,0), DAYS_LATE2 number(10,0), DAYS_LATE3 number(10,0), IS_DEFAULT char(1 char), primary key (ID));
create table KSA.KSSA_PERSON_NAME (ID number(19,0) not null, CREATOR_ID varchar2(45 char), IS_DEFAULT char(1 char), EDITOR_ID varchar2(45 char), FIRST_NAME varchar2(100 char), KIM_NAME_TYPE varchar2(45 char), LAST_NAME varchar2(100 char), LAST_UPDATE timestamp, MIDDLE_NAME varchar2(100 char), SUFFIX varchar2(10 char), TITLE varchar2(10 char), ACNT_ID_FK varchar2(45 char), primary key (ID));
create table KSA.KSSA_POSTAL_ADDRESS (ID number(19,0) not null, COUNTRY_CODE varchar2(10 char), CREATOR_ID varchar2(45 char), IS_DEFAULT char(1 char), EDITOR_ID varchar2(45 char), KIM_ADDRESS_TYPE varchar2(45 char), LAST_UPDATE timestamp, POSTAL_CODE varchar2(12 char), STATE_CODE varchar2(5 char), LINE1 varchar2(100 char), LINE2 varchar2(100 char), LINE3 varchar2(100 char), ACNT_ID_FK varchar2(45 char), primary key (ID));
create table KSA.KSSA_ROLLUP (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_TAG (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), ACCESS_LEVEL number(10,0), primary key (ID));
create table KSA.KSSA_TAX_TYPE (ID number(19,0) not null, CREATOR_ID varchar2(45 char), DESCRIPTION varchar2(2000 char), EDITOR_ID varchar2(45 char), LAST_UPDATE timestamp, NAME varchar2(100 char), primary key (ID));
create table KSA.KSSA_TRANSACTION (TYPE varchar2(31 char) not null, ID number(19,0) not null, ALLOCATED number(19,2), AMNT number(19,2), EFFECTIVE_DATE timestamp, EXTN_ID varchar2(45 char), GL_ENTRY_GENERATED char(1 char), IS_INTERNAL_TRN char(1 char), LEDGER_DATE timestamp, LOCKED_ALLOCATED number(19,2), NATIVE_AMNT number(19,2), ORIG_DATE timestamp, RESP_ENTITY varchar2(45 char), STATEMENT_TXT varchar2(100 char), DEFER_ID number(19,0), EXPIRATION_DATE timestamp, CLEAR_DATE timestamp, ACNT_ID_FK varchar2(45 char), CURRENCY_ID_FK number(19,0), DOCUMENT_ID_FK number(19,0), ROLLUP_ID_FK number(19,0), TYPE_ID varchar2(20 char), TYPE_SUB_CODE number(10,0), primary key (ID));
create table KSA.KSSA_TRANSACTION_TYPE (TYPE varchar2(31 char) not null, ID varchar2(20 char) not null, SUB_CODE number(10,0) not null, CREATOR_ID varchar2(45 char), DEF_TRN_TXT varchar2(100 char), EDITOR_ID varchar2(45 char), END_DATE timestamp, LAST_UPDATE timestamp, START_DATE timestamp, AUTH_TXT varchar2(45 char), CLEAR_PERIOD number(10,0), REFUND_RULE varchar2(2000 char), PRIORITY number(10,0), DEF_ROLLUP_ID_FK number(19,0), primary key (ID, SUB_CODE));
create table KSA.KSSA_TRANSACTION_TYPE_TAG (TRANSACTION_TYPE_ID_FK varchar2(20 char) not null, TRANSACTION_TYPE_SUB_CODE_FK number(10,0) not null, TAG_ID_FK number(19,0) not null);

-- Creating constraints

alter table KSA.KSSA_ACNT add constraint FKB8F7925156D383B8 foreign key (LATE_PERIOD_ID_FK) references KSA.KSSA_LATE_PERIOD;
alter table KSA.KSSA_ACTIVITY add constraint FK7D1E4778D043544A foreign key (ACTIVITY_TYPE_ID_FK) references KSA.KSSA_ACTIVITY_TYPE;
alter table KSA.KSSA_ACTIVITY add constraint FK7D1E477898518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_ALLOCATION add constraint FKC2912B0998518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_ALLOCATION add constraint FKC2912B09ED7E538 foreign key (TRN_ID_2_FK) references KSA.KSSA_TRANSACTION;
alter table KSA.KSSA_ALLOCATION add constraint FKC2912B09ED770D9 foreign key (TRN_ID_1_FK) references KSA.KSSA_TRANSACTION;
alter table KSA.KSSA_CREDIT_PERMISSION add constraint FK1F74048CBC57B259 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSA.KSSA_TRANSACTION_TYPE;
alter table KSA.KSSA_ELECTRONIC_CONTACT add constraint FK77758E6698518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_GL_BREAKDOWN add constraint FKF48BE710AE27AC92 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSA.KSSA_TRANSACTION_TYPE;
alter table KSA.KSSA_GL_BREAKDOWN add constraint FKF48BE710FA70171C foreign key (GL_TYPE_ID_FK) references KSA.KSSA_GL_TYPE;
alter table KSA.KSSA_INFORMATION add constraint FKD2A2E003DBB2002A foreign key (FLAG_TYPE_ID_FK) references KSA.KSSA_FLAG_TYPE;
alter table KSA.KSSA_INFORMATION add constraint FKD2A2E00316BC312E foreign key (NEXT_ID) references KSA.KSSA_INFORMATION;
alter table KSA.KSSA_INFORMATION add constraint FKD2A2E00395ACD1EE foreign key (PREV_ID) references KSA.KSSA_INFORMATION;
alter table KSA.KSSA_INFORMATION add constraint FKD2A2E00398518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_INFORMATION add constraint FKD2A2E003FE6E074B foreign key (TRN_ID_FK) references KSA.KSSA_TRANSACTION;
alter table KSA.KSSA_PERSON_NAME add constraint FK45AEC02C98518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_POSTAL_ADDRESS add constraint FKC90A760998518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_TRANSACTION add constraint FKDCED3DB598518DD2 foreign key (ACNT_ID_FK) references KSA.KSSA_ACNT;
alter table KSA.KSSA_TRANSACTION add constraint FKDCED3DB5FB9EC59 foreign key (CURRENCY_ID_FK) references KSA.KSSA_CURRENCY;
alter table KSA.KSSA_TRANSACTION add constraint FKDCED3DB590ED3EED foreign key (DOCUMENT_ID_FK) references KSA.KSSA_DOCUMENT;
alter table KSA.KSSA_TRANSACTION add constraint FKDCED3DB534E0733C foreign key (TYPE_ID, TYPE_SUB_CODE) references KSA.KSSA_TRANSACTION_TYPE;
alter table KSA.KSSA_TRANSACTION add constraint FKDCED3DB5F7D721E7 foreign key (ROLLUP_ID_FK) references KSA.KSSA_ROLLUP;
alter table KSA.KSSA_TRANSACTION_TYPE_TAG add constraint FKA1635C3F47AB5D71 foreign key (TAG_ID_FK) references KSA.KSSA_TAG;
alter table KSA.KSSA_TRANSACTION_TYPE_TAG add constraint FKA1635C3FCE008744 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSA.KSSA_TRANSACTION_TYPE;
