package com.sigmasys.bsinas.service;

import org.collegeboard.inas._2012.NeedAnalysisXmlEngine;
import org.collegeboard.inas._2012.input.NeedAnalysisInput;
import org.collegeboard.inas._2012.output.NeedAnalysisOutput;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

/**
 * The main BSINAS service that parses the incoming XML, using JAX WS sends the SOAP request and gets the response
 * containing the calculated
 */
public interface BsinasService {

    String BSINAS_NAMESPACE = "http://INAS.collegeboard.org/2012/";

    QName BSINAS_QNAME = new QName(BSINAS_NAMESPACE, NeedAnalysisXmlEngine.class.getSimpleName());


    /**
     * Parses the incoming XML specified by StringReader and converts it into a Java object using JAXB.
     *
     * @param xml String representation of XML
     * @return NeedAnalysisInput instance
     */
    <T> T fromXml(String xml, Class<T> type) throws JAXBException;


    /**
     * Takes an object instance and serializes it to XML.
     *
     * @param object a Java bean instance to be serialized to XML
     * @return String representation of XML
     */
    <T> String toXml(T object) throws JAXBException;


    /**
     * Runs NeedAnalysisXmlEngine and returns the result.
     *
     * @param input NeedAnalysisInput instance
     * @return NeedAnalysisOutput instance
     */
    NeedAnalysisOutput runEngine(NeedAnalysisInput input);


}
