package com.sigmasys.bsinas.model;

/**
 * KSA commonly used constants.
 *
 * @author Michael Ivanov
 */
public interface Constants {

    // Generic constants
    String MODULE_NAME = "KSA";

    // Persistence units
    String KSA_PERSISTENCE_UNIT = "ksa";
    String RICE_PERSISTENCE_UNIT = "rice";

    // KSA parameter names
    String DEFAULT_WRITE_OFF_ROLLUP_PARAM_NAME = "ksa.writeoff.rollup";
    String DEFAULT_MEMO_LEVEL_PARAM_NAME = "ksa.memo.level";
    String LOCALE_LANG_PARAM_NAME = "ksa.locale.lang";
    String LOCALE_COUNTRY_PARAM_NAME = "ksa.locale.country";
    String IMPORT_SINGLE_BATCH_FAILURE_PARAM_NAME = "ksa.import.single.batch.failure";
    String DEFAULT_GL_TYPE_PARAM_NAME = "ksa.general.ledger.type";
    String DEFAULT_GL_MODE_PARAM_NAME = "ksa.general.ledger.mode";
    String DEFAULT_DEFERMENT_TYPE_PARAM_NAME = "ksa.deferment.type.id";
    String LOGGING_OPERATION = "ksa.logging.operation";

    // Drools parameters
    String DROOLS_CLASSPATH = "drools";
    String DROOLS_DSL_ID_PARAM_NAME = "ksa.drools.dsl";
    String DROOLS_PERSISTENCE_PARAM_NAME = "ksa.drools.persistence";
    String DROOLS_FA_RULE_SET_PARAM_NAME = "ksa.drools.rule_set.fee_assessment";

    // Quick View parameters
    String QUICKVIEW_INFORMATION_COUNT = "ksa.quickview.information.count";

    // WS constants
    String WS_NAMESPACE = "http://sigmasys.com/";

    // Rice constants
    String APPLICATION_HOST_PARAM_NAME = "application.host";

    // ---------------------------------------------------------------
    // DATE FORMATS
    // ---------------------------------------------------------------
    String DATE_FORMAT_US = "MM/dd/yyyy"; // should actually be locale-based

    String TIMESTAMP_FORMAT = "MM/dd/yyyy HH:mm:ss.SSS";
    String TIMESTAMP_FORMAT_NO_MS = "MM/dd/yyyy HH:mm:ss";
    String DB_TIMESTAMP_FORMAT_NO_MS = "mm/dd/yyyy hh24:mi:ss";

    String TIME_FORMAT = "HH:mm:ss.SSS";
    String TIME_FORMAT_NO_MS = "HH:mm:ss";
    String TIME_FORMAT_MINUTES = "HH:mm";

    // URL mapping constants
    String CONFIG_SERVICE_URL = "config.service";
    String ACCOUNT_SERVICE_URL = "account.service";
    String TRANSACTION_SERVICE_URL = "transaction.service";
    String CURRENCY_SERVICE_URL = "currency.service";




}
