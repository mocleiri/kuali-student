package net.pandoragames.far.ui.model;

/**
 * Progress listener. Receives notifications about the progress of 
 * searches or find-and-replace operations. Implicitely there are three
 * types of operations:
 * <ul>
 * <li>Simple Find: Lookup for file names that match a particular pattern.
 * <li>Content Find: Allways preceeded by a simple find, this operation will
 * in addition match the content of the fieles in the result set of the previous operations
 * agains some (regular) expression.
 * <li>Find And Replace: Always preceeded by simple find and content find. 
 * </ul>
 * @author Olivier Wehner at 27.02.2008
 * <!-- copyright note --> 
 */
public interface ProgressListener
{
	
	/**
	 * Gets notified when an operation has been started. 
	 * @param type type of operation that has been started
	 */
	public void operationStarted( OperationType type );
	
	/**
	 * Receives progress informations. The total count parameter can not
	 * be provided for simple find operations and will thus be minus one.
	 * @param count progress count
	 * @param total total count to be attained or -1 if unknown
	 * @param type type of operation that is progressing
	 */
	public void operationProgressed( int count, int total,  OperationType type  );
	
	/**
	 * Notified when the operation has terminated.
	 * @param type type of operation that has terminated
	 */
	public void operationTerminated( OperationType type );
	
	/**
	 * Notified when the operation has been aborted.
	 * @param type type of operation that has been aborted
	 */
	public void operationAborted( OperationType type );
}
