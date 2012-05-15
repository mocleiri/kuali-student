package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.google.gwt.user.client.Element;

/**
 * KSA Root Panel
 *
 * @author Michael Ivanov
 */
public class KsaPanel extends TabPanel {

    public KsaPanel() {
        Registry.register(KsaPanel.class.getName(), this);
    }

    private void init() {

        TabItem tabItem = new TabItem("Accounts");
        tabItem.setIconStyle("icon-user");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        tabItem.add(new AccountCompositePanel());
        add(tabItem);

        tabItem = new TabItem("Charges");
        tabItem.setIconStyle("icon-card");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        tabItem.add(new TransactionCompositePanel());
        add(tabItem);

        tabItem = new TabItem("Payments");
        tabItem.setIconStyle("icon-coins");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        add(tabItem);

        tabItem = new TabItem("Deferments");
        tabItem.setIconStyle("icon-calculator");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        add(tabItem);

        tabItem = new TabItem("Alerts");
        tabItem.setIconStyle("icon-alert");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        add(tabItem);

        tabItem = new TabItem("Memos");
        tabItem.setIconStyle("icon-memo");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        add(tabItem);

        tabItem = new TabItem("Flags");
        tabItem.setIconStyle("icon-flag");
        tabItem.setLayout(new FitLayout());
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        add(tabItem);

    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        init();
    }

}
