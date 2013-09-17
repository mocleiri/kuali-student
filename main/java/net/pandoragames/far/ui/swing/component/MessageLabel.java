package net.pandoragames.far.ui.swing.component;

import java.awt.Color;

import javax.swing.JLabel;

import net.pandoragames.far.ui.model.MessageBox;

/**
 * Adds functionality to the JLabel in order to display (error) messages
 * @author Olivier Wehner at 25.02.2008
 * <!-- copyright note --> 
 */
public class MessageLabel extends JLabel implements MessageBox
{
	private boolean onError = false;
	/**
	 * Display an error message.
	 * @param message to be displayed.
	 */
	public void error(String message) {
		message = cleanMessage( message );
		this.setForeground( Color.RED );
		this.setText( message );		
		this.setToolTipText( message );
		onError = true;
	}
	/**
	 * Displays the specified message, unless an error message is displayed.
	 * @param message to be displayed.
	 */
	public void info(String message) {
		if(! onError ) {
			message = cleanMessage( message );
			this.setForeground( Color.BLACK );
			this.setText( message );		
			this.setToolTipText( message );
		}
	}
	/**
	 * Clears the display.
	 */
	public void clear() {
		this.setText("");
		this.setToolTipText(null);
		this.setForeground( Color.BLACK );
		onError = false;
	}
	
	private String cleanMessage(String message) {
		if(message == null) return "";
		int breakpoint = message.indexOf("\n");
		if(breakpoint > 0) return message.substring(0, breakpoint);
		return message;
	}

}
