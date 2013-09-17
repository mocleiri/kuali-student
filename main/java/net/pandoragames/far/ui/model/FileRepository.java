package net.pandoragames.far.ui.model;

import java.io.File;

/**
 * A wrapper for a directory. Used together with a file chooser dialogue
 * to add a check if the selected directory is accepted.
 * @author Olivier Wehner at 22.03.2008
 * <!-- copyright note --> 
 */
public interface FileRepository
{
	/**
	 * Returns a file 
	 * @return non null file
	 */
	public abstract File getFile();
	
	/**
	 * Sets a new file and returns true if the file was accepted. 
	 * Null will be ignored, the method returning false.
	 * @param file file object
	 * @return true if the file was acccepted, false otherwise
	 */
	public abstract boolean setFile(File file);
	
}
