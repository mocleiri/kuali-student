/**
 * 
 */
package net.pandoragames.far.ui.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;

/**
 * 
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
class StandardFileSelector implements ActionListener {
	private FileSetTableModel tableModel;
	private int mode;
	/** 
	 * Define if this selector should select all (1), nothing (-1) or invert selection (0).
	 * @param model files to be selected
	 * @param mode select all (1), unselect all (-1) or invert(0)
	 */
	public StandardFileSelector(FileSetTableModel model, int mode) {
		tableModel = model;
		this.mode = mode;
	}
	public void actionPerformed(ActionEvent e) {
		if(mode == 0) {
			for(TargetFile row : tableModel.listRows() ) {
				row.clear();
				row.setSelected( ! row.isSelected() );
			}
		} else {
			for(TargetFile row : tableModel.listRows() ) {
				row.clear();
				row.setSelected( mode > 0 );
			}
		}
		tableModel.notifyUpdate();
	}	
}
