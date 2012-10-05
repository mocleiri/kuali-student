package com.sigmasys.kuali.ksa.service.drools;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.model.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DroolsService
 *
 * @author Michael Ivanov
 *         Date: 5/21/12
 */
@Service("droolsService")
@Transactional
@SuppressWarnings("unchecked")
public class DroolsService {

    private static final Log logger = LogFactory.getLog(DroolsService.class);

    static {
        System.setProperty("drools.compiler", "JANINO");
        System.setProperty("drools.dialect.java.compiler", "JANINO");
    }

    // Map of <DRL file name, KnowledgeBase> objects
    private final Map<String, KnowledgeBase> knowledgeBases = new HashMap<String, KnowledgeBase>();

    private Resource dslResource;

    @Autowired
    private ConfigService configService;


    @PostConstruct
    private void postConstruct() {
        String dslFileName = getDslFileName();
        logger.info("Initializing DSL resource '" + dslFileName + "'");
        dslResource = ResourceFactory.newClassPathResource(getDslFileName());
        logger.info("DSL resource '" + dslFileName + "' has been initialized");
    }


    private String getDslFileName() {
        return configService.getInitialParameter(Constants.DROOLS_DSL_FILE_PARAM_NAME);
    }


    private synchronized KnowledgeBase getKnowledgeBase(String drlFileName, ResourceType resourceType) {
        try {
            KnowledgeBase knowledgeBase = knowledgeBases.get(drlFileName);
            if (knowledgeBase == null) {
                KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
                if (ResourceType.DSLR.equals(resourceType)) {
                    logger.info("Initializing DSL knowledge base...");
                    builder.add(dslResource, ResourceType.DSL);
                }
                builder.add(ResourceFactory.newClassPathResource(drlFileName), resourceType);
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

    private <T> T fireRules(KnowledgeBase knowledgeBase, T droolsContext) {
        try {
            StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();
            Command command = CommandFactory.newInsert(droolsContext, "droolsContext");
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return (T) results.getValue("droolsContext");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    public <T> T fireRules(String drlFileName, ResourceType resourceType, T droolsContext) {
        return fireRules(getKnowledgeBase(drlFileName, resourceType), droolsContext);
    }


}
