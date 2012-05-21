package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.util.CommonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DroolsService
 *
 * @author Michael Ivanov
 *         Date: 5/21/12
 */
public class DroolsService {

    private static final Log logger = LogFactory.getLog(DroolsService.class);

    private static final String RULE_PATH = "/rules";

    static {
        System.setProperty("drools.compiler", "JANINO");
        System.setProperty("drools.dialect.java.compiler", "JANINO");
    }

    // Map of <DRL file name, KnowledgeBase> objects
    private static final Map<String, KnowledgeBase> knowledgeBases = new HashMap<String, KnowledgeBase>();


    private synchronized KnowledgeBase getKnowledgeBase(String drlFileName) {
        try {
            KnowledgeBase knowledgeBase = knowledgeBases.get(drlFileName);
            if (knowledgeBase == null) {
                String drl = CommonUtils.getResourceAsString(RULE_PATH + "/" + drlFileName);
                logger.info("Retrieved the following Drools rule set:\n" + drl);
                KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
                kbuilder.add(ResourceFactory.newByteArrayResource(drl.getBytes("UTF-8")), ResourceType.DRL);
                handleErrors(kbuilder.getErrors(), drlFileName);
                knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
                knowledgeBases.put(drlFileName, knowledgeBase);
            }
            return knowledgeBase;
        } catch (Throwable t) {
            logger.error("Cannot retrieve KnowledgeBase from '" + drlFileName + "'", t);
            throw new RuntimeException("Cannot retrieve KnowledgeBase from" + drlFileName + "'", t);
        }
    }

    private void handleErrors(KnowledgeBuilderErrors errors, String drlFileName) {
        if (errors != null && !errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("There are problems in compiling the rules for '").append(drlFileName).append("' \n");
            for (KnowledgeBuilderError error : errors) {
                errorMessage.append(error).append("\n");
            }
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage.toString());
        }
    }

    private Object fireRules(KnowledgeBase knowledgeBase, Object argument) {
        try {
            StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();
            Command command = CommandFactory.newInsert(argument, "object");
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return results.getValue("object");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    public Object fireRules(String drlFileName, Object argument) {
        return fireRules(getKnowledgeBase(drlFileName), argument);
    }


}
