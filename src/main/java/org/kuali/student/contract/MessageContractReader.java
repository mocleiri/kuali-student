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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class MessageContractReader extends ContractReader {

	/**
	 * @param url
	 * @throws IOException
	 */
	public MessageContractReader(URL url, String jsessionId) throws IOException {
		super(url, jsessionId);
		// TODO Auto-generated constructor stub
	}

	public MessageContractReader(File file) throws FileNotFoundException,
			IOException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kuali.student.contract.ContractReader#trimContract(java.io.BufferedReader)
	 */
	@Override
	protected String trimContract(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		String line;

		// Add in xml header and entity definitions
		builder.append("<?xml version=\"1.0\"?>" + "<!DOCTYPE xsl:stylesheet ["
			       + "<!ENTITY nbsp '&#160;'>" + "]>");

		boolean description = false;
                boolean meta = false;
		boolean struct = false;

		builder.append("\n<content>\n");
		
		while ((line = reader.readLine()) != null) {
		    if (line.contains("<h2>")) {
			builder.append(line + "\n");
			continue;
		    }

		    if (line.contains("Description</h3>")) {
			description = true;
			builder.append(line + "\n");
			continue;
		    }

		    if (description) {
			builder.append(line + "\n");
			if (line.contains("</p>")) {
			    description = false;
			}
		    }

		    if (line.contains("<table id=\"structureMetaTable\"")) {
			meta = true;
			builder.append(line + "\n");
			continue;
		    }

		    if (meta) {
			builder.append(line + "\n");
			if (line.contains("</table>")) {
			    meta = false;
			}
		    }

		    if (line.contains("<table class=\"structTable\"")) {
			struct = true;
			builder.append(line + "\n");
			continue;
		    }

		    if (struct) {
			builder.append(line + "\n");
			if (line.contains("</table>")) {
			    struct = false;
			}
		    }
		}

		builder.append("\n</content>\n");
		return builder.toString();
	}

}
