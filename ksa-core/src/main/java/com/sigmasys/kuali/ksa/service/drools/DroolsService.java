package com.sigmasys.kuali.ksa.service.drools;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
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

    @Autowired
    private ConfigService configService;

    @Autowired
    private DroolsPersistenceService droolsPersistenceService;

    private Resource dslResource;

    // Specifies whether to use the classpath or database as a rule persistent store
    private boolean useClasspath;

    // Map of <DRL file name, KnowledgeBase> objects
    private final Map<String, KnowledgeBase> knowledgeBases = new HashMap<String, KnowledgeBase>();


    @PostConstruct
    private void postConstruct() {
        String dslId = getDslId();
        logger.info("Initializing DSL resource '" + dslId + "'");
        dslResource = getRuleSetResource(getDslId());
        logger.info("DSL resource '" + dslId + "' has been initialized");
    }

    private Collection<KnowledgePackage> buildKnowledgePackages(String ruleSetId, Resource rulesResource,
                                                                ResourceType resourceType) {
            KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            if (ResourceType.DSLR.equals(resourceType)) {
                logger.info("Initializing DSL knowledge base...");
                builder.add(dslResource, ResourceType.DSL);
            }
            builder.add(rulesResource, resourceType);
            handleErrors(builder.getErrors(), ruleSetId);
            return builder.getKnowledgePackages();
    }

    private synchronized KnowledgeBase getKnowledgeBase(String ruleSetId, ResourceType resourceType) {
        try {
            KnowledgeBase knowledgeBase = knowledgeBases.get(ruleSetId);
            if (knowledgeBase == null) {
                Collection<KnowledgePackage> knowledgePackages =
                        buildKnowledgePackages(ruleSetId, getRuleSetResource(ruleSetId), resourceType);
                knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(knowledgePackages);
                knowledgeBases.put(ruleSetId, knowledgeBase);
            }
            return knowledgeBase;
        } catch (Throwable t) {
            String errMsg = "Cannot retrieve KnowledgeBase from '" + ruleSetId + "'";
            logger.error(errMsg, t);
            throw new RuntimeException(errMsg, t);
        }
    }

    private void handleErrors(KnowledgeBuilderErrors errors, String ruleSetId) {
        if (errors != null && !errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("There are problems in compiling the rules for '").append(ruleSetId).append("' \n");
            for (KnowledgeBuilderError error : errors) {
                errorMessage.append(error).append("\n");
            }
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage.toString());
        }
    }

    private <T> T fireRules(KnowledgeBase knowledgeBase, T droolsContext, Map<String, Object> globalParams) {
        try {
            StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();
            if (globalParams != null) {
                for (Map.Entry<String, Object> entry : globalParams.entrySet()) {
                    session.setGlobal(entry.getKey(), entry.getValue());
                }
            }
            Command command = CommandFactory.newInsert(droolsContext, "droolsContext");
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return (T) results.getValue("droolsContext");
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    /**
     * Returns a Drools resource using a rule set identifier either from the classpath (by a filename) or
     * from the database (by a rule set ID column name).
     *
     * @param ruleSetId a rule set identifier
     * @return <code>Resource</code> instance
     */
    protected Resource getRuleSetResource(String ruleSetId) {
        if (useClasspath) {
            return ResourceFactory.newClassPathResource(ruleSetId);
        } else {
            RuleSet ruleSet = droolsPersistenceService.getRules(ruleSetId);
            if (ruleSet == null) {
                String errMsg = "Rule Set specified by ID = " + ruleSetId + " does not exist";
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }
            return ResourceFactory.newReaderResource(new StringReader(ruleSet.getRules()));
        }
    }

    // ------------------------   PUBLIC METHOD DEFINITIONS -----------------------------------------------------

    public boolean isUseClasspath() {
        return useClasspath;
    }

    public void setUseClasspath(boolean useClasspath) {
        this.useClasspath = useClasspath;
    }

    public <T> T fireRules(String drlFileName, ResourceType resourceType, T droolsContext,
                           Map<String, Object> globalParams) {
        return fireRules(getKnowledgeBase(drlFileName, resourceType), droolsContext, globalParams);
    }

    public <T> T fireRules(String drlFileName, ResourceType resourceType, T droolsContext) {
        return fireRules(getKnowledgeBase(drlFileName, resourceType), droolsContext, null);
    }

    public void validateRuleSet(RuleSet ruleSet, ResourceType resourceType) {
        Resource ruleSetResource = ResourceFactory.newReaderResource(new StringReader(ruleSet.getRules()));
        buildKnowledgePackages(ruleSet.getId(), ruleSetResource, resourceType);
    }

    public String getDslId() {
        return configService.getInitialParameter(Constants.DROOLS_DSL_ID_PARAM_NAME);
    }


}
