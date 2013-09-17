package net.pandoragames.far.ui.model;

import java.util.EventListener;

/**
 * Allows to register with a {@link OperationForm OperationForm}
 * to get notified about form udpates. Changes of single properties
 * by means of ordinary getter calls are not counted as "form updates".
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface FormUpdateListener extends EventListener {

	/**
	 * Called when the form is updated with a new form.
	 * @param event holding the updated form as source
	 */
	public void formUpdated( FormUpdateEvent event );
}
