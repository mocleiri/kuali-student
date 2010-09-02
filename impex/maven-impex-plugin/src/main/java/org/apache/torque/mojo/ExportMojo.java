package org.apache.torque.mojo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Reads the content of tables from the database and exports the data to XML files.
 */
public abstract class ExportMojo extends AntTaskMojo {

	/**
	 * Comment that gets placed into the generated XML document
	 * 
	 * @parameter expression="${comment}"
	 *            default-value="Automatically generated by the maven-impex-plugin version ${project.version}"
	 */
	private String comment;

	/**
	 * Comma separated list of regular expressions for tables to include in the export
	 * 
	 * @parameter expression="${includePatterns}"
	 */
	private String includePatterns;

	/**
	 * Comma separated list of regular expressions for tables to exclude from the export
	 * 
	 * @parameter expression="${excludePatterns}"
	 */
	private String excludePatterns;

	/**
	 * Database type (oracle, mysql etc)
	 * 
	 * @parameter expression="${targetDatabase}"
	 * @required
	 */
	private String targetDatabase;

	/**
	 * The schema containing the tables to dump
	 * 
	 * @parameter expression="${schema}"
	 * @required
	 */
	private String schema;

	/**
	 * The fully qualified class name of the database driver.
	 * 
	 * @parameter expression="${driver}"
	 * @required
	 */
	private String driver;

	/**
	 * The connect URL of the database.
	 * 
	 * @parameter expression="${url}"
	 * @required
	 */
	private String url;

	/**
	 * The user name to connect to the database.
	 * 
	 * @parameter expression="${username}"
	 */
	private String username;

	/**
	 * The password for the database user.
	 * 
	 * @parameter expression="${password}"
	 */
	private String password;

	protected List<String> getListFromCSV(String csv) {
		if (StringUtils.isEmpty(csv)) {
			return new ArrayList<String>();
		}
		String[] tokens = csv.split(",");
		List<String> list = new ArrayList<String>();
		for (String token : tokens) {
			list.add(token.trim());
		}
		return list;
	}

	/**
	 * Returns the fully qualified class name of the database driver.
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * Sets the fully qualified class name of the database driver.
	 * 
	 * @param driver
	 *            the fully qualified class name of the database driver.
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * Returns the password of the database user.
	 * 
	 * @return the password of the database user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of the database user.
	 * 
	 * @param password
	 *            the password of the database user.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the connect URL to the database.
	 * 
	 * @return the connect URL to the database.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the connect URL to the database.
	 * 
	 * @param url
	 *            the connect URL to the database.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTargetDatabase() {
		return targetDatabase;
	}

	public void setTargetDatabase(String targetDatabase) {
		this.targetDatabase = targetDatabase;
	}

	public String getIncludePatterns() {
		return includePatterns;
	}

	public void setIncludePatterns(String includePatterns) {
		this.includePatterns = includePatterns;
	}

	public String getExcludePatterns() {
		return excludePatterns;
	}

	public void setExcludePatterns(String excludePatterns) {
		this.excludePatterns = excludePatterns;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
