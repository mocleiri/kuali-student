package com.sigmasys.kuali.ksa.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Information main class
 * <p/>
 *
 * @author Michael Ivanov
 *         Date: 1/22/12
 *         Time: 3:47 PM
 */
@Entity
@Table(name = "KSSA_INFORMATION")
@DiscriminatorColumn(name = "TYPE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Information extends AccountIdAware implements Identifiable {

    /**
     * Information ID
     */
    private Long id;

    /**
     * Text
     */
    private String text;

    /**
     * Creation date
     */
    private Date creationDate;

    /**
     * Effective date
     */
    private Date effectiveDate;

    /**
     * Expiration date
     */
    private Date expirationDate;

    /**
     * Account
     */
    private Account account;

    /**
     * Access Level
     */
    private Integer accessLevel;

    /**
     * Transaction
     */
    private Transaction transaction;

    /**
     * Creator user ID
     */
    private String creatorId;

    /**
     * Editor user ID
     */
    private String editorId;

    /**
     * Timestamp
     */
    private Date lastUpdate;


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @TableGenerator(name = "TABLE_GEN_INFO",
            table = "KSSA_SEQUENCE_TABLE",
            pkColumnName = "SEQ_NAME",
            valueColumnName = "SEQ_VALUE",
            pkColumnValue = "INFORMATION_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN_INFO")
    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ACCESS_LEVEL")
    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * TODO: Remove this eventually.  This is a hack because Uif-HorizontalRadioControl can only deal with Strings.  I'm sorry.
     * Discussion on KRAD list: https://groups.google.com/a/kuali.org/d/topic/rice.usergroup.krad/I2x9Z55amBw/discussion
     * @return
     */
    @Transient
    public String getAccessLevelString(){
        return accessLevel.toString();
    }

    public void setAccessLevelString(String accessLevel){
        this.accessLevel = Integer.parseInt(accessLevel);
    }

    @Column(name = "CREATOR_ID", length = 45)
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Column(name = "EDITOR_ID", length = 45)
    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    @Column(name = "LAST_UPDATE")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Column(name = "CREATION_DATE")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "EFFECTIVE_DATE")
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Column(name = "EXPIRATION_DATE")
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACNT_ID_FK")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRN_ID_FK")
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Column(name = "TEXT", length = 4000)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Transient
    public boolean isActive() {
        Date curDate = new Date();
        return effectiveDate != null && effectiveDate.compareTo(curDate) <= 0 &&
                (expirationDate == null || expirationDate.after(curDate));
    }

    /**
     * A composition of fields
     */
    @Transient
    public String getDisplayValue() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_US);
        StringBuilder builder = new StringBuilder(dateFormat.format(getCreationDate()));
        String text = getText();
        if (StringUtils.isNotEmpty(text)) {
            builder.append(" ");
            builder.append(text);
        }
        return builder.toString();
    }


}
