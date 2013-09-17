package net.pandoragames.far.ui.swing.component.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import net.pandoragames.far.ui.model.FileRepository;

/**
 * Opens a JFileChooser dialogue that allows to select a directory.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class BrowseButtonListener implements ActionListener
{
	private JTextField display;
	private FileRepository config;
	private String label;
	private List<ActionListener> listenerList;
	/**
	 * Constructor for this listener.
	 * @param textField where the selected path is displayed
	 * @param fileRepository where the selected file or directory is stored
	 * @param titleLabel label for the file chooser dialogue title
	 */
	public BrowseButtonListener(JTextField textField, FileRepository fileRepository, String titleLabel) {
		display = textField;
		config = fileRepository;
		label = titleLabel;
	}
	/**
	 * Opens a JFileChooser dialogue that allows to select a directory.
	 * @param event triggered by a pressed "browse" button
	 */
	public void actionPerformed(ActionEvent event) {
		JFileChooser fileChooser = new JFileChooser( config.getFile() );
		fileChooser.setDialogTitle( label );
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		int returnVal = fileChooser.showOpenDialog( display );
	    if( returnVal == JFileChooser.APPROVE_OPTION ) {
	    	if( config.setFile( fileChooser.getSelectedFile() ) ) {
		    	display.setText( fileChooser.getSelectedFile().getPath() );
		    	display.setToolTipText( fileChooser.getSelectedFile().getPath() );
		    	if( listenerList != null ) {
		    		for(ActionListener a : listenerList) {
		    			a.actionPerformed( event );
		    		}
		    	}
	    	}
	    }
	}
	
	/**
	 * Accepts itself ActionListener.
	 * @param listener notified when a directory was selected.
	 */
	public void addActionListener(ActionListener listener) {
		if( listenerList == null ) {
			listenerList = new ArrayList<ActionListener>();
		}
		listenerList.add( listener );
	}
}
