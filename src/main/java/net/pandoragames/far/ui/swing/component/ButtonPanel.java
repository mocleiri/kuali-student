package net.pandoragames.far.ui.swing.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.pandoragames.far.ui.UIFace;
import net.pandoragames.far.ui.model.FileNamePattern;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.model.OperationType;
import net.pandoragames.far.ui.swing.ComponentRepository;
import net.pandoragames.far.ui.swing.SwingConfig;
import net.pandoragames.far.ui.swing.component.listener.ConfirmReplaceListener;
import net.pandoragames.util.i18n.Localizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * The main button panel. 
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class ButtonPanel extends JPanel {
	
	private OperationType operationType;
	// private FindForm dataModel;
	private UIFace backend;
	private Localizer localizer;
	private Log logger = LogFactory.getLog( this.getClass() );
	private MessageBox messageBox;
	
	public ButtonPanel(OperationType type, SwingConfig config, ComponentRepository componentRepository) {
		operationType = type;
		localizer = config.getLocalizer();
		backend = componentRepository.getUiface();
		messageBox = componentRepository.getMessageBox();
		init( config, componentRepository );
	}
	
	/**
	 * Sets the component as center.
	 */
	public void setMainPanel(JPanel panel) {
		this.add( panel, BorderLayout.CENTER );
	}
	
	private void init(SwingConfig config, ComponentRepository componentRepository) {
		this.setLayout( new BorderLayout() );
		this.add( initButtonPannel( config, componentRepository ), BorderLayout.SOUTH );
	}
	
	/**
	 * Initialises the BUTTON panel.
	 * @param config configuration properties
	 * @param componentRepository repository for shared components
	 */
	private JPanel initButtonPannel(SwingConfig config, ComponentRepository componentRepository) {
		
		JPanel buttonPannel = new JPanel();
		buttonPannel.setAlignmentX( Component.LEFT_ALIGNMENT );
		buttonPannel.setLayout( new FlowLayout( FlowLayout.TRAILING ) );
		// FIND
		if( operationType == OperationType.FIND ) {
			JButton findButton = new JButton( localizer.localize("button.find"));
			componentRepository.getOperationCallBackListener().addComponentStartDisabled( findButton, OperationType.FIND );
			componentRepository.getOperationCallBackListener().addComponentTerminationEnabled( findButton, OperationType.FIND );
			findButton.addActionListener( componentRepository.getFindCommand() );
			findButton.addActionListener( new ReorderFilePatternListListener( 
												componentRepository.getFindForm(),
												config.getFileNamePatternList() ) );
			buttonPannel.add( findButton );
		}
		// REPLACE
		if( operationType == OperationType.REPLACE ) {
			JButton replaceButton = new JButton( localizer.localize("button.replace"));
			replaceButton.setEnabled( false );
			componentRepository.getOperationCallBackListener().addComponentTerminationEnabled( replaceButton, OperationType.FIND );
			componentRepository.getResetDispatcher().addToBeDisabled( replaceButton );
			componentRepository.getSearchBaseListener().addToBeDisabled( replaceButton );
			ConfirmReplaceListener replaceListener = 
				new ConfirmReplaceListener(componentRepository.getRootWindow(), config, componentRepository.getReplaceForm());
			replaceListener.addActionListener( componentRepository.getReplaceCommand() );
			replaceButton.addActionListener( replaceListener );
			buttonPannel.add( replaceButton );
		}
		// RENAME
		if( operationType == OperationType.RENAME ) {
			JButton renameButton = new JButton( componentRepository.getRenameCommand() );
			renameButton.setEnabled( false );
			componentRepository.getResetDispatcher().addToBeDisabled( renameButton );
			buttonPannel.add( renameButton );			
		}
		// CANCEL
		JButton cancelButton = new JButton( localizer.localize("button.cancel"));
		cancelButton.setEnabled( false );
		componentRepository.getOperationCallBackListener().addComponentStartEnabled( cancelButton, OperationType.ANY );
		cancelButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				backend.abort();
			}						
		});
		buttonPannel.add( cancelButton );	
		componentRepository.getResetDispatcher().addToBeDisabled( cancelButton );
		// UNDO
		if( operationType == OperationType.REPLACE ) {
			JButton undoButton = new JButton( localizer.localize("button.undo"));
			undoButton.setEnabled( false );
			componentRepository.getOperationCallBackListener().addComponentTerminationEnabled( undoButton, OperationType.REPLACE );
			componentRepository.getOperationCallBackListener().addComponentStartDisabled(undoButton, OperationType.FIND );
			undoButton.addActionListener( componentRepository.getUndoListener() );
			undoButton.addActionListener( new OnClickDisable(undoButton) );
			buttonPannel.add( undoButton );
		}
		// RESET
		JButton clearButton = new JButton( localizer.localize("button.reset"));
		clearButton.addActionListener( componentRepository.getResetDispatcher() );
		buttonPannel.add( clearButton );		
		// this.add( buttonPannel );
		return buttonPannel;
	}
	
	/**
	 * Disables a button once it has been clicked.
	 */
	class OnClickDisable implements ActionListener {
		JButton me;
		public OnClickDisable(JButton button) {
			me = button;
		}
		public void actionPerformed(ActionEvent event) {
			me.setEnabled( false );
		}								
	}
	
	/**
	 * Puts the last used file pattern on top of the list.
	 * Visible only after application restart.
	 */
	class ReorderFilePatternListListener implements ActionListener {
		private FindForm findForm;
		private List<FileNamePattern> patternList;
		public ReorderFilePatternListListener(FindForm form, List<FileNamePattern> filePattern) {
			findForm = form;
			patternList = filePattern;
		}
		public void actionPerformed(ActionEvent event) {
			if( ! findForm.getFileNamePattern().equals( patternList.get(0) ) ) {
				boolean found = false;
				int count = 1;
				while((! found) && (count < patternList.size())) {
					if( findForm.getFileNamePattern().equals( patternList.get(count) ) ){
						found = true;
						FileNamePattern pattern = patternList.remove(count);
						patternList.add(0, pattern);
						logger.debug("Moved " + pattern + " on top");
					}
					count++;
				}
			}
		}								
	}
	
}
