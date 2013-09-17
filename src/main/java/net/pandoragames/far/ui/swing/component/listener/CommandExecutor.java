package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import net.pandoragames.far.ui.UIFace;
import net.pandoragames.far.ui.model.Resetable;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;

/**
 * Base class for the Find and the Replace command. 
 * This classes serve as a bridge between Swing specific classes
 * and the business logic. 
 * They actually start a Threads in which the command will be processed.
 * 
 * @author Olivier Wehner at 26.02.2008
 * <!-- copyright note --> 
 */
public abstract class CommandExecutor implements ActionListener
{
	private List<Resetable> resetables;
	
	/**
	 * Interface to business logic. Available for inheriting classes.
	 */
	protected UIFace uibean;
	/**
	 * Result table. Available for inheriting classes.
	 */
	protected FileSetTableModel fileSetTable;
	
	/**
	 * Takes the UI independend command implementation as argument.
	 * @param uiBean business logic implementation.
	 */
	public CommandExecutor(UIFace uiBean) {
		uibean = uiBean;
	}

	/**
	 * Sets the Swing table that displays the result
	 * @param fileSetTable result table
	 */
	public void setFileSetTable(FileSetTableModel fileSetTable)
	{
		this.fileSetTable = fileSetTable;
	}

	/**
	 * Adds some component that should be reseted when (i.e. before)
	 * the command is triggered).
	 * @param toBeReseted to be reseted on command execution
	 */
	public void addResetable(Resetable toBeReseted) {
		if(resetables == null) resetables = new ArrayList<Resetable>();
		resetables.add( toBeReseted );
	}
	
	/**
	 * Resets all registered resetable components.
	 */
	protected void applyReset() {
		if(resetables != null) {
			for(Resetable resetable : resetables) {
				resetable.reset();
			}
		}
	}
}
