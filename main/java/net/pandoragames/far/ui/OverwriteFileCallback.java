package net.pandoragames.far.ui;

import java.io.File;

/**
 * A callback handler used by the 
 * {@link UIFace#rename(java.util.List, OverwriteFileCallback) UIFace.rename()}
 * operation.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface OverwriteFileCallback {

	
	/**
	 * Called to request user feedback. Implementations should display a question like
	 * "A file called 'toBeReplaced' already exists. Do you want to replace it?" to the user.
	 * @param toBeReplaced a file carrying the same name as the file currently to be renamed
	 * @return true if the file should be replaced, false otherwise
	 */
	public boolean askForOverwrite(File toBeReplaced);
}
