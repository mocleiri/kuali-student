package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.sigmasys.kuali.ksa.gwt.client.model.AccountModel;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;
import com.sigmasys.kuali.ksa.gwt.client.model.StringModelData;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.DateRangeField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.EntityNameField;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.ListViewAdapter;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.WidgetFactory;
import com.sigmasys.kuali.ksa.gwt.client.view.widget.value.EntityRefName;


import java.util.ArrayList;
import java.util.List;

public class AccountSearchPanel extends AbstractSearchPanel<AccountModel> {

	private static final int ELEMENT_WIDTH = 150;
    private static final String COLON = ":";

    private NumberField xcptnId;
    private EntityNameField firmId;
    private EntityNameField issue;
    private ListViewAdapter<StringModelData> label;
    private ListViewAdapter<StringModelData> marketClass;
    //Optional
    private ComboBox<StringModelData> assignee;
    private ListViewAdapter<StringModelData> type;
    private ListViewAdapter<StringModelData> disposition;
    //Optional
    private ListViewAdapter<StringModelData> marketCenter;
    private DateRangeField startDate;
    private DateRangeField endDate;


    @Override
	protected void fillSearchElementsContainer(LayoutContainer cont) {

    	addSearchFields(cont);
		
        // TODO
    }
    

    protected void addSearchFields (LayoutContainer cont){
        
        // TODO:

        //addFieldBinding(startDate, ExceptionColumnConstants.COLUMN_START_DATE);
    }
    
    @Override
	protected Component[] getEnterKeyDownEventAwareComponents() {
		return new Component[] {};
	}

}
