package net.pandoragames.far.ui.model;

import java.io.File;
import java.util.Properties;

/**
 * Saves form data into property sets and vice versa.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public abstract class FormPropertySet<O extends OperationForm> {

	private static final FindFormPropertySet findFormSet = new FindFormPropertySet();
	
	private static final ReplaceFormPropertySet replaceFormSet = new ReplaceFormPropertySet();
	
	/**
	 * Reads the properties from the map into the form object.
	 * @param form to be updated withe the map
	 * @param properties where to read the data from
	 */
	public abstract void load(O form, Properties properties);
	
	/**
	 * Write the form data as properties.
	 * @param form to be saved as properties map
	 * @param properties where to write the properties to
	 */
	public abstract void store(O form, Properties properties);
	
	/**
	 * Returns an instance for a find form.
	 * @return FindForm instance
	 */
	public static FormPropertySet<FindForm> getFindFormPropertySet() {
		return findFormSet;
	}
	
	/**
	 * Returns an instance for a replace form.
	 * @return ReplaceForm instance
	 */
	public static FormPropertySet<ReplaceForm> getReplaceFormPropertySet() {
		return replaceFormSet;
	}
}

class FindFormPropertySet extends FormPropertySet<FindForm> {

	private static final String BASEDIR = "baseDirectory";
	private static final String SUBDIRFLAG = "includeSubDirs";
	
	private static final String FILENAMEPATTERN = "fileNamePattern";
	private static final String FILENAMEPATTERN_REGEX = "fnpRegex";

	private static final String SEARCHSTRING = "searchRegexString";
	private static final String ISREGEX = "regexContentPattern";
	private static final String ISINVERT = "invertContentFilter";
	private static final String IGNORECASE = "ignoreCase";

	public void load(FindForm form, Properties properties) {
		if( properties.containsKey(BASEDIR) ) form.setBaseDirectory( new File(properties.getProperty(BASEDIR)) );
		if( properties.containsKey(SUBDIRFLAG) ) form.setIncludeSubDirs( Boolean.parseBoolean( properties.getProperty(SUBDIRFLAG) ));
		if( properties.containsKey(FILENAMEPATTERN) ) {
			FileNamePattern pattern = new FileNamePattern( properties.getProperty(FILENAMEPATTERN), 
											Boolean.parseBoolean( properties.getProperty(FILENAMEPATTERN_REGEX)) );
			form.setFileNamePattern(pattern);
		}
		if( properties.containsKey(SEARCHSTRING) ) form.setSearchStringContent(properties.getProperty(SEARCHSTRING));
		if( properties.containsKey(ISREGEX) ) form.setRegexContentPattern(Boolean.parseBoolean( properties.getProperty(ISREGEX) ));
		if( properties.containsKey(IGNORECASE) ) form.setIgnoreCase(Boolean.parseBoolean( properties.getProperty(IGNORECASE) ));
		if( properties.containsKey(ISINVERT) ) form.setInvertContentFilter(Boolean.parseBoolean( properties.getProperty(ISINVERT) ));
		form.fireFormUpdateEvent();
	}
	
	public void store(FindForm form, Properties properties) {
		properties.setProperty(BASEDIR,  form.getBaseDirectory().getAbsolutePath() );
		properties.setProperty(SUBDIRFLAG, String.valueOf(form.isIncludeSubDirs() ));
		properties.setProperty(FILENAMEPATTERN,  form.getFileNamePattern().getPattern() );
		properties.setProperty(FILENAMEPATTERN_REGEX,  String.valueOf(form.getFileNamePattern().isRegex()) );
		properties.setProperty(SEARCHSTRING,  form.getSearchStringContent() );
		properties.setProperty(ISREGEX, String.valueOf(form.isRegexContentPattern()) );
		properties.setProperty(ISINVERT, String.valueOf(form.isInvertContentFilter() ));
		properties.setProperty(IGNORECASE, String.valueOf(form.isIgnoreCase() ));
	}
}

class ReplaceFormPropertySet extends FormPropertySet<ReplaceForm> {

	private String SEARCH_STRING = "searchRegexString";
	private String ISREGEX = "regexContentPattern";
	private String IGNORECASE = "ignore";
	private String REPLACE_STRING = "replacement";
	private String GROUPREFERENCE = "groupReferenceIndicator";
	
	@Override
	public void load(ReplaceForm form, Properties properties) {
		if( properties.containsKey(SEARCH_STRING) ) form.setSearchStringContent( properties.getProperty(SEARCH_STRING) );
		if( properties.containsKey(ISREGEX) ) form.setRegexContentPattern( Boolean.parseBoolean( properties.getProperty(ISREGEX)) );
		if( properties.containsKey(IGNORECASE) ) form.setIgnoreCase( Boolean.parseBoolean( properties.getProperty(IGNORECASE) ));
		if( properties.containsKey(REPLACE_STRING) ) form.setReplacementString( properties.getProperty( REPLACE_STRING ));
		if( properties.containsKey(GROUPREFERENCE) && properties.getProperty(GROUPREFERENCE).length() > 0 ) form.setGroupReference( properties.getProperty(GROUPREFERENCE).charAt(0) );
		form.fireFormUpdateEvent();
	} 

	@Override
	public void store(ReplaceForm form, Properties properties) {
		properties.setProperty(SEARCH_STRING, form.getSearchStringContent());
		properties.setProperty(ISREGEX, String.valueOf( form.isRegexContentPattern()));
		properties.setProperty(IGNORECASE, String.valueOf( form.isIgnoreCase()));
		properties.setProperty(REPLACE_STRING, form.getReplacementString());
		properties.setProperty(GROUPREFERENCE, String.valueOf( form.getGroupReference()));
	}
	
}