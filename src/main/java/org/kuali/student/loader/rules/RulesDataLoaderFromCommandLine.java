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
package org.kuali.student.loader.rules;

import org.kuali.student.loader.atp.AtpServiceFactory;
import org.kuali.student.loader.course.*;
import org.kuali.student.loader.organization.OrganizationServiceFactory;
import org.kuali.student.r2.core.atp.service.AtpService;
import org.kuali.student.r2.core.organization.service.OrganizationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author nwright
 */
public class RulesDataLoaderFromCommandLine {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RulesDataLoaderFromCommandLine instance =
                new RulesDataLoaderFromCommandLine();
        instance.execute(args);

    }

    private void execute(String[] args) {
        if (args == null) {
            throw new RuntimeException("args is null");
        }
        if (args.length == 0) {
            throw new RuntimeException("no args specified");
        }
        if (args.length == 1) {
            //Assume source and destination are the same
            String sourceHost = args[0];
            String destHost = args[0];
            generate(sourceHost, destHost);
        } else {
            String sourceHost = args[0];
            String destHost = args[1];
            generate(sourceHost, destHost);
        }
    }

    protected void generate(String sourceHostUrl, String destHostUrl) {
        RulesDataLoader rulesLoader = new RulesDataLoader();
        KRMSHelper krmsHelper = new KRMSHelper();
        StatementHelper statementHelper = new StatementHelper();


        ServiceFactory serviceFactory = new ServiceFactory();
        serviceFactory.setHostUrl(sourceHostUrl);
        statementHelper.setCourseService(serviceFactory.getCourseService());
        statementHelper.setStatementService(serviceFactory.getStatementService());

        krmsHelper.setRuleManagementService(serviceFactory.getRuleManagementService());
        krmsHelper.setKrmsTypeRepositoryService(serviceFactory.getKrmsTypeRepositoryService());
        krmsHelper.setTermRepositoryService(serviceFactory.getTermRepositoryService());

//  AtpServiceFactory aptServFactory = new AtpServiceFactory();
//  aptServFactory.setHostUrl(hostUrl);
//  AtpService atpService = aptServFactory.getAtpService();
//  ccLoader.getHelperService().put("atp", atpService);
//
//  OrganizationServiceFactory orgServFactory = new OrganizationServiceFactory ();
//  orgServFactory.setHostUrl (hostUrl);
//  OrganizationService orgService = orgServFactory.getOrganizationService ();
//  ccLoader.getHelperService().put("org", orgService);

        System.out.println(new Date() + " starting conversion... "
        );
        rulesLoader.setKrmsHelper(krmsHelper);
        rulesLoader.setStatementHelper(statementHelper);
        rulesLoader.startConversion();
        //TODO have the loader populate a load result list
        List<CreditCourseLoadResult> results = new ArrayList<CreditCourseLoadResult>();

        // output good results
        for (CreditCourseLoadResult result : results) {
            switch (result.getStatus()) {
                case CREATED:
                case COURSE_VARIATION_PROCESSED_WITH_MAIN_COURSE:
                    System.out.println(result.getCourseInfo().getCode()
                            + " " + result.getStatus() + " id = "
                            + result.getCourseInfo().getId());
            }
        }

        // output summary
        for (CreditCourseLoadResult result : results) {
            result.getStatus().increment();
        }
        //System.out.println (list.size () + " credit courses");
        for (CreditCourseLoadResult.Status status :
                CreditCourseLoadResult.Status.values()) {
            System.out.println("Total with status of " + status + " = "
                    + status.getCount());
        }

        // output errors
        for (CreditCourseLoadResult result : results) {
            switch (result.getStatus()) {
                case CREATED:
                case COURSE_VARIATION_PROCESSED_WITH_MAIN_COURSE:
                    break;
                default:
                    System.out.println(result);
            }
        }
        if (CreditCourseLoadResult.Status.VALIDATION_ERROR.getCount() > 0) {
            throw new RuntimeException(CreditCourseLoadResult.Status.VALIDATION_ERROR.getCount()
                    + " records failed to load");
        }
        if (CreditCourseLoadResult.Status.EXCEPTION.getCount() > 0) {
            throw new RuntimeException(CreditCourseLoadResult.Status.EXCEPTION.getCount()
                    + " records failed to load");
        }
    }
}
