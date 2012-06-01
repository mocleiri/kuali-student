package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.annotation.AnnotationUtils;
import com.sigmasys.kuali.ksa.annotation.Auditable;
import com.sigmasys.kuali.ksa.model.Activity;
import com.sigmasys.kuali.ksa.model.Identifiable;
import com.sigmasys.kuali.ksa.util.ContextUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.internal.AbstractPersistentCollection;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.EmbeddedComponentType;
import org.hibernate.type.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Audit trail interceptor for Hibernate.
 * Contains all the logic of capturing and storing changes to JPA entities.
 *
 * @author Michael Ivanov
 */
@SuppressWarnings("serial")
public abstract class AuditTrailInterceptor extends EmptyInterceptor {


    private static final Log logger = LogFactory.getLog(AuditTrailInterceptor.class);


    protected SessionFactory sessionFactory;


    protected abstract EntityManagerFactoryInfo getEntityManagerFactoryInfo();

    /**
     * Lazy initializes the session factory and stores it for obtaining session in the future
     *
     * @return SessionFactory
     */
    protected synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            EntityManagerFactoryInfo factoryInfo = getEntityManagerFactoryInfo();
            HibernateEntityManagerFactory entityManagerFactory = (HibernateEntityManagerFactory)
                    factoryInfo.getNativeEntityManagerFactory();
            sessionFactory = entityManagerFactory.getSessionFactory();
        }
        return sessionFactory;
    }

    private void onCollectionCreateRemove(Object collectionObject, Serializable id) {

        if (collectionObject instanceof PersistentCollection) {

            PersistentCollection collection = (PersistentCollection) collectionObject;

            Auditable auditable = AnnotationUtils.getAnnotation(Auditable.class, collection.getOwner());

            if (auditable != null) {

                Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
                UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
                String userId = sessionManager.getUserId(RequestUtils.getThreadRequest());

                if (collection instanceof Collection<?>) {
                    Collection<?> javaCollection = (Collection<?>) collection;
                    String propertyName = getPropertyName(collection);
                    for (Object item : javaCollection) {
                        createActivityForCollectionItem(session, id, collection, userId, item, propertyName, true);
                    }
                }
            }
        }
    }

    @Override
    public void onCollectionRecreate(Object collectionObject, Serializable id) throws CallbackException {
        onCollectionCreateRemove(collectionObject, id);
    }

    @Override
    public void onCollectionRemove(Object collectionObject, Serializable id) throws CallbackException {
        onCollectionCreateRemove(collectionObject, id);
    }


    /**
     * This method is called if entries were added or removed from a collection
     * But Hibernate is not very smart, and can call this method even if there are no changes to be persisted,
     * so we have special logic to check and filter that out
     */
    @Override
    public void onCollectionUpdate(Object collectionObject, Serializable id) throws CallbackException {

        if (collectionObject instanceof AbstractPersistentCollection) {

            AbstractPersistentCollection collection = (AbstractPersistentCollection) collectionObject;

            if (logger.isDebugEnabled()) {
                logger.debug("Checking if auditing should be done for collection " + collection.getRole() +
                        " of " + collection.getOwner());
            }

            if (!collection.wasInitialized()) {
                logger.debug("Skipping audit because collection was not initialized");
                return;
            }

            Auditable auditable = AnnotationUtils.getAnnotation(Auditable.class, collection.getOwner());

            if (auditable != null) {

                // Collection changes should be audited
                SessionFactoryImplementor factory = collection.getSession().getFactory();
                CollectionPersister collectionPersister = factory.getCollectionPersister(collection.getRole());

                @SuppressWarnings("rawtypes")
                Iterator entries = collection.entries(collectionPersister);
                Type elemType = collectionPersister.getElementType();

                Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
                UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
                String userId = sessionManager.getUserId(RequestUtils.getThreadRequest());

                String propertyName = getPropertyName(collection);

                for (int i = 0; entries.hasNext(); i++) {
                    Object entry = entries.next();
                    if (collection.needsInserting(entry, i, elemType)) {
                        createActivityForCollectionItem(session, id, collection, userId, entry, propertyName, true);
                    }
                }

                boolean deleteByIndex = !collectionPersister.isOneToMany() && collectionPersister.hasIndex();
                entries = collection.getDeletes(collectionPersister, !deleteByIndex);
                while (entries.hasNext()) {
                    createActivityForCollectionItem(session, id, collection, userId, entries.next(), propertyName, false);
                }
            }
        }
    }

    /**
     * Returns bean property name that represents given collection
     * Hibernate role stores fully qualified name, so we have to clip it out
     *
     * @param collection PersistentCollection instance
     * @return a simple property name such as "groups"
     */
    private String getPropertyName(PersistentCollection collection) {

        String propertyName = collection.getRole();

        if (propertyName != null) {

            // Hibernate role is <className>.<propertyName>
            int lastDot = propertyName.lastIndexOf('.');

            if (lastDot != -1) {
                propertyName = propertyName.substring(lastDot + 1);
            }

        } else if (collection.getOwner() != null) {

            // No help from Hibernate, so try to figure out property name using reflection
            Object owner = collection.getOwner();
            PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(owner.getClass());

            for (PropertyDescriptor property : properties) {
                try {
                    if (property.getReadMethod() != null) {
                        Object value = property.getReadMethod().invoke(owner);
                        if (collection == value || collection.equals(value)) {
                            // Found
                            propertyName = property.getName();
                            break;
                        }
                    }
                } catch (Exception x) {
                    logger.warn("Failed to lookup property name for property descriptor " + property, x);
                }
            }
        }

        if (propertyName == null) {
            throw new IllegalStateException("Failed to determine property name for collection " + collection +
                    ", owner = " + collection.getOwner());
        }

        return propertyName;
    }

    protected void createActivityForCollectionItem(Session session, Serializable id, PersistentCollection collection,
                                                   String userId, Object entry, String propertyName, boolean isInsert) {

        Object owner = collection.getOwner();
        String propertyValue = getPropertyValue(entry);

        Activity activity = new Activity();
        activity.setEntityId(id.toString());
        activity.setEntityType(owner.getClass().getSimpleName());
        activity.setCreatorId(userId);
        activity.setIpAddress(RequestUtils.getClientIpAddress());
        activity.setLogDetail("Persistent collection insert/remove");

        activity.setOldAttribute(isInsert ? null : propertyValue);
        activity.setNewAttribute(isInsert ? propertyValue : null);

        activity.setTimestamp(new Date());

        // TODO - persist activity

        session.save(activity);

    }


    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        Auditable auditable = AnnotationUtils.getAnnotation(Auditable.class, entity);

        if (auditable != null) {

            Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
            UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
            String userId = sessionManager.getUserId(RequestUtils.getThreadRequest());

            for (int i = 0; i < state.length; i++) {

                if (state[i] instanceof Collection<?>) {
                    // Updates to collections are tracked through different methods
                    continue;
                }

                // We do not want to see some strange state[i] in ATR table
                if (propertyNames[i].startsWith("_") && types[i] instanceof EmbeddedComponentType) {
                    continue;
                }

                String oldValue = getPropertyValue(state[i]);

                if (oldValue == null) {
                    // Nothing to save -- null to null
                    continue;
                }


            }
        }
    }

    /**
     * This method is called when entity has changes
     */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                String[] propertyNames, Type[] types) {

        Auditable auditable = AnnotationUtils.getAnnotation(Auditable.class, entity);

        if (auditable != null) {

            Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
            UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
            String userId = sessionManager.getUserId(RequestUtils.getThreadRequest());

            for (int i = 0; i < currentState.length; i++) {

                if (currentState[i] instanceof Collection<?>) {
                    // Updates to collections are tracked through different methods
                    continue;
                }

                // We do not want to see some strange state[i] in ATR table
                if (propertyNames[i].startsWith("_") && types[i] instanceof EmbeddedComponentType) {
                    continue;
                }

                if (!objectsMatch(currentState[i], previousState[i])) {

                    String newValue = getPropertyValue(currentState[i]);
                    String oldValue = getPropertyValue(previousState[i]);

                    if (objectsMatch(newValue, oldValue)) {
                        // Nothing to save -- they are equal or null
                        continue;
                    }

                    // Insert audit trail record
                    Activity activity = new Activity();

                    // TODO
                }
            }
        }
        return false;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

        Auditable auditable = AnnotationUtils.getAnnotation(Auditable.class, entity);

        if (auditable != null) {

            Session session = SessionFactoryUtils.getSession(getSessionFactory(), false);
            UserSessionManager sessionManager = ContextUtils.getBean(UserSessionManager.class);
            String userId = sessionManager.getUserId(RequestUtils.getThreadRequest());

            // Insert audit trail records for each property
            for (int i = 0; i < state.length; i++) {

                if (state[i] instanceof Collection<?>) {
                    // Updates to collections are tracked through different methods
                    continue;
                }

                // We do not want to see some strange state[i] in ATR table
                if (propertyNames[i].startsWith("_") && types[i] instanceof EmbeddedComponentType) {
                    continue;
                }

                String newValue = getPropertyValue(state[i]);
                if (newValue == null) {
                    //nothing changed -- null to null 
                    continue;
                }

                // Insert audit trail record
                Activity activity = new Activity();

                // TODO
            }
        }
        return false;
    }

    protected String getPropertyValue(Object object) {

        if (object == null) {
            return null;
        }

        if (object instanceof Identifiable) {
            // For entities use their id instead of toString()
            Serializable objectId = ((Identifiable) object).getId();
            return (objectId != null) ? objectId.toString() : null;
        }

        return (object instanceof Date) ? getFormattedDate((Date) object) : object.toString();
    }

    protected String getFormattedDate(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date);
    }

    private boolean objectsMatch(Object a, Object b) {
        return (a == null && b == null) || (a != null && a.equals(b));
    }
}
