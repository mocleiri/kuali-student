/*
 * Copyright 2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may	obtain a copy of the License at
 *
 * 	http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.student.contract;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ContractReader {

    private String contractText;

    private String contractPath;

    /* Test adding in a comment just to see if subversion works */

    
    public ContractReader(File file) throws FileNotFoundException, IOException {
	contractPath = file.getCanonicalPath();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        contractText = trimContract(reader);	
    }


    public ContractReader(URL url, String jsessionId) throws IOException {
    	this.contractPath = url.toString();
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Cookie", "JSESSIONID=" + jsessionId);

        InputStreamReader myReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(myReader);

        contractText = trimContract(reader);
    }

    public Document getDocument() throws ParserConfigurationException, UnsupportedEncodingException, IOException, SAXException {
        DocumentBuilderFactory  factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new ByteArrayInputStream(contractText.getBytes("UTF-8")));
    }

    public StreamSource getStreamSource() {
        StringReader stringReader = new StringReader(contractText);
        return new StreamSource(stringReader);
    }

    protected String trimContract(BufferedReader reader) throws IOException {

        StringBuilder builder = new StringBuilder();
        String line;
        boolean inContract = false;
	boolean content = false;
	boolean style = false;
	boolean form = false;
	boolean script = false;
	boolean p = false;

        while ((line = reader.readLine()) != null) {
	    if (line.contains("wiki-content")) {
		content = true;
		continue;
	    }

	    if (!content) {
		continue;
	    }

	    if (line.contains("<style")) {
		style = true;
	    }

	    if (line.contains("/style>")) {
		style = false;
		continue;
	    }

	    if (style) {
		continue;
	    }

	    if (line.contains("<form")) {
		form = true;
	    }

	    if (line.contains("/form>")) {
		form = false;
		continue;
	    }

	    if (form) {
		continue;
	    }
	    
	    if (line.contains("<script")) {
		script = true;
	    }

	    if (line.contains("/script>")) {
		script = false;
		continue;
	    }

	    if (script) {
		continue;
	    }
	  
	    if (line.contains("<p")) {
		p = true;
	    }

	    if (line.contains("/p>")) {
		p = false;
		continue;
	    }

	    if (p) {
		continue;
	    }

	    if (line.contains("<link") || (line.contains("<meta"))) {
		continue;
	    }

            if (!inContract) {
                if (line.contains("<em>Setup</em>")) {
                    inContract = true;
		    builder.append("<contents>\n");
                }
            } else {
                if (line.contains("</a>Capabilities</h3>")) {
                    inContract = false;
                } else {
		    builder.append(line + "\n");
                }
            }
        }

	builder.append("</contents>");
	return builder.toString();
    }
    
    /**
     * @return the contractText
     */
	
    public String getContractText() {
	return contractText;
    }

    /**
     *
     * @return the URL
     */

    public String getContractPath() {
	return contractPath;
    }
}
