package net.pandoragames.far.ui.model;

import java.util.EventObject;

/**
 * Event object for {@link FormUpdateListener FormUpdateListener}.
 * The updated form will be returned as "event source".
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public class FormUpdateEvent extends EventObject {

	private OperationType formType;
	/**
	 * Constructor takes the form that has been updated.
	 * It will be returned as "event source".
	 * @param form
	 */
	public FormUpdateEvent(OperationForm form) {
		super(form);
		formType = form.getType();
	}
	
	/**
	 * Returns the form type. Shortcut for
	 * <code>((OperationForm) <b>this</b>.getSource()).getType()</code>
	 * @return form type
	 */
	public OperationType getType() {
		return formType;
	}
}
