package org.kuali.maven.mojo.s3;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * Provide non-alphanumeric sorting for version numbers.
 * 
 * 1.1.10 sorts after 1.1.2<br>
 * 1.1.10-SNAPSHOT sorts before 1.1.10
 */
public class DisplayRowComparator implements Comparator<DisplayRow> {
	public static final String DEFAULT_SEPARATORS = ".-";
	String separators = DEFAULT_SEPARATORS;
	String snapshot = "SNAPSHOT";

	@Override
	public int compare(DisplayRow one, DisplayRow two) {
		String show1 = one.getShow();
		String show2 = two.getShow();

		if (show1 == null && show2 == null) {
			return 0;
		}

		if (show1 == null) {
			return -1;
		}

		if (show2 == null) {
			return 1;
		}
		String[] tokens1 = StringUtils.split(show1, separators);
		String[] tokens2 = StringUtils.split(show2, separators);
		int len = Math.min(tokens1.length, tokens2.length);

		for (int i = 0; i < len; i++) {
			String token1 = tokens1[i];
			String token2 = tokens2[i];
			int compareTo = compareTokens(token1, token2);
			if (compareTo != 0) {
				return compareTo;
			}
		}

		if (tokens1.length > tokens2.length) {
			return -1;
		} else if (tokens1.length < tokens2.length) {
			return 1;
		} else {
			return 0;
		}
	}

	protected int compareTokens(String token1, String token2) {
		try {
			Double d1 = new Double(token1);
			Double d2 = new Double(token2);
			return d1.compareTo(d2);
		} catch (NumberFormatException e) {
			// ignore
		}
		return token1.compareTo(token2);

	}

	public String getSeparators() {
		return separators;
	}

	public void setSeparators(String separators) {
		this.separators = separators;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

}
