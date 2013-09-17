package net.pandoragames.far.ui.swing.component;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;

import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.model.Resetable;
import net.pandoragames.far.ui.swing.SwingConfig;

/**
 * A panel that displays informations and errormessages in two distinct areas
 * as to avoid overwriting error messages with informative messages.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class InfoLine extends JPanel implements MessageBox, Resetable {

	private MessageLabel errorMessages;
	private MessageLabel infoMessages;
	
	/**
	 * Constructor takes a SwingConfig instance for configuration details.
	 * @param config configuration instance
	 */
	public InfoLine(SwingConfig config) {
		errorMessages = new MessageLabel(); 
		errorMessages.setMinimumSize( new Dimension(100, config.getStandardComponentHight()));
		errorMessages.setBorder( BorderFactory.createEmptyBorder(1, SwingConfig.PADDING, 2, SwingConfig.PADDING));
		infoMessages = new MessageLabel(); 
		infoMessages.setMinimumSize( new Dimension(100, config.getStandardComponentHight()));
		infoMessages.setBorder( BorderFactory.createEmptyBorder(1, SwingConfig.PADDING, 2, SwingConfig.PADDING));
		this.setLayout( new BorderLayout() );
		this.add( errorMessages, BorderLayout.WEST );
		this.add( Box.createRigidArea( new Dimension(SwingConfig.PADDING, 20)), 
								BorderLayout.CENTER );
		this.add( infoMessages, BorderLayout.EAST );
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		errorMessages.clear();
		infoMessages.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(String message) {
		errorMessages.error(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void info(String message) {
		infoMessages.info(message);
	}

	/**
	 * Alias for <code>clear()</code> from interface 
	 * {@link net.pandoragames.far.ui.model.Resetable Resetable}
	 */
	public void reset() {
		clear();
	}
	
}
