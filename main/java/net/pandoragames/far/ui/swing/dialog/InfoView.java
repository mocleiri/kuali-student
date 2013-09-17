package net.pandoragames.far.ui.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.pandoragames.far.ui.FilePropertiesFormater;
import net.pandoragames.far.ui.model.CharacterUtil;
import net.pandoragames.far.ui.model.LineSeparator;
import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.SwingConfig;
import net.pandoragames.far.ui.swing.component.TwoComponentsPanel;

/**
 * Displays file information like size and line count.
 *
 * @author olive
 * <!-- copyright note -->
 */
public class InfoView extends SubWindow {
	
	private static FilePropertiesFormater formater = new FilePropertiesFormater();

	private TargetFile targetFile;

	public InfoView(JFrame owner, TargetFile file, SwingConfig config) {
		super(owner, file.getName());
		targetFile = file;
		init( config );
	}
	
	private void init(SwingConfig config) {
		
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout( new BorderLayout() );
		
		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BoxLayout( basePanel, BoxLayout.Y_AXIS ) );
		basePanel.setBorder( BorderFactory.createEmptyBorder( 0, 
				SwingConfig.PADDING, 
				SwingConfig.PADDING, 
				SwingConfig.PADDING ) );
		registerCloseWindowKeyListener( basePanel );
		
		// some headspace
		basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		
		// file name
		JLabel labelFileName = new JLabel( config.getLocalizer().localize("label.filename"));
		JTextField nameField = new JTextField( targetFile.getName() );
		nameField.setEditable( false );
		nameField.setBorder( BorderFactory.createEmptyBorder() );
		TwoComponentsPanel nameLine = new TwoComponentsPanel(labelFileName, nameField);
		nameLine.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( nameLine );
		
		basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));

		// file size
		JLabel labelFileSize = new JLabel( config.getLocalizer().localize("label.filesize"));
		JTextField sizeField = new JTextField( formater.formatBytes( targetFile.getFile().length(), config.getLocalizer()) );
		sizeField.setEditable( false );
		sizeField.setBorder( BorderFactory.createEmptyBorder() );
		TwoComponentsPanel sizeLine = new TwoComponentsPanel(labelFileSize, sizeField);
		sizeLine.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( sizeLine );

		basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
		
		// lines
		CharacterUtil.LineCount lines = countLines();
		
		// line count
		JLabel labelLineCount = new JLabel( config.getLocalizer().localize("label.lineCount"));
		JTextField lineField = new JTextField( String.valueOf( lines.getLineCount() ) );
		lineField.setEditable( false );
		lineField.setBorder( BorderFactory.createEmptyBorder() );
		TwoComponentsPanel linesLine = new TwoComponentsPanel(labelLineCount, lineField);
		linesLine.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( linesLine );

		basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));

		// line separator
		JLabel labelSeparator = new JLabel( config.getLocalizer().localize("label.line-separator"));
		JTextField sepField = new JTextField( config.getLocalizer().localize("label.line-separator." + lines.getSeparator().name()) );
		sepField.setEditable( false );
		sepField.setBorder( BorderFactory.createEmptyBorder() );
		TwoComponentsPanel sepLine = new TwoComponentsPanel(labelSeparator, sepField);
		sepLine.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( sepLine );

		basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));

		// last modified
		JLabel labelLastModified = new JLabel( config.getLocalizer().localize("label.last-modified"));
		JTextField modifiedField = new JTextField( formater.timeStamp( targetFile.getFile().lastModified() ) );
		modifiedField.setEditable( false );
		modifiedField.setBorder( BorderFactory.createEmptyBorder() );
		TwoComponentsPanel modifiedLine = new TwoComponentsPanel(labelLastModified, modifiedField);
		modifiedLine.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( modifiedLine );
		
		// encoding
		JLabel labelCharset = new JLabel( config.getLocalizer().localize("label.charset"));
		JComboBox  charsetList = new JComboBox( config.getCharsetList().toArray() );
		Charset charset = targetFile.getCharacterset() == null ? config.getDefaultCharset() : targetFile.getCharacterset();
		charsetList.setSelectedItem( charset );
 		charsetList.addItemListener( new ItemListener()  {
 			public void itemStateChanged(ItemEvent event) {
 				Charset charset = (Charset) event.getItem();
 				logger.debug("Character set changed to " + charset.name());
 				targetFile.setCharacterset( charset );
 			}
 		} );
 		JPanel boxpanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
 		boxpanel.add( charsetList );
 		TwoComponentsPanel charsetLine = new TwoComponentsPanel(labelCharset, boxpanel);
 		charsetLine.setAlignmentX( Component.LEFT_ALIGNMENT );
 		basePanel.add( charsetLine );
 
 		// full path
 		JLabel labelFilePath = new JLabel( config.getLocalizer().localize("label.path"));
 		labelFilePath.setAlignmentX( Component.LEFT_ALIGNMENT );
 		basePanel.add( labelFilePath );
 		JTextArea pathField = new JTextArea( targetFile.getFile().getPath() );
 		pathField.setLineWrap( true );
 		pathField.setEditable( false );
 		pathField.setRows(2);
 		pathField.setBorder( BorderFactory.createEmptyBorder() );
 		JScrollPane contentScrollPane = new JScrollPane( pathField );
 		contentScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
 		contentScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		contentScrollPane.setAlignmentX( Component.LEFT_ALIGNMENT );
		basePanel.add( contentScrollPane );
		
		// READ ONLY Warning
		if( ! targetFile.getFile().canWrite() ) {
			basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
			
			JLabel roWarning = new JLabel( config.getLocalizer().localize("label.read-only") + "!");
			roWarning.setAlignmentX( Component.LEFT_ALIGNMENT );
	 		basePanel.add( roWarning );
		}
		
		// Error messages
		if( targetFile.getErrorMessage() != null ) {
			basePanel.add( Box.createRigidArea( new Dimension(1, SwingConfig.PADDING )));
			
	 		JTextArea errorField = new JTextArea( targetFile.getErrorMessage() );
	 		errorField.setForeground( Color.RED );
	 		errorField.setLineWrap( true );
	 		errorField.setEditable( false );
	 		errorField.setRows(3);
	 		errorField.setBorder( BorderFactory.createEmptyBorder() );
	 		JScrollPane errorScrollPane = new JScrollPane( errorField );
	 		errorScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
	 		errorScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
	 		errorScrollPane.setAlignmentX( Component.LEFT_ALIGNMENT );
			basePanel.add( errorScrollPane );
		}
 		
 		getContentPane().add( basePanel, BorderLayout.CENTER );
 		
 		// close button
 		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
 		JButton closeButton = new JButton( config.getLocalizer().localize("button.close") );
 		closeButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				InfoView.this.dispose();
			}						
		});
 		buttonPanel.add( closeButton );
 		//    buttonPanel.setAlignmentX( Component.LEFT_ALIGNMENT );
 		getContentPane().add( buttonPanel, BorderLayout.SOUTH );
 		
 		placeOnScreen( config.getScreenCenter() );
	}
	
	
	private CharacterUtil.LineCount countLines() {
		File file = targetFile.getFile();
		InputStream instream = null;
		try {
			instream = new BufferedInputStream( new FileInputStream( file ) );
			return CharacterUtil.countLinebreaks(instream);
		} catch (IOException iox) {
			logger.error(iox.getClass().getSimpleName() + " reading " + file.getPath() + ": " + iox.getMessage());
			return new CharacterUtil.LineCount(LineSeparator.UNDEFINED, 0);
		} finally {
			if(instream != null) try { instream.close(); } catch(IOException iox) { /* ignore */ }
		}
	}
}
