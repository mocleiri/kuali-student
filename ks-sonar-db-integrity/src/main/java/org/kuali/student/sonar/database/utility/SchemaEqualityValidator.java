/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.kuali.student.sonar.database.utility;

import java.io.IOException;
import javax.xml.bind.JAXBException;

import org.kuali.common.jalc.ProducerUtils;
import org.kuali.common.jalc.model.Schema;
import org.kuali.common.jalc.model.compare.SchemaCompare;
import org.kuali.common.jalc.model.compare.SchemaCompareResult;

public class SchemaEqualityValidator {

    public static final String SCHEMA_FOLDER = "schemaXml/";
    public static final String SCHEMA_1_FILENAME = "ks-app-schema.xml";
    public static final String SCHEMA_1_LABEL = "KSAPP";
    public static final String SCHEMA_2_FILENAME = "ks-ddl-schema.xml";
    public static final String SCHEMA_2_LABEL = "DDL";

    public SchemaCompareResult compareSchemas() throws JAXBException, IOException {
        Schema schema1 = ProducerUtils.unmarshalSchema(SCHEMA_FOLDER + SCHEMA_1_FILENAME);
        Schema schema2 = ProducerUtils.unmarshalSchema(SCHEMA_FOLDER + SCHEMA_2_FILENAME);

        schema1.setName(SCHEMA_1_LABEL);
        schema2.setName(SCHEMA_2_LABEL);

        return new SchemaCompare(schema1, schema2).compare();
    }
}
