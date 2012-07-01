package com.sigmasys.kuali.ksa.service;

import com.sigmasys.kuali.ksa.util.CommonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by: dmulderink on 6/29/12 at 2:56 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {ServiceTestSuite.TEST_KSA_CONTEXT})
@Transactional
public class TransactionImportServiceTest extends AbstractServiceTest {

   @Autowired
   private TransactionImportService transactionImportService;

   @Test
   public void batchImport() throws Exception{

      String content = CommonUtils.getResourceAsString("xmlImport/transactions.xml");

      Assert.notNull(content);
      String base64XmlValue = encode(content);
      Assert.notNull(base64XmlValue);
      String response = transactionImportService.xmlUpload(base64XmlValue);
      String xmlResponse = decode(response);
      Assert.notNull(xmlResponse);
      System.out.println(xmlResponse);
   }

   /**
    * Encode an XML string to Base64
    * @param content
    * @return
    */
   private String encode(String content){

      byte[] encoded = content.getBytes();

      String base64Value = DatatypeConverter.printBase64Binary(encoded);

      return base64Value;
   }

   /**
    * Decode a Base64 string to an XML string representation
    * @param base64Xml
    * @return
    */
   private String decode(String base64Xml) {
      byte[] decoded = DatatypeConverter.parseBase64Binary(base64Xml);
      String xmlDataStream = new String(decoded);
      return xmlDataStream;
   }
}
