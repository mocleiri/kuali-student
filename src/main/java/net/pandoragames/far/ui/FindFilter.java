package net.pandoragames.far.ui;

/**
 * Aggregates DirectoryFilter, FileNameFilter and ContentFilter.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public interface FindFilter extends FileFilter, ContentFilter {

	/**
	 * Returns a copy of this object, with all interface method behaving exactly as the original instance.
	 * @return copy of this object.
	 */
	public FindFilter clone();

}
