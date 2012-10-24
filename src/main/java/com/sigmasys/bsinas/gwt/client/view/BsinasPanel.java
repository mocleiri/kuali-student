package com.sigmasys.bsinas.gwt.client.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.sigmasys.bsinas.gwt.client.service.GenericCallback;
import com.sigmasys.bsinas.gwt.client.service.GwtErrorHandler;
import com.sigmasys.bsinas.gwt.client.service.ServiceFactory;
import com.sigmasys.bsinas.gwt.client.util.StringUtils;
import com.sigmasys.bsinas.gwt.client.view.widget.WidgetFactory;

/**
 * BSINAS Root Panel
 *
 * @author Michael Ivanov
 */
public class BsinasPanel extends LayoutContainer {

    private static final String DEFAULT_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<needAnalysisInput CreatedDate=\"2012-10-23\" AwardYear=\"2012\" xmlns=\"http://INAS.collegeboard.org/2012/Input/\">\n" +
            "</needAnalysisInput>\n";

    private TextArea requestTextArea;
    private TextArea responseTextArea;

    public BsinasPanel() {

        setLayout(new FitLayout());

        final LayoutContainer panel = new LayoutContainer(new TableLayout(3));

        requestTextArea = new TextArea();
        requestTextArea.setWidth(750);
        requestTextArea.setHeight(600);
        requestTextArea.setValue(DEFAULT_REQUEST);

        responseTextArea = new TextArea();
        responseTextArea.setWidth(750);
        responseTextArea.setHeight(600);

        LayoutContainer buttonPanel = new LayoutContainer(new TableLayout(1));


        Button submitButton = new Button("Submit");
        submitButton.setWidth(80);
        submitButton.setIconStyle("icon-check");
        submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent event) {
                try {
                    String requestXml = requestTextArea.getValue();
                    if (!StringUtils.isEmpty(requestXml)) {
                        responseTextArea.mask("Processing request...");
                        ServiceFactory.getBsinasService().runEngine(requestXml, new GenericCallback<String>() {
                            @Override
                            public void onSuccess(String responseXml) {
                                responseTextArea.setValue(responseXml);
                                responseTextArea.unmask();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                responseTextArea.unmask();
                                super.onFailure(t);
                            }
                        });
                    } else {
                        GwtErrorHandler.error("XML Request cannot be empty");
                    }
                } catch (Exception e) {
                    responseTextArea.unmask();
                    GwtErrorHandler.error(e);
                }
            }
        });

        Button resetButton = new Button("Reset");
        resetButton.setWidth(80);
        resetButton.setIconStyle("icon-undo");
        resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
            public void componentSelected(ButtonEvent event) {
                responseTextArea.unmask();
                requestTextArea.setValue(DEFAULT_REQUEST);
                responseTextArea.setValue("");
            }
        });

        TableData td = new TableData(Style.HorizontalAlignment.CENTER, Style.VerticalAlignment.TOP);
        td.setMargin(10);
        td.setPadding(10);

        buttonPanel.add(submitButton, td);
        buttonPanel.add(resetButton, td);

        panel.add(WidgetFactory.createText("XML Request:"), td);
        panel.add(WidgetFactory.createText(""), td);
        panel.add(WidgetFactory.createText("XML Response:"), td);

        panel.add(requestTextArea, td);
        panel.add(buttonPanel);
        panel.add(responseTextArea, td);

        add(panel);
    }

}
