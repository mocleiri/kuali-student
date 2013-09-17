package net.pandoragames.far.ui;

import java.util.regex.Pattern;

/**
 * Data for a file filter that tests the file content.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface ContentFilter extends Cloneable {

	/**
	 * Returns the filter criteria as a java regex Pattern object.
	 * @return filter criteria
	 */
	public Pattern getContentPatternAsRegex();

	/**
	 * Returns the litteral content pattern. This pattern is looked up <i>within</i> the files.
	 * Its interpretation is implementation dependend. The method {@link #getContentPatternAsRegex() getContentPatternAsRegex()}
	 * should be called to retreive the actual regular expression pattern. However, if this method 
	 * (<code>getSearchStringContent()</code>) returns the empty string, then <code>getContentPatternAsRegex()</code>
	 * returns a Pattern that accepts any input (.*).
	 * @return content pattern, possibly empty, never null
	 */
	public String getSearchStringContent();
	
	/**
	 * Returns false if the content pattern is applied as an inclusion filter
	 * (the default). If the method returns true, files matching the
	 * pattern will be <b>excluded</b> from the selection.
	 * @return false to include files matching the content pattern, true to exclude them 
	 */
	public boolean isInvertContentFilter();

	/**
	 * Returns a copy of this object, with all interface method behaving exactly as the original instance.
	 * @return copy of this object.
	 */
	public Object clone();
}
