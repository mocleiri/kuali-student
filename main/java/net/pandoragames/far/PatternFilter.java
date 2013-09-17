package net.pandoragames.far;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Pattern;

import net.pandoragames.util.file.FileUtil;

/**
 * Filters for files that match a particular RegEx pattern.
 * @author Olivier Wehner at 21.02.2008
 * <!-- copyright note --> 
 */
public class PatternFilter implements FilenameFilter {
    
    private Pattern pattern;
    private File baseDir;
    private boolean subdirectories;
    private int callCount = 0;
    
    /**
     * Specifies the matching conditions for this filter. If the "directory" argument is not null,
     * only files in, or below the specified directory will match. Otherwise any file that matches
     * the pattern will match.
     * @param directory if null, parent directory will not be tested.
     * @param fileNamePattern pattern to be matched by the file name.
     * @throws IOException if the "directory" argument is not null, but does not denote an existing directory.
     */
    public PatternFilter(File directory, boolean includeSubdirectories, Pattern fileNamePattern) throws IOException {
        if(fileNamePattern == null) throw new NullPointerException("Filename pattern must not be null");
        pattern = fileNamePattern;
        baseDir = directory;
        subdirectories = includeSubdirectories;
        if((directory != null) && (!directory.isDirectory())) throw new IOException(directory.getPath() + " does not denote a directory"); 
    }
    
    /**
     * Returns true if the file matches the pattern specified with the constructor.
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept(File directory, String fileName) {
        if( fileName == null ) return false;
        
        if(( subdirectories && FileUtil.isSubdirectory(baseDir, directory ) )
        		|| (!subdirectories && 
        				((baseDir == null && directory == null) || (baseDir != null && baseDir.equals(directory))))) {
        	callCount++;
            return pattern.matcher( fileName ).matches();
        }
        
        return false;
    }
    
    /**
     * Returns the number of files recently tested by this method.
     * More precisely, the method returns the number of times the
     * <code>accept()</code> method was called
     * and resets the counter to 0. 
     * @return number of times <code>accept()</code> since last call to this method
     */
    public int getCallCount() {
    	int cc = callCount;
    	callCount = 0;
    	return cc;
    }    
}
