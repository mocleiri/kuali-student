package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.exception.InvalidRulesException;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.rule.RuleSet;
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
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Drools-based implementation of BrmService
 *
 * @author Michael Ivanov
 */
@Service("droolsService")
@Transactional
@SuppressWarnings("unchecked")
public class DroolsService implements BrmService {

    private static final Log logger = LogFactory.getLog(DroolsService.class);

    public static final String DROOLS_CONTEXT_NAME = "droolsContext";

    static {
        System.setProperty("drools.compiler", "JANINO");
        System.setProperty("drools.dialect.java.compiler", "JANINO");
    }

    public static enum PersistenceType {
        CLASSPATH,
        DATABASE
    }

    @Autowired
    private ConfigService configService;

    @Autowired
    private BrmPersistenceService brmPersistenceService;

    private Resource dslResource;

    // Map of <DRL file name, KnowledgeBase> objects
    private final Map<String, KnowledgeBase> knowledgeBases = new HashMap<String, KnowledgeBase>();


    //@PostConstruct
    private void postConstruct() {
        String dslId = getDslId();
        logger.info("Initializing DSL resource '" + dslId + "'");
        dslResource = getRuleSetResource(getDslId());
        logger.info("DSL resource '" + dslId + "' has been initialized");
    }

    private Collection<KnowledgePackage> buildKnowledgePackages(String ruleSetName, Resource rulesResource,
                                                                ResourceType resourceType) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        if (ResourceType.DSLR.equals(resourceType)) {
            logger.info("Initializing DSL knowledge base...");
            builder.add(dslResource, ResourceType.DSL);
        }
        builder.add(rulesResource, resourceType);
        handleErrors(builder.getErrors(), ruleSetName);
        return builder.getKnowledgePackages();
    }

    private synchronized KnowledgeBase getKnowledgeBase(String ruleSetName, ResourceType resourceType) {
        try {
            KnowledgeBase knowledgeBase = knowledgeBases.get(ruleSetName);
            if (knowledgeBase == null) {
                Collection<KnowledgePackage> knowledgePackages =
                        buildKnowledgePackages(ruleSetName, getRuleSetResource(ruleSetName), resourceType);
                knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(knowledgePackages);
                knowledgeBases.put(ruleSetName, knowledgeBase);
            }
            return knowledgeBase;
        } catch (InvalidRulesException ire) {
            logger.error(ire.getMessage(), ire);
            throw ire;
        } catch (Throwable t) {
            String errMsg = "Cannot retrieve KnowledgeBase from '" + ruleSetName + "'";
            logger.error(errMsg, t);
            throw new RuntimeException(errMsg, t);
        }
    }

    private void handleErrors(KnowledgeBuilderErrors errors, String ruleSetName) {
        if (errors != null && !errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("There are problems in compiling the rules for '").append(ruleSetName).append("' \n");
            for (KnowledgeBuilderError error : errors) {
                errorMessage.append(error).append("\n");
            }
            logger.error(errorMessage);
            throw new InvalidRulesException(errorMessage.toString());
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
            Command command = CommandFactory.newInsert(droolsContext, DROOLS_CONTEXT_NAME);
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return (T) results.getValue(DROOLS_CONTEXT_NAME);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    /**
     * Returns a Drools resource using a rule set identifier either from the classpath (by a filename) or
     * from the database (by a rule set name).
     *
     * @param ruleSetName a rule set identifier
     * @return <code>Resource</code> instance
     */
    protected Resource getRuleSetResource(String ruleSetName) {
        switch (getPersistenceType()) {
            case CLASSPATH:
                return ResourceFactory.newClassPathResource(Constants.DROOLS_CLASSPATH + "/" + ruleSetName);
            case DATABASE:
                RuleSet ruleSet = brmPersistenceService.getRuleSet(ruleSetName);
                if (ruleSet == null) {
                    String errMsg = "Rule Set specified by ID = " + ruleSetName + " does not exist";
                    logger.error(errMsg);
                    throw new IllegalStateException(errMsg);
                }
                String ruleSetContent = DroolsRuleBuilder.toString(ruleSet);
                return ResourceFactory.newByteArrayResource(ruleSetContent.getBytes());
            default:
                String errMsg = "Cannot find resource handlers for persistence type = " + getPersistenceType();
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
        }
    }

    // ------------------------   PUBLIC METHOD DEFINITIONS -----------------------------------------------------

    public <T> T fireRules(String ruleSetName, ResourceType resourceType, T droolsContext,
                           Map<String, Object> globalParams) {
        return fireRules(getKnowledgeBase(ruleSetName, resourceType), droolsContext, globalParams);
    }

    public <T> T fireRules(String ruleSetName, ResourceType resourceType, T droolsContext) {
        return fireRules(getKnowledgeBase(ruleSetName, resourceType), droolsContext, null);
    }

    public void validateRuleSet(RuleSet ruleSet) {
        String ruleSetContent = DroolsRuleBuilder.toString(ruleSet);
        Resource ruleSetResource = ResourceFactory.newReaderResource(new StringReader(ruleSetContent));
        ResourceType resourceType = ResourceType.getResourceType(ruleSet.getType().getName());
        buildKnowledgePackages(ruleSet.getName(), ruleSetResource, resourceType);
    }

    public String getDslId() {
        String dslId = configService.getInitialParameter(Constants.DROOLS_DSL_ID_PARAM_NAME);
        if (!StringUtils.hasText(dslId)) {
            String errMsg = "Parameter '" + Constants.DROOLS_DSL_ID_PARAM_NAME + "' must be set";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        return dslId;
    }

    public PersistenceType getPersistenceType() {
        String type = configService.getInitialParameter(Constants.DROOLS_PERSISTENCE_PARAM_NAME);
        if (!StringUtils.hasText(type)) {
            String errMsg = "Parameter '" + Constants.DROOLS_PERSISTENCE_PARAM_NAME + "' must be set";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        return Enum.valueOf(PersistenceType.class, type);
    }

    public void refresh() {
        knowledgeBases.clear();
        postConstruct();
    }

    public void reloadRuleSet(String ruleSetName, ResourceType resourceType) {
        if (ruleSetName.equals(getDslId())) {
            postConstruct();
        } else {
            knowledgeBases.remove(ruleSetName);
            getKnowledgeBase(ruleSetName, resourceType);
        }
    }


    @Override
    public <T> T fireRules(Long ruleSetId, T brmContext, Map<String, Object> globalParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T fireRules(Long ruleSetId, T brmContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T fireRules(String ruleSetName, T brmContext, Map<String, Object> globalParams) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public <T> T fireRules(String ruleSetName, T brmContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void reloadRuleSet(Long ruleSetId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
