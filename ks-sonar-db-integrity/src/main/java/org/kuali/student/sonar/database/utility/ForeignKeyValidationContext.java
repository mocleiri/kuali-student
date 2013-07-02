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

import java.sql.Connection;

/**
 * This class contains configuration information for the DatabaseIntegritySensor
 */
public class ForeignKeyValidationContext {

    protected Connection connection;
    protected Boolean skip;
    protected String queryFilePath;
    protected String queryFileName;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Boolean getSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public String getQueryFilePath() {
        return queryFilePath;
    }

    public void setQueryFilePath(String queryFilePath) {
        this.queryFilePath = queryFilePath;
    }

    public String getQueryFileName() {
        return queryFileName;
    }

    public void setQueryFileName(String queryFileName) {
        this.queryFileName = queryFileName;
    }

}
