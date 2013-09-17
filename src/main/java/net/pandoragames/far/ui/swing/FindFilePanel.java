package net.pandoragames.far.ui.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import net.pandoragames.far.ui.FARConfig;
import net.pandoragames.far.ui.SimpleFileNamePattern;
import net.pandoragames.far.ui.model.AbstractFileRepository;
import net.pandoragames.far.ui.model.FileNamePattern;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.FormUpdateEvent;
import net.pandoragames.far.ui.model.FormUpdateListener;
import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.swing.component.ContentSearchPanel;
import net.pandoragames.far.ui.swing.component.TwoComponentsPanel;
import net.pandoragames.far.ui.swing.component.listener.FindFilePanelBrowseButtonListener;
import net.pandoragames.util.i18n.Localizer;

import org.apache.commons.logging.LogFactory;

/**
 * Defines the search criteria for files. 
 * @author Olivier Wehner at 26.02.2008
 * <!-- copyright note --> 
 */
public class FindFilePanel extends JPanel implements FormUpdateListener
{
	private FindForm dataModel;
	private Localizer localizer;
	private FindFilePanelBrowseButtonListener browseButtonListener;
	
	// components
	private JTextField baseDirPathTextField = new JTextField();
	private JCheckBox subdirFlag;
	private JComboBox listPattern;
	private JCheckBox patternFlag;
	private ContentSearchPanel contentSearchPanel;
	private JCheckBox inversionFlag;

	
	public FindFilePanel(SwingConfig config, ComponentRepository componentRepository) {
		localizer = config.getLocalizer();
		dataModel = componentRepository.getFindForm();		
		dataModel.addFormUpdateListener( this );
		init( config, componentRepository );		
	}
	
	/** 
	 * {@inheritDoc}
	 */
	public void formUpdated( FormUpdateEvent event ) {
		baseDirPathTextField.setText( dataModel.getBaseDirectory().getPath() );
		baseDirPathTextField.setToolTipText( dataModel.getBaseDirectory().getPath() );
		subdirFlag.setSelected( dataModel.isIncludeSubDirs() );
		boolean itemfound = false;
		for(int i = 0; i < listPattern.getItemCount(); i++) {
			if( dataModel.getFileNamePattern().equals( listPattern.getItemAt(i))) {
				itemfound = true;
				break;
			}
		}
		if( ! itemfound ) {
			listPattern.addItem(dataModel.getFileNamePattern());
		}
		listPattern.setSelectedItem( dataModel.getFileNamePattern() );
		patternFlag.setSelected( dataModel.getFileNamePattern().isRegex() );
		inversionFlag.setSelected( dataModel.isInvertContentFilter() );
		contentSearchPanel.loadForm(dataModel, true);
	}
	
	private void init(SwingConfig config, ComponentRepository componentRepository) {
		
		this.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
		
		this.setBorder( BorderFactory.createEmptyBorder( 0, 
														SwingConfig.PADDING, 
														SwingConfig.PADDING, 
														SwingConfig.PADDING ) );
		initBaseDirPanel( config, componentRepository );
		initFileNamePatternPanel( config, componentRepository );
		initContentSearchPanel( config, componentRepository );
	}
	
	private void initBaseDirPanel( SwingConfig config,  ComponentRepository componentRepository) {
		
		this.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));

		
		JLabel labelBaseDir = new JLabel( config.getLocalizer().localize("label.base-directory") );
		labelBaseDir.setAlignmentX( Component.LEFT_ALIGNMENT );
		this.add( labelBaseDir );
		
		baseDirPathTextField.setPreferredSize( new Dimension(  SwingConfig.COMPONENT_WIDTH, 
																config.getStandardComponentHight()) );
		baseDirPathTextField.setMaximumSize( new Dimension(  SwingConfig.COMPONENT_WIDTH_MAX, 
																config.getStandardComponentHight()) );
		baseDirPathTextField.setText( dataModel.getBaseDirectory().getPath() );
		baseDirPathTextField.setToolTipText( dataModel.getBaseDirectory().getPath() );
		baseDirPathTextField.setEditable( false );

		JButton openBaseDirFileChooserButton = new JButton( localizer.localize("button.browse") );
		openBaseDirFileChooserButton.requestFocusInWindow();
		class BaseDirectoryRepository extends AbstractFileRepository {
			public BaseDirectoryRepository(FARConfig cfg, ComponentRepository repository ) {
				super(cfg, repository.getFindForm(), repository.getReplaceForm(), repository.getMessageBox());
			}
			public File getFile() {
				return farconfig.getBaseDirectory();
			}
			public boolean setFile(File file) {
				if( isSubdirectory(replaceForm.getBackupDirectory(), file) ) {
					messageBox.error( localizer.localize("message.nested-base-child") );
					return false;
				} else if ( isSubdirectory( file, replaceForm.getBackupDirectory()) ) {
					messageBox.error( localizer.localize("message.nested-base-parent") );
					return false;					
				} else {
					farconfig.setBaseDirectory(file);
					findForm.setBaseDirectory(file);
					return true;
				}
			}
		}
		browseButtonListener = new FindFilePanelBrowseButtonListener(baseDirPathTextField,
															new BaseDirectoryRepository(config, componentRepository),
															localizer.localize( "label.choose-base-directory" ),
															componentRepository.getFindCommand(),
															componentRepository.getResetDispatcher());
		browseButtonListener.addActionListener( componentRepository.getSearchBaseListener() );
		openBaseDirFileChooserButton.addActionListener( browseButtonListener );
		TwoComponentsPanel lineBaseDir = new TwoComponentsPanel( baseDirPathTextField, openBaseDirFileChooserButton);
		lineBaseDir.setAlignmentX( Component.LEFT_ALIGNMENT );
		this.add( lineBaseDir );
		
		subdirFlag = new JCheckBox( localizer.localize("label.include-subdir"));
		subdirFlag.setAlignmentX( Component.LEFT_ALIGNMENT );
		subdirFlag.setSelected( dataModel.isIncludeSubDirs() );
		subdirFlag.addItemListener( new ItemListener() {	
				public void itemStateChanged(ItemEvent event) {
						dataModel.setIncludeSubDirs( (ItemEvent.SELECTED == event.getStateChange()) );
				}
			});
		subdirFlag.addItemListener( componentRepository.getSearchBaseListener() );
		componentRepository.getResetDispatcher().addToBeSelected( subdirFlag );
		browseButtonListener.setSubdirCheckBox( subdirFlag );
		this.add( subdirFlag );
		
		this.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		this.add( Box.createVerticalGlue() );
	}
	
	private void initFileNamePatternPanel(SwingConfig config,  ComponentRepository componentRepository) {
		
		JLabel labelPattern = new JLabel( localizer.localize("label.file-name-pattern") );
		labelPattern.setAlignmentX( Component.LEFT_ALIGNMENT );
		this.add( labelPattern );
		
		listPattern = new JComboBox( config.getFileNamePatternList().toArray() );
		listPattern.setEditable( true );
		listPattern.setMaximumSize( new Dimension(  SwingConfig.COMPONENT_WIDTH_MAX, 
									config.getStandardComponentHight()) );
		
		JButton buttonSavePattern = new JButton( localizer.localize("button.save-pattern"));
		buttonSavePattern.setEnabled( false );
		TwoComponentsPanel linePattern = new TwoComponentsPanel( listPattern, buttonSavePattern);
		linePattern.setAlignmentX( Component.LEFT_ALIGNMENT );
		this.add( linePattern );
		
		patternFlag = new JCheckBox( localizer.localize("label.regular-expression"));
		patternFlag.setAlignmentX( Component.LEFT_ALIGNMENT );
		patternFlag.setSelected( dataModel.getFileNamePattern().isRegex() );
		patternFlag.addItemListener( new ItemListener() {	
				public void itemStateChanged(ItemEvent event) {
						dataModel.getFileNamePattern().setRegex( (ItemEvent.SELECTED == event.getStateChange()) );
				}
			});
		patternFlag.addItemListener( componentRepository.getSearchBaseListener() );
		browseButtonListener.setRegexCheckBox( patternFlag );
		browseButtonListener.addComponentToBeDisabledForSingleFiles( patternFlag );
		componentRepository.getSearchBaseListener().addToBeEnabled( patternFlag );
		componentRepository.getResetDispatcher().addToBeEnabled( patternFlag );
		listPattern.addActionListener( new PatternListListener(patternFlag, componentRepository.getMessageBox()) );
		listPattern.addActionListener( componentRepository.getSearchBaseListener() );
		ComboBoxEditor comboBoxEditor = new FileNamePatternEditor( buttonSavePattern ); 
		listPattern.setEditor( comboBoxEditor );
		browseButtonListener.setComboBox( listPattern );
		browseButtonListener.addComponentToBeDisabledForSingleFiles( listPattern );
		componentRepository.getSearchBaseListener().addToBeEnabled( listPattern );
		componentRepository.getResetDispatcher().addToBeEnabled( listPattern );
		buttonSavePattern.addActionListener( new SaveFileNamePatternListener(listPattern, 
														patternFlag, 
														config.getFileNamePatternList(),
														componentRepository.getMessageBox()) );
		this.add( patternFlag );

		this.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		this.add( Box.createVerticalGlue() );
	}
	
	private void initContentSearchPanel(SwingConfig config, ComponentRepository componentRepository) {
		
		this.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		
		String title = localizer.localize("label.content-pattern");
		contentSearchPanel = new ContentSearchPanel( title, dataModel, config, componentRepository);
		contentSearchPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
		browseButtonListener.addComponentToBeDisabledForSingleFiles( contentSearchPanel );
		componentRepository.getSearchBaseListener().addToBeEnabled( contentSearchPanel );
		componentRepository.getResetDispatcher().addToBeEnabled( contentSearchPanel );
		this.add( contentSearchPanel );

		inversionFlag = new JCheckBox( localizer.localize("label.exclude-matches"));
		inversionFlag.setAlignmentX( Component.LEFT_ALIGNMENT );
		inversionFlag.setSelected( dataModel.isInvertContentFilter() );
		inversionFlag.addItemListener( new ItemListener() {	
				public void itemStateChanged(ItemEvent event) {
						dataModel.setInvertContentFilter( (ItemEvent.SELECTED == event.getStateChange()) );
				}
			});
		browseButtonListener.addComponentToBeDisabledForSingleFiles( inversionFlag );
		componentRepository.getSearchBaseListener().addToBeEnabled( inversionFlag );
		componentRepository.getResetDispatcher().addToBeEnabled( inversionFlag );
		contentSearchPanel.addFlag( inversionFlag );
		
		this.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		this.add( Box.createVerticalGlue() );

	}
	
// -- event listener ------------------------------------------------------------------------------------------------------------------	
		
	/**
	 * Updates the data model and the "file name pattern - regular expression" checkbox according to
	 * the selected search string.
	 */
	class PatternListListener implements ActionListener {
		private JCheckBox checkBox;
		private MessageBox messageBox;
		public PatternListListener(JCheckBox box, MessageBox messageSink) {
			checkBox = box;
			messageBox = messageSink;
		}
		public void actionPerformed(ActionEvent event) {
	        JComboBox cbox = (JComboBox) event.getSource();
	        FileNamePattern fileNamePattern = (FileNamePattern) cbox.getSelectedItem();
	        // First update the data model...
	        dataModel.setFileNamePattern( fileNamePattern );
	        // ... because checkBox.setSelected will trigger another update of it!
	        checkBox.setSelected( fileNamePattern.isRegex() );
	        // print a warning if pattern is invalid
	        if( ! SimpleFileNamePattern.getInstance().validateFileNamePattern(fileNamePattern) ) {
	        	String messageCode = fileNamePattern.isRegex() ? "message.invalid-file-name-regex" : "message.invalid-file-name-pattern";
	        	messageBox.error(localizer.localize(messageCode, new String[]{fileNamePattern.getPattern()}));
	        }
	    }
	}
	

	/**
	 * Listens to changes of the file name pattern.
	 *
	 * @author Olivier Wehner
	 */
	class SaveFileNamePatternListener implements ActionListener {
		private JCheckBox checkBox; 
		private JComboBox comboBox;
		private List<FileNamePattern> backupList;
		private MessageBox messageBox;
		public SaveFileNamePatternListener(JComboBox combo, 
											JCheckBox check, 
											List<FileNamePattern> list,
											MessageBox messageSink) {
			comboBox = combo;
			checkBox = check;
			backupList = list;
			messageBox = messageSink;
		}
		public void actionPerformed(ActionEvent event) {
			FileNamePattern pattern = (FileNamePattern) comboBox.getSelectedItem();
			pattern.setRegex( checkBox.isSelected() );
			if( SimpleFileNamePattern.getInstance().validateFileNamePattern(pattern) ) {
				// comboBox.getSelectedIndex() always < 0 !
				boolean found = false;
				int count = 0;
				do {
					FileNamePattern item = (FileNamePattern) comboBox.getItemAt( count );
					if( item.getPattern().equals( pattern.getPattern() )) {
						found = true;
						item.setRegex( pattern.isRegex() );
					}
					count++;
				} while ((! found) && (count < comboBox.getItemCount()));
				if( ! found ) {
					comboBox.addItem( pattern );
					backupList.add( pattern );
				} 
				((JButton) event.getSource() ).setEnabled( false );
			} else {
				messageBox.error(localizer.localize("message.invalid-file-name-pattern", 
													new String[]{pattern.getPattern()}));
			}
		}
	}

	/**
	 * Editor component for the FileNamePattern combobox.
	 * @author Olivier Wehner at 03.03.2008
	 */
	class FileNamePatternEditor extends JTextField implements ComboBoxEditor {
		private JButton saveButton;
		private FileNamePattern lastUnsavedValue;
		public FileNamePatternEditor(JButton button) {
			saveButton = button;
			getDocument().addDocumentListener(new DocumentListener(){
				public void changedUpdate(DocumentEvent e) {
				}
				public void removeUpdate(DocumentEvent e){
					if( e.getDocument().getLength() > 0) {
						saveButton.setEnabled( true );
						lastUnsavedValue = new FileNamePattern( getText( e.getDocument() ), patternFlag.isSelected() );
						dataModel.setFileNamePattern(lastUnsavedValue);
					}
				}
				public void insertUpdate(DocumentEvent e) {
					String text = getText( e.getDocument() );
					if( ! text.equals( lastUnsavedValue.getPattern() )) {
						saveButton.setEnabled( true );
						lastUnsavedValue = new FileNamePattern( text, patternFlag.isSelected() );
						dataModel.setFileNamePattern(lastUnsavedValue);
					}
				}
				private String getText(Document document) {
					try {
						return document.getText(0, document.getLength());
					} catch (Exception x) {
						LogFactory.getLog( this.getClass() ).error(x.getClass().getName() 
											+ " reading file name pattern: " + x.getMessage(), x);
						return "";
					}
				}
			}
			);
		}
		public Component getEditorComponent() {
			return this;
		}
		public Object getItem() {
			return lastUnsavedValue;
		}
		public void setItem(Object anObject) {
			FileNamePattern pattern = (FileNamePattern) anObject;
			if( ! pattern.equals( lastUnsavedValue ) ) saveButton.setEnabled( false );
			lastUnsavedValue = pattern;
			setText( pattern.getPattern() );
		}
	}
	
	
}
