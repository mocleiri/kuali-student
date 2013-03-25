This branch of the CM 2.0 data is intended to provide a cleaned-up version of the CM 2.0 data. We have seen that new institutions sometime struggle with the volume and complexity of the data and have difficulty knowing what should be deleted, what should be kept and what should be changed. Cleaned-up here means that the instance data (specific courses, departments, campuses, etc) from the reference institutions have been deleted.  What is left is the type data, which means new implementing institutions can use it as a simpler starting point. Implementing institutions may choose to use the types as-is or replace them with types more suitable for them. The types are left for convenience and examples.

As of March 2013, this is a first cut. We will be updating this as we learn more during our implemention of CM 2.0. We may find that certain remaining data is better deleted or that certain deleted data should be restored. We will not be adding any data, types or instance data, specific to our implementation. 

The validity, or at least internal consistency, of the data is demonstrated by a couple tests:
1.	It successfully impex:installs into a database schema.
2.	CM 2.0 will run (or at least, start) when using that database schema.

The KR* tables have not been cleaned up although many of them also have instance data. 

Below are listed several groups of tables:
	those with all data deleted, that is, they no longer have an impex xml file. There are about 90 of these.  
	those that have not been changed
	those that have had some but not all data deleted. There are two of these. 

Tables with all CM 2.0 data deleted
-------------------------------------
KSEN_ATP 
KSEN_ATPATP_RELTN 
KSEN_ATPMSTONE_RELTN 
KSEN_CO_SEAT_POOL_DEFN 
KSEN_HOLD_ISSUE 
KSEN_LPR 
KSEN_LRC_RESULT_SCALE
KSEN_LRC_RESULT_VALUE 
KSEN_LRC_RVG 
KSEN_LRC_RVG_RESULT_VALUE 
KSEN_LRR 
KSEN_LRR_RES_SOURCE
KSEN_LUI 
KSEN_LUILUI_RELTN 
KSEN_LUI_ATTR 
KSEN_LUI_IDENT
KSEN_LUI_LU_CD 
KSEN_LUI_RELATED_LUI_TYPE 
KSEN_LUI_RESULT_VAL_GRP 
KSEN_LUI_UNITS_CONT_OWNER 
KSEN_LUI_UNITS_DEPLOYMENT 
KSEN_MSTONE 
KSEN_POPULATION 
KSEN_POPULATION_RULE 
KSEN_POPULATION_RULE_AGENDA 
KSEN_POPULATION_RULE_CHILD_POP 
KSEN_PROCESS 
KSEN_PROCESS_CHECK 
KSEN_PROCESS_INSTRN 
KSEN_RICH_TEXT_T 
KSEN_SCHED 
KSEN_SCHED_CMP 
KSEN_SCHED_CMP_TMSLOT 
KSEN_SCHED_TMSLOT 
KSEN_SOC 
KSEN_SOC_ATTR 
KSLO_LO_ATTR 
KSLO_LO 
KSLO_LO_JN_LOCATEGORY 
KSLO_LO_RELTN 
KSLU_CLU
KSLU_CLUCLU_RELTN 
KSLU_CLURES_JN_RESOPT 
KSLU_CLU_ACCT 
KSLU_CLU_ACCT_JN_AFFIL_ORG 
KSLU_CLU_ADMIN_ORG 
KSLU_CLU_AFFIL_ORG 
KSLU_CLU_ATP_TYPE_KEY 
KSLU_CLU_ATTR 
KSLU_CLU_FEE 
KSLU_CLUFEEREC_JN_AFFIL_ORG 
KSLU_CLU_FEE_AMOUNT 
KSLU_CLU_FEE_JN_CLU_FEE_REC 
KSLU_CLU_FEE_REC 
KSLU_CLU_IDENT 
KSLU_CLU_JN_CAMP_LOC 
KSLU_CLU_JN_CLU_IDENT 
KSLU_CLU_LO_RELTN 
KSLU_CLU_PUBL 
KSLU_CLU_RSLT 
KSLU_CLU_SET 
KSLU_CLU_SET_JN_CLU 
KSLU_CLU_SET_JN_CLU_SET
KSLU_LU_CODE 
KSLU_MEMSHIP 
KSLU_MEMSHIP_KSLU_SPARAM 
KSLU_RICH_TEXT_T 
KSLU_RSLT_OPT
KSLU_SPARAM 
KSLU_SPARAM_KSLU_SPVALUE 
KSLU_SPVALUE 
KSOR_ORG 
KSOR_ORG_HIRCHY 
KSOR_ORG_HIRCHY_JN_ORG_TYPE  
KSOR_ORG_JN_ORG_PERS_REL_TYPE 
KSOR_ORG_ORG_RELTN 
KSOR_ORG_ORG_RELTN_TYPE 
KSOR_ORG_PERS_RELTN  
KSOR_ORG_PERS_RELTN_TYPE 
KSOR_ORG_POS_RESTR 
KSSC_SUBJ_CD 
KSSC_SUBJ_CD_JN_ORG 
KSST_RC_JN_RC_FIELD 
KSST_REF_STMT_REL 
KSST_REQ_COM 
KSST_REQ_COM_FIELD 
KSST_RICH_TEXT_T 
KSST_STMT 
KSST_STMT_JN_REQ_COM 
KSST_STMT_JN_STMT



Type tables, left as is in CM 2.0
--------------------------------------------
KSCO_COMMENT_TYPE	
KSCO_REFERENCE_TYPE 
KSCO_TAG_TYPE
KSDO_DOCUMENT_CATEGORY
KSDO_DOCUMENT_TYPE
KSDO_REF_DOC_RELTN_TYPE 
KSDO_REF_OBJ_SUB_TYPE 
KSDO_REF_OBJ_TYPE 
KSDO_REF_REL_TYP_JN_SUB_TYPE 
KSDO_RICH_TEXT_T 
KSEM_ENUM_T 
KSEM_CTX_T 
KSEM_CTX_JN_ENUM_VAL_T
KSEN_COMM_STATE
KSEN_LRR_TYPE 
KSEN_STATE 
KSEN_STATEPROCESS_RELTN 
KSEN_STATE_LIFECYCLE 
KSEN_STATE_PROCESS 
KSEN_TYPE 
KSEN_TYPETYPE_RELTN 
KSLO_LO_ALLOWED_RELTN_TYPE 
KSLO_LO_CATEGORY 
KSLO_LO_CATEGORY_TYPE 
KSLO_LO_RELTN_TYPE 
KSLO_LO_REPOSITORY 
KSLO_LO_TYPE 
KSLU_CLU_LO_RELTN_TYPE 
KSLU_CLU_PUBL_TYPE 
KSLU_CLU_RSLT_TYP 
KSLU_CLU_SET_TYPE 
KSLU_LULU_RELTN_TYPE 
KSLU_LUTYPE 
KSLU_RSLT_USG_TYPE 
KSMG_MESSAGE
KSOR_ORG_TYPE 
KSOR_ORG_TYPE_JN_ORG_PERRRL_TYP 
KSPR_PROPOSAL_REFTYPE 
KSPR_PROPOSAL_TYPE 
KSSC_SUBJ_CD_TYPE 
KSST_NL_USAGE_TYPE 
KSST_OBJECT_TYPE 
KSST_RCTYP_JN_RCFLDTYP 
KSST_REF_STMT_REL_TYPE 
KSST_REQ_COM_TYPE 
KSST_REQ_COM_FIELD_TYPE 
KSST_REQ_COM_TYPE_NL_TMPL 
KSST_RSTMT_RTYP_JN_STMT_TYP  
KSST_STMT_TYPE 
KSST_STMT_TYP_JN_RC_TYP 
KSST_STMT_TYP_JN_STMT_TYP 
KS_DB_VERSION – I’m not sure what this is.



Some data deleted, some retained
-------------------------------------------
KSEM_ENUM_VAL_T –  deleted SubjectAreas, campusLocations, CIP2000 codes
KSLO_RICH_TEXT_T – deleted all but 3 rows that are referenced by  KSLO_LO_REPOSITORY rows

