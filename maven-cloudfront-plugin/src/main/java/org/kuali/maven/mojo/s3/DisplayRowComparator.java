package org.kuali.maven.mojo.s3;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class DisplayRowComparator implements Comparator<DisplayRow> {
	public static final String DEFAULT_SEPARATOR = ".";
	String separator = DEFAULT_SEPARATOR;

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
		String[] tokens1 = StringUtils.splitByWholeSeparator(show1, separator);
		String[] tokens2 = StringUtils.splitByWholeSeparator(show2, separator);
		int len = Math.min(tokens1.length, tokens2.length);

		for (int i = 0; i < len; i++) {
			String token1 = tokens1[i];
			String token2 = tokens2[i];
			int compareTo = token1.compareTo(token2);
			if (compareTo != 0) {
				return compareTo;
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

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
