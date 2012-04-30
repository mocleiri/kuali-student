package com.sigmasys.kuali.ksa.gwt.client.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.*;

/**
 * KSA Root Panel
 *
 * @author Michael Ivanov
 */
public class KsaPanel extends LayoutContainer {


    public KsaPanel() {

        setLayout(new FitLayout());

        TabPanel tabPanel = new TabPanel();

        TabItem tabItem = new TabItem("Accounts");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        tabItem.add(new AccountCompositePanel());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Charges");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Payments");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Deferments");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Alerts");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Memos");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);

        tabItem = new TabItem("Flags");
        tabItem.setScrollMode(Style.Scroll.AUTO);
        // TODO
        tabItem.add(new LayoutContainer());
        tabPanel.add(tabItem);


        add(tabPanel);
    }

}
