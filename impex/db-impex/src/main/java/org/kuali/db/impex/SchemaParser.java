package org.kuali.db.impex;

import java.io.IOException;

import org.apache.torque.engine.database.model.Database;
import org.kuali.core.db.torque.KualiXmlToAppData;

public class SchemaParser {
	String databaseVendor;
	String location;
	Database database;

	public void parse() throws IOException {
		KualiXmlToAppData xmlParser = new KualiXmlToAppData(databaseVendor, "");

		// Parse schema.xml into a database object
		try {
			database = xmlParser.parseResource(location);
		} catch (Exception e) {
			throw new IOException("Error parsing: " + location, e);
		}
	}

	public String getDatabaseVendor() {
		return databaseVendor;
	}

	public void setDatabaseVendor(String targetDatabase) {
		this.databaseVendor = targetDatabase;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

}
