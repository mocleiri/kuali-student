package org.kuali.student.sqlOrganizer;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 12/30/13
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public enum DatabaseModule {
    // ENUM(module.endsWith)
    RICE("Rice", "rice-sql"),
    KSCM("KS Curriculum Managment", "lum-sql"),
    KSENR("KS Enrollment", "enroll-sql"),
    KSCORE("KS Core", "core-sql"),
    EXCEPTION("UNKNOWN", "exception-sql");

    private final String label;
    private final String endsWith;

    private DatabaseModule(String label, String endsWith) {
        this.label = label;
        this.endsWith = endsWith;
    }

    public String getEndsWith() {
        return endsWith;
    }

    public String getLabel() {
        return label;
    }
}
