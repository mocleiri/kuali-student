package net.pandoragames.far.ui;

/**
 * Signalls a <i>fatal</i> error during startup or later. The application must not
 * continue to work if a ConfigurationException was thrown.
 * @author Olivier Wehner at 25.02.2008
 * <!-- copyright note --> 
 */
public class ConfigurationException extends RuntimeException
{
	/**
	 * Always supply a useful message.
	 * @param message always supply a useful message
	 */
	public ConfigurationException(String message) {
		super( message );
	}
	
	/**
	 * Wrapper constructor.
	 * @param message always supply a useful message
	 * @param cause wrapped exception
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
}
