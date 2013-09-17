package net.pandoragames.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes lists of Strings from and to files. This utility class spares the 
 * construction of streams and readers to handle lists stored in files. Respective files
 * are generally UTF-8 encoded. Each string will be read from (writtten to) a line of its own. 
 * However, strings with line break may still be stored as they will be escaped with "\n".<p>
 * 
 * Whitespace at both ends of the strings will be striped when reading and writing. An empty
 * string will be stored as a single "\" backslash character, while null values will
 * generally be ignored when writing.<p>
 * 
 * A line that starts with a "#" hash character is considdered a comment. Comment lines are
 * ingored when reading, but you may include them in the file by adding them to the list.<br>
 * @author Olivier Wehner at 29.03.2008
 * <!-- copyright note --> 
 */
public class ListFile
{
	private static final String UTF8 = "UTF-8";
	private static final String LB = System.getProperty("line.separator");
	private File myFile;
	
	public ListFile(File file) {
		if( file == null ) throw new NullPointerException("File must not be null");
		myFile = file;
	}
	
	/**
	 * Returns the file this class was instantiated with.
	 * @return underlying file object.
	 */
	public File getFile() {
		return myFile;
	}
	
	/**
	 * Loads the list from the file specified with the constructor. Creates the file
	 * if it does not exitst. See class comment for further details.
	 * @return list loaded from file
	 * @throws IOException if the list could not be written for any reason
	 */
	public List<String> load() throws IOException {
		if( myFile.isFile() ) {
			BufferedReader reader = null;
			List<String> result = new ArrayList<String>();
			try {
				reader = new BufferedReader(new InputStreamReader( new FileInputStream( myFile ), UTF8 ) );
				String line = null;
				do {
					line = reader.readLine();
					if( line != null ) {
						line = line.trim();
						if((line.length() > 0) && (line.charAt(0) != '#')) {
							if("\\".equals(line)) {
								result.add("");
							} else {
								line = line.replaceAll("(^|[^\\\\])\\n", "\n");
								line = line.replaceAll("\\\\{2}", "\\\\");
								result.add( line );
							}
						}
					}
				} while( line != null );
				return result;
			} finally {
				if(reader != null) reader.close();
			}
		} else {
			throw new FileNotFoundException("The specified file " + myFile.getPath() + " could not be found");
		}
	}

	/**
	 * Stores the specified list in the file supplied with the constructor.  Creates the file
	 * if it does not exitst, rewrites it if does exist. Thus this method is equivalent to calling
	 * <code>store( list, <b>false</b> )</code>. See class comment for further details. 
	 * @param list to be saved to the file system
	 */
	public void store(List<String> list) throws IOException {
		store(list, false);
	}

	/**
	 * Stores the specified list in the file supplied with the constructor.  Creates the file
	 * if it does not exitst. See class comment for further details.
	 * @param list to be saved to the file system
	 * @param append set to true to append to the specified file
	 */
	public void store(List<String> list, boolean append) throws IOException {
		if(! myFile.exists()) {
			if(! myFile.createNewFile()) throw new IOException("Could not create " + myFile.getName() 
																									+ " at " + myFile.getParentFile().getPath());
		}
		if(myFile.isFile() && myFile.canWrite()) {
			Writer output = null;
			try {
				output = new BufferedWriter(new OutputStreamWriter( new FileOutputStream( myFile, append ), UTF8 ) );
				for(String line : list) {
					line = line.trim();
					line = line.replaceAll("\\\\", "\\\\\\\\");
					line = line.replaceAll("\\n", "\\\\n");
					output.write( line );
					output.write( LB );
				}
				output.flush();
			} finally {
				if(output != null) output.close();  
			}
		} else {
			throw new FileNotFoundException("The specified path " + myFile.getPath() + " does not point to a writable file");			
		}
	}
}
