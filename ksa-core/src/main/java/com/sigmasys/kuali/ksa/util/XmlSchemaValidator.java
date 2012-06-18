package com.sigmasys.kuali.ksa.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

public class XmlSchemaValidator {

    private static final Log logger = LogFactory.getLog(XmlSchemaValidator.class);

    private static final SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

    private final Schema schema;

    public XmlSchemaValidator(String schemaLocation) {
        schema = getSchema(schemaLocation);
    }

    private Schema getSchema(String schemaLocation) {
        try {
            ResourcePatternResolver resourcePatternResolver =
                    ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());
            Resource[] resources = resourcePatternResolver.getResources(schemaLocation);
            if (resources == null || resources.length < 1) {
                String errMsg = "Cannot retrieve the XML schema located at " + schemaLocation;
                logger.error(errMsg);
                throw new IllegalStateException(errMsg);
            }
            URL schemaUrl = resources[0].getURL();
            return schemaFactory.newSchema(schemaUrl);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public boolean validateXml(String xml) throws SAXException, IOException {
        return validateXml(new StringReader(xml));
    }

    public boolean validateXml(Reader reader) throws SAXException, IOException {

        // Get a validator from the schema.
        Validator validator = schema.newValidator();

        // Parse the document
        Source source = new StreamSource(reader);

        // Validate the document against the schema
        try {
            validator.validate(source);
            logger.info("XML schema is valid");
            return true;
        } catch (Throwable t) {
            logger.info("XML schema is invalid", t);
            return false;
        }

    }

}