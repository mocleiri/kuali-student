package com.sigmasys.kuali.ksa.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DroolsService
 *
 * @author Michael Ivanov
 *         Date: 5/21/12
 */
@SuppressWarnings("unchecked")
@Service("droolsService")
@Transactional
public class DroolsService {

    private static final Log logger = LogFactory.getLog(DroolsService.class);

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
                KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
                InputStream inputStream = getClass().getResourceAsStream(drlFileName);
                builder.add(ResourceFactory.newInputStreamResource(inputStream), ResourceType.XDRL);
                handleErrors(builder.getErrors(), drlFileName);
                knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
                knowledgeBases.put(drlFileName, knowledgeBase);
            }
            return knowledgeBase;
        } catch (Throwable t) {
            String errMsg = "Cannot retrieve KnowledgeBase from '" + drlFileName + "'";
            logger.error(errMsg, t);
            throw new RuntimeException(errMsg, t);
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

    private <T> T fireRules(KnowledgeBase knowledgeBase, T argument) {
        try {
            StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();
            Command command = CommandFactory.newInsert(argument, "object");
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return (T) results.getValue("object");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    public <T> T fireRules(String drlFileName, T argument) {
        return fireRules(getKnowledgeBase(drlFileName), argument);
    }


}
