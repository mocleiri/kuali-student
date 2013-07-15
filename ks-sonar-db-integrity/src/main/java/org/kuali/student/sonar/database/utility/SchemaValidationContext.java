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

import org.kuali.common.impex.model.compare.service.SchemaCompareService;

public class SchemaValidationContext {
    private Boolean skip;

    protected String appSchemaName;

    protected String appPath;

    protected String appSchemaFilename;

    protected String appConstraintsFilename;

    protected String ddlSchemaName;

    protected String ddlPath;

    protected String ddlSchemaFilename;

    protected String ddlConstraintsFilename;

    protected SchemaCompareService compareService;

    public Boolean getSkip() {
        return skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public String getAppConstraintsFilename() {
        return appConstraintsFilename;
    }

    public void setAppConstraintsFilename(String appConstraintsFilename) {
        this.appConstraintsFilename = appConstraintsFilename;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getAppSchemaFilename() {
        return appSchemaFilename;
    }

    public void setAppSchemaFilename(String appSchemaFilename) {
        this.appSchemaFilename = appSchemaFilename;
    }

    public String getAppSchemaName() {
        return appSchemaName;
    }

    public void setAppSchemaName(String appSchemaName) {
        this.appSchemaName = appSchemaName;
    }

    public String getDdlConstraintsFilename() {
        return ddlConstraintsFilename;
    }

    public void setDdlConstraintsFilename(String ddlConstraintsFilename) {
        this.ddlConstraintsFilename = ddlConstraintsFilename;
    }

    public String getDdlPath() {
        return ddlPath;
    }

    public void setDdlPath(String ddlPath) {
        this.ddlPath = ddlPath;
    }

    public String getDdlSchemaFilename() {
        return ddlSchemaFilename;
    }

    public void setDdlSchemaFilename(String ddlSchemaFilename) {
        this.ddlSchemaFilename = ddlSchemaFilename;
    }

    public String getDdlSchemaName() {
        return ddlSchemaName;
    }

    public void setDdlSchemaName(String ddlSchemaName) {
        this.ddlSchemaName = ddlSchemaName;
    }

    public SchemaCompareService getCompareService() {
        return compareService;
    }

    public void setCompareService(SchemaCompareService compareService) {
        this.compareService = compareService;
    }
}
