package net.pandoragames.far.ui.swing.component.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.pandoragames.far.ui.model.ReplaceForm;
import net.pandoragames.far.ui.swing.SwingConfig;

/**
 * Specialisation of the confirm listener for the backup button.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class ConfirmReplaceListener extends ConfirmActionListener {

	private ReplaceForm replaceForm;
	
	/**
	 * Constructor needs a reference to the actual FindForm instance.
	 * @param caller "parent" component
	 * @param config used for translations
	 * @param form reference to the used FindForm
	 */
	public ConfirmReplaceListener(Component caller, SwingConfig config, ReplaceForm form) {
		super(caller, 
				config.getLocalizer().localize("label.confirm-replacement"), 
				config.getLocalizer().localize("question.confirm-replacement"));
		replaceForm = form;
	}
	/**
	 * Forwards the event to registered listeners, but only if 
	 * backup is enabled, or if the user confirms.
	 * @param e event to be forwarded
	 */
	public void actionPerformed(ActionEvent e) {
		if( replaceForm.isDoBackup() ) {
			fireEvent( e );
		} else {
			super.actionPerformed(e);
		}
	}

}
