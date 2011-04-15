package org.kuali.maven.mojo.s3;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

/**
 * Provide intelligent sorting for version numbers.
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

		if (isOnlyDifferenceSnapshot(tokens1, tokens2)) {
			if (isSnapshot(tokens1)) {
				return -1;
			} else {
				return 1;
			}
		}

		if (tokens1.length > tokens2.length) {
			return 1;
		} else if (tokens1.length < tokens2.length) {
			return -1;
		} else {
			return 0;
		}
	}

	protected boolean isOnlyDifferenceSnapshot(String[] tokens1, String[] tokens2) {
		if (!isLengthDifferentByOne(tokens1, tokens2)) {
			return false;
		}
		if (!isSnapshot(tokens1) && !isSnapshot(tokens2)) {
			return false;
		}
		return true;
	}

	protected boolean isSnapshot(String[] tokens) {
		int len = tokens.length;
		String token = tokens[len - 1];
		return token.toUpperCase().equals(this.snapshot.toUpperCase());
	}

	protected boolean isLengthDifferentByOne(String[] tokens1, String[] tokens2) {
		int len1 = tokens1.length;
		int len2 = tokens2.length;
		int diff = Math.abs(len1 - len2);
		if (diff == 1) {
			return true;
		} else {
			return false;
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
