package net.pandoragames.far.ui;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.pandoragames.far.FileSelector;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.MessageBox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Executes {@link net.pandoragames.far.FileSelector#listFiles(Pattern, File) FileSelector.listFiles()}
 * in a thread on its own. This class is used internally by {@link UIBean UIBean}.
 * @author Olivier Wehner at 07.03.2008
 * <!-- copyright note --> 
 */
public class FileSearchThread extends Thread
{
	private FileSelector fileSelector;
	private FileFilter criteria;
	private Set<File> fileSet;
	private MessageBox messageBox;
	private Log threadLog;
	
	/**
	 * This class is used internally by {@link UIBean UIBean}.
	 * @param selector not null
	 * @param findForm not null
	 * @param messageSink not null
	 */
	FileSearchThread(FileSelector selector,  FileFilter findForm, MessageBox messageSink) {
		threadLog = LogFactory.getLog( this.getClass() );
		criteria = findForm;
		fileSelector = selector;
		messageBox = messageSink;
	}
	
	/**
	 * Executes {@link net.pandoragames.far.FileSelector#listFiles(Pattern, File) FileSelector.listFiles()}.
	 */
	public void run() {
		threadLog.info("Searching for " + criteria.getFileNamePattern() 
				+ (criteria.getFileNamePattern().isRegex() ? " (regex) " : " ")
				+ "at " + criteria.getBaseDirectory().getPath() 
				+ (criteria.isIncludeSubDirs() ? " and subdirectories" : ""));
		fileSelector.setMaxSearchDepth( criteria.isIncludeSubDirs() ? 256 : 0);
		try{
			Pattern pattern = criteria.getFileNamePatternAsRegex();
			fileSet = fileSelector.listFiles( pattern, criteria.getBaseDirectory() );
			threadLog.info( fileSet.size() + " files found");
		} catch (Exception x) {
			threadLog.error( x.getClass().getName() + ": " +x.getMessage(), x);
			messageBox.error( x.getMessage() );
			fileSet = new HashSet<File>();
		}			
	}
	
	/**
	 * Returns the search result.
	 * @return found files.
	 */
	public Set<File>  getResult() {
		return fileSet;
	}
}
