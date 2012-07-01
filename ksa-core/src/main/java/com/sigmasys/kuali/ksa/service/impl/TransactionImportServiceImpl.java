package com.sigmasys.kuali.ksa.service.impl;

import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.service.TransactionImportService;
import com.sigmasys.kuali.ksa.service.TransactionService;
import com.sigmasys.kuali.ksa.transform.XmlToObj;
import com.sigmasys.kuali.ksa.util.XmlSchemaValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.DatatypeConverter;
import java.io.*;

/**
 * Created by: dmulderink on 6/29/12 at 11:07 AM
 */
@Service("transactionImportService")
@Transactional(readOnly = true)
@WebService(serviceName = TransactionImportService.SERVICE_NAME, portName = TransactionImportService.PORT_NAME,
      targetNamespace = Constants.WS_NAMESPACE)
@SuppressWarnings("unchecked")
public class TransactionImportServiceImpl implements TransactionImportService {

   private static final String IMPORT_SCHEMA_LOCATION = "classpath*:ConsolidatedSchemata.xsd";
   private static final String XML_SCHEMA_LOCATION = "classpath*:xml.xsd";

   private final XmlSchemaValidator schemaValidator =
         new XmlSchemaValidator(XML_SCHEMA_LOCATION, IMPORT_SCHEMA_LOCATION);

   @WebMethod
   public String xmlUpload(String base64Xml) {
      String xmlToPersistResponse = "";
      boolean validSchema = false;

      System.out.println(IMPORT_SCHEMA_LOCATION);
      System.out.println(XML_SCHEMA_LOCATION);

      if (!base64Xml.isEmpty()) {

         byte[] decoded = DatatypeConverter.parseBase64Binary(base64Xml);
         String xmlDataStream = new String(decoded);
         InputStream is = new ByteArrayInputStream(decoded);

         try {
            // validate against schema
            validSchema = schemaValidator.validateXml(xmlDataStream);
         } catch(IOException ioExp){
            xmlToPersistResponse = ioExp.getLocalizedMessage();
         } catch(SAXException saxExp) {
            xmlToPersistResponse = saxExp.getLocalizedMessage();
         } finally {
            // if schema is valid
            if (validSchema) {
               // convert to an object model of 1 or more
               XmlToObj xmlToObj = new XmlToObj();

               // if there is an InputStream created
               if (is != null) {
                  // TODO need configuration information single, batch...
                  boolean isBatch = true;
                  xmlToPersistResponse = xmlToObj.convertXmlToTransaction(is, isBatch);
                  System.out.println(xmlToPersistResponse);
               }
            } else {
               // send the failed schema message as the response
               xmlToPersistResponse = "Transaction content is invalid.\n\n" + xmlDataStream;
               //logger.error(errMsg);
               throw new IllegalStateException(xmlToPersistResponse);
            }
         }
      }

      // write the XML result to base64 string
      byte[] encoded = xmlToPersistResponse.getBytes();
      String base64Value = DatatypeConverter.printBase64Binary(encoded);

      return base64Value;
   }
}
