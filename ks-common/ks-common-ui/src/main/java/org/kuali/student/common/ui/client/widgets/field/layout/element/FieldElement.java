/**
 * Copyright 2010 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.common.ui.client.widgets.field.layout.element;

import org.kuali.student.common.ui.client.application.Application;
import org.kuali.student.common.ui.client.configurable.mvc.sections.ValidationMessagePanel;
import org.kuali.student.common.ui.client.widgets.HasInputWidget;
import org.kuali.student.common.ui.client.widgets.HasWatermark;
import org.kuali.student.common.ui.client.widgets.KSLabel;
import org.kuali.student.common.ui.client.widgets.KSTitleDescPanel;
import org.kuali.student.common.ui.client.widgets.field.layout.element.AbbrButton.AbbrButtonType;
import org.kuali.student.common.ui.client.widgets.field.layout.layouts.FieldLayoutComponent;
import org.kuali.student.core.validation.dto.ValidationResultInfo;
import org.kuali.student.core.validation.dto.ValidationResultInfo.ErrorLevel;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A ui field element.  Areas which contain a field for user data entry should use
 * this class for visual and behavior consistency.  Used by FieldDescriptor for its layout.
 * 
 * General layout of a field element, all elements are conditional based on the field's
 * configuration through set methods on this class: <br>
 * <b>[label][requiredness indicator][help]<br>
 * [input widget]<br>
 * [constraint text]<br></b>
 * <br>
 * 
 * The messageKeyInfo passed in is used to generate the messages needed for a field these include:<br>
 * Field Label<br>
 * Help text<br>
 * Instructions<br>
 * Constraint text<br>
 * Watermark text - only if the widget one that accepts text input<br><br>
 * These are generated by the single key, the additional text is determined by special suffixes on keys within
 * the messages data - example - you pass in "sampleField" for the message key - it is automatically determined
 * that if there is a message in the messages data named "sampleField-instruct", instructions will be added to the field
 * in the appropriate location.<br>
 * List of the appended keys for use in messages data:<br>
 * "-help" for help text<br>
 * "-instruct" for instructions<br>
 * "-constraints" for constraint text<br>
 * "-watermark" for watermark text<br>
 * 
 * @author Kuali Student Team
 * @see FieldDescriptor
 * @see FieldLayout
 */
public class FieldElement extends Composite implements FieldLayoutComponent{

	//Layout
	private KSTitleDescPanel titlePanel = new KSTitleDescPanel();
	private FlowPanel layout = new FlowPanel();
	private FieldTitle fieldTitle;
	private SpanPanel instructions = new SpanPanel();
	private SpanPanel constraints = new SpanPanel();
	private AbbrPanel required = new AbbrPanel("Required", "ks-form-module-elements-required", " * ");
	private AbbrButton help = new AbbrButton(AbbrButtonType.HELP);
	private Widget fieldWidget;
	private SpanPanel widgetSpan = new SpanPanel();
	private String fieldHTMLId;
	private LineNum margin;
	private String watermarkText = null;
	public static enum LineNum{SINGLE, DOUBLE, TRIPLE}

	private ValidationMessagePanel validationPanel;
	private String fieldKey;
	private boolean labelShown = true;
	
	private Panel parentPanel;
	private Element parentElement;

	/**
	 * Sets this field's validation panel, note that this does not add it to the ui.  Attaching must be done
	 * elsewhere.  Most FieldLayout implementations handle this attachment.
	 * @param validationPanel
	 */
	public void setValidationPanel(ValidationMessagePanel validationPanel) {
		this.validationPanel = validationPanel;
	}


	public Panel getParentPanel() {
		return parentPanel;
	}

	public void setParentPanel(Panel parentPanel) {
		this.parentPanel = parentPanel;
		this.parentElement = parentPanel.getElement();
	}

	public void setParentElement(Element element){
		parentElement = element;
	}

	private String fieldName;
	private String instructionText;

    public String getInstructionText() {
		return instructionText;
	}

	public FieldElement(String title, Widget widget) {
    	generateLayout(title, title, null, null, null, widget);

    }

    public FieldElement(String key, String title, Widget widget){
    	generateLayout(key, title, null, null, null, widget);
    }

    /**
     * Standard constructor for FieldElement
     * @param key unique key to identify this field
     * @param info key for the messages used in this field element - see class description
     */
    public FieldElement(String key, MessageKeyInfo info){
    	init(key, info, null);
    }

    /**
     * Standard constructor for FieldElement
     * @param key unique key to identify this field
     * @param info key for the messages used in this field element - see class description
     * @param widget widget used for input/display on this field
     */
    public FieldElement(String key, MessageKeyInfo info, Widget widget){
    	init(key, info, widget);

    }
    
    private void init(String key, MessageKeyInfo info, Widget widget){
    	String title = Application.getApplicationContext().getUILabel(info.getGroup(), info.getType(), info.getState(), info.getId());
    	
    	String help = Application.getApplicationContext().getUILabel(info.getGroup(), info.getType(), info.getState(),
    			info.getId() + HELP_MESSAGE_KEY);
    	if(help.equals(info.getId() + HELP_MESSAGE_KEY)){
    		help = null;
    	}
    	
    	String instructions = Application.getApplicationContext().getUILabel(info.getGroup(), info.getType(), info.getState(),
    			info.getId() + INSTRUCT_MESSAGE_KEY);
    	if(instructions.equals(info.getId() + INSTRUCT_MESSAGE_KEY)){
    		instructions = null;
    	}
    	
    	String constraints = Application.getApplicationContext().getUILabel(info.getGroup(), info.getType(), info.getState(),
    			info.getId() + CONSTRAINT_MESSAGE_KEY);
    	if(constraints.equals(info.getId() + CONSTRAINT_MESSAGE_KEY)){
    		constraints = null;
    	}
    	
    	watermarkText = Application.getApplicationContext().getUILabel(info.getGroup(), info.getType(), info.getState(),
    			info.getId() + WATERMARK_MESSAGE_KEY);
    	if(watermarkText.equals(info.getId() + WATERMARK_MESSAGE_KEY)){
    		watermarkText = null;
    	}
    	
    	generateLayout(key, title, help, instructions, constraints, widget);
    }

    private void generateLayout(String key, String title, String helpText, String instructText, String constraintText, Widget widget){
    	this.setKey(key);
		fieldName = title;

		fieldHTMLId = HTMLPanel.createUniqueId();
		fieldTitle = new LabelPanel(title, fieldHTMLId);

		required.setVisible(false);
		fieldTitle.add(required);
		if(helpText != null){
			this.setHelp(helpText);
		}
		else{
			help.setVisible(false);
		}

		fieldTitle.add(help);
		layout.add(fieldTitle);
		if(instructText != null){
			this.setInstructions(instructText);
		}
		else{
			instructions.setVisible(false);
		}
		
		if(constraintText != null){
			this.setConstraintText(constraintText);
		}
		else{
			constraints.setVisible(false);
		}
		instructions.setStyleName("ks-form-module-elements-instruction");
		layout.add(instructions);
		layout.add(widgetSpan);
		if(widget != null){
			this.setWidget(widget);
		}
		constraints.setStyleName("ks-form-module-elements-help-text");
		layout.add(constraints);


        initWidget(layout);
        layout.addStyleName("ks-form-module-elements");
        layout.addStyleName("ks-form-module-single-line-margin");
    }

    /**
     * Sets the widget for this field, automatically adds an id which will match this field's label-for tag in
     * its label
     * @see com.google.gwt.user.client.ui.Composite#setWidget(com.google.gwt.user.client.ui.Widget)
     */
    public void setWidget(Widget w){
    	if(fieldWidget != null){
    		widgetSpan.remove(fieldWidget);
    	}
    	fieldWidget = w;
    	//TODO Do a check here to change the type of label based on widget type eventually
    	if(fieldWidget != null){
    		
    		//Checks for input widgets that may be incased in a more complex widget layout
    		if(fieldWidget instanceof HasInputWidget){
    			Widget input = ((HasInputWidget)fieldWidget).getInputWidget();
    			if(input != null){
	    			if(input instanceof HasWatermark && watermarkText != null){
	    				((HasWatermark)input).setWatermarkText(watermarkText);
	    			}
	    			input.getElement().setAttribute("id", fieldHTMLId);
    			}
    			else{
    				fieldWidget.getElement().setAttribute("id", fieldHTMLId);
    			}
    		}
    		else{
        		if(fieldWidget instanceof HasWatermark && watermarkText != null){
        			((HasWatermark)fieldWidget).setWatermarkText(watermarkText);
        		}
    			fieldWidget.getElement().setAttribute("id", fieldHTMLId);
    		}
    		
    		widgetSpan.add(fieldWidget);
    	}
    }
    
    /**
     * Sets the required string key to be used for when a field is required.  For example, in most cases this
     * key maps to a "*"
     * 
     * @param requiredKey
     */
    public void setRequiredString(String requiredKey){
    	String requiredText = Application.getApplicationContext().getMessage(requiredKey);
    	required.setText(requiredText);
    	required.setVisible(true);
    }
    
    public void setRequiredString(String requiredKey, String style){
    	String requiredText = Application.getApplicationContext().getMessage(requiredKey);
    	required.setText(requiredText);
    	required.setStyleName(style);
    	required.setVisible(true);
    }

    public void clearRequiredText(){
        required.setText("");
    }

    public Widget getFieldWidget(){
    	return fieldWidget;
    }

    public FlowPanel getFieldDetailsLayout(){
    	FlowPanel div = new FlowPanel();
		div.add(fieldTitle);
		div.add(instructions);
		div.addStyleName("ks-form-module-elements");
		return div;
    }

    public FlowPanel getFieldWidgetAreaLayout(){
    	FlowPanel div = new FlowPanel();
    	div.add(fieldWidget);
    	div.add(constraints);
    	div.addStyleName("ks-form-module-elements");
    	return div;
    }
    
    public boolean isRequired() {
        return required.isVisible();
    }

    public void setRequired(boolean isRequired){
    	required.setVisible(isRequired);
    }

    public void setInstructions(String text){
    	instructionText = text;
    	if(instructionText != null && !instructionText.trim().equals("")){
    		instructions.setHTML(text);
    		instructions.setVisible(true);
    	}
    }

    public void setConstraintText(String text){
    	if(text != null && !text.trim().equals("")){
    		constraints.setHTML(text);
    		constraints.setVisible(true);
    	}
    }

    public void setHelp(final String html){
    	if(html != null && !html.trim().equals("")){
    		help.setVisible(true);
    		help.setHoverHTML(html);
    		/*help.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					//TODO show actual help window
					Window.alert(html);

				}
			});*/
    	}
    	else{
    		help.setVisible(false);
    	}
    }

	public Widget getTitleWidget() {
	    return titlePanel.getTitleWidget();
	}

	public ValidationMessagePanel getValidationPanel() {
		return validationPanel;
	}

	public Panel getEncapsulatingPanel() {
		return parentPanel;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setErrorState(boolean error){
		if(error){
			fieldTitle.addStyleName("invalid");
			if(parentPanel != null){
				parentPanel.addStyleName("error");
			}
			else if(parentElement != null){
				parentElement.setClassName("error");
			}

		}
		else{
			fieldTitle.removeStyleName("invalid");
			if(parentPanel != null){
				parentPanel.removeStyleName("error");
			}
			else if(parentElement != null){
				parentElement.setClassName("");
			}
		}

	}

	/**
	 * Processes a validation result and adds an appropriate message, if needed
	 * @param vr
	 * @return
	 */
	public ErrorLevel processValidationResult(ValidationResultInfo vr) {
		ErrorLevel status = ErrorLevel.OK;

		if(vr.getLevel() == ErrorLevel.ERROR){
			String message = Application.getApplicationContext().getUILabel("validation", vr.getMessage());
			this.addValidationErrorMessage(message);
			
			if(status.getLevel() < ErrorLevel.ERROR.getLevel()){
				status = vr.getLevel();
			}
		}
		else if(vr.getLevel() == ErrorLevel.WARN){
			if(status.getLevel() < ErrorLevel.WARN.getLevel()){
				status = vr.getLevel();
			}
			//TODO does nothing on warn, warn is not currently used
		}
		else{
			//TODO does nothing on ok, ok is not currently used
		}
		return status;
	}

	/**
	 * Add a validation message to this fields validation panel as defined by setValidationPanel.
	 * @param text
	 */
	public void addValidationErrorMessage(String text){
		if(validationPanel != null){
			KSLabel message;
			if(fieldName != null && !fieldName.trim().equals("")){
				message = new KSLabel(fieldName + " - " + text);
			}
			else{
				message = new KSLabel(text);
			}
			message.setStyleName("ks-form-validation-label");
			this.setErrorState(true);
			this.validationPanel.addMessage(message);
		}
	}

	public void clearValidationPanel(){
		this.setErrorState(false);
		if(validationPanel != null){
			this.validationPanel.clear();
		}
	}

	@Override
	public String getKey() {
		return fieldKey;
	}

	@Override
	public void setKey(String key) {
    	if(key == null){
			//TODO better way to generate unique id here?
			key = HTMLPanel.createUniqueId();
		}
		this.fieldKey = key;
	}

	/**
	 * Sets the expected number of lines needed to space the vertical height of this field consistently
	 * with fields next to it horizontally
	 * @param margin
	 */
	public void setTitleDescLineHeight(LineNum margin) {
		layout.removeStyleName("ks-form-module-single-line-margin");
		switch(margin){
			case TRIPLE:
				if(firstLineExists() && secondLineExists()){
					layout.addStyleName("ks-form-module-single-line-margin");
				}
				else if((firstLineExists() || secondLineExists())){
					layout.addStyleName("ks-form-module-double-line-margin");
				}
				else{
					layout.addStyleName("ks-form-module-triple-line-margin");
				}

				break;
			case DOUBLE:
				if((firstLineExists() || secondLineExists())){
					layout.addStyleName("ks-form-module-single-line-margin");
				}
				else{
					layout.addStyleName("ks-form-module-double-line-margin");
				}
				break;
			case SINGLE:
				layout.addStyleName("ks-form-module-single-line-margin");
				break;
		}

	}

	private boolean firstLineExists(){
		boolean exists = false;
		if((fieldName != null && !fieldName.equals("")) || required.isVisible() || help.isVisible()){
			exists = true;
		}
		return exists;
	}

	private boolean secondLineExists(){
		boolean exists = false;
		if(instructions.isVisible()){
			exists = true;
		}
		return exists;
	}

	/**
	 * Hides the label of this field visually, but screen readers can still read it for accessibility
	 */
	public void hideLabel() {
		layout.removeStyleName("ks-form-module-double-line-margin");
		layout.removeStyleName("ks-form-module-triple-line-margin");
		layout.addStyleName("ks-form-module-single-line-margin");
		fieldTitle.setStyleName("accessibility-hidden");
		instructions.setVisible(false);
		labelShown = false;
	}
	
	public boolean isLabelShown(){
		return labelShown;
	}
}
