package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;

public abstract class DetailsPanel<M extends BaseModel> extends ContentPanel {

    private ListPanel<M> listPanel;
    

    public DetailsPanel() {
        setHeading("Details");
    }

    public ListPanel<M> getListPanel() {
        return listPanel;
    }

    public void setListPanel(ListPanel<M> listPanel) {
        this.listPanel = listPanel;
    }

    public void updateViewAndRefresh(M item) {
        updateView(item);
        refreshView();
    }

    abstract public void updateView(M model);

    protected void refreshView() {
        setItemsVisible(true);
        layout();
    }

    public void setItemsVisible(boolean val) {
        for (Component c : getItems()) {
            c.setVisible(val);
        }
    }

    protected void addTo(LayoutContainer container, String label, Text value, boolean isRightCellNoWrap) {
        AbstractDetailsContainer.addTo(container, label, value, isRightCellNoWrap);
    }

    protected void addTo(LayoutContainer container, String label, Text value) {
        AbstractDetailsContainer.addTo(container, label, value);
    }

}
