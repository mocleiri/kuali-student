package net.pandoragames.far.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import net.pandoragames.far.ui.model.FileNamePattern;
import net.pandoragames.util.file.ObjectListFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Loads and saves the FAR configuration. 
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class FARConfigurationFactory
{
	private static final String preferencesFileName = "far.properties";
	private static final String fileNamePatternList = "fnpattern.list";
	private static final String charactersetList = "characterset.list";	
	private Log logger;
	
	/**
	 * Default constructor.
	 */
	public FARConfigurationFactory() {
		logger = LogFactory.getLog(this.getClass());
	}
	
	/**
	 * Adds stored information to the configuration object.
	 * @param config to be initialised.
	 */
	public void loadConfig(FARConfig config) {
		Properties preferences = getPreferences();
		config.loadFromProperties( preferences );
		config.setPatternList( getFileNamePattern() );
		config.setCharsetList( getSupportedCharacterSets() );
		config.setDefaultCharset( config.getCharsetList().get(0) );
		config.setConfigDir( getConfigDir() );
	}
	
	/**
	 * Saves the configuration object and returns true if the operation was successfull.
	 * @param config to be saved.
	 * @return true if ok
	 */
	public boolean saveConfig(FARConfig config) {
		boolean success = true;
		//
		//	store the properties
		//
		Properties properties = new Properties();
		File preferences = new File(getConfigDir(), preferencesFileName);
		success = findOrCreate( preferences );
		// write it
		if(preferences.exists()) {
			OutputStream outputStream = null;
			config.writeToProperties( properties );
			try {
				outputStream = new BufferedOutputStream( new FileOutputStream( preferences ) );
				writeCommentHeader( outputStream, "FAR - Find And Replace" );
				properties.store( outputStream, null );
			} catch (IOException iox) {
				logger.error("IOException saving preferences: " + iox.getMessage());
				success = false;	
			} finally {
				if(outputStream != null) try { outputStream.close(); } 
				catch (IOException iox) { logger.warn("IOException closing stream: " + iox.getMessage() );}
			}
		}
		//
		// store the pattern
		//
		File storedList = new File(getConfigDir(), fileNamePatternList);	
		success = success & findOrCreate( storedList );
		if(storedList.exists()) {
			FileNamePatternParser parser = new FileNamePatternParser( storedList );
			try{
				parser.storeList(config.getFileNamePatternList(), "FAR - Find And Replace\nFilename Pattern");
			} catch (IOException iox) {
				logger.error("IOException saving file name pattern: " + iox.getMessage());
				success = false;	
			}
		}
		//
		// store the character set
		//
		File characterList = new File(getConfigDir(), charactersetList);	
		success = success & findOrCreate( characterList );
		if(characterList.exists()) {
			CharacterSetListParser parser = new CharacterSetListParser( characterList );
			try{
				if( config.getCharsetList().contains(config.getDefaultCharset())
					&& (! config.getDefaultCharset().equals(config.getCharsetList().get(0)))) {
					config.getCharsetList().remove(config.getDefaultCharset());
					config.getCharsetList().add(0, config.getDefaultCharset());
				}
				parser.storeList(config.getCharsetList(), "FAR - Find And Replace\nCharacter Sets");
			} catch (IOException iox) {
				logger.error("IOException storing list of character sets: " + iox.getMessage());
				success = false;	
			}
		}		
		return success;
	}
		
	/**
	 * Finds and returns the configuration directory (for this user).
	 * Creates it if necessary. The standard for this is 
	 * <code>$USER_HOME/.net/pandoragames/FAR</code>
	 * @return configuration directory.
	 */
	protected File getConfigDir() {
		File userHome = new File( System.getProperty("user.home") );
		File configDir = new File( userHome, ".net/pandoragames/FAR");
		if(( ! configDir.isDirectory() ) && ( ! configDir.mkdirs() )) {
			throw new ConfigurationException("Configuration directory " + configDir.getPath() + " could not be created");
		}
		return configDir;
	}
	
	/**
	 * Loads and returns the user preferences in a Properties object.
	 * The returned Properties object should contain the following entries:
	 * <ul>
	 * <li><b>far.basedir</b>: directory path
	 * <li><b>far.backupdir</b>: directory path
	 * </ul>
	 * It may be empty, but it must not be null
	 * @return non null Properties object containing user preferences.
	 */
	private Properties getPreferences() {
		Properties properties = new Properties();
		File preferences = new File(getConfigDir(), preferencesFileName);
		if(preferences.isFile()) {
			InputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream( new FileInputStream( preferences ) );
				properties.load( inputStream );
			} catch (IOException iox) {
				logger.error("IOException loading preferences: " + iox.getMessage());
			} finally {
				if(inputStream != null) try { inputStream.close(); } 
				catch (IOException iox) { logger.warn("IOException closing stream: " + iox.getMessage() );}
			}
		}
		return properties;
	}
	
	/**
	 * Restores the saved pattern list from the file system, or returns the default list.
	 * @return list of file name patterns
	 */
	private List<FileNamePattern> getFileNamePattern() {
		File storedList = new File(getConfigDir(), fileNamePatternList);
		if( storedList.isFile() ) {
			FileNamePatternParser parser = new FileNamePatternParser( storedList );
			try{
				return parser.loadList();
			} catch (IOException iox) {
				logger.error("IOException loading file name pattern: " + iox.getMessage());
				return getDefaultFilenamePatternList();
			}
		} else {
			return getDefaultFilenamePatternList();
		}
	}
	
	/**
	 * Returns the character sets in the character set configuration file.
	 * @return character sets stored so far
	 */
	private List<Charset> getSupportedCharacterSets() {
		File storedList = new File(getConfigDir(), charactersetList);
		if( storedList.isFile() ) {
			CharacterSetListParser parser = new CharacterSetListParser( storedList );
			try{
				return parser.loadList();
			} catch (IOException iox) {
				logger.error("IOException loading supportd character sets: " + iox.getMessage());
			}
		}
		
		// if unable to load, return default
		List<Charset> defaultResult = new ArrayList<Charset>();
		defaultResult.add( Charset.defaultCharset() );
		
		// ensure at least ascii and utf-8 are contained
		loadCharset(defaultResult, "UTF-8");
		loadCharset(defaultResult, "US-ASCII");

		return defaultResult;
	}
	
	/**
	 * Returns the default file name pattern list to start with.
	 * @return default file name pattern list
	 */
	private List<FileNamePattern> getDefaultFilenamePatternList() {
		return Arrays.asList( new FileNamePattern[] {new FileNamePattern("*.txt"), 
				new FileNamePattern("*.xml"),
				new FileNamePattern("*.html, *.htm"),
				new FileNamePattern("\\w{1,8}(\\.\\w{1,3})?", true)});	
	}

	/**
	 * Makes sure the specified file exists.
	 * @param file to be created if not existing
	 * @return true if file exists, fals if it could not be created
	 */
	private boolean findOrCreate(File file) {
		if(! file.exists() ) {
			try{
				if( ! file.createNewFile() ) {
					logger.warn("Could not create file " + file.getPath() );
					return false;
				} else {
					return true;
				}
			} catch (IOException iox) {
				logger.warn("IOException creating file " + file.getPath() +": " + iox.getMessage() );
				return false;				
			}
		} else {
			return true;
		}
	}
	
	/**
	 * Adds a decent header for property files
	 * @param stream to be written to
	 * @param comment may span multiple lines
	 * @throws IOException in case of
	 */
	private void writeCommentHeader(OutputStream stream, String comment) throws IOException {
		Writer writer = new OutputStreamWriter( stream, "ISO-8859-1" );
		String[] commentLines = comment.split("\\n");
		writer.write("##\n");
		for( String line : commentLines ){
			writer.write("# ");
			writer.write( line );
			writer.write("\n");
		}
		writer.write("##\n");
		writer.flush();
	}
	
	/**
	 * Loads a Character set into a list unless it is indeed the default charset.
	 * Does the necessary error handling arround. Used for loading default charsets.
	 * @param defaultResult list to take the additional character set.
	 * @param charsetName charset to be loaded
	 */
	private void loadCharset(List<Charset> charsetList, String charsetName) {
		try {
			if( !Charset.defaultCharset().name().equals(charsetName) ) {
				charsetList.add( Charset.forName(charsetName) );
			}
		} catch (Exception x) {
			logger.error(x.getClass().getName() + " loading character set " + charsetName 
					+ ":" + x.getMessage());
		}

	}
	
	class FileNamePatternParser extends ObjectListFile<FileNamePattern> {
		public FileNamePatternParser(File file) {
			super(file);
		}
		/**
		 * Parses a String into a FileNamePattern if possible. Returns null
		 * if the String can not be parsed. This is the invers method to
		 * {@link #formatFNPattern(FileNamePattern)}.
		 * @param pattern to be parsed
		 * @return FileNamePattern or null
		 */
		public FileNamePattern parse(String pattern) {
			if(( pattern == null ) || ( pattern.length() < 3 ) 
				|| ( pattern.indexOf(":") != 1) || ( pattern.length() > (256+2) )) return null;
			boolean isRegex = ( pattern.charAt( 0 ) == 'R' );
			return new FileNamePattern( pattern.substring(2),  isRegex );
		}
		
		/**
		 * Formats a FileNamePattern instance into a String. The inverse method
		 * to {@link #parserFNPattern(String)}.
		 * @param pattern to be formated.
		 * @return pattern as String (or the empty String if pattern is null)
		 */
		public String format(FileNamePattern pattern) {
			if(( pattern == null ) || (pattern.getPattern().length() > 256)) return "";
			StringBuffer buffer = new StringBuffer( (pattern.isRegex()) ? "R" : "S" );
			buffer.append(":").append( pattern.getPattern() );
			return buffer.toString();
		}
	}
	
	class CharacterSetListParser extends ObjectListFile<Charset> {
		public CharacterSetListParser(File file) {
			super(file);
		}
		public Charset parse(String charsetName ) {
			try {
				return Charset.forName( charsetName );
			} catch(Exception x) {
				logger.warn("Could not load character set " + charsetName + " from file " + getFile().getName() 
							 		+ " due to " + x.getClass().getName() + ": " + x.getMessage());
				return null;
			}
		}
		public String format(Charset charset) {
			return charset.name();
		}
	}
}
