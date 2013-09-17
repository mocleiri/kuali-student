package net.pandoragames.util.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * This default instance of the Localizer interface reads it replacement codes
 * from some property file (or object). It effectively does not care very much 
 * about the Locale it is instantiated with, except for the decimal seperator.
 * Relevant code parts are supposed to be synchronized.
 * 
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class DefaultLocalizer extends LocalizerBase
{

	private Properties myCodeList;
	/**
	 * Instantiates the DefaultLocalizer with some locale object and a Properties
	 * object that contains the code translations as key-value pairs.
	 */
	public DefaultLocalizer(Properties codelist, Locale locale)
	{
		super( locale );
		if( codelist == null )throw new NullPointerException("Codelist must not be null");
		myCodeList = codelist;
	}
	
	/**
	 * Instantiates the DefaultLocalizer with some locale object and a (properties)
	 * file that contains the code translations as key-value pairs.
	 */
	public DefaultLocalizer(File propertyFile, Locale locale)
	throws IOException
	{
		this( readProperties(propertyFile), locale);
	}

	/**
	 * Returns some locale specific, human readable text for the specified code, 
	 * or the code unchanged if no such translation could be done,
	 * @param code application internal code for some text
	 * @return human readable text (or supplied code)
	 */
	public String localize(String code)
	{
		String text = myCodeList.getProperty(code);
		if( text == null ) return code;
		return text;
	}

	private static Properties readProperties(File properties)
	throws IOException
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(properties);
			Properties props = new Properties();
			props.load(fis);
			return props;
		}
		finally
		{
			if (fis != null) try { fis.close(); }catch(IOException iox){ /* ignore */ }
		}
	}
}
