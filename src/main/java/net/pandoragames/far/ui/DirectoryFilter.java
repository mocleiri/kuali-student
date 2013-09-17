package net.pandoragames.far.ui;

import java.io.File;

/**
 * Data for a file filter that tests the files directory.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface DirectoryFilter {

	/**
	 * Returns the base directory. Only files in this directory
	 * (or below if the includeSubDirs flag is set) will pass the filter.
	 *  
	 * @return base directory
	 */
	public File getBaseDirectory();
	
	
	/**
	 * Should files in subdirectories of the base directory pass the filter?
	 * @return include subdirectories
	 */
	public boolean isIncludeSubDirs();

}
