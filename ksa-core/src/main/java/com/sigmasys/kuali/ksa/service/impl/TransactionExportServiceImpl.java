package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.config.ConfigService;
import com.sigmasys.kuali.ksa.exception.InvalidGeneralLedgerAccountException;
import com.sigmasys.kuali.ksa.model.*;
import com.sigmasys.kuali.ksa.model.export.*;
import com.sigmasys.kuali.ksa.service.*;
import com.sigmasys.kuali.ksa.util.CalendarUtils;
import com.sigmasys.kuali.ksa.util.JaxbUtils;
import com.sigmasys.kuali.ksa.util.RequestUtils;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sigmasys.kuali.ksa.model.Constants.*;

/**
 * Transaction Export Service implementation.
 *
 * @author Michael Ivanov
 */
@Service("transactionExportService")
@Transactional(readOnly = true)
@WebService(serviceName = TransactionExportService.SERVICE_NAME, portName = TransactionExportService.PORT_NAME,
        targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class TransactionExportServiceImpl extends GenericPersistenceService implements TransactionExportService {

    private static final Log logger = LogFactory.getLog(TransactionExportServiceImpl.class);

    private static final String EXPORT_SCHEMA_LOCATION = "classpath*:/xsd/export/transaction-export.xsd";
    private static final String EXPORT_TYPES_LOCATION = "classpath*:/xsd/export/types.xsd";
    private static final String EXPORT_DD_TYPES_LOCATION = "classpath*:/xsd/export/ddTypes.xsd";
    private static final String XML_SCHEMA_LOCATION = "classpath*:/xsd/xml.xsd";

    private static final XmlSchemaValidator schemaValidator = new XmlSchemaValidator(
            XML_SCHEMA_LOCATION,
            EXPORT_TYPES_LOCATION,
            EXPORT_DD_TYPES_LOCATION,
            EXPORT_SCHEMA_LOCATION);

    @Autowired
    private GeneralLedgerService glService;

    @Autowired
    private ConfigService configService;


    /**
     * Gets all the GlTransmission objects with a result that is blank and
     * returns the completed XML file that will be uploaded to KFS or any other external system.
     *
     * @return XML content that contains the transactions to be exported
     */
    @Override
    public String exportTransactions() {
        long batchId = RandomUtils.nextLong();
        List<GlTransmission> glTransmissions = glService.getGlTransmissionsForExport();
        String xml = convertGlTransmissionsToXml(glTransmissions, batchId);
        // Validate XML against the schema
        if (schemaValidator.validateXml(xml)) {
            return xml;
        }
        String errMsg = "XML content is invalid:\n" + xml;
        logger.error(errMsg);
        throw new RuntimeException(errMsg);
    }

    /**
     * Parses the GL account and returns the list of <code>java.util.String</code> values.
     * The format of GL account is organization-specific. The current implementation supports the UMD format.
     *
     * @param glAccount GL account number
     * @return list of String values
     */
    @Override
    public List<String> parseGlAccount(String glAccount) {

        if (!org.springframework.util.StringUtils.hasText(glAccount)) {
            String errMsg = "GL Account cannot be empty";
            logger.error(errMsg);
            throw new InvalidGeneralLedgerAccountException(errMsg);
        }

        glAccount = org.springframework.util.StringUtils.deleteAny(glAccount, " \n\t\r");

        if (glAccount.length() != 12) {
            String errMsg = "GL Account '" + glAccount + "' is invalid, the length must be 12";
            logger.error(errMsg);
            throw new InvalidGeneralLedgerAccountException(errMsg);
        }

        List<String> accountParts = new ArrayList<String>(3);

        accountParts.add(glAccount.substring(0, 2));
        accountParts.add(glAccount.substring(2, 8));
        accountParts.add(glAccount.substring(8));

        return accountParts;
    }

    protected String convertGlTransmissionsToXml(List<GlTransmission> glTransmissions, long batchId) {

        final com.sigmasys.kuali.ksa.model.export.ObjectFactory objectFactory = new com.sigmasys.kuali.ksa.model.export.ObjectFactory();

        final BigInteger batchNumber = BigInteger.valueOf(batchId);

        final String currentUserId = userSessionManager.getUserId(RequestUtils.getThreadRequest());

        final Date currentDate = new Date();

        final XMLGregorianCalendar currentXmlDate = CalendarUtils.toXmlGregorianCalendar(currentDate);

        // Creating BatchType instance
        final BatchType batchType = objectFactory.createBatchType();

        // Creating TrailerType instance
        final TrailerType trailerType = objectFactory.createTrailerType();

        final String balanceTypeCode = configService.getInitialParameter(KFS_BALANCE_TYPE_CODE_PARAM_NAME);
        final String objectTypeCode = configService.getInitialParameter(KFS_OBJECT_TYPE_CODE_PARAM_NAME);
        final String documentTypeCode = configService.getInitialParameter(KFS_DOCUMENT_TYPE_CODE_PARAM_NAME);
        final String documentNumberPrefix = configService.getInitialParameter(KFS_DOCUMENT_NUMBER_PREFIX_PARAM_NAME);
        final String originationCode = configService.getInitialParameter(KFS_ORIGINATION_CODE_PARAM_NAME);
        final String glEntryDesc = configService.getInitialParameter(KFS_TRANSACTION_GL_ENTRY_DESCRIPTION_PARAM_NAME);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GlTransmission glTransmission : glTransmissions) {

            if (org.springframework.util.StringUtils.hasLength(glTransmission.getResult())) {
                String errMsg = "GL Transmission with ID = " + glTransmission.getId() +
                        " already has the result value = " + glTransmission.getResult();
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }

            totalAmount = totalAmount.add(glTransmission.getAmount());

            // Creating GLEntryType instance
            GlEntryType glEntryType = objectFactory.createGlEntryType();

            final String recognitionPeriodCode = glTransmission.getRecognitionPeriod().getCode();

            final String documentNumber = documentNumberPrefix + glTransmission.getId();

            final List<String> parsedGlAccount = parseGlAccount(glTransmission.getGlAccountId());

            glEntryType.setChartOfAccountsCode(parsedGlAccount.get(0));
            glEntryType.setAccountNumber(parsedGlAccount.get(1));
            glEntryType.setObjectCode(parsedGlAccount.get(2));

            glEntryType.setBalanceTypeCode(balanceTypeCode);
            glEntryType.setDebitOrCreditCode(glTransmission.getGlOperation().getId());
            glEntryType.setDocumentNumber(documentNumber);
            // TODO Mapping to what ?????
            //glEntryType.setDocumentReversalDate();
            glEntryType.setDocumentTypeCode(documentTypeCode);
            glEntryType.setObjectTypeCode(objectTypeCode);
            // TODO Mapping to what ?????
            //glEntryType.setEncumbranceUpdateCode();
            // TODO Mapping to what ?????
            //glEntryType.setOrganizationDocumentNumber();
            // TODO Mapping to what ?????
            //glEntryType.setOrganizationReferenceId();
            glEntryType.setOriginationCode(originationCode);
            // TODO Mapping to what ?????
            //glEntryType.setReferenceOriginationCode();
            // TODO Mapping to what ?????
            //glEntryType.setProjectCode();
            // TODO Mapping to what ?????
            //glEntryType.setReferenceDocumentNumber();
            // TODO Mapping to what ?????
            //glEntryType.setReferenceDocumentTypeCode();
            // TODO Mapping to what ?????
            //glEntryType.setSubAccountNumber();
            // TODO Mapping to what ?????
            //glEntryType.setSubObjectCode();
            glEntryType.setTransactionDate(currentDate.toString()); // TODO ???? XML or just String value of Date??
            // TODO Mapping to what ?????
            //glEntryType.setTransactionEntrySequenceId();
            glEntryType.setTransactionLedgerEntryAmount(glTransmission.getAmount().toString());
            glEntryType.setTransactionLedgerEntryDescription(glEntryDesc);

            // TODO: are these 2 using the same recognition period code?
            glEntryType.setUniversityFiscalAccountingPeriod(recognitionPeriodCode);
            glEntryType.setUniversityFiscalYear(recognitionPeriodCode);

            // Creating DetailType instance
            DetailType detailType = objectFactory.createDetailType();

            detailType.setChartOfAccountsCode(parsedGlAccount.get(0));
            detailType.setAccountNumber(parsedGlAccount.get(1));
            detailType.setObjectCode(parsedGlAccount.get(2));

            detailType.setAmount(glTransmission.getAmount());
            detailType.setBalanceTypeCode(balanceTypeCode);
            // TODO Mapping to what ?????
            //detailType.setCollectorDetailSequenceNumber();
            detailType.setCreateDate(currentXmlDate);
            // TODO Mapping to what ?????
            //detailType.setDetailText();
            detailType.setObjectTypeCode(objectTypeCode);
            detailType.setDocumentTypeCode(documentTypeCode);
            detailType.setDocumentNumber(documentNumber);
            // TODO Mapping to what ?????
            //detailType.setSubObjectCode();
            // TODO Mapping to what ?????
            //detailType.setSubAccountNumber();
            detailType.setOriginationCode(originationCode);

            // TODO: are these 2 using the same recognition period code?
            detailType.setUniversityFiscalAccountingPeriod(recognitionPeriodCode);
            detailType.setUniversityFiscalYear(recognitionPeriodCode);

            // Adding GLEntryType and DetailType instances to "glEntryAndDetail" list of BatchType
            batchType.getGlEntryAndDetail().add(glEntryType);
            batchType.getGlEntryAndDetail().add(detailType);

        }

        // Creating HeaderType instance
        HeaderType headerType = objectFactory.createHeaderType();
        headerType.setBatchSequenceNumber(batchNumber);
        headerType.setCampusCode(configService.getInitialParameter(KFS_CAMPUS_CODE_PARAM_NAME));
        headerType.setChartOfAccountsCode(configService.getInitialParameter(KFS_CHART_OF_ACCOUNTS_CODE_PARAM_NAME));
        headerType.setDepartmentName(configService.getInitialParameter(KFS_DEPARTMENT_NAME_PARAM_NAME));
        headerType.setEmailAddress(configService.getInitialParameter(KFS_EMAIL_ADDRESS_PARAM_NAME));
        headerType.setMailingAddress(configService.getInitialParameter(KFS_POSTAL_ADDRESS_PARAM_NAME));
        headerType.setOrganizationCode(configService.getInitialParameter(KFS_ORGANIZATION_CODE_PARAM_NAME));
        headerType.setPersonUserId(currentUserId);
        headerType.setPhoneNumber(configService.getInitialParameter(KFS_PHONE_NUMBER_PARAM_NAME));
        headerType.setTransmissionDate(currentXmlDate);

        batchType.setHeader(headerType);

        trailerType.setTotalAmount(totalAmount);
        trailerType.setTotalRecords(BigInteger.valueOf(glTransmissions.size()));

        batchType.setTrailer(trailerType);

        // Converting BatchType to XML
        return JaxbUtils.toXml(batchType);

    }


}
