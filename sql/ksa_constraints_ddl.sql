-- Constraints for non-annotated association tables

alter table KSSA_ACNT_KYPR add constraint FK0001_KSSA_ACNT_KYPR foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_ACNT_KYPR add constraint FK0002_KSSA_ACNT_KYPR foreign key (KYPR_ID_FK) references KSSA_KYPR;


-- Creating constraints

alter table KSSA_CASH_LIMIT_EVENT_TRANS add constraint FKEF56CBD3D61AFDF9 foreign key (TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_CASH_LIMIT_EVENT_TRANS add constraint FKEF56CBD3E0CEA4F3 foreign key (CASH_LIMIT_EVENT_ID_FK) references KSSA_CASH_LIMIT_EVENT;
alter table KSSA_ACNT add constraint FKB8F79251AC72F7E6 foreign key (ACNT_STATUS_TYPE_ID_FK) references KSSA_ACNT_STATUS_TYPE;
alter table KSSA_ACNT add constraint FKB8F7925156D383B8 foreign key (LATE_PERIOD_ID_FK) references KSSA_LATE_PERIOD;
alter table KSSA_ACNT_PROTECTED_INFO add constraint FKEF75726D2C28B62A foreign key (BANK_TYPE_ID_FK) references KSSA_BANK_TYPE;
alter table KSSA_ACNT_PROTECTED_INFO add constraint FKEF75726D73E6C8ED foreign key (ID_TYPE_ID_FK) references KSSA_ID_TYPE;
alter table KSSA_ACNT_PROTECTED_INFO add constraint FKEF75726DACFC7690 foreign key (TAX_TYPE_ID_FK) references KSSA_TAX_TYPE;
alter table KSSA_ALLOCATION add constraint FKC2912B0998518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_ALLOCATION add constraint FKC2912B09ED7E538 foreign key (TRN_ID_2_FK) references KSSA_TRANSACTION;
alter table KSSA_ALLOCATION add constraint FKC2912B09ED770D9 foreign key (TRN_ID_1_FK) references KSSA_TRANSACTION;
alter table KSSA_BATCH_RECEIPT add constraint FK6961BF2A4861130D foreign key (INCOMING_XML_ID_FK) references KSSA_XML;
alter table KSSA_BATCH_RECEIPT add constraint FK6961BF2A98518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_BATCH_RECEIPT add constraint FK6961BF2A8016ED3 foreign key (OUTGOING_XML_ID_FK) references KSSA_XML;
alter table KSSA_BILL_AUTHORITY add constraint FK2239C2947FD92EAC foreign key (PERSON_NAME_ID_FK) references KSSA_PERSON_NAME;
alter table KSSA_BILL_AUTHORITY add constraint FK2239C29498518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_BILL_AUTHORITY add constraint FK2239C294F6AF9F26 foreign key (POSTAL_ADDRESS_ID_FK) references KSSA_POSTAL_ADDRESS;
alter table KSSA_BILL_AUTHORITY add constraint FK2239C294E5947A1E foreign key (ELECTRONIC_CONTACT_ID_FK) references KSSA_ELECTRONIC_CONTACT;
alter table KSSA_CASH_LIMIT_EVENT add constraint FK6B695933F13B97E6 foreign key (XML_ID_FK) references KSSA_XML;
alter table KSSA_CASH_LIMIT_PARAMETER add constraint FKEA9B9DC247AB5D71 foreign key (TAG_ID_FK) references KSSA_TAG;
alter table KSSA_CREDIT_PERMISSION add constraint FK1F74048CBC57B259 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSSA_TRANSACTION_TYPE;
alter table KSSA_ELECTRONIC_CONTACT_ACNT add constraint FKED3ACBA198518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_ELECTRONIC_CONTACT_ACNT add constraint FKED3ACBA1E5947A1E foreign key (ELECTRONIC_CONTACT_ID_FK) references KSSA_ELECTRONIC_CONTACT;
alter table KSSA_EXTERNAL_STATEMENT add constraint FK818A3B0498518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_FD_KYPR add constraint FKDD4CD1485C970A06 foreign key (KYPR_ID_FK) references KSSA_KYPR;
alter table KSSA_FD_KYPR add constraint FKDD4CD148C0260932 foreign key (FD_ID_FK) references KSSA_FEE_DETAIL;
alter table KSSA_FEE_DETAIL add constraint FKF349E9739BE8AA30 foreign key (FEE_TYPE_ID_FK) references KSSA_FEE_TYPE;
alter table KSSA_FEE_DETAIL_AMOUNT add constraint FK295260A4309F229E foreign key (FEE_DETAIL_ID_FK) references KSSA_FEE_DETAIL;
alter table KSSA_GL_BREAKDOWN add constraint FKF48BE710AE27AC92 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSSA_TRANSACTION_TYPE;
alter table KSSA_GL_BREAKDOWN add constraint FKF48BE710FA70171C foreign key (GL_TYPE_ID_FK) references KSSA_GL_TYPE;
alter table KSSA_GL_BREAKDOWN_OVERRIDE add constraint FKD521481BD61AFDF9 foreign key (TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_GL_TRANSACTION add constraint FK1009500DAF219853 foreign key (GL_RECOGNITION_PERIOD_ID_FK) references KSSA_GL_RECOGNITION_PERIOD;
alter table KSSA_GL_TRANSACTION add constraint FK1009500D103CB71E foreign key (GL_TRANSMISSION_ID_FK) references KSSA_GL_TRANSMISSION;
alter table KSSA_GL_TRANSMISSION add constraint FK761EEA75AF219853 foreign key (GL_RECOGNITION_PERIOD_ID_FK) references KSSA_GL_RECOGNITION_PERIOD;
alter table KSSA_GL_TRANS_TRANSACTION add constraint FK1A29A596D61AFDF9 foreign key (TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_GL_TRANS_TRANSACTION add constraint FK1A29A596C852663A foreign key (GL_TRANSACTION_ID_FK) references KSSA_GL_TRANSACTION;
alter table KSSA_INFORMATION add constraint FKD2A2E003DBB2002A foreign key (FLAG_TYPE_ID_FK) references KSSA_FLAG_TYPE;
alter table KSSA_INFORMATION add constraint FKD2A2E00316BC312E foreign key (NEXT_ID) references KSSA_INFORMATION;
alter table KSSA_INFORMATION add constraint FKD2A2E00395ACD1EE foreign key (PREV_ID) references KSSA_INFORMATION;
alter table KSSA_INFORMATION add constraint FKD2A2E00398518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_INFORMATION add constraint FKD2A2E003FE6E074B foreign key (TRN_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_IRS_1098T add constraint FKCD8EF71887CB7AAA foreign key (STUDENT_POSTAL_ADDRESS_ID_FK) references KSSA_POSTAL_ADDRESS;
alter table KSSA_IRS_1098T add constraint FKCD8EF71898518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_IRS_1098T add constraint FKCD8EF718F13B97E6 foreign key (XML_ID_FK) references KSSA_XML;
alter table KSSA_KYPR add constraint FKB8FC70D99A56DE38 foreign key (LEARNING_PERIOD_ID_FK) references KSSA_LEARNING_PERIOD;
alter table KSSA_LU add constraint FK11C630B29A56DE38 foreign key (LEARNING_PERIOD_ID_FK) references KSSA_LEARNING_PERIOD;
alter table KSSA_LU add constraint FK11C630B298518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_LU_KYPR add constraint FK37B4975DF8899302 foreign key (LU_ID_FK) references KSSA_LU;
alter table KSSA_LU_KYPR add constraint FK37B4975D5C970A06 foreign key (KYPR_ID_FK) references KSSA_KYPR;
alter table KSSA_PERSON_NAME_ACNT add constraint FK9F7C59B7FD92EAC foreign key (PERSON_NAME_ID_FK) references KSSA_PERSON_NAME;
alter table KSSA_PERSON_NAME_ACNT add constraint FK9F7C59B98518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_POSTAL_ADDRESS_ACNT add constraint FKBDC431DE98518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_POSTAL_ADDRESS_ACNT add constraint FKBDC431DEF6AF9F26 foreign key (POSTAL_ADDRESS_ID_FK) references KSSA_POSTAL_ADDRESS;
alter table KSSA_REFUND add constraint FK76773E217FC9D8F2 foreign key (REFUND_TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_REFUND add constraint FK76773E21D61AFDF9 foreign key (TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_REFUND add constraint FK76773E21A0B2BA5 foreign key (AUTHORIZED_BY_ID_FK) references KSSA_ACNT;
alter table KSSA_REFUND add constraint FK76773E21C8C8E732 foreign key (REQUESTED_BY_ID_FK) references KSSA_ACNT;
alter table KSSA_REFUND add constraint FK76773E21FF0A3794 foreign key (REFUND_MANIFEST_ID_FK) references KSSA_REFUND_MANIFEST;
alter table KSSA_REFUND add constraint FK76773E21BC852B6A foreign key (REFUND_TYPE_ID_FK) references KSSA_REFUND_TYPE;
alter table KSSA_REFUND_MANIFEST add constraint FK621A668DA0AC5650 foreign key (REFUND_ACCOUNT_ID_FK) references KSSA_ACNT;
alter table KSSA_REFUND_MANIFEST add constraint FK621A668D7FC9D8F2 foreign key (REFUND_TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_RULE add constraint FKB8FF8FE593E85A22 foreign key (RULE_TYPE_ID_FK) references KSSA_RULE_TYPE;
alter table KSSA_RULE_SET add constraint FKD6784E893E85A22 foreign key (RULE_TYPE_ID_FK) references KSSA_RULE_TYPE;
alter table KSSA_RULE_SET_RULE add constraint FKBF093F391A9A467 foreign key (RULE_ID_FK) references KSSA_RULE;
alter table KSSA_RULE_SET_RULE add constraint FKBF093F37FE8AA6A foreign key (RULE_SET_ID_FK) references KSSA_RULE_SET;
alter table KSSA_TRANSACTION add constraint FKDCED3DB598518DD2 foreign key (ACNT_ID_FK) references KSSA_ACNT;
alter table KSSA_TRANSACTION add constraint FKDCED3DB5FB9EC59 foreign key (CURRENCY_ID_FK) references KSSA_CURRENCY;
alter table KSSA_TRANSACTION add constraint FKDCED3DB590ED3EED foreign key (DOCUMENT_ID_FK) references KSSA_DOCUMENT;
alter table KSSA_TRANSACTION add constraint FKDCED3DB5CE008744 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSSA_TRANSACTION_TYPE;
alter table KSSA_TRANSACTION add constraint FKDCED3DB5F7D721E7 foreign key (ROLLUP_ID_FK) references KSSA_ROLLUP;
alter table KSSA_TRANSACTION add constraint FKDCED3DB5FA70171C foreign key (GL_TYPE_ID_FK) references KSSA_GL_TYPE;
alter table KSSA_TRANSACTION_TAG add constraint FK429CDF0D61AFDF9 foreign key (TRANSACTION_ID_FK) references KSSA_TRANSACTION;
alter table KSSA_TRANSACTION_TAG add constraint FK429CDF047AB5D71 foreign key (TAG_ID_FK) references KSSA_TAG;
alter table KSSA_TRANSACTION_TYPE add constraint FK81104B8496077E1 foreign key (DEF_ROLLUP_ID_FK) references KSSA_ROLLUP;
alter table KSSA_TRANSACTION_TYPE_TAG add constraint FKA1635C3F47AB5D71 foreign key (TAG_ID_FK) references KSSA_TAG;
alter table KSSA_TRANSACTION_TYPE_TAG add constraint FKA1635C3FCE008744 foreign key (TRANSACTION_TYPE_ID_FK, TRANSACTION_TYPE_SUB_CODE_FK) references KSSA_TRANSACTION_TYPE;
