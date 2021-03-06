-- DO NOT COPY AND PASTE THIS COMMENT.  VIOLATORS WILL LOSE COMMIT ACCESS.
-- KEY1:MjAxNC0wNC0xMS1DcmVhdGUtYW5kLWFsdGVyLXR5cGUtaW5kZXhlcy1mb3Ita3Nlbi1rc2x1LnNxbA==
-- KEY2:U1RSVUNUVVJF
-- TYPE:STRUCTURE

--KSEN
CREATE INDEX KSPRF0 ON KSEN_ROOM_BUILDING (BUILDING_TYPE)
/
ALTER INDEX KSPRF0 REBUILD
/
CREATE INDEX KSPRF1 ON KSEN_ROOM_FIXED_RSRC (FIXED_RSRC_TYPE)
/
ALTER INDEX KSPRF1 REBUILD
/
CREATE INDEX KSPRF2 ON KSEN_ROOM_USAGE (LAYOUT_TYPE)
/
ALTER INDEX KSPRF2 REBUILD
/
CREATE INDEX KSPRF3 ON KSEN_SCHED_RQST (SCHED_RQST_TYPE)
/
ALTER INDEX KSPRF3 REBUILD
/
CREATE INDEX KSPRF4 ON KSEN_SOC_ROR (SOC_ROR_TYPE)
/
ALTER INDEX KSPRF4 REBUILD
/
CREATE INDEX KSPRF5 ON KSEN_SOC_ROR_ITEM (SOC_ROR_TYPE)
/
ALTER INDEX KSPRF5 REBUILD
/
CREATE INDEX KSPRF6 ON KSEN_TYPETYPE_RELTN (TYPETYPE_RELTN_TYPE)
/
ALTER INDEX KSPRF6 REBUILD
/
CREATE INDEX KSPRF7 ON KSEN_LPR_ROSTER (CHECK_IN_FREQ_DUR_TYPE)
/
ALTER INDEX KSPRF7 REBUILD
/
CREATE INDEX KSPRF8 ON KSEN_LPR_ROSTER_ENTRY (LPR_ROSTER_ENTRY_TYPE)
/
ALTER INDEX KSPRF8 REBUILD
/
CREATE INDEX KSPRF9 ON KSEN_LPR_TRANS (LPR_TRANS_TYPE)
/
ALTER INDEX KSPRF9 REBUILD
/
CREATE INDEX KSPRF10 ON KSEN_LPR_TRANS_ITEM (LPR_TRANS_ITEM_TYPE)
/
ALTER INDEX KSPRF10 REBUILD
/
CREATE INDEX KSPRF11 ON KSEN_LRC_RESULT_SCALE (RESULT_SCALE_TYPE)
/
ALTER INDEX KSPRF11 REBUILD
/
CREATE INDEX KSPRF12 ON KSEN_LUI (LUI_TYPE)
/
ALTER INDEX KSPRF12 REBUILD
/
CREATE INDEX KSPRF13 ON KSEN_LUI_IDENT (LUI_ID_TYPE)
/
ALTER INDEX KSPRF13 REBUILD
/
CREATE INDEX KSPRF14 ON KSEN_LUI_LU_CD (LUI_LUCD_TYPE)
/
ALTER INDEX KSPRF14 REBUILD
/
CREATE INDEX KSPRF15 ON KSEN_PROCESS_CHECK (MSTONE_TYPE)
/
ALTER INDEX KSPRF15 REBUILD
/
CREATE INDEX KSPRF16 ON KSEN_APPT_SLOT (APPT_SLOT_TYPE)
/
ALTER INDEX KSPRF16 REBUILD
/
CREATE INDEX KSPRF17 ON KSEN_APPT_WINDOW (ASSIGNED_ORDER_TYPE)
/
ALTER INDEX KSPRF17 REBUILD
/
CREATE INDEX KSPRF18 ON KSEN_APPT_WINDOW (SR_DUR_TYPE)
/
ALTER INDEX KSPRF18 REBUILD
/
CREATE INDEX KSPRF19 ON KSEN_APPT_WINDOW (SR_START_INTVL_DUR_TYPE)
/
ALTER INDEX KSPRF19 REBUILD
/
CREATE INDEX KSPRF20 ON KSEN_CO_AO_CLUSTER (AO_CLUSTER_TYPE)
/
ALTER INDEX KSPRF20 REBUILD
/
CREATE INDEX KSPRF21 ON KSEN_CO_AO_CLUSTER_SET (ACTIVITY_OFFERING_TYPE)
/
ALTER INDEX KSPRF21 REBUILD
/
CREATE INDEX KSPRF22 ON KSEN_CO_SEAT_POOL_DEFN (EXPIR_MSTONE_TYPE)
/
ALTER INDEX KSPRF22 REBUILD
/
CREATE INDEX KSPRF23 ON KSEN_CO_SEAT_POOL_DEFN (SEAT_POOL_DEFN_TYPE)
/
ALTER INDEX KSPRF23 REBUILD
/
CREATE INDEX KSPRF24 ON KSEN_CWL (STD_DUR_TYPE)
/
ALTER INDEX KSPRF24 REBUILD
/
CREATE INDEX KSPRF25 ON KSEN_ENROLLMENT_FEE (CURRENCY_TYPE)
/
ALTER INDEX KSPRF25 REBUILD
/

--KSLU
CREATE INDEX KSPRF26 ON KSLU_CLU (CLU_INTSTY_TYPE)
/
ALTER INDEX KSPRF26 REBUILD
/
CREATE INDEX KSPRF27 ON KSLU_CLU_ADMIN_ORG (TYPE)
/
ALTER INDEX KSPRF27 REBUILD
/
CREATE INDEX KSPRF28 ON KSLU_CLU_FEE_AMOUNT (CURRENCY_TYPE)
/
ALTER INDEX KSPRF28 REBUILD
/
CREATE INDEX KSPRF29 ON KSLU_CLU_FEE_REC (FEE_TYPE)
/
ALTER INDEX KSPRF29 REBUILD
/
CREATE INDEX KSPRF30 ON KSLU_CLU_FEE_REC (RATE_TYPE)
/
ALTER INDEX KSPRF30 REBUILD
/
CREATE INDEX KSPRF31 ON KSLU_CLU_IDENT (TYPE)
/
ALTER INDEX KSPRF31 REBUILD
/
CREATE INDEX KSPRF32 ON KSLU_CLU_SET (TYPE)
/
ALTER INDEX KSPRF32 REBUILD
/
CREATE INDEX KSPRF33 ON KSLU_LU_CODE (TYPE)
/
ALTER INDEX KSPRF33 REBUILD
/