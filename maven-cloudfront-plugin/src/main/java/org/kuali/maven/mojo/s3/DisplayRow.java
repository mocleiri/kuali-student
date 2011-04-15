package org.kuali.maven.mojo.s3;

/**
 * Pojo that represents one row in the directory listing of the contents of a directory in an S3 bucket
 */
public class DisplayRow implements Comparable<DisplayRow> {
	String show;
	String image;
	String ahref;
	String lastModified;
	String size;

	@Override
	public int compareTo(DisplayRow other) {
		return lastModified.compareTo(other.getLastModified());
	}

	public String getImage() {
		return image;
	}

	public void setImage(final String image) {
		this.image = image;
	}

	public String getAhref() {
		return ahref;
	}

	public void setAhref(final String ahref) {
		this.ahref = ahref;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(final String date) {
		this.lastModified = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(final String size) {
		this.size = size;
	}

	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}

}
