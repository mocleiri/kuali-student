package com.sigmasys.bsinas.service;

import org.collegeboard.inas._2012.NeedAnalysisXmlEngine;
import org.collegeboard.inas._2012.input.NeedAnalysisInput;
import org.collegeboard.inas._2012.output.NeedAnalysisOutput;

import javax.xml.namespace.QName;

/**
 * The main BSINAS service that parses the incoming XML, using JAX WS sends the SOAP request and gets the response
 * containing the calculated
 */
public interface BsinasService {

    String BSINAS_NAMESPACE = "http://INAS.collegeboard.org/2012/";

    QName BSINAS_QNAME = new QName(BSINAS_NAMESPACE, NeedAnalysisXmlEngine.class.getSimpleName());


    /**
     * Parses the incoming XML specified by StringReader and converts it into NeedAnalysisInput using JAXB.
     *
     * @param request String representation of XML
     * @return NeedAnalysisInput instance
     */
    NeedAnalysisInput parseRequest(String request);


    /**
     * Takes the incoming  specified by StringReader
     *
     * @param response NeedAnalysisOutput instance
     * @return String representation of XML
     */
    String parseResponse(NeedAnalysisOutput response);


    /**
     * Runs NeedAnalysisXmlEngine and returns the result.
     *
     * @param input NeedAnalysisInput instance
     * @return NeedAnalysisOutput instance
     */
    NeedAnalysisOutput runEngine(NeedAnalysisInput input);


}
