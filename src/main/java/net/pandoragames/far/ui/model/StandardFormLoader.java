package net.pandoragames.far.ui.model;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Standard FormLoader implementation, uses standard java
 * xml serialisation mechanism.
 *
 * @author Olivier Wehner
 * <!-- copyright note --> 
 */
public class StandardFormLoader implements FormLoader {

	/**
	 * {@inheritDoc}
	 */
	public FindForm loadForm(InputStream input) 
	 throws IOException {
		XMLDecoder decoder = new XMLDecoder( input );
		return (FindForm) decoder.readObject();
	}

	/**
	 * {@inheritDoc}
	 */
	public void saveForm(FindForm form, OutputStream output)
	 throws IOException {
    	XMLEncoder xmlEncoder = new XMLEncoder( output );
    	xmlEncoder.setPersistenceDelegate( File.class, 
				  new DefaultPersistenceDelegate(new String[]{"path"}));
    	xmlEncoder.writeObject( form );
	}

}
