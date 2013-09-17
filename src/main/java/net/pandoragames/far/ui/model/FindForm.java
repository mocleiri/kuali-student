package net.pandoragames.far.ui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.pandoragames.far.PatternException;
import net.pandoragames.far.PatternFilter;
import net.pandoragames.far.ui.FindFilter;
import net.pandoragames.far.ui.SimpleFileNamePattern;

/**
 * Combines all data necessary to launch a query.
 * @author Olivier Wehner at 26.02.2008
 * <!-- copyright note --> 
 */
public class FindForm extends FARForm implements FindFilter {
	
	private boolean includeSubDirs = true;
	private FileNamePattern filePattern = new FileNamePattern("*");
	private List<PropertyChangeListener> baseDirectoryChangeListener = new ArrayList<PropertyChangeListener>();

	public FindForm(){
		super( OperationType.FIND );
	}
	/**
	 * Returns the file name pattern that should be used to filter files.
	 * @return file name pattern
	 */
	public FileNamePattern getFileNamePattern()
	{
		return filePattern;
	}
	
	/**
	 * Sets the file name pattern that should be used to filter files.
	 * @param filePattern file name pattern
	 */
	public void setFileNamePattern(FileNamePattern filePattern)
	{
		if( filePattern != null ) {
			this.filePattern = filePattern;
		}
	}
	
	/**
	 * Returns the file name pattern as a regular expression.
	 * @return file name pattern as regular expression
	 * @throws PatternException if the pattern string is invalid
	 */
	public Pattern getFileNamePatternAsRegex() throws PatternException {
		if( filePattern.isRegex() ) {
			try {
				return Pattern.compile( filePattern.getPattern(), Pattern.CASE_INSENSITIVE );
			} catch (PatternSyntaxException psx) {
				throw new PatternException( filePattern.getPattern(), psx.getMessage() );
			}
		} else {
			return SimpleFileNamePattern.getInstance().createPattern(filePattern.getPattern(), true);
		}
	}
	
	/**
	 * Should the file search include subdirectories.
	 * @return include subdirectories
	 */
	public boolean isIncludeSubDirs()
	{
		return includeSubDirs;
	}
	
	/**
	 * Set to false to exclude files in subdirectories.
	 * @param includeSubDirs include subdirectories in file search
	 */
	public void setIncludeSubDirs(boolean includeSubDirs)
	{
		this.includeSubDirs = includeSubDirs;
	}
	
	
	/**
	 * {@inheritDoc} <br>
	 * This method additionally calls any registered property change listener.
	 * @param baseDirectory {@inheritDoc}
	 */
	public void setBaseDirectory(File baseDirectory)
	{
		File old = getBaseDirectory();
		super.setBaseDirectory(baseDirectory);
		if((old == null && baseDirectory != null)
			|| (old != null && ! old.equals(baseDirectory))) {
			PropertyChangeEvent event = new PropertyChangeEvent( this, BD_PROPERTY_NAME, old, baseDirectory);
			for(PropertyChangeListener pcl : baseDirectoryChangeListener) {
				pcl.propertyChange( event );
			}
		}
	}

	/**
	 * Updates this form with the data from the "form" parameter.
	 * Inheriting classes must take care to call this method
	 * <b>after</b> they have performed the update.
	 * @param form data to replace the content of this form
	 */
	public void update(FindForm form) {
		filePattern = form.filePattern;
		includeSubDirs = form.includeSubDirs;
		super.update( form );
	}

	
// -- functional methods ----------------------------------------------------------------------------
	
	/**
	 * Adds a PropertyChangeListener for the BaseDirectory property.
	 * @param pcl PropertyChangeListener to be added
	 */
	public void addBaseDirectoryListener(PropertyChangeListener pcl) {
		if(pcl != null) baseDirectoryChangeListener.add( pcl );
	}
	
	/**
	 * Removes a PropertyChangeListener.
	 * @param pcl PropertyChangeListener to be removed
	 */
	public void removeBaseDirectoryListener(PropertyChangeListener pcl) {
		if(pcl != null) baseDirectoryChangeListener.remove( pcl );
	}
	
	/**
	 * Returns true if the properties "baseDirectory" and "filePattern" of both FindForm instances
	 * are non null and equal, together with their respective flags. Returns false if one (of the four)
	 * properties has not the same value in both forms. 
	 */
	public boolean sameBaseSearch(FindForm otherForm){
		if(otherForm == null) return false;
		if(( getBaseDirectory() == null ) || (! getBaseDirectory().equals( otherForm.getBaseDirectory() ))) return false;
		if( includeSubDirs != otherForm.isIncludeSubDirs() ) return false;
		if(( filePattern == null ) || (! filePattern.equals( otherForm.getFileNamePattern()))) return false;
		return true;
	}
	
	/**
	 * Returns a PatternFilter for this FindForm. If the currently specified file pattern
	 * is valid, the filter will accept only files that comply with this find form (ignoring
	 * any content pattern that might be specified). If the currently specified file pattern
	 * is <i>not</i> valid, an accept all filter will be returned.
	 * @return PatternFilter for this FindForm if file name pattern is valid, accept all 
	 * filter otherwise.
	 */
	public PatternFilter createPatternFilter() {
		try {
			return new PatternFilter( this.getBaseDirectory(), this.isIncludeSubDirs(), this.getFileNamePatternAsRegex() );
		} catch(Exception x) {
			try {
				return new PatternFilter( null, true, Pattern.compile(".*") );
			} catch(IOException iox) {
				throw new IllegalStateException("Can not instantiate fallback PatternFilter");
			}
		}
	}

	
	/**
	 * Returns a copy of this object.
	 * @see java.lang.Object#clone()
	 */
	public FindForm clone() {
		FindForm clown = (FindForm) super.clone();
		clown.baseDirectoryChangeListener = new ArrayList<PropertyChangeListener>();
		clown.filePattern = (FileNamePattern) filePattern.clone();
		return clown;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if(! super.equals(o)) return false;
		try {
			FindForm form = (FindForm) o;
			class Diff {
				private boolean different(Object a, Object b) {
					if( a == null ) {
						return (b != null);
					} else {
						return ! a.equals( b );
					}
				}
			}
			Diff diff = new Diff();
			if( includeSubDirs != form.includeSubDirs ) return false;
			if( diff.different( filePattern, form.filePattern ) ) return false;
			return true;
		} catch (ClassCastException ccx) {
			return false;
		}
	}
		
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = super.hashCode();
		hash = hash * 31 + ( includeSubDirs ? 1 : 0 );
		hash = hash * 31 + ( filePattern != null ? filePattern.hashCode() : 0 );
		return hash;		
	}
}
