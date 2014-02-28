package org.kuali.student.cm.course.service.impl;


import org.apache.commons.beanutils.BeanUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.student.logging.FormattedLogger;
import org.kuali.student.lum.lu.util.CurriculumManagementConstants;
import org.kuali.student.r2.common.exceptions.DataValidationErrorException;
import org.kuali.student.r2.common.util.ContextUtils;
import org.kuali.student.r2.common.util.constants.LearningObjectiveServiceConstants;
import org.kuali.student.r2.lum.lo.dto.LoCategoryInfo;
import org.kuali.student.r2.lum.lo.service.LearningObjectiveService;

import javax.xml.namespace.QName;
import java.util.Collection;
import java.util.List;

public class LoCategoryViewHelperServiceImpl extends ViewHelperServiceImpl {

    private transient LearningObjectiveService loService;
    public final String ACTIVE = "Active";
    public final String KS_LO_CAT_TABLE = "KS-LoCatTable";
    public final String ERROR_CM_KRAD_LOCAT_DUPLICATE = "error.cmkrad.locat.duplicate";


    @Override
    public void processBeforeAddLine(ViewModel model, Object addLine, String collectionId, String collectionPath) {
        LoCategoryInfo loCategory = (LoCategoryInfo) addLine;
        loCategory.setStateKey(ACTIVE);
        loCategory.setLoRepositoryKey(CurriculumManagementConstants.KUALI_LO_REPOSITORY_KEY_SINGLE_USE);
        try {
            LoCategoryInfo savedLoCat = getLoService().createLoCategory(loCategory.getTypeKey(), loCategory,
                    ContextUtils.getContextInfo());
            BeanUtils.copyProperties(loCategory, savedLoCat);
        } catch (DataValidationErrorException e) {
            GlobalVariables.getMessageMap().putErrorForSectionId(KS_LO_CAT_TABLE, ERROR_CM_KRAD_LOCAT_DUPLICATE);
        } catch (Exception e) {
            FormattedLogger.error(CurriculumManagementConstants.ConfigProperties.ERROR_CREATE_NEW_LEARNING_OBJECTIVE_CATEGORY,
                    e.getMessage());
        }
    }

    @Override
    public void processBeforeSaveLine(ViewModel model, Object lineObject, String collectionId, String collectionPath) {
        LoCategoryInfo loCategory = (LoCategoryInfo) lineObject;
        //The 'loRepositoryKey' is not retrieved on the initial load of the existing Categories, so we are setting it again.
        loCategory.setLoRepositoryKey(CurriculumManagementConstants.KUALI_LO_REPOSITORY_KEY_SINGLE_USE);
        try {
            LoCategoryInfo updatedLoCat = getLoService().updateLoCategory(loCategory.getId(), loCategory,
                    ContextUtils.getContextInfo());
            BeanUtils.copyProperties(loCategory, updatedLoCat);
        } catch (DataValidationErrorException e) {
            GlobalVariables.getMessageMap().putErrorForSectionId( KS_LO_CAT_TABLE , ERROR_CM_KRAD_LOCAT_DUPLICATE);
        } catch (Exception e) {
            FormattedLogger.error(CurriculumManagementConstants.ConfigProperties.ERROR_UPDATING_LEARNING_OBJECTIVE_CATEGORY,
                    e.getMessage());
        }
    }

    /**
     * This method was overridden to perform the database delete action. Since there's no
     * 'processBeforeDeleteLine' method and the 'processAfterDeleteLine' method doesn't
     * contain the deleted item anymore, the original method was copied and the database
     * delete method call was inserted.
     * 
     * @see org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#processCollectionDeleteLine(org.kuali.rice.krad.uif.view.ViewModel, String, String, int)
     */
    @Override
    public void processCollectionDeleteLine(ViewModel model, String collectionId, String collectionPath, int lineIndex) {
        // get the collection group from the view
        CollectionGroup collectionGroup = model.getView().getViewIndex().getCollectionGroupByPath(collectionPath);
        if (collectionGroup == null) {
            logAndThrowRuntime(CurriculumManagementConstants.ConfigProperties.UNABLE_TO_GET_COLLECTION_GROUP + collectionPath);
        }

        // get the collection instance for adding the new line
        Collection<Object> collection = ObjectPropertyUtils.getPropertyValue(model, collectionPath);
        if (collection == null) {
            logAndThrowRuntime(CurriculumManagementConstants.ConfigProperties.UNABLE_TO_GET_COLLECTION_PROPERTY + collectionPath);
        }

        // TODO: look into other ways of identifying a line so we can deal with
        // unordered collections
        if (collection instanceof List) {
            Object deleteLine = ((List<Object>) collection).get(lineIndex);

            // validate the delete action is allowed for this line
            boolean isValid = performDeleteLineValidation(model, collectionId, collectionPath, deleteLine);
            if (isValid) {
                //This part was altered to include the database delete method call.
                LoCategoryInfo loCategory = (LoCategoryInfo) deleteLine;
                try {
                    getLoService().deleteLoCategory(loCategory.getId(), ContextUtils.getContextInfo());
                } catch (Exception e) {
                    FormattedLogger.error(CurriculumManagementConstants.ConfigProperties.ERROR_DELETE_LEARNING_OBJECTIVE_CATEGORY,
                            e.getMessage());
                }
                ((List<Object>) collection).remove(lineIndex);
                processAfterDeleteLine(model, collectionId, collectionPath, lineIndex);
            }
        } else {
            logAndThrowRuntime(CurriculumManagementConstants.ConfigProperties.LIST_COLLECTION_IMPLEMENTATIONS_SUPPORTED_FOR_DELETE_INDEX);
        }
    }

    protected LearningObjectiveService getLoService() {
        if (loService == null) {
            loService = GlobalResourceLoader.getService(new QName(LearningObjectiveServiceConstants.NAMESPACE,
                    LearningObjectiveService.class.getSimpleName()));
        }
        return loService;
    }

}
