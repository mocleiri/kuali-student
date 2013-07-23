package org.kuali.student.r2.core.organization.dao;



import org.kuali.student.r2.common.dao.GenericEntityDao;
import org.kuali.student.r2.core.organization.model.OrgEntity;

import javax.persistence.Query;
import java.util.List;

public class OrgDao extends GenericEntityDao<OrgEntity> {

    public List<String> getIdsByType(String type) {
        Query query = em.createNamedQuery("OrgEntity.getIdsByType");
        query.setParameter("type", type);
        return query.getResultList();
    }

}
