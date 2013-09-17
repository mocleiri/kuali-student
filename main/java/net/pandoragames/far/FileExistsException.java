package net.pandoragames.far;

import java.io.File;
import java.io.IOException;

/**
 * Thrown during a renaming operation if the target file already exists.
 * @author Olivier Wehner at 22.02.2008
 * <!-- copyright note --> 
 */
public class FileExistsException extends IOException
{

	/**
	 * Specify target file name and directory.
	 * @param name target file name
	 * @param dir directory
	 */
	public FileExistsException(String name, File dir)
	{
		super("A file with the name " + name + " already exists in " + dir.getPath());
	}

}
