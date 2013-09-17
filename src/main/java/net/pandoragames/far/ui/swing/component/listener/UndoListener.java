package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.pandoragames.far.ui.UIFace;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;

/**
 * ActionListener for the Undo button. Triggers the operation
 * and informs the table model about the changes.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class UndoListener implements ActionListener {

	private UIFace uiFace;
	private FileSetTableModel tableModel;
	
	/**
	 * Constructor takes the backend interface.
	 * @param ui interface to backend
	 */
	public UndoListener(UIFace ui) {
		uiFace = ui;
	}
	
	/**
	 * Triggers the operation and informs the table model about the changes.
	 */
	public void actionPerformed(ActionEvent event) {
		uiFace.undoLastReplace();
		tableModel.notifyUpdate();
	}
	
	/**
	 * Sets the reference to the FileSetTableModel.
	 * @param model reference to TableModel
	 */
	public void setTableModel(FileSetTableModel model) {
		tableModel = model;
	}

}
