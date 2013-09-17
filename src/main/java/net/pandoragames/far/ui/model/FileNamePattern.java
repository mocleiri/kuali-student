package net.pandoragames.far.ui.model;

import java.io.Serializable;

/**
 * Search pattern for file names. The pattern can be a regular expression or a 
 * simple pattern. A simple pattern is a simpliefied regular expression syntax,
 * typically allowing <b>*</b> as a wild card for "any number of characters" 
 * (in contrast to <b>.*</b> in proper regular expressions). 
 * See the implementations of 
 * {@link net.pandoragames.far.ui.SimpleFileNamePattern SimpleFileNamePattern}
 * for available implementations and their syntax.
 * @see net.pandoragames.far.ui.SimpleFileNamePatternFAR
 * @see net.pandoragames.far.ui.SimpleFileNamePatternShell
 * @author Olivier Wehner at 28.02.2008
 * <!-- copyright note --> 
 */
public class FileNamePattern implements Serializable, Cloneable
{
	private String pattern;
	private boolean isRegex = false;
	private boolean ignoreCase = true;
	
	/**
	 * By default a pattern is a simple pattern. Change the
	 * respective flag to mark this pattern as a regular expression.
	 * @param simplePattern pattern for this class.
	 */
	public FileNamePattern(String simplePattern) {
		this( simplePattern, false);
	}

	/**
	 * Allows to create a regex pattern or a simple pattern.
	 * @param pattern regex or simple
	 * @param isRegex true if the pattern should be treated as regular expression
	 */
	public FileNamePattern(String pattern, boolean isRegex) {
		if(pattern == null) throw new NullPointerException("Pattern must not be null");
		this.pattern = pattern;
		this.isRegex = isRegex;
	}

	/**
	 * Returns true if this pattern represents a regular expression,
	 * false if it is a simple pattern.
	 * @return true if pattern is regular expression
	 */
	public boolean isRegex()
	{
		return isRegex;
	}
	/**
	 * Set to true if this pattern represents a regular expression,
	 * false if it is a simple pattern.
	 * @param isRegex true if pattern is regular expression
	 */
	public void setRegex(boolean isRegex)
	{
		this.isRegex = isRegex;
	}
	/**
	 * Returns the pattern.
	 * @return pattern
	 */
	public String getPattern()
	{
		return pattern;
	}
	/**
	 * Sets the  pattern
	 * @param pattern
	 */
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
		if( pattern == null ) this.pattern = "";
	}
	
	/**
	 * Returns true if letter case should be ignored 
	 * (this is the default).
	 * @return true if letter case should be ignored 
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * Specify if letter case should be ignored. 
	 * The default behaviour is to indeed ignore the letter case.
	 * @param ignoreCase true if letter case should be ignored 
	 */
	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Returns the pattern.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return pattern;
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
	 * Returns true if pattern and hashcode flag are the same.
	 * @return true if o is the same pattern as this one
	 */
	public boolean equals(Object o) {
		if(o == null) return false;
		try {
			FileNamePattern other = (FileNamePattern) o;
			return pattern.equals( other.pattern ) 
						&& (isRegex == other.isRegex) 
						&& (ignoreCase == other.ignoreCase);
		} catch (ClassCastException ccx) {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		int hash = isRegex ? pattern.hashCode() : - pattern.hashCode();
		hash = hash + (ignoreCase ? 1 : 2);
		return hash;
	}
}
