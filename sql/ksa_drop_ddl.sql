
-- This Oracle script drops database objects in KSA schema.
-- It needs to be run by the schema owner

drop table KSA.KSSA_ACNT cascade constraints;
drop table KSA.KSSA_ACNT_PROTECTED_INFO cascade constraints;
drop table KSA.KSSA_ACNT_STATUS_TYPE cascade constraints;
drop table KSA.KSSA_ACTIVITY cascade constraints;
drop table KSA.KSSA_ACTIVITY_TYPE cascade constraints;
drop table KSA.KSSA_ALLOCATION cascade constraints;
drop table KSA.KSSA_BANK_TYPE cascade constraints;
drop table KSA.KSSA_CREDIT_PERMISSION cascade constraints;
drop table KSA.KSSA_CURRENCY cascade constraints;
drop table KSA.KSSA_DOCUMENT cascade constraints;
drop table KSA.KSSA_ELECTRONIC_CONTACT cascade constraints;
drop table KSA.KSSA_FLAG_TYPE cascade constraints;
drop table KSA.KSSA_GL_BREAKDOWN cascade constraints;
drop table KSA.KSSA_GL_TYPE cascade constraints;
drop table KSA.KSSA_INFORMATION cascade constraints;
drop table KSA.KSSA_LATE_PERIOD cascade constraints;
drop table KSA.KSSA_PERSON_NAME cascade constraints;
drop table KSA.KSSA_POSTAL_ADDRESS cascade constraints;
drop table KSA.KSSA_ROLLUP cascade constraints;
drop table KSA.KSSA_TAG cascade constraints;
drop table KSA.KSSA_TAX_TYPE cascade constraints;
drop table KSA.KSSA_TRANSACTION cascade constraints;
drop table KSA.KSSA_TRANSACTION_TYPE cascade constraints;
drop table KSA.KSSA_TRANSACTION_TYPE_TAG cascade constraints;
drop table KSA.KSSA_SEQUENCE_TABLE cascade constraints;