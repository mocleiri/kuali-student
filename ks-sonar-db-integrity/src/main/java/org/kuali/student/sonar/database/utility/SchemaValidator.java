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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;

import org.kuali.common.impex.ProducerUtils;
import org.kuali.common.impex.model.ForeignKey;
import org.kuali.common.impex.model.Index;
import org.kuali.common.impex.model.NamedElement;
import org.kuali.common.impex.model.Schema;
import org.kuali.common.impex.model.Table;
import org.kuali.common.impex.model.UniqueConstraint;
import org.kuali.common.impex.model.compare.SchemaCompareResult;

public class SchemaValidator {

    public SchemaCompareResult compareSchemas(SchemaValidationContext context) throws JAXBException, IOException {
        Schema ksSchema = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppSchemaFilename());
        Schema ddlSchema = ProducerUtils.unmarshalSchema(context.getDdlPath() + context.getDdlSchemaFilename());

        ksSchema.setName(context.getAppSchemaName());
        ddlSchema.setName(context.getDdlSchemaName());

        return context.getCompareService().compare(ksSchema, ddlSchema);
    }

    public SchemaCompareResult compareConstraints(SchemaValidationContext context) throws JAXBException, IOException {
        Schema ksConstraints = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppConstraintsFilename());
        Schema ddlConstraints = ProducerUtils.unmarshalSchema(context.getDdlPath() + context.getDdlConstraintsFilename());

        ksConstraints.setName(context.getAppSchemaName());
        ddlConstraints.setName(context.getDdlSchemaName());

        return context.getCompareService().compare(ksConstraints, ddlConstraints);
    }

    public List<NamedElement> getInvalidNameConstraintsAndIndices(SchemaValidationContext context) throws JAXBException, IOException {
        Schema ksSchema = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppSchemaFilename());
        Schema ksConstraints = ProducerUtils.unmarshalSchema(context.getAppPath() + context.getAppConstraintsFilename());

        List<NamedElement> results = new ArrayList<NamedElement>();

        for(Table table : ksSchema.getTables()) {
            for (UniqueConstraint uc : table.getUniqueConstraints()) {
                if (!uc.getName().startsWith(table.getName())) {
                    results.add(uc);
                }
            }
            for (Index index : table.getIndices()) {
                if (!index.getName().startsWith(table.getName())) {
                    results.add(index);
                }
            }
        }

        for (ForeignKey foreignKey : ksConstraints.getForeignKeys()) {
            if (!foreignKey.getName().startsWith(foreignKey.getLocalTableName())) {
                results.add(foreignKey);
            }
        }

        return results;
    }
}
