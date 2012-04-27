package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.sigmasys.kuali.ksa.gwt.client.model.SearchCriteria;

public interface SearchPanel<M extends BaseModel> {

    void collapse();

    void expand();

    void setViewer(CompositePanel<M> viewer);

    void setListPanel(ListPanel<M> listPanel);

    void setDetailsPanel(DetailsPanel<M> detailsPanel);

    void submitForm();

    boolean isTopPanel();

    void updateFormWithSearchCriteria(SearchCriteria searchCriteria);

    SearchCriteria getSearchCriteria();
}
