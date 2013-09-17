package net.pandoragames.far.ui.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface for saving and loading a particular search configuration.
 * Implementing classes controle the way the form object is persisted.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public interface FormLoader
{
	/**
	 * Loads the form from a stream.
	 * @param input where to read from
	 * @return FindForm data model
	 */
	public FindForm loadForm(InputStream input) throws IOException;
	
	/**
	 * Writes the form to a stream.
	 * @param form to be saved
	 * @param output where to write to
	 */
	public void saveForm(FindForm form, OutputStream output) throws IOException;
}
