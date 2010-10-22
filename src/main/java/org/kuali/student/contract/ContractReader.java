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
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ContractReader {

    private String contractText;
    private String contractPath;
    private URL url;
    private String jsessionId;
    private boolean nested;

   
    public ContractReader(URL url, String jsessionId) throws IOException {
	this(url, jsessionId, false);
	return;
    }


    public ContractReader(URL url, String jsessionId, boolean nested) throws IOException {
	this.nested = nested;
    	this.contractPath = url.toString();
	this.url = url;
	this.jsessionId = jsessionId;

        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Cookie", "JSESSIONID=" + jsessionId);

        InputStreamReader myReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(myReader);

        contractText = trimContract(reader);
	return;
    }

	
    public ContractReader(File file) throws FileNotFoundException, IOException {
	contractPath = file.getCanonicalPath();
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

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

    
    public String getText() {
	return (this.contractText);
    }


    /**
     *  Filters out the HTML gunk that trips the SAX parser. The magic
     *  html tokens are "wiki-content" for the start of the content
     *  portion of the wiki page, "Setup" for the start of the
     *  contract and "Capabilities" for the end of the contract. If a
     *  page has a "Linked Operations" heading, it is expected to
     *  contain links to other pages that will be merged into the
     *  contract text.
     *
     *  @param reader input text reader
     *  @return a string containing an XML-like contract
     *  @throws IOException problem reading stream
     */

    protected String trimContract(BufferedReader reader) throws IOException {

        StringBuilder builder = new StringBuilder();
        String line;
        boolean inContract = false;
	boolean linkPage = false;
	Collection <String>linkedPages = new ArrayList<String>();
	boolean p = false;

	if (!this.nested) {
	    builder.append("<contents>\n");	
	}

        while ((line = reader.readLine()) != null) {
	    if (line.contains("ServiceOperationStart")) {
		inContract = true;
		continue;
	    }

	    if (line.contains("ServiceOperationStop")) {
		inContract = false;
		continue;
	    }

	    if (linkPage) {
		if (line.contains("href=")) {
		    linkedPages.add(parsePageLink(line));
		}

		if (line.contains("</div>")) {
		    linkPage = false;
		}

		continue;
	    }

	    /* link to another page */
	    if (line.contains("</a>Linked Operations</h3>")) {
		linkPage = true;
		continue;
	    }

	    if (!inContract) {
		continue;
	    }

	    builder.append(line + "\n");
        }

	for (String page : linkedPages) {
	    ContractReader cr = new ContractReader(new URL(this.url.getProtocol(), 
							   this.url.getHost(), 
							   this.url.getPort(), 
							   page), 
						   this.jsessionId, true);
	    builder.append(cr.getText());
	}

	if (!this.nested) {
	    builder.append("</contents>\n");
	}

	return (builder.toString());
    }


    private String parsePageLink(String line) {
	String[] tok = line.split("href=");
	if (tok.length < 2) {
	    return ("");
	}

	tok = tok[1].split("\"");
	return (tok[1]);
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
