package net.pandoragames.far.ui.model;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Common base class for FAR data models. Each tab of the FAR GUI is backed
 * by a specialized form class. The form receives data from the UI and serves as
 * "protocol" for the backend.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public abstract class OperationForm implements Serializable, Cloneable, Resetable {
	
	/** Used for PropertyChangeListener. */
	protected static final String BD_PROPERTY_NAME = "baseDirectory";
	
	/**
	 * The group reference indicator used in replacement strings.
	 * This class uses the backslash ('\') as default.
	 */
	protected char groupReference = '\\';
	
	// base directory for relative pathes
	private File baseDirectory;
	// default character set for file manipulation
	private Charset characterset;
	// shortcut for instanceof
	private OperationType type = OperationType.NONE;
	// listener for form updates
	private List<FormUpdateListener> listenerList;

	protected OperationForm(OperationType operation) {
		type = operation;
	}
	
	/**
	 * Returns the type of this form.
	 * This is a convinience form of the instanceof operator,
	 * suitable for switch-case.
	 * @return type of form, one of the constants of this class
	 */
	public OperationType getType() {
		return type;
	}
	
	/**
	 * Returns the base directory. The base directory is used
	 * for the construction of search and backup trees.
	 * @see net.pandoragames.far.FileMatcher#getBaseDirectory()
	 * @return base directory for file operations
	 */
	public File getBaseDirectory()
	{
		return baseDirectory;
	}
	
	/**
	 * Sets the base directory. The base directory is used
	 * for the construction of search and backup trees.
	 * @see net.pandoragames.far.FileMatcher#getBaseDirectory()
	 * @param baseDirectory base directory for file operations
	 */
	public void setBaseDirectory(File baseDirectory)
	{
		this.baseDirectory = baseDirectory;
	}

	/**
	 * Returns the default character set used for reading (and writing) files.
	 * @return default character set for reading and writing
	 */
	public Charset getCharacterset()
	{
		return characterset;
	}
	
	/**
	 * Sets the character set used for reading (and writing) files.
	 * @param characterset file chararcter set
	 */
	public void setCharacterset(Charset characterset)
	{
		this.characterset = characterset;
	}	
	
	/**
	 * Returns the group reference indicator used in replacement strings.
	 * This class uses the backslash ('\') as default.
	 * @see net.pandoragames.far.ReplacementString
	 * @return group reference indicator
	 */
	public char getGroupReference()
	{
		return groupReference;
	}

	/**
	 * Sets the group reference indicator used in replacement strings.
	 * @see net.pandoragames.far.ReplacementString
	 * @param groupReferenceIndicator group reference indicator
	 */
	public void setGroupReference(char groupReferenceIndicator)
	{
		groupReference = groupReferenceIndicator;
	}

	
	/**
	 * Updates this form with the data from the "form" parameter.
	 * Inheriting classes must take care to call this method
	 * <b>after</b> they have performed the update.
	 * @param form data to replace the content of this form
	 */
	protected void update(OperationForm form) {
		baseDirectory = form.baseDirectory;
		characterset = form.characterset;
		groupReference = form.groupReference;
		fireFormUpdateEvent();
	}
	
	/**
	 * Adds a Listener that will be informed whenever this form is
	 * updated through method {@link #update(OperationForm) update()}.
	 * The listener will be called <i>after</i> the fields of this
	 * form have been updated.
	 * @param listener to be informed whenever this form is updated.
	 */
	public void addFormUpdateListener(FormUpdateListener listener) {
		if( listener == null ) return;
		if( listenerList == null ) listenerList = new ArrayList<FormUpdateListener>();
		listenerList.add( listener );
	}
	/**
	 * Removes a previously registered update listener.
	 * @param listener to be removed
	 */
	public void removeFormUpdateListener(FormUpdateListener listener) {
		if( listener != null && listenerList != null ) listenerList.remove( listener );
	}
	
	// default access, so it can be triggered form FormPropertySet
	void fireFormUpdateEvent() {
		if( listenerList == null ) return;
		FormUpdateEvent event = new FormUpdateEvent( this );
		for(FormUpdateListener listener : listenerList) {
			listener.formUpdated( event );
		}
	}
	
//-- equals & hashcode -------------------------------------------------------------------------

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
	 * Only the base directory is taken into account for equals!
	 */
	public boolean equals(Object o) {
		if( o == null ) return false;
		try {
			OperationForm form = (OperationForm) o;
			if( baseDirectory == null ) return (form.baseDirectory == null);
			return baseDirectory.equals( form.baseDirectory );
		} catch (ClassCastException ccx) {
			return false;
		}
	}
		
	/**
	 * Only the base directory is taken into account for the hash code!
	 */
	public int hashCode() {
		return ((baseDirectory != null) ? baseDirectory.hashCode() : 0);
	}
}
