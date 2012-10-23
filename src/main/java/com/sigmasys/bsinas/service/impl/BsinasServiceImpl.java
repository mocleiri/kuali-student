package com.sigmasys.bsinas.service.impl;

import com.sigmasys.bsinas.config.ConfigService;
import com.sigmasys.bsinas.model.Constants;
import com.sigmasys.bsinas.service.BsinasService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.collegeboard.inas._2012.NeedAnalysisXmlEngine;
import org.collegeboard.inas._2012.input.NeedAnalysisInput;
import org.collegeboard.inas._2012.output.NeedAnalysisOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

/**
 * The default implementation of BSINAS service.
 * <p/>
 *
 * @author Michael Ivanov
 */
@Service("bsinasService")
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class BsinasServiceImpl extends GenericPersistenceService implements BsinasService {

    private static final Log logger = LogFactory.getLog(BsinasServiceImpl.class);

    @Autowired
    private ConfigService configService;

    private NeedAnalysisXmlEngine engine;


    @PostConstruct
    private void postConstruct() {
        String wsdlUrl = configService.getInitialParameter(Constants.BSINAS_WSDL_URL_PARAM_NAME);
        if (StringUtils.isBlank(wsdlUrl)) {
            String errMsg = "Initial parameter '" + Constants.BSINAS_WSDL_URL_PARAM_NAME + "' must be set";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        try {
            engine = new NeedAnalysisXmlEngine(new URL(wsdlUrl), BSINAS_QNAME);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Parses the incoming XML specified by StringReader and converts it into NeedAnalysisInput using JAXB.
     *
     * @param request String representation of XML
     * @return NeedAnalysisInput instance
     */
    @Override
    public NeedAnalysisInput parseRequest(String request) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(NeedAnalysisInput.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (NeedAnalysisInput) unmarshaller.unmarshal(new StringReader(request));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Takes the incoming  specified by StringReader
     *
     * @param response NeedAnalysisOutput instance
     * @return String representation of XML
     */
    @Override
    public String parseResponse(NeedAnalysisOutput response) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(NeedAnalysisOutput.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(response, writer);
            return writer.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Runs NeedAnalysisXmlEngine and returns the result.
     *
     * @param input NeedAnalysisInput instance
     * @return NeedAnalysisOutput instance
     */
    @Override
    public NeedAnalysisOutput runEngine(NeedAnalysisInput input) {
        try {
            return engine.getBasicHttp().runNeedAnalysis(input);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
