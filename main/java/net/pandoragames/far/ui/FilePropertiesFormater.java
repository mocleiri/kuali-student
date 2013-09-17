package net.pandoragames.far.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.pandoragames.far.ui.swing.SwingConfig;
import net.pandoragames.util.i18n.Localizer;

/**
 * Utility class with formating methods.
 *
 * @author Olivier Wehner
 * <!-- copyright note -->
 */
public class FilePropertiesFormater {

	// Mind, this is not fit for concurrent use!
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat( SwingConfig.STANDARD_DATE_TIME_FORMAT );

	/**
	 * Formats the long value as a human readable time
	 * stamp using yyyy-MM-dd HH:mm as format.
	 * @see net.pandoragames.far.ui.FARConfig#STANDARD_DATE_TIME_FORMAT
	 * @param timestamp to be formated
	 * @return human readable time stamp
	 */
	public synchronized String timeStamp(long timestamp) {
		return dateTimeFormat.format( new Date(timestamp) );
	}
	/**
	 * Formats bytes in a human readable form as kB, MB or GB.
	 * @param bytes to be formated.
	 * @param localizer for fraction formating
	 * @return human readable byte format
	 */
	public String formatBytes( long bytes, Localizer localizer ) {
		final double KILO = 1024;
		if( bytes < KILO ) return bytes + " byte";
		double reduced = bytes / KILO;
		if( reduced < KILO ) return localizer.localize(reduced, 1) + " kB";
		reduced  = reduced / KILO;
		if( reduced < KILO ) return localizer.localize(reduced, 1) + " MB";
		reduced  = reduced / KILO;
		return localizer.localize(reduced, 1) + " GB";
	}

}
