package net.pandoragames.far.ui;

import java.util.regex.Pattern;

import net.pandoragames.far.PatternException;
import net.pandoragames.far.PatternFilter;
import net.pandoragames.far.ui.model.FileNamePattern;

/**
 * Data for an abstract file name filter.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface FileNameFilter extends Cloneable {

	/**
	 * Returns a PatternFilter for this FileNameFilter. If the currently specified file pattern
	 * is valid, the filter will accept only files that comply with this pattern. 
	 * If the currently specified file pattern
	 * is <i>not</i> valid, an accept all filter will be returned.
	 * @return PatternFilter for this FileNameFilter if file name pattern is valid, accept all 
	 * filter otherwise.
	 */
	public PatternFilter createPatternFilter();
	
	/**
	 * Returns the file name pattern that should be used to filter files.
	 * @return file name pattern
	 */
	public FileNamePattern getFileNamePattern();

	/**
	 * Returns the file name pattern as a regular expression.
	 * @return file name pattern as regular expression
	 * @throws PatternException if the pattern string is invalid
	 */
	public Pattern getFileNamePatternAsRegex() throws PatternException;

	/**
	 * Returns a copy of this object, with all interface method behaving exactly as the original instance.
	 * @return copy of this object.
	 */
	public Object clone();

}
