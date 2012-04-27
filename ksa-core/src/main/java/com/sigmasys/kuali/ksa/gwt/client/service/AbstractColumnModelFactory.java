package com.sigmasys.kuali.ksa.gwt.client.service;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.sigmasys.kuali.ksa.gwt.client.view.ListPanel;

import java.util.List;

/**
 * AbstractColumnModelFactory
 *
 * @author Michael Ivanov
 */
public abstract class AbstractColumnModelFactory<M extends BaseModel> implements ColumnModelFactory<M> {
    
    public static final String LABEL_GENERATION_DATE = "Generation Date";
    public static final String LABEL_GENERATION_DATE_SHORT = "Gen. Date";
    public static final String LABEL_ID = "ID";
    public static final String LABEL_START_DATE = "Start Date";
    public static final String LABEL_END_DATE = "End Date";
    public static final String LABEL_FIRM_ID = "Firm ID";
    public static final String LABEL_TYPE = "Type";
    public static final String LABEL_SUBTYPE = "Subtype";
    public static final String LABEL_ISSUE = "Issue Sym";
    public static final String LABEL_LABEL = "Label";
    public static final String LABEL_MARKET_CLASS = "Mkt Class";
    public static final String LABEL_MARKET_CENTER = "Mkt Center";
    public static final String LABEL_START_TIME = "Start Time";
    public static final String LABEL_END_TIME = "End Time";
    public static final String LABEL_DISPOSITION = "Disposition";
    public static final String LABEL_COMMENT = "Comment";
    public static final String LABEL_PATTERN_NAME = "Pattern Name";
    public static final String LABEL_VERSION = "Version";
    public static final String LABEL_SURV_PERIOD = "Surv. Period";
    public static final String LABEL_DURATION = "Duration";
    public static final String LABEL_ASSIGNEE = "Assignee";

    // To make it list panel aware - subclasses may want to override it if necessary
    public void setListPanel(ListPanel<M> listPanel) {
        // Do nothing here
    }

    // To process grid pre loading
    public void processPostLoading(LoadEvent be) {
        // Do nothing here
    }

    public ColumnModel getColumnModel() {
        return new ColumnModel(getColumnConfigs());
    }

    public abstract List<ColumnConfig> getColumnConfigs();

}

