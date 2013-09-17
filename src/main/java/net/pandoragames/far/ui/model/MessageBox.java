package net.pandoragames.far.ui.model;

/**
 * Interface for a message sink.
 * @author Olivier Wehner at 26.02.2008
 * <!-- copyright note --> 
 */
public interface MessageBox
{
	
	/**
	 * Consumes an error message.
	 * @param message error message
	 */
	public void error(String message);
	
	/**
	 * Consumes a normal message.
	 * @param message normal message
	 */
	public void info(String message);
	
	/**
	 * Clears all messages.
	 */
	public void clear();
}
