package net.pandoragames.far;

/**
 * Indicates an unmatched group reference in a replacement string.
 * @author Olivier Wehner at 23.02.2008
 * <!-- copyright note --> 
 */
public class UnmatchedGroupReferenceException extends IndexOutOfBoundsException
{
	/**
	 * Allways supply a usefull error message.
	 * @param errorMessage error message
	 */
	public UnmatchedGroupReferenceException(String errorMessage)
	{
		super( errorMessage );
	}

}
