package com.sigmasys.kuali.ksa.gwt.client.model;

import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * User: dmulderink
 * Date: 5/11/12
 * Time: 1:20 PM
 */
public class TransactionModel extends AbstractModel {

   // ---------------------------------------------------------------
   // TRANSACTION TYPES
   // ---------------------------------------------------------------

   public static final List<String> transactionTypes = Arrays.asList("All", "Charge", "Deferment", "Payment");

   private static String selectedTransactionType;

   // The search criteria keys
   public static final String TYPE = "transaction.type";
   public static final String ID = "transaction.id";
   public static final String ALLOCATED = "transaction.allocated";
   public static final String AMNT = "transaction.amnt";
   public static final String EFFECTIVE_DATE = "transaction.effectiveDate";
   public static final String EXTN_ID = "transaction.extnId";
   public static final String GL_ENTRY_GENERATED = "transaction.glEntryGenerated";
   public static final String IS_INTERNAL_TRN = "transaction.isInternalTrn";
   public static final String LEDGER_DATE = "transaction.ledgerDate";
   public static final String LOCKED_ALLOCATED = "transaction.lockedAllocated";
   public static final String NATIVE_AMNT = "transaction.nativeAmnt";
   public static final String ORIG_DATE = "transaction.origDate";
   public static final String REFUND_RULE = "transaction.refundRule";
   public static final String RESP_ENTITY = "transaction.respEntity";
   public static final String STATEMENT_TXT = "transaction.statementText";
   public static final String IS_REFUNDABLE = "transaction.isRefundable";
   public static final String DEFER_ID = "transaction.deferId";
   public static final String EXPIRATION_DATE = "transaction.expirationId";
   public static final String CLEAR_DATE = "transaction.clearDate";
   public static final String IS_DEFERRED = "transaction.isDeferred";
   public static final String IS_GL_OVERRIDDEN = "transaction.isGLOverridden";
   public static final String TRANSACTION_TYPE_ID_FK = "transaction.transaction_type_id_fk";
   public static final String TRANSACTION_TYPE_SUB_CODE_FK = "transaction.transaction_type_sub_code_fk";
   public static final String ROLLUP_ID_FK = "transaction.rollup_id_fk";
   public static final String CURRENCY_ID_FK = "transaction.currency_id_fk";

   // The real column names
   public static final String COLUMN_TYPE = "trans_type";
   public static final String COLUMN_ID = "trans_id";
   public static final String COLUMN_ALLOCATED = "allocated";
   public static final String COLUMN_AMNT = "amnt";
   public static final String COLUMN_EFFECTIVE_DATE = "effective_date";
   public static final String COLUMN_EXTN_ID = "extn_Id";
   public static final String COLUMN_GL_ENTRY_GENERATED = "gl_entry_generated";
   public static final String COLUMN_IS_INTERNAL_TRN = "is_internal_trn";
   public static final String COLUMN_LEDGER_DATE = "ledger_date";
   public static final String COLUMN_LOCKED_ALLOCATED = "locked_allocated";
   public static final String COLUMN_NATIVE_AMNT = "native_amnt";
   public static final String COLUMN_ORIG_DATE = "orig_date";
   public static final String COLUMN_REFUND_RULE = "refund_rle";
   public static final String COLUMN_RESP_ENTITY = "resp_entity";
   public static final String COLUMN_STATEMENT_TXT = "statement_text";
   public static final String COLUMN_IS_REFUNDABLE = "is_refundable";
   public static final String COLUMN_DEFER_ID = "defer_id";
   public static final String COLUMN_EXPIRATION_DATE = "expiration_id";
   public static final String COLUMN_CLEAR_DATE = "clear_date";
   public static final String COLUMN_IS_DEFERRED = "is_deferred";
   public static final String COLUMN_IS_GL_OVERRIDDEN = "is_gl_overridden";
   public static final String COLUMN_TRANSACTION_TYPE_ID_FK = "transaction_type_id_fk";
   public static final String COLUMN_TRANSACTION_TYPE_SUB_CODE_FK = "transaction_type_sub_code_fk";
   public static final String COLUMN_ROLLUP_ID_FK = "rollup_id_fk";
   public static final String COLUMN_CURRENCY_ID_FK = "currency_id_fk";

   public static String getSelectedTransactionType() {
      return selectedTransactionType;
   }

   public static void setSelectedTransactionType(String selectedTransactionType) {
      TransactionModel.selectedTransactionType = selectedTransactionType;
   }


   public String getId() {
      return get(COLUMN_ID);
   }

   public void setId(String id) {
      set(COLUMN_ID, id);
   }
   
   public String getType() {
      return get(COLUMN_TYPE);
   }

   public void setType(String type) {
      set(COLUMN_TYPE, type);
   }

   public Double getAllocated() {
      return get(COLUMN_ALLOCATED);
   }

   public void setAllocated(Double allocated) {
      set(COLUMN_ALLOCATED, allocated);
   }

   public Double getAmnt() {
      return get(COLUMN_AMNT);
   }

   public void setAmnt(Double amnt) {
      set(COLUMN_AMNT, amnt);
   }
   
   public Date getEffectiveDate() {
      return get(COLUMN_EFFECTIVE_DATE);
   }

   public void setEffectiveDate(Date effectiveDate) {
      set(COLUMN_EFFECTIVE_DATE, effectiveDate);
   }
   
   public String getExtnId() {
      return get(COLUMN_EXTN_ID);
   }

   public void setExtnId(String extnId) {
      set(COLUMN_EXTN_ID, extnId);
   }
   
   public String getGlEntryGenerated() {
      return get(COLUMN_GL_ENTRY_GENERATED);
   }

   public void setGlEntryGenerated(String glEntryGenerated) {
      set(COLUMN_GL_ENTRY_GENERATED, glEntryGenerated);
   }
   
   public Boolean getIsInternalTrn() {
      return get(COLUMN_IS_INTERNAL_TRN);
   }

   public void setIsInternalTrn(Boolean isInternalTrn) {
      set(COLUMN_IS_INTERNAL_TRN, isInternalTrn);
   }
   
   public Date getLedgerDate() {
      return get(COLUMN_LEDGER_DATE);
   }

   public void setLedgerDate(Date ledgerDate) {
      set(COLUMN_LEDGER_DATE, ledgerDate);
   }
   
   public String getLockedAllocated() {
      return get(COLUMN_LOCKED_ALLOCATED);
   }

   public void setLockedAllocated(String lockedAllocated) {
      set(COLUMN_LOCKED_ALLOCATED, lockedAllocated);
   }
   
   public Double getNativeAmnt() {
      return get(COLUMN_NATIVE_AMNT);
   }

   public void setNativeAmnt(Double nativeAmnt) {
      set(COLUMN_NATIVE_AMNT, nativeAmnt);
   }
   
   public Date getOrigDate() {
      return get(COLUMN_ORIG_DATE);
   }

   public void setOrigDate(Date origDate) {
     set(COLUMN_ORIG_DATE, origDate);
   }
   
   public String getRefundRule() {
      return get(COLUMN_REFUND_RULE);
   }

   public void setRefundRule(String refundRule) {
      set(COLUMN_REFUND_RULE, refundRule);
   }
   
   public String getRespEntity() {
      return get(COLUMN_RESP_ENTITY);
   }

   public void setRespEntity(String entityId) {
      set(COLUMN_RESP_ENTITY, entityId);
   }
   
   public String getStatementTxt() {
      return get(COLUMN_STATEMENT_TXT);
   }

   public void setStatementTxt(String statementText) {
      set(COLUMN_STATEMENT_TXT, statementText);
   }
   
   public Boolean getIsRefundable() {
      return get(COLUMN_IS_REFUNDABLE);
   }

   public void setIsRefundable(Boolean isRefundable) {
      set(COLUMN_IS_REFUNDABLE, isRefundable);
   }
   
   public String getDeferId() {
      return get(COLUMN_DEFER_ID);
   }

   public void setDeferId(String deferId) {
      set(COLUMN_DEFER_ID, deferId);
   }
   
   public Date getExpirationDate() {
      return get(COLUMN_EXPIRATION_DATE);
   }

   public void setExpirationDate(Date expirationDate) {
      set(COLUMN_EXPIRATION_DATE, expirationDate);
   }
   
   public Date getClearDate() {
      return get(COLUMN_CLEAR_DATE);
   }

   public void setClearDate(Date clearDate) {
      set(COLUMN_CLEAR_DATE, clearDate);
   }

   public Boolean getIsDeferred() {
      return get(COLUMN_IS_DEFERRED);
   }

   public void setIsDeferred(Boolean isDeferred) {
      set(COLUMN_IS_DEFERRED, isDeferred);
   }
   
   public Boolean getIsGlOverridden() {
      return get(COLUMN_IS_GL_OVERRIDDEN);
   }

   public void setIsGlOverridden(Boolean isGlOveridden) {
      set(COLUMN_IS_GL_OVERRIDDEN, isGlOveridden);
   }

   public String getTransactionTypeIdFk() {
      return get(COLUMN_TRANSACTION_TYPE_ID_FK);
   }

   public void setTransactionTypeIdFk(String TransactionTypeIdFk) {
      set(COLUMN_TRANSACTION_TYPE_ID_FK, TransactionTypeIdFk);
   }

   public String getTransactionTypeSubCodeFk() {
      return get(COLUMN_TRANSACTION_TYPE_SUB_CODE_FK);
   }

   public void setTransactionTypeSubCodeFk(String TransactionTypeSubCodeFk) {
      set(COLUMN_TRANSACTION_TYPE_SUB_CODE_FK, TransactionTypeSubCodeFk);
   }

   public String getRollupIdFk() {
      return get(COLUMN_ROLLUP_ID_FK);
   }

   public void setRollupIdFk(String RollupIdFk) {
      set(COLUMN_ROLLUP_ID_FK, RollupIdFk);
   }

   public String getCurrencyIdFk() {
      return get(COLUMN_CURRENCY_ID_FK);
   }

   public void setCurrencyIdFk(String CurrencyIdFk) {
      set(COLUMN_CURRENCY_ID_FK, CurrencyIdFk);
   }
 }
