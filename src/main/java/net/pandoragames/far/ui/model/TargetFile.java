package net.pandoragames.far.ui.model;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Data container for the files in a result list. Files in that list may have errors, be subject to 
 * name changes and selection or be excluded from a subset.  
 * @author Olivier Wehner at 26.02.2008
 * <!-- copyright note --> 
 */
public class TargetFile implements MessageBox, Cloneable
{
	private boolean selected = true;
	private boolean included = true;
	private File file;
	private String newName;
	private String infoMessage;
	private String errorMessage;
	private Charset characterSet;
	
	/**
	 * Constructor requires a non null file object.
	 * @param file not null
	 */
	public TargetFile(File file) {
		if( file == null) throw new NullPointerException("File must not be null");
		this.file = file;
	}
	/**
	 * Returns the file name. This is a read only property.
	 * @return file name
	 */
	public String getName() {
		return file.getName();
	}
	
	/**
	 * Returns the files directory path. This is a read only property.
	 * @return directory path
	 */
	public String getPath() {
		return file.getParentFile().getPath();
	}
	/**
	 * Returns the error message if any.
	 * @return error message or null
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}
	/**
	 * Returns the info message if any.
	 * @return informative message or null
	 */
	public String getInfoMessage()
	{
		return infoMessage;
	}
	/**
	 * Sets an error message for this file.
	 * @param errorMessage error message
	 */
	public void error(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	/**
	 * Sets an info message for this file.
	 * @param message informative message
	 */
	public void info(String message) {
		infoMessage = message;
	}
	/**
	 * Clears all messages.
	 */
	public void clear() {
		errorMessage = null;
		infoMessage = null;
		newName = null;
	}
	/**
	 * Returns the underlying <code>java.io.File</code> object.
	 * @return underlying <code>File</code> object
	 */
	public File getFile()
	{
		return file;
	}
	/**
	 * Returns true (the default) if this file object has bee selected.
	 * @return true if selected
	 */
	public boolean isSelected()
	{
		return selected;
	}
	/**
	 * Selects or deselects this object. By dafault a TargetFile is selected.
	 * @param select true if this file object should be selected.
	 */
	public void setSelected(boolean select)
	{
		this.selected = select;
	}
	/**
	 * Returns a planned new name if any was set.
	 * @return new name property
	 */
	public String getNewName()
	{
		return newName;
	}
	/**
	 * Sets the new name property of this object.
	 * This has no direct programatic consequences.
	 * @param newName property value
	 */
	public void setNewName(String newName)
	{
		this.newName = newName;
	}
	/**
	 * Returns true if this file should be considdered part of 
	 * a subset in the same collection. Returns true by default.
	 * @return true if part of subset
	 */
	public boolean isIncluded()
	{
		return included;
	}
	/**
	 * Includes or excludes this object in a subset of the same
	 * collection this instance was taken from. 
	 * @param included set to false to excluide
	 */
	public void setIncluded(boolean included)
	{
		this.included = included;
	}
	
	/**
	 * Returns the character set of this file, if any was specified.
	 * Returns null if the default character set should be used.
	 * @return character set or null
	 */
	public Charset getCharacterset()
	{
		return characterSet;
	}
	/**
	 * Specifies a character set for this file.
	 */
	public void setCharacterset(Charset charaset)
	{
		characterSet = charaset;
	}
	
	/**
	 * Returns a copy of this object.
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch(CloneNotSupportedException cnsx) {
			throw new IllegalStateException("A clown refused to clone!");
		}
	}	
	
	/**
	 * Two target files are equal if the wraped files are equal.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if( o == null ) return false;
		try {
			TargetFile other = (TargetFile) o;
			return this.file.equals( other.file );
		} catch(ClassCastException ccx) {
			return false;
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return file.hashCode();
	}
	
}
