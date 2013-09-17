package net.pandoragames.far.ui;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.pandoragames.far.PatternException;
import net.pandoragames.far.ui.model.FileNamePattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility for simplified regular expresions. At several places, especially for the
 * selection of file names, the application will accept a <i>simple pattern</i> as
 * alternative to an ordinary regular expression. 
 * A simple pattern is a simpliefied regular expression syntax,
 * typically allowing <b>*</b> as a wild card for "any number of characters" 
 * (in contrast to <b>.*</b> in proper regular expressions). 
 * <p>
 * The default implementation for a simple pattern is 
 * {@link net.pandoragames.far.ui.SimpleFileNamePatternFAR SimpleFileNamePatternFAR},
 * but an alternative
 * {@link net.pandoragames.far.ui.SimpleFileNamePatternShell SimpleFileNamePatternShell}
 * is provided and others could be added. See the respective implementations for
 * details of the pattern syntax.
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public abstract class SimpleFileNamePattern {

    private static final char[] forbiddenCharactersInSimplePattern = new char[]{'/', '\\', ';', ':'};
    private static final char[] toBeEscapedInSimplePattern = new char[]{'.', '^', '$', '+', '-', '*', '?',
                                                                    '(', ')', '[', ']', '{', '}', '|'};
    /**
     * Set of characters that should not be tolerated in a simple pattern.
     * This set contains the characters '/','\',':' and ';'.
     * In addition, the platform file separator (/ on *X,  \ on Windows)
     * and the platform file-name separator (: on *X, ; on Windows) are not permitted.
     * Implementations should not allow characters below below u20 (blank, ascii 32) either.
     */
    protected Set<Character> forbiddenCharacters = new HashSet<Character>();
    /**
     * Set of characters that must be escaped when producing a regular expression.
     * The set contains the following characters:
     * '.', '^', '$', '+', '-', '*', '?', '(', ')', '[', ']', '{', '}' and '|'.
     */
    protected Set<Character> toBeEscaped = new HashSet<Character>();

	/**
	 * Never leave without a logger.
	 */
    protected Log logger = LogFactory.getLog( this.getClass() );
    
    private static SimpleFileNamePattern singleton = new SimpleFileNamePatternFAR();
    
    /**
     * Protected constructor for inheriting classes.
     */
    protected SimpleFileNamePattern() {
        for(char character : forbiddenCharactersInSimplePattern) {
            forbiddenCharacters.add(Character.valueOf(character));
        }
        forbiddenCharacters.add( Character.valueOf(System.getProperty("file.separator").charAt(0)) );
        forbiddenCharacters.add( Character.valueOf(System.getProperty("path.separator").charAt(0)) );
        for(char character : toBeEscapedInSimplePattern) {
            toBeEscaped.add(Character.valueOf(character));
        }    	
    }    
    
    /**
     * Returns an instance of this utility class.
     * By default this returns an instance of class
     * {@link net.pandoragames.far.ui.SimpleFileNamePatternFAR SimpleFileNamePatternFAR}.
     * This behaviour can be changed by calling the <code>setInstance()</code> method.
     * @return instance of this class
     */
    public static SimpleFileNamePattern getInstance() {
    	return singleton;
    }
    
    /**
     * Sets a new implementation of this class as to be returned by the 
     * <code>getInstance()</code> method. Null values will be ignored.
     * @param instance new default instance for <code>getInstance()</code> method.
     */
    protected static void setInstance(SimpleFileNamePattern instance) {
    	if( instance != null ) singleton = instance;
    }
    
    /**
     * Create a <code>java.util.regex.Pattern</code> from a simple string pattern. 
     * The syntax of the simple pattern is implementation dependend.
     * @param simplePattern see class description
     * @param ignoreCase should letter case be considdered
     * @return Pattern object for specified simple pattern string
     * @throws PatternException if the simple pattern is not valid
     */
    public abstract Pattern createPattern(String simplePattern, boolean ignoreCase ) throws PatternException;
    
    /**
     * Creates a regular expression Pattern from a FileNamePattern object.
     * 
     * @param pattern to be transformed into a regular expression Pattern
     * @return regular expression Pattern
     * @throws PatternSyntaxException if the FileNamePattern represents a regular expression pattern
     * but the pattern is not valid
     * @throws PatternException if the FileNamePattern represents a simple pattern
     * but the pattern is not valid
     */
    public Pattern createPattern(FileNamePattern pattern) throws PatternException {
    	if( pattern == null ) throw new NullPointerException("Fielname pattern was null");
    	if( pattern.isRegex() ) {
    		if( pattern.isIgnoreCase() ) {
    			return Pattern.compile( pattern.getPattern(), Pattern.CASE_INSENSITIVE );
    		} else {
    			return Pattern.compile( pattern.getPattern() );
    		}
    	} else {
    		return createPattern(pattern.getPattern(), pattern.isIgnoreCase() );
    	}
    }
    
    /**
     * Validates the specified FileNamePattern for syntactically correctness.
     * @param pattern to be validated
     * @return true if pattern is ok
     */
    public boolean validateFileNamePattern(FileNamePattern pattern) {
    	if(pattern == null) {
    		logger.warn("FileNamePattern object is null");
    		return false;
    	}
    	if(pattern.getPattern() == null) {
    		logger.warn("Pattern string is null");
    		return false;
    	}
		if( pattern.isRegex() ) {
			try {
				return (null != Pattern.compile( pattern.getPattern() ));
			} catch (PatternSyntaxException psx) {
				logger.info( psx.getMessage() );
				return false;
			}
		} else {
			try {
				return (null != createPattern(pattern.getPattern(), true ));
			} catch(PatternException px) {
				logger.info( px.getMessage() );
				return false;
			}
		}
	}
}
