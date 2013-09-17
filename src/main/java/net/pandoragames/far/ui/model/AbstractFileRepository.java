package net.pandoragames.far.ui.model;

import java.io.File;

import net.pandoragames.far.ui.FARConfig;

/**
 * Base class for FileRepository implementations that reference 
 * the find form and replace form models.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public abstract class AbstractFileRepository implements FileRepository {

	/** Protected member for inheriting classes */
	protected FindForm findForm; 
	/** Protected member for inheriting classes */
	protected ReplaceForm replaceForm;
	/** Protected member for inheriting classes */
	protected FARConfig farconfig;
	/** Protected member for inheriting classes */
	protected MessageBox messageBox;
	
	/**
	 * Constructor takes both forms and a FARConfig instance.
	 * @param cfg FAR configuration.
	 * @param find data of the find form
	 * @param replace data of the replace form
	 */
	public AbstractFileRepository(FARConfig cfg, FindForm find, ReplaceForm replace, MessageBox errorsink) {
		farconfig = cfg;
		findForm = find;
		replaceForm = replace;
		messageBox = errorsink;
	}

	/**
	 * Returns true if test is a subdirectory of base.
	 * @param base base directory
	 * @param test potential subdirectory
	 * @return true if test is a subdirectory of base
	 */
	protected boolean isSubdirectory( File base, File test ) {
		return test.getPath().startsWith( base.getPath() );
	}

}
