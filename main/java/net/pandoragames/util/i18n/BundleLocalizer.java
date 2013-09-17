package net.pandoragames.util.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ResourceBundle based implementation of the Localizer interface. 
 *
 * @see java.util.ResourceBundle
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class BundleLocalizer extends LocalizerBase {
	

	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor takes the bundle base name and the desired locale.
	 * See <code>java.util.ResourceBundle</code> for details.
	 * @param baseName the base name of the resource bundle, a fully qualified class name
	 * @param locale non null locale
	 */
	public BundleLocalizer(String baseName, Locale locale) {
		super( locale );
		resourceBundle = ResourceBundle.getBundle(baseName, locale);
	}

	/**
	 * Returns some locale specific, human readable text for the specified code, 
	 * or the code unchanged if no such translation could be done,
	 * @param code application internal code for some text
	 * @return human readable text (or supplied code)
	 */
	public String localize(String code) {
		if( code == null ) return "";
		try {
			return resourceBundle.getString( code );
		} catch (MissingResourceException mrx) {
			return code;
		}
	}
}
