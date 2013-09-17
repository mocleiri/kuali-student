package net.pandoragames.far.ui.swing.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import net.pandoragames.far.ui.swing.ComponentRepository;
import net.pandoragames.far.ui.swing.SwingConfig;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.far.ui.swing.dialog.SubSelectDialog;

/**
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
class SelectByContentListener implements ActionListener {
	private SwingConfig config;
	private ComponentRepository repository; 
	private JFrame jframe;
	private FileSetTableModel tableModel;
	public SelectByContentListener(JFrame owner, FileSetTableModel model, ComponentRepository cr, SwingConfig swc) {
		config = swc;
		repository = cr;
		jframe = owner;
		tableModel = model;
	}
	public void actionPerformed(ActionEvent e) {
		SubSelectDialog dialog = new SubSelectDialog(jframe, tableModel, repository, config);
		dialog.pack();
		dialog.show();
	}
}

