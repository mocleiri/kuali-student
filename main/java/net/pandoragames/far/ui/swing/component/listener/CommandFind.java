package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ActionEvent;
import java.util.List;

import net.pandoragames.far.ui.UIFace;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.TargetFile;

/**
 * The Find command. 
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class CommandFind extends CommandExecutor {
	
	/**
	 * Search criteria. Available for inheriting classes.
	 */
	protected FindForm findForm;

	/**
	 * Takes the UI independend command implementation as argument.
	 * @param uiBean business logic implementation.
	 */
	public CommandFind(UIFace uiBean) {
		super(uiBean);
	}

	/**
	 * Executes the command.
	 */
	public void actionPerformed(ActionEvent event)
	{
		applyReset();
		fileSetTable.clearInfo();
		FindCommand ce = new FindCommand();
		ce.setDaemon( true );
		ce.start();
	}
	
	/**
	 * Sets the container for the query criteria
	 * @param findForm query criteria
	 */
	public void setFindForm(FindForm findForm)
	{
		this.findForm = findForm;
	}
	
	
	class FindCommand extends Thread {
		
		public void run() {
			List<TargetFile> result = uibean.findFiles( findForm );
			fileSetTable.setFileList( result, findForm.getBaseDirectory() );
		}
	}

}
