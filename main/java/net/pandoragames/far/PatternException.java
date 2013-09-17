package net.pandoragames.far;

/**
 * Exception that indicates an invalid search pattern or 
 * invalid regular expression.
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class PatternException extends RuntimeException {

    private String pattern;
    
    /**
     * Standard constructor.
     * @param pattern the pattern that caused this exception
     * @param message additional error message for logging
     */
    public PatternException(String pattern, String message) {
        super(message);
        this.pattern = pattern;
    }
    
    /**
     * Constructor for underlying <code>java.util.regex.PatternSyntaxException</code> 
     * or <code>java.text.ParseException</code>.
     * @param pattern the pattern that caused this exception
     * @param t the underlying exception
     */
    public PatternException(String pattern, Throwable t) {
        super(t.getMessage(), t);
        this.pattern = pattern;
    }

    /**
     * Returns the pattern that caused this exception.
     * @return the pattern that caused this exception
     */
    public String getPattern() {
        return pattern;
    }
}
