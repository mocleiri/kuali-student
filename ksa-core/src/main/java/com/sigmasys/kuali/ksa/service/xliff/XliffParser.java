package com.sigmasys.kuali.ksa.service.xliff;

import java.io.Reader;
import java.io.StringReader;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.codehaus.stax2.XMLInputFactory2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementation for the &lt;xliff/&gt; tag for the XLIFF 1.2 OASIS specification.
 * An xliff tag contains the whole translation for one language.
 *
 * @author Michael Ivanov
 */
@Service("xliffParser")
@Transactional(readOnly = true)
public class XliffParser {

    private static final Log logger = LogFactory.getLog(XliffParser.class);

    private final XMLInputFactory2 xmlInputFactory;

    private XliffParser() {
        xmlInputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlInputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        xmlInputFactory.configureForSpeed();
    }


    /**
     * Parses the xliff content
     */
    public Map<String, TransUnit> parse(String xliffContent) {
        return parse(new StringReader(xliffContent));
    }

    /**
     * Parses the xliff reading its content from the Reader
     */
    public Map<String, TransUnit> parse(Reader xliffReader) {
        try {
            Map<String, TransUnit> transUnits = new HashMap<String, TransUnit>();
            XMLStreamReader streamReader = xmlInputFactory.createXMLStreamReader(xliffReader);
            while (streamReader.next() != XMLEvent.END_DOCUMENT) {
                int eventType = streamReader.getEventType();
                switch (eventType) {
                    case XMLEvent.START_ELEMENT:
                        String currentElement = streamReader.getName().toString();
                        if ("trans-unit".equals(currentElement)) {
                            TransUnit transUnit = getTransUnit(streamReader, eventType);
                            if (transUnit.getId() != null) {
                                transUnits.put(transUnit.getId(), transUnit);
                            }
                        }
                }
            }
            return transUnits;
        } catch (Exception ex) {
            String errMsg = "Error while loading dictionary from XLIFF file: " + ex.getMessage();
            logger.error(errMsg, ex);
            throw new RuntimeException(errMsg, ex);
        }
    }


    /**
     * Retrieve one &lt;trans-unit/&gt; contents.
     *
     * @param streamReader XML Stream reader
     * @param eventType    Type of event where parsing stopped in previous step
     * @return Trans unit read
     */
    private TransUnit getTransUnit(XMLStreamReader streamReader, int eventType) {

        String transUnitId = null;
        for (int i = 0; i < streamReader.getAttributeCount(); i++) {
            String attrName = streamReader.getAttributeName(i).toString();
            String attrVal = streamReader.getAttributeValue(i);
            if ("id".equals(attrName)) {
                transUnitId = attrVal;
            }
        }

        try {

            TransUnit transUnit = new TransUnit();
            transUnit.setId(transUnitId);

            // Read until </trans-unit>
            String currentElement = "";
            while (!("trans-unit".equals(currentElement) && eventType == XMLEvent.END_ELEMENT)) {
                streamReader.next();
                eventType = streamReader.getEventType();
                switch (eventType) {
                    case XMLEvent.START_ELEMENT:
                        currentElement = streamReader.getName().toString();
                        break;
                    case XMLEvent.END_ELEMENT:
                        currentElement = streamReader.getName().toString();
                        break;
                    case XMLEvent.CHARACTERS:
                        String text = streamReader.getText();
                        if ("source".equals(currentElement)) {
                            transUnit.setSource(text);
                        }
                        if ("target".equals(currentElement)) {
                            transUnit.setTarget(text);
                        }
                }
            }

            return transUnit;

        } catch (XMLStreamException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
