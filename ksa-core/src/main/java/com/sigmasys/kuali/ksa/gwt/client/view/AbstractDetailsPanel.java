package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;

/**
 * AbstractDetailsPanel
 *
 * @param <M>
 */
public abstract class AbstractDetailsPanel<M extends BaseModel> extends ContentPanel {

    private AbstractListPanel<M> listPanel;


    public AbstractDetailsPanel() {
        setHeading("Details");
    }

    abstract public void updateView(M model);

    public AbstractListPanel<M> getListPanel() {
        return listPanel;
    }

    public void setListPanel(AbstractListPanel<M> listPanel) {
        this.listPanel = listPanel;
    }

    public void updateViewAndRefresh(M item) {
        updateView(item);
        refreshView();
    }

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
