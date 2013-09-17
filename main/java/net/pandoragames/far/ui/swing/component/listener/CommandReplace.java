package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ActionEvent;

import net.pandoragames.far.ui.UIFace;
import net.pandoragames.far.ui.model.ReplaceForm;

/**
 * The replace command.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class CommandReplace extends CommandExecutor {
	/**
	 * Search criteria. Available for inheriting classes.
	 */
	protected ReplaceForm replaceForm;

	/**
	 * Takes the UI independend command implementation as argument.
	 * @param uiBean business logic implementation.
	 */
	public CommandReplace(UIFace uiBean) {
		super(uiBean);
	}

	/**
	 * Executes the command.
	 */
	public void actionPerformed(ActionEvent event)
	{
		applyReset();
		fileSetTable.clearInfo();
		ReplaceCommand rc = new ReplaceCommand();
		rc.setDaemon( true );
		rc.start();
	}

	/**
	 * Sets the container for the operation parameter
	 * @param form operation parameter
	 */
	public void setReplaceForm(ReplaceForm form)
	{
		replaceForm = form;
	}
	
	class ReplaceCommand extends Thread {
		
		public void run() {
			uibean.replace( replaceForm, fileSetTable.listRows() );
			fileSetTable.notifyUpdate();
		}
	}
}
